/*
 * Copyright 2016-2020 47 Degrees <https://www.47deg.com>
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
import github4s.GithubConfig
import github4s.GithubResponses.GHResponse
import github4s.domain.Pagination
import github4s.http.HttpClient
import io.circe.{Decoder, Encoder}
import org.http4s.client.Client
import org.scalamock.scalatest.MockFactory
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

trait BaseSpec extends AnyFlatSpec with Matchers with TestData with MockFactory {

  implicit val ec = scala.concurrent.ExecutionContext.Implicits.global
  implicit val io = cats.effect.IO.contextShift(ec)
  implicit val dummyConfig: GithubConfig = GithubConfig(
    baseUrl = "http://127.0.0.1:9999/",
    authorizeUrl = "http://127.0.0.1:9999/authorize?client_id=%s&redirect_uri=%s&scope=%s&state=%s",
    accessTokenUrl = "http://127.0.0.1:9999/login/oauth/access_token"
  )

  @com.github.ghik.silencer.silent("deprecated")
  class HttpClientTest extends HttpClient[IO](mock[Client[IO]], implicitly)

  def httpClientMockGet[Out](
      url: String,
      params: Map[String, String] = Map.empty,
      headers: Map[String, String] = Map.empty,
      response: IO[GHResponse[Out]]
  ): HttpClient[IO] = {
    val httpClientMock = mock[HttpClientTest]
    (httpClientMock
      .get[Out](
        _: Option[String],
        _: String,
        _: Map[String, String],
        _: Map[String, String],
        _: Option[Pagination]
      )(_: Decoder[Out]))
      .expects(sampleToken, url, headers ++ headerUserAgent, params, *, *)
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
      .post[In, Out](_: Option[String], _: String, _: Map[String, String], _: In)(
        _: Encoder[In],
        _: Decoder[Out]
      ))
      .expects(sampleToken, url, headerUserAgent, req, *, *)
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

  def httpClientMockPostOAuth[In, Out](
      url: String,
      req: In,
      response: IO[GHResponse[Out]]
  ): HttpClient[IO] = {
    val httpClientMock = mock[HttpClientTest]
    (httpClientMock
      .postOAuth[In, Out](_: String, _: Map[String, String], _: In)(
        _: Encoder[In],
        _: Decoder[Out]
      ))
      .expects(url, headerUserAgent, req, *, *)
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
      .patch[In, Out](_: Option[String], _: String, _: Map[String, String], _: In)(
        _: Encoder[In],
        _: Decoder[Out]
      ))
      .expects(sampleToken, url, headerUserAgent, req, *, *)
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
      .put[In, Out](_: Option[String], _: String, _: Map[String, String], _: In)(
        _: Encoder[In],
        _: Decoder[Out]
      ))
      .expects(sampleToken, url, headerUserAgent, req, *, *)
      .returns(response)
    httpClientMock
  }

  def httpClientMockDelete(url: String, response: IO[GHResponse[Unit]]): HttpClient[IO] = {
    val httpClientMock = mock[HttpClientTest]
    (httpClientMock
      .delete(_: Option[String], _: String, _: Map[String, String]))
      .expects(sampleToken, url, headerUserAgent)
      .returns(response)
    httpClientMock
  }

  def httpClientMockDeleteWithResponse[Out](
      url: String,
      response: IO[GHResponse[Out]]
  ): HttpClient[IO] = {
    val httpClientMock = mock[HttpClientTest]
    (httpClientMock
      .deleteWithResponse[Out](_: Option[String], _: String, _: Map[String, String])(
        _: Decoder[Out]
      ))
      .expects(sampleToken, url, headerUserAgent, *)
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
      .deleteWithBody[In, Out](_: Option[String], _: String, _: Map[String, String], _: In)(
        _: Encoder[In],
        _: Decoder[Out]
      ))
      .expects(sampleToken, url, headerUserAgent, req, *, *)
      .returns(response)
    httpClientMock
  }

}
