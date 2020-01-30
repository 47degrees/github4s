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

package github4s.unit

import cats.effect.IO
import github4s.GithubResponses.{GHResponse, GHResult}
import github4s.http.HttpClient
import github4s.interpreters.PullRequestsInterpreter
import github4s.domain._
import github4s.utils.BaseSpec

class PullRequestsSpec extends BaseSpec {

  implicit val token = sampleToken

  "PullRequests.get" should "call to httpClient.get with the right parameters" in {

    val response: IO[GHResponse[PullRequest]] =
      IO(Right(GHResult(pullRequest, okStatusCode, Map.empty)))

    implicit val httpClientMock = httpClientMockGet[PullRequest](
      url = s"repos/$validRepoOwner/$validRepoName/pulls/$validPullRequestNumber",
      response = response
    )
    val pullRequests = new PullRequestsInterpreter[IO]
    pullRequests.get(validRepoOwner,validRepoName,validPullRequestNumber,headerUserAgent)

  }

  "PullRequests.list" should "call to httpClient.get with the right parameters" in {

    val response: IO[GHResponse[List[PullRequest]]] =
      IO(Right(GHResult(List(pullRequest), okStatusCode, Map.empty)))

    implicit val httpClientMock = httpClientMockGet[List[PullRequest]](
      url = s"repos/$validRepoOwner/$validRepoName/pulls",
      response = response
    )
    val pullRequests = new PullRequestsInterpreter[IO]
    pullRequests.list(validRepoOwner,validRepoName,Nil,headerUserAgent)

  }

  "PullRequests.listFiles" should "call to httpClient.get with the right parameters" in {

    val response: IO[GHResponse[List[PullRequestFile]]] =
      IO(Right(GHResult(List(pullRequestFile), okStatusCode, Map.empty)))

    implicit val httpClientMock = httpClientMockGet[List[PullRequestFile]](
      url = s"repos/$validRepoOwner/$validRepoName/pulls/$validPullRequestNumber/files",
      response = response
    )
    val pullRequests = new PullRequestsInterpreter[IO]
    pullRequests
      .listFiles(validRepoOwner,validRepoName,validPullRequestNumber,headerUserAgent)

  }

  "GHPullRequests.create" should "call to httpClient.post with the right parameters" in {

    val response: IO[GHResponse[PullRequest]] =
      IO(Right(GHResult(pullRequest, okStatusCode, Map.empty)))

    val request = NewPullRequestData("Title","Body")

    implicit val httpClientMock = httpClientMockPost[NewPullRequest, PullRequest](
      url = s"repos/$validRepoOwner/$validRepoName/pulls",
      req = request,
      response = response
    )

    val pullRequests = new PullRequestsInterpreter[IO]

    pullRequests.create(validRepoOwner,validRepoName,validNewPullRequestData,validHead,validBase,Some(true),headerUserAgent)

  }

  "GHPullRequests.create" should "call to httpClient.post with the right parameters" in {

    val response: IO[GHResponse[PullRequest]] =
      IO(Right(GHResult(pullRequest, okStatusCode, Map.empty)))

    val request = NewPullRequestIssue(31)

    implicit val httpClientMock = httpClientMockPost[NewPullRequest, PullRequest](
      url = s"repos/$validRepoOwner/$validRepoName/pulls",
      req = request,
      response = response
    )

    val pullRequests = new PullRequestsInterpreter[IO]

    pullRequests.create(validRepoOwner,validRepoName,validNewPullRequestIssue,validHead,validBase,Some(true),headerUserAgent)

  }

  "GHPullRequests.listReviews" should "call to httpClient.post with the right parameters" in {

    val response: IO[GHResponse[List[PullRequestReview]]] =
      IO(Right(GHResult(List(pullRequestReview), okStatusCode, Map.empty)))

    implicit val httpClientMock = httpClientMockGet[List[PullRequestReview]](
      url = s"repos/$validRepoOwner/$validRepoName/pulls/$validPullRequestNumber/reviews",
      response = response
    )

    val pullRequests = new PullRequestsInterpreter[IO]

    pullRequests.listReviews(validRepoOwner,validRepoName,validPullRequestNumber,headerUserAgent)

  }

  "GHPullRequests.getReview" should "call to httpClient.post with the right parameters" in {

    val response: IO[GHResponse[PullRequestReview]] =
      IO(Right(GHResult(pullRequestReview, okStatusCode, Map.empty)))

    implicit val httpClientMock = httpClientMockGet[PullRequestReview](
      url =
        s"repos/$validRepoOwner/$validRepoName/pulls/$validPullRequestNumber/reviews/$validPullRequestReviewNumber",
      response = response
    )

    val pullRequests = new PullRequestsInterpreter[IO]

    pullRequests.getReview(validRepoOwner,validRepoName,validPullRequestNumber,validPullRequestReviewNumber,headerUserAgent)

  }

}
