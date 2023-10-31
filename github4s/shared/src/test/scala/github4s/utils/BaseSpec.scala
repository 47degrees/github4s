/*
 * Copyright 2016-2023 47 Degrees Open Source <https://www.47deg.com>
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

package github4s.utils

import cats.effect.{unsafe, IO}
import github4s.http.HttpClient
import github4s.interpreters.StaticAccessToken
import github4s.{GithubConfig, IOAssertions}
import io.circe.{Decoder, Encoder}
import org.http4s.client.Client
import org.http4s.syntax.all._
import org.http4s._
import org.scalatest.flatspec.AsyncFlatSpec
import org.scalatest.matchers.should.Matchers
import org.typelevel.ci.CIString

import scala.concurrent.ExecutionContext

trait BaseSpec extends AsyncFlatSpec with Matchers with TestData with IOAssertions {
  import org.http4s.circe.CirceEntityDecoder._
  import org.http4s.circe.CirceEntityEncoder._
  import org.http4s.dsl.io._

  protected implicit val ec: ExecutionContext        = TestContext.context
  protected implicit val ioRuntime: unsafe.IORuntime = unsafe.IORuntime.global
  protected val dummyConfig: GithubConfig = GithubConfig(
    baseUrl = "http://127.0.0.1:9999/",
    authorizeUrl = "http://127.0.0.1:9999/authorize?client_id=%s&redirect_uri=%s&scope=%s&state=%s",
    accessTokenUrl = "http://127.0.0.1:9999/login/oauth/access_token",
    Map.empty
  )

  private val userAgent = headerUserAgent.toList.map { case (k, v) => Header.Raw(CIString(k), v) }

  protected def httpClientMockGet[Out: Encoder](
      url: String,
      params: Map[String, String] = Map.empty,
      headers: Map[String, String] = Map.empty,
      response: IO[Out],
      responseStatus: Status = Status.Ok,
      respHeaders: Headers = Headers.empty
  ): HttpClient[IO] = {
    val httpClientMock = httpMock {
      case req
          if req.uri == Uri.unsafeFromString(url) &&
            (req.headers == (Headers(userAgent) ++ Headers(headers.toList))) &&
            req.params == params =>
        response.map(body => Response[IO](responseStatus).withEntity(body).putHeaders(respHeaders))
    }
    new HttpClient(httpClientMock, dummyConfig, new StaticAccessToken(sampleToken))
  }

  protected def httpClientMockGetWithoutResponse(
      url: String,
      response: IO[Unit],
      responseStatus: Status = Status.Ok,
      respHeaders: Headers = Headers.empty
  ): HttpClient[IO] = {
    val httpClientMock = httpMock {
      case req
          if req.uri == Uri.unsafeFromString(url) &&
            req.headers == Headers(userAgent) =>
        response.map(_ => Response[IO](responseStatus).putHeaders(respHeaders))
    }
    new HttpClient(httpClientMock, dummyConfig, new StaticAccessToken(sampleToken))
  }

  protected def httpClientMockPost[In: Decoder, Out: Encoder](
      url: String,
      req: In,
      response: IO[Out],
      responseStatus: Status = Status.Ok,
      respHeaders: Headers = Headers.empty
  ): HttpClient[IO] =
    httpInOut(POST, url, req, response, responseStatus, respHeaders)

  protected def httpClientMockPostOAuth[Out: Encoder](
      url: String,
      response: IO[Out],
      responseStatus: Status = Status.Ok,
      respHeaders: Headers = Headers.empty
  ): HttpClient[IO] = {
    val httpClientMock = httpMock {
      case req @ POST -> _
          if req.uri == Uri.unsafeFromString(url) &&
            req.headers == Headers(userAgent) =>
        response.map(body => Response[IO](responseStatus).withEntity(body).putHeaders(respHeaders))
    }

    new HttpClient(httpClientMock, dummyConfig, new StaticAccessToken(sampleToken))
  }

  protected def httpClientMockPatch[In: Decoder, Out: Encoder](
      url: String,
      req: In,
      response: IO[Out],
      responseStatus: Status = Status.Ok,
      respHeaders: Headers = Headers.empty
  ): HttpClient[IO] =
    httpInOut(PATCH, url, req, response, responseStatus, respHeaders)

  protected def httpClientMockPut[In: Decoder, Out: Encoder](
      url: String,
      req: In,
      response: IO[Out],
      responseStatus: Status = Status.Ok,
      respHeaders: Headers = Headers.empty
  ): HttpClient[IO] =
    httpInOut(PUT, url, req, response, responseStatus, respHeaders)

  protected def httpClientMockDelete(
      url: String,
      response: IO[Unit],
      responseStatus: Status = Status.Ok,
      respHeaders: Headers = Headers.empty
  ): HttpClient[IO] = {

    val httpClientMock = httpMock {
      case req @ DELETE -> _
          if req.uri == Uri.unsafeFromString(url) &&
            req.headers == Headers(userAgent) =>
        response.as(Response[IO](responseStatus).putHeaders(respHeaders))
    }
    new HttpClient(httpClientMock, dummyConfig, new StaticAccessToken(sampleToken))
  }

  protected def httpClientMockDeleteWithResponse[Out: Encoder](
      url: String,
      response: IO[Out],
      responseStatus: Status = Status.Ok,
      respHeaders: Headers = Headers.empty
  ): HttpClient[IO] = {
    val httpClientMock = httpMock {
      case req @ DELETE -> _
          if req.uri == Uri.unsafeFromString(url) &&
            req.headers == Headers(userAgent) =>
        response.map(body => Response[IO](responseStatus).withEntity(body).putHeaders(respHeaders))
    }
    new HttpClient(httpClientMock, dummyConfig, new StaticAccessToken(sampleToken))
  }

  protected def httpClientMockDeleteWithBody[In: Decoder, Out: Encoder](
      url: String,
      req: In,
      response: IO[Out],
      responseStatus: Status = Status.Ok,
      respHeaders: Headers = Headers.empty
  ): HttpClient[IO] =
    httpInOut(DELETE, url, req, response, responseStatus, respHeaders)

  private def httpInOut[In: Decoder, Out: Encoder](
      method: Method,
      url: String,
      req: In,
      response: IO[Out],
      responseStatus: Status,
      respHeaders: Headers
  ) = {

    val input = req
    val httpClientMock = httpMock {
      case req @ `method` -> _
          if req.uri == Uri.unsafeFromString(url) &&
            req.headers == Headers(userAgent) =>
        val validate = req
          .as[In]
          .flatMap(reqBody =>
            IO.raiseWhen(reqBody != input)(
              new IllegalArgumentException(s"Expected $input, got $reqBody")
            )
          )
        validate >> response.map(body =>
          Response[IO](responseStatus).withEntity(body).putHeaders(respHeaders)
        )
    }
    new HttpClient(httpClientMock, dummyConfig, new StaticAccessToken(sampleToken))
  }

  private def httpMock(pf: PartialFunction[Request[IO], IO[Response[IO]]]) =
    Client.fromHttpApp(HttpRoutes.of[IO](pf).orNotFound)

}
