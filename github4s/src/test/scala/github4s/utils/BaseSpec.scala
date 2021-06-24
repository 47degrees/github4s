/*
 * Copyright 2016-2021 47 Degrees Open Source <https://www.47deg.com>
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

import cats.effect.IO
import cats.effect.unsafe
import github4s.domain.Pagination
import github4s.http.HttpClient
import github4s.interpreters.StaticAccessToken
import github4s.{GHResponse, GithubConfig}
import io.circe.{Decoder, Encoder}
import org.http4s.client.Client
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

import scala.annotation.nowarn
import scala.concurrent.ExecutionContext
import org.http4s.HttpApp
import org.http4s.HttpRoutes
import org.http4s.Response
import org.http4s.Status
import org.http4s.Uri
import github4s.Decoders._
import github4s.Encoders._

trait BaseSpec extends AnyFlatSpec with Matchers with TestData {
  import org.http4s.circe.CirceEntityEncoder._
  import org.http4s.dsl.io._

  implicit val ec: ExecutionContext        = scala.concurrent.ExecutionContext.Implicits.global
  implicit val ioRuntime: unsafe.IORuntime = unsafe.IORuntime.global
  val dummyConfig: GithubConfig = GithubConfig(
    baseUrl = "http://127.0.0.1:9999/",
    authorizeUrl = "http://127.0.0.1:9999/authorize?client_id=%s&redirect_uri=%s&scope=%s&state=%s",
    accessTokenUrl = "http://127.0.0.1:9999/login/oauth/access_token",
    Map.empty
  )

  @nowarn("msg=deprecated")
  class HttpClientTest
      extends HttpClient[IO](mock[Client[IO]], implicitly, new StaticAccessToken(sampleToken))

  def httpClientMockGet[Out: Encoder](
      url: String,
      params: Map[String, String] = Map.empty,
      headers: Map[String, String] = Map.empty,
      response: IO[GHResponse[Out]]
  ): HttpClient[IO] = {
    val httpClientMock = Client.fromHttpApp[IO](HttpRoutes.of {
      case req
          if req.uri == Uri.unsafeFromString(url) &&
            req.headers == (headers ++ headerUserAgent) &&
            req.params == params =>
        response.map(ghr =>
          Response(Status.fromInt(ghr.statusCode).getOrElse(e => fail(e)), body = ghr.result)
        )
    }.orNotFound)
    new HttpClient(httpClientMock, dummyConfig, new StaticAccessToken(sampleToken))
  }

  def httpClientMockGetWithoutResponse(
      url: String,
      response: IO[GHResponse[Unit]]
  ): HttpClient[IO] = {
    val httpClientMock = mock[HttpClientTest]
    (httpClientMock
      .getWithoutResponse(_: String, _: Map[String, String]))
      .expects(url, headerUserAgent)
      .returns(response)
    httpClientMock
  }

  def httpClientMockPost[In, Out](
      url: String,
      req: In,
      response: IO[GHResponse[Out]]
  ): HttpClient[IO] = {
    val httpClientMock = mock[HttpClientTest]
    (httpClientMock
      .post[In, Out](_: String, _: Map[String, String], _: In)(
        _: Encoder[In],
        _: Decoder[Out]
      ))
      .expects(url, headerUserAgent, req, *, *)
      .returns(response)
    httpClientMock
  }

  def httpClientMockPostAuth[In, Out](
      url: String,
      headers: Map[String, String],
      req: In,
      response: IO[GHResponse[Out]]
  ): HttpClient[IO] = {
    val httpClientMock = mock[HttpClientTest]
    (httpClientMock
      .postAuth[In, Out](_: String, _: Map[String, String], _: In)(
        _: Encoder[In],
        _: Decoder[Out]
      ))
      .expects(url, headers ++ headerUserAgent, req, *, *)
      .returns(response)
    httpClientMock
  }

  def httpClientMockPostOAuth[Out](
      url: String,
      response: IO[GHResponse[Out]]
  ): HttpClient[IO] = {
    val httpClientMock = mock[HttpClientTest]
    (httpClientMock
      .postOAuth[Out](_: String, _: Map[String, String], _: Map[String, String])(
        _: Decoder[Out]
      ))
      .expects(url, headerUserAgent, *, *)
      .returns(response)
    httpClientMock
  }

  def httpClientMockPatch[In, Out](
      url: String,
      req: In,
      response: IO[GHResponse[Out]]
  ): HttpClient[IO] = {
    val httpClientMock = mock[HttpClientTest]
    (httpClientMock
      .patch[In, Out](_: String, _: Map[String, String], _: In)(
        _: Encoder[In],
        _: Decoder[Out]
      ))
      .expects(url, headerUserAgent, req, *, *)
      .returns(response)
    httpClientMock
  }

  def httpClientMockPut[In, Out](
      url: String,
      req: In,
      response: IO[GHResponse[Out]]
  ): HttpClient[IO] = {
    val httpClientMock = mock[HttpClientTest]
    (httpClientMock
      .put[In, Out](_: String, _: Map[String, String], _: In)(
        _: Encoder[In],
        _: Decoder[Out]
      ))
      .expects(url, headerUserAgent, req, *, *)
      .returns(response)
    httpClientMock
  }

  def httpClientMockDelete(url: String, response: IO[GHResponse[Unit]]): HttpClient[IO] = {
    val httpClientMock = mock[HttpClientTest]
    (httpClientMock
      .delete(_: String, _: Map[String, String]))
      .expects(url, headerUserAgent)
      .returns(response)
    httpClientMock
  }

  def httpClientMockDeleteWithResponse[Out](
      url: String,
      response: IO[GHResponse[Out]]
  ): HttpClient[IO] = {
    val httpClientMock = mock[HttpClientTest]
    (httpClientMock
      .deleteWithResponse[Out](_: String, _: Map[String, String])(
        _: Decoder[Out]
      ))
      .expects(url, headerUserAgent, *)
      .returns(response)
    httpClientMock
  }

  def httpClientMockDeleteWithBody[In, Out](
      url: String,
      req: In,
      response: IO[GHResponse[Out]]
  ): HttpClient[IO] = {
    val httpClientMock = mock[HttpClientTest]
    (httpClientMock
      .deleteWithBody[In, Out](_: String, _: Map[String, String], _: In)(
        _: Encoder[In],
        _: Decoder[Out]
      ))
      .expects(url, headerUserAgent, req, *, *)
      .returns(response)
    httpClientMock
  }

}
