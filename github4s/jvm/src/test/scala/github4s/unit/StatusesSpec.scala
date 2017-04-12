/*
 * Copyright 2016-2017 47 Degrees, LLC. <http://www.47deg.com>
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

package github4s.unit

import cats.Id
import github4s.GithubResponses.{GHResponse, GHResult}
import github4s.api.Statuses
import github4s.free.domain._
import github4s.utils.{DummyGithubUrls, TestUtils}
import github4s.{HttpClient, HttpRequestBuilderExtensionJVM, IdInstances}
import io.circe.Decoder
import org.mockito.ArgumentMatchers.{any, eq => argEq}
import org.mockito.Mockito._
import org.scalatest.{FlatSpec, Matchers}
import org.scalatest.mockito.MockitoSugar.mock

import scalaj.http.HttpResponse

class StatusesSpec
    extends FlatSpec
    with Matchers
    with TestUtils
    with DummyGithubUrls
    with IdInstances
    with HttpRequestBuilderExtensionJVM {

  "Statuses.list" should "call htppClient.get with the right parameters" in {
    val response: GHResponse[List[Status]] =
      Right(GHResult(List(status), okStatusCode, Map.empty))

    val httpClientMock = mock[HttpClient[HttpResponse[String], Id]]
    when(
      httpClientMock.get[List[Status]](
        any[Option[String]],
        any[String],
        any[Map[String, String]],
        any[Map[String, String]],
        any[Option[Pagination]])(any[Decoder[List[Status]]]))
      .thenReturn(response)

    val token = Some("token")
    val statuses = new Statuses[HttpResponse[String], Id] {
      override val httpClient: HttpClient[HttpResponse[String], Id] = httpClientMock
    }
    statuses.list(token, headerUserAgent, validRepoOwner, validRepoName, validRefSingle)

    verify(httpClientMock).get[List[Status]](
      argEq(token),
      argEq(s"repos/$validRepoOwner/$validRepoName/commits/$validRefSingle/statuses"),
      argEq(headerUserAgent),
      any[Map[String, String]],
      any[Option[Pagination]]
    )(any[Decoder[List[Status]]])
  }
}
