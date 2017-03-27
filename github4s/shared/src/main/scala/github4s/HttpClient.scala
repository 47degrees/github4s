/*
 * Copyright (c) 2016 47 Degrees, LLC. <http://www.47deg.com>
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
 * the Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
 * FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
 * IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package github4s

import github4s.free.domain.Pagination
import io.circe.Decoder
import github4s.GithubResponses.GHResponse
import github4s.HttpClient._
import github4s.free.interpreters.Capture

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

  case object HttpCode400 extends HttpStatus {
    def statusCode = 400
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
      headers: Map[String, String] = Map(),
      method: String)(implicit D: Decoder[A]): M[GHResponse[A]] =
    httpRbImpl.run[A](
      httpRequestBuilder(buildURL(method)).putMethod
        .withAuth(accessToken)
        .withHeaders(Map("Content-Length" → "0") ++ headers))

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

  def delete[A](
      accessToken: Option[String] = None,
      method: String,
      headers: Map[String, String] = Map.empty)(implicit D: Decoder[A]): M[GHResponse[A]] =
    httpRbImpl.run[A](
      httpRequestBuilder(buildURL(method)).deleteMethod.withHeaders(headers).withAuth(accessToken))

  private def buildURL(method: String) = urls.baseUrl + method

  val defaultPage: Int    = 1
  val defaultPerPage: Int = 30
}
