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

package github4s.unit

import cats.Id
import cats.data.NonEmptyList
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

}
