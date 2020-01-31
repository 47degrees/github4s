/*
 * Copyright 2016-2020 47 Degrees, LLC. <http://www.47deg.com>
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

import github4s.GithubResponses.GHResponse
import github4s.domain.Pagination
import github4s.http.HttpClient
import io.circe.{Decoder, Encoder}
import org.scalamock.scalatest.MockFactory
import org.scalatest.matchers.should.Matchers
import org.scalatest.flatspec.AnyFlatSpec

import scala.concurrent.duration.Duration
import java.util.concurrent.TimeUnit

trait BaseSpec extends AnyFlatSpec with Matchers with TestData with MockFactory {

  implicit val ec = scala.concurrent.ExecutionContext.Implicits.global
  implicit val io = cats.effect.IO.contextShift(ec)
  import cats.effect.IO

  class HttpClientTest extends HttpClient[IO](Duration(1000, TimeUnit.MILLISECONDS))

  def httpClientMockGet[Res](
      url: String,
      params: Map[String, String] = Map.empty,
      response: IO[GHResponse[Res]]): HttpClient[IO] = {
    val httpClientMock = mock[HttpClientTest]
    (httpClientMock
      .get[Res](
        _: Option[String],
        _: String,
        _: Map[String, String],
        _: Map[String, String],
        _: Option[Pagination])(_: Decoder[Res]))
      .expects(sampleToken, url, headerUserAgent, params, *, *)
      .returns(response)
    httpClientMock
  }

  def httpClientMockPost[Req, Res](
      url: String,
      req: Req,
      response: IO[GHResponse[Res]]): HttpClient[IO] = {
    val httpClientMock = mock[HttpClientTest]
    (httpClientMock
      .post[Req, Res](_: Option[String], _: String, _: Map[String, String], _: Req)(
        _: Encoder[Req],
        _: Decoder[Res]))
      .expects(sampleToken, url, headerUserAgent, req, *, *)
      .returns(response)
    httpClientMock
  }

  def httpClientMockPostAuth[Req, Res](
      url: String,
      headers: Map[String, String],
      req: Req,
      response: IO[GHResponse[Res]]): HttpClient[IO] = {
    val httpClientMock = mock[HttpClientTest]
    (httpClientMock
      .postAuth[Req, Res](_: String, _: Map[String, String], _: Req)(
        _: Encoder[Req],
        _: Decoder[Res]))
      .expects(url, headers ++ headerUserAgent, req, *, *)
      .returns(response)
    httpClientMock
  }

  def httpClientMockPostOAuth[Req, Res](
      url: String,
      req: Req,
      response: IO[GHResponse[Res]]): HttpClient[IO] = {
    val httpClientMock = mock[HttpClientTest]
    (httpClientMock
      .postOAuth[Req, Res](_: String, _: Map[String, String], _: Req)(
        _: Encoder[Req],
        _: Decoder[Res]))
      .expects(url, headerUserAgent, req, *, *)
      .returns(response)
    httpClientMock
  }

  def httpClientMockPatch[Req, Res](
      url: String,
      req: Req,
      response: IO[GHResponse[Res]]): HttpClient[IO] = {
    val httpClientMock = mock[HttpClientTest]
    (httpClientMock
      .patch[Req, Res](_: Option[String], _: String, _: Map[String, String], _: Req)(
        _: Encoder[Req],
        _: Decoder[Res]))
      .expects(sampleToken, url, headerUserAgent, req, *, *)
      .returns(response)
    httpClientMock
  }

  def httpClientMockPut[Req, Res](
      url: String,
      req: Req,
      response: IO[GHResponse[Res]]): HttpClient[IO] = {
    val httpClientMock = mock[HttpClientTest]
    (httpClientMock
      .put[Req, Res](_: Option[String], _: String, _: Map[String, String], _: Req)(
        _: Encoder[Req],
        _: Decoder[Res]))
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

  def httpClientMockDeleteWithResponse[Res](
      url: String,
      response: IO[GHResponse[Res]]): HttpClient[IO] = {
    val httpClientMock = mock[HttpClientTest]
    (httpClientMock
      .deleteWithResponse[Res](_: Option[String], _: String, _: Map[String, String])(
        _: Decoder[Res]))
      .expects(sampleToken, url, headerUserAgent, *)
      .returns(response)
    httpClientMock
  }

}
