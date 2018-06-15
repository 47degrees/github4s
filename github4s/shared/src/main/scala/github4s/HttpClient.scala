/*
 * Copyright 2016-2018 47 Degrees, LLC. <http://www.47deg.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package github4s

import github4s.free.domain.Pagination
import io.circe.Decoder
import github4s.GithubResponses.GHResponse
import github4s.HttpClient._

object HttpClient {
  type Headers = Map[String, String]

  sealed trait HttpVerb {
    def verb: String
  }

  case object Get extends HttpVerb {
    def verb = "GET"
  }

  case object Post extends HttpVerb {
    def verb = "POST"
  }

  case object Put extends HttpVerb {
    def verb = "PUT"
  }

  case object Delete extends HttpVerb {
    def verb = "DELETE"
  }

  case object Patch extends HttpVerb {
    def verb = "PATCH"
  }

  sealed trait HttpStatus {
    def statusCode: Int
  }

  case object HttpCode200 extends HttpStatus {
    def statusCode = 200
  }

  case object HttpCode299 extends HttpStatus {
    def statusCode = 299
  }
}

class HttpRequestBuilder[C, M[_]](
    val url: String,
    val httpVerb: HttpVerb = Get,
    val authHeader: Map[String, String] = Map.empty[String, String],
    val data: Option[String] = None,
    val params: Map[String, String] = Map.empty[String, String],
    val headers: Map[String, String] = Map.empty[String, String]
) {

  def postMethod = new HttpRequestBuilder[C, M](url, Post, authHeader, data, params, headers)

  def patchMethod = new HttpRequestBuilder[C, M](url, Patch, authHeader, data, params, headers)

  def putMethod = new HttpRequestBuilder[C, M](url, Put, authHeader, data, params, headers)

  def deleteMethod = new HttpRequestBuilder[C, M](url, Delete, authHeader, data, params, headers)

  def withAuth(accessToken: Option[String] = None) = {
    val authHeader = accessToken match {
      case Some(token) ⇒ Map("Authorization" → s"token $token")
      case _           ⇒ Map.empty[String, String]
    }
    new HttpRequestBuilder[C, M](url, httpVerb, authHeader, data, params, headers)
  }

  def withHeaders(headers: Map[String, String]) =
    new HttpRequestBuilder[C, M](url, httpVerb, authHeader, data, params, headers)

  def withParams(params: Map[String, String]) =
    new HttpRequestBuilder[C, M](url, httpVerb, authHeader, data, params, headers)

  def withData(data: String) =
    new HttpRequestBuilder[C, M](url, httpVerb, authHeader, Option(data), params, headers)
}

object HttpRequestBuilder {
  def httpRequestBuilder[C, M[_]](
      url: String,
      httpVerb: HttpVerb = Get,
      authHeader: Map[String, String] = Map.empty[String, String],
      data: Option[String] = None,
      params: Map[String, String] = Map.empty[String, String],
      headers: Map[String, String] = Map.empty[String, String]
  ) = new HttpRequestBuilder[C, M](url, httpVerb, authHeader, data, params, headers)
}

class HttpClient[C, M[_]](
    implicit urls: GithubApiUrls,
    httpRbImpl: HttpRequestBuilderExtension[C, M]) {
  import HttpRequestBuilder._

  val defaultPagination = Pagination(1, 1000)

  def get[A](
      accessToken: Option[String] = None,
      method: String,
      headers: Map[String, String] = Map.empty,
      params: Map[String, String] = Map.empty,
      pagination: Option[Pagination] = None
  )(implicit D: Decoder[A]): M[GHResponse[A]] =
    httpRbImpl.run[A](
      httpRequestBuilder(buildURL(method))
        .withAuth(accessToken)
        .withHeaders(headers)
        .withParams(params ++ pagination.fold(Map.empty[String, String])(p ⇒
          Map("page" → p.page.toString, "per_page" → p.per_page.toString))))

  def patch[A](
      accessToken: Option[String] = None,
      method: String,
      headers: Map[String, String] = Map.empty,
      data: String)(implicit D: Decoder[A]): M[GHResponse[A]] =
    httpRbImpl.run[A](
      httpRequestBuilder(buildURL(method)).patchMethod
        .withAuth(accessToken)
        .withHeaders(headers)
        .withData(data))

  def put[A](
      accessToken: Option[String] = None,
      url: String,
      headers: Map[String, String] = Map(),
      data: String)(implicit D: Decoder[A]): M[GHResponse[A]] =
    httpRbImpl.run[A](
      httpRequestBuilder(buildURL(url)).putMethod
        .withAuth(accessToken)
        .withHeaders(headers)
        .withData(data))

  def post[A](
      accessToken: Option[String] = None,
      url: String,
      headers: Map[String, String] = Map.empty,
      data: String
  )(implicit D: Decoder[A]): M[GHResponse[A]] =
    httpRbImpl.run[A](
      httpRequestBuilder(buildURL(url)).postMethod
        .withAuth(accessToken)
        .withHeaders(headers)
        .withData(data))

  def postAuth[A](
      method: String,
      headers: Map[String, String] = Map.empty,
      data: String
  )(implicit D: Decoder[A]): M[GHResponse[A]] =
    httpRbImpl.run[A](
      httpRequestBuilder(buildURL(method)).postMethod.withHeaders(headers).withData(data))

  def postOAuth[A](
      url: String,
      headers: Map[String, String] = Map.empty,
      data: String
  )(implicit D: Decoder[A]): M[GHResponse[A]] =
    httpRbImpl.run[A](
      httpRequestBuilder(url).postMethod
        .withHeaders(Map("Accept" → "application/json") ++ headers)
        .withData(data))

  def delete(
      accessToken: Option[String] = None,
      method: String,
      headers: Map[String, String] = Map.empty): M[GHResponse[Unit]] =
    httpRbImpl.runEmpty(
      httpRequestBuilder(buildURL(method)).deleteMethod.withHeaders(headers).withAuth(accessToken))

  def deleteWithResponse[A](
      accessToken: Option[String] = None,
      url: String,
      headers: Map[String, String] = Map.empty
  )(implicit D: Decoder[A]): M[GHResponse[A]] =
    httpRbImpl.run[A](
      httpRequestBuilder(buildURL(url)).deleteMethod
        .withAuth(accessToken)
        .withHeaders(headers))

  private def buildURL(method: String) = urls.baseUrl + method

  val defaultPage: Int    = 1
  val defaultPerPage: Int = 30
}
