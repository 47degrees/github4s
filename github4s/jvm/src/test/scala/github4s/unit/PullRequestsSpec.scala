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
import github4s.api.PullRequests
import github4s.free.domain._
import github4s.utils.{DummyGithubUrls, TestUtils}
import github4s.{HttpClient, HttpRequestBuilderExtensionJVM, IdInstances}
import io.circe.Decoder
import org.mockito.ArgumentMatchers.{any, eq => argEq}
import org.mockito.Mockito._
import org.scalatest.mockito.MockitoSugar.mock
import org.scalatest.{FlatSpec, Matchers}

import scalaj.http.HttpResponse

class PullRequestsSpec
    extends FlatSpec
    with Matchers
    with TestUtils
    with DummyGithubUrls
    with IdInstances
    with HttpRequestBuilderExtensionJVM {

  "PullRequests.list" should "call to httpClient.get with the right parameters" in {

    val response: GHResponse[List[PullRequest]] =
      Right(GHResult(List(pullRequest), okStatusCode, Map.empty))

    val httpClientMock = mock[HttpClient[HttpResponse[String], Id]]
    when(
      httpClientMock.get[List[PullRequest]](
        any[Option[String]],
        any[String],
        any[Map[String, String]],
        any[Map[String, String]],
        any[Option[Pagination]])(any[Decoder[List[PullRequest]]]))
      .thenReturn(response)

    val token = Some("token")
    val pullRequests = new PullRequests[HttpResponse[String], Id] {
      override val httpClient: HttpClient[HttpResponse[String], Id] = httpClientMock
    }
    pullRequests.list(token, headerUserAgent, validRepoOwner, validRepoName, Nil)

    verify(httpClientMock).get[List[PullRequest]](
      argEq(token),
      argEq(s"repos/$validRepoOwner/$validRepoName/pulls"),
      argEq(headerUserAgent),
      any[Map[String, String]],
      any[Option[Pagination]]
    )(any[Decoder[List[PullRequest]]])
  }

  "PullRequests.listFiles" should "call to httpClient.get with the right parameters" in {

    val response: GHResponse[List[PullRequestFile]] =
      Right(GHResult(List(pullRequestFile), okStatusCode, Map.empty))

    val httpClientMock = mock[HttpClient[HttpResponse[String], Id]]
    when(
      httpClientMock.get[List[PullRequestFile]](
        any[Option[String]],
        any[String],
        any[Map[String, String]],
        any[Map[String, String]],
        any[Option[Pagination]])(any[Decoder[List[PullRequestFile]]]))
      .thenReturn(response)

    val token = Some("token")
    val pullRequests = new PullRequests[HttpResponse[String], Id] {
      override val httpClient: HttpClient[HttpResponse[String], Id] = httpClientMock
    }
    pullRequests
      .listFiles(token, headerUserAgent, validRepoOwner, validRepoName, validPullRequestNumber)

    verify(httpClientMock).get[List[PullRequestFile]](
      argEq(token),
      argEq(s"repos/$validRepoOwner/$validRepoName/pulls/$validPullRequestNumber/files"),
      argEq(headerUserAgent),
      any[Map[String, String]],
      any[Option[Pagination]]
    )(any[Decoder[List[PullRequestFile]]])
  }

}
