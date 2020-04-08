/*
 * Copyright 2016-2020 47 Degrees Open Source <https://www.47deg.com>
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

package github4s.http

import cats.data.EitherT
import cats.effect.Sync
import cats.instances.string._
import cats.syntax.applicative._
import cats.syntax.either._
import cats.syntax.functor._
import github4s._
import github4s.GHError._
import github4s.domain.Pagination
import github4s.http.Http4sSyntax._
import org.http4s.{Request, Response, Status}
import org.http4s.client.Client
import org.http4s.circe.CirceEntityDecoder._
import io.circe.{Decoder, Encoder}

class HttpClient[F[_]: Sync](client: Client[F], val config: GithubConfig) {

  def get[Res: Decoder](
      accessToken: Option[String] = None,
      method: String,
      headers: Map[String, String] = Map.empty,
      params: Map[String, String] = Map.empty,
      pagination: Option[Pagination] = None
  ): F[GHResponse[Res]] =
    run[Unit, Res](
      RequestBuilder(url = buildURL(method))
        .withAuth(accessToken)
        .withHeaders(headers)
        .withParams(
          params ++ pagination.fold(Map.empty[String, String])(p =>
            Map("page" -> p.page.toString, "per_page" -> p.per_page.toString)
          )
        )
    )

  def patch[Req: Encoder, Res: Decoder](
      accessToken: Option[String] = None,
      method: String,
      headers: Map[String, String] = Map.empty,
      data: Req
  ): F[GHResponse[Res]] =
    run[Req, Res](
      RequestBuilder(buildURL(method)).patchMethod
        .withAuth(accessToken)
        .withHeaders(headers)
        .withData(data)
    )

  def put[Req: Encoder, Res: Decoder](
      accessToken: Option[String] = None,
      url: String,
      headers: Map[String, String] = Map(),
      data: Req
  ): F[GHResponse[Res]] =
    run[Req, Res](
      RequestBuilder(buildURL(url)).putMethod
        .withAuth(accessToken)
        .withHeaders(headers)
        .withData(data)
    )

  def post[Req: Encoder, Res: Decoder](
      accessToken: Option[String] = None,
      url: String,
      headers: Map[String, String] = Map.empty,
      data: Req
  ): F[GHResponse[Res]] =
    run[Req, Res](
      RequestBuilder(buildURL(url)).postMethod
        .withAuth(accessToken)
        .withHeaders(headers)
        .withData(data)
    )

  def postAuth[Req: Encoder, Res: Decoder](
      method: String,
      headers: Map[String, String] = Map.empty,
      data: Req
  ): F[GHResponse[Res]] =
    run[Req, Res](RequestBuilder(buildURL(method)).postMethod.withHeaders(headers).withData(data))

  def postOAuth[Req: Encoder, Res: Decoder](
      url: String,
      headers: Map[String, String] = Map.empty,
      data: Req
  ): F[GHResponse[Res]] =
    run[Req, Res](
      RequestBuilder(url).postMethod
        .withHeaders(Map("Accept" -> "application/json") ++ headers)
        .withData(data)
    )

  def delete(
      accessToken: Option[String] = None,
      url: String,
      headers: Map[String, String] = Map.empty
  ): F[GHResponse[Unit]] =
    run[Unit, Unit](
      RequestBuilder(buildURL(url)).deleteMethod.withHeaders(headers).withAuth(accessToken)
    )

  def deleteWithResponse[Res: Decoder](
      accessToken: Option[String] = None,
      url: String,
      headers: Map[String, String] = Map.empty
  ): F[GHResponse[Res]] =
    run[Unit, Res](
      RequestBuilder(buildURL(url)).deleteMethod
        .withAuth(accessToken)
        .withHeaders(headers)
    )

  def deleteWithBody[Req: Encoder, Res: Decoder](
      accessToken: Option[String] = None,
      url: String,
      headers: Map[String, String] = Map.empty,
      data: Req
  ): F[GHResponse[Res]] =
    run[Req, Res](
      RequestBuilder(buildURL(url)).deleteMethod
        .withAuth(accessToken)
        .withHeaders(headers)
        .withData(data)
    )

  val defaultPagination   = Pagination(1, 1000)
  val defaultPage: Int    = 1
  val defaultPerPage: Int = 30

  private def buildURL(method: String): String = s"${config.baseUrl}$method"

  private def run[Req: Encoder, Res: Decoder](request: RequestBuilder[Req]): F[GHResponse[Res]] = {
    client
      .run(
        Request[F]()
          .withMethod(request.httpVerb)
          .withUri(request.toUri(config))
          .withHeaders(request.toHeaderList: _*)
          .withJsonBody(request.data)
      )
      .use { response =>
        buildResponse(response).map(GHResponse(_, response.status.code, response.headers.toMap))
      }
  }

  private def buildResponse[A: Decoder](response: Response[F]): F[Either[GHError, A]] =
    (response.status.code match {
      case i if Status(i).isSuccess => response.attemptAs[A].map(_.asRight)
      case 400                      => response.attemptAs[BadRequestError].map(_.asLeft)
      case 401                      => response.attemptAs[UnauthorizedError].map(_.asLeft)
      case 403                      => response.attemptAs[ForbiddenError].map(_.asLeft)
      case 404                      => response.attemptAs[NotFoundError].map(_.asLeft)
      case 422                      => response.attemptAs[UnprocessableEntityError].map(_.asLeft)
      case 423                      => response.attemptAs[RateLimitExceededError].map(_.asLeft)
      case _ =>
        EitherT
          .right(responseBody(response))
          .map(s =>
            UnknownError(s"Could not build response with code ${response.status.code}", s).asLeft
          )
    }).foldF(
      e => responseBody(response).map(UnknownError(e.message, _).asLeft),
      _.leftMap(e => e: GHError).pure[F]
    )

  private def responseBody(response: Response[F]): F[String] =
    response.bodyAsText.compile.foldMonoid
}
