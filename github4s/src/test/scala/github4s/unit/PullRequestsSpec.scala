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
import cats.syntax.either._
import github4s.GithubResponses.GHResponse
import github4s.interpreters.PullRequestsInterpreter
import github4s.domain._
import github4s.utils.BaseSpec

class PullRequestsSpec extends BaseSpec {

  implicit val token = sampleToken

  "PullRequests.get" should "call to httpClient.get with the right parameters" in {

    val response: IO[GHResponse[PullRequest]] =
      IO(GHResponse(pullRequest.asRight, okStatusCode, Map.empty))

    implicit val httpClientMock = httpClientMockGet[PullRequest](
      url = s"repos/$validRepoOwner/$validRepoName/pulls/$validPullRequestNumber",
      response = response
    )
    val pullRequests = new PullRequestsInterpreter[IO]
    pullRequests.getPullRequest(
      validRepoOwner,
      validRepoName,
      validPullRequestNumber,
      headerUserAgent
    )

  }

  "PullRequests.list" should "call to httpClient.get with the right parameters" in {

    val response: IO[GHResponse[List[PullRequest]]] =
      IO(GHResponse(List(pullRequest).asRight, okStatusCode, Map.empty))

    implicit val httpClientMock = httpClientMockGet[List[PullRequest]](
      url = s"repos/$validRepoOwner/$validRepoName/pulls",
      response = response
    )
    val pullRequests = new PullRequestsInterpreter[IO]
    pullRequests.listPullRequests(validRepoOwner, validRepoName, Nil, None, headerUserAgent)

  }

  "PullRequests.listFiles" should "call to httpClient.get with the right parameters" in {

    val response: IO[GHResponse[List[PullRequestFile]]] =
      IO(GHResponse(List(pullRequestFile).asRight, okStatusCode, Map.empty))

    implicit val httpClientMock = httpClientMockGet[List[PullRequestFile]](
      url = s"repos/$validRepoOwner/$validRepoName/pulls/$validPullRequestNumber/files",
      response = response
    )
    val pullRequests = new PullRequestsInterpreter[IO]
    pullRequests
      .listFiles(validRepoOwner, validRepoName, validPullRequestNumber, None, headerUserAgent)

  }

  "PullRequests.create data" should "call to httpClient.post with the right parameters" in {
    val response: IO[GHResponse[PullRequest]] =
      IO(GHResponse(pullRequest.asRight, okStatusCode, Map.empty))

    val request = CreatePullRequestData(
      "Amazing new feature",
      validHead,
      validBase,
      "Please pull this in!",
      Some(true)
    )

    implicit val httpClientMock = httpClientMockPost[CreatePullRequestData, PullRequest](
      url = s"repos/$validRepoOwner/$validRepoName/pulls",
      req = request,
      response = response
    )

    val pullRequests = new PullRequestsInterpreter[IO]

    pullRequests.createPullRequest(
      validRepoOwner,
      validRepoName,
      validNewPullRequestData,
      validHead,
      validBase,
      Some(true),
      headerUserAgent
    )

  }

  "PullRequests.create issue" should "call to httpClient.post with the right parameters" in {
    val response: IO[GHResponse[PullRequest]] =
      IO(GHResponse(pullRequest.asRight, okStatusCode, Map.empty))

    val request = CreatePullRequestIssue(31, validHead, validBase, Some(true))

    implicit val httpClientMock = httpClientMockPost[CreatePullRequestIssue, PullRequest](
      url = s"repos/$validRepoOwner/$validRepoName/pulls",
      req = request,
      response = response
    )

    val pullRequests = new PullRequestsInterpreter[IO]

    pullRequests.createPullRequest(
      validRepoOwner,
      validRepoName,
      validNewPullRequestIssue,
      validHead,
      validBase,
      Some(true),
      headerUserAgent
    )

  }

  "GHPullRequests.listReviews" should "call to httpClient.post with the right parameters" in {
    val response: IO[GHResponse[List[PullRequestReview]]] =
      IO(GHResponse(List(pullRequestReview).asRight, okStatusCode, Map.empty))

    implicit val httpClientMock = httpClientMockGet[List[PullRequestReview]](
      url = s"repos/$validRepoOwner/$validRepoName/pulls/$validPullRequestNumber/reviews",
      response = response
    )

    val pullRequests = new PullRequestsInterpreter[IO]

    pullRequests.listReviews(
      validRepoOwner,
      validRepoName,
      validPullRequestNumber,
      None,
      headerUserAgent
    )

  }

  "GHPullRequests.getReview" should "call to httpClient.post with the right parameters" in {
    val response: IO[GHResponse[PullRequestReview]] =
      IO(GHResponse(pullRequestReview.asRight, okStatusCode, Map.empty))

    implicit val httpClientMock = httpClientMockGet[PullRequestReview](
      url =
        s"repos/$validRepoOwner/$validRepoName/pulls/$validPullRequestNumber/reviews/$validPullRequestReviewNumber",
      response = response
    )

    val pullRequests = new PullRequestsInterpreter[IO]

    pullRequests.getReview(
      validRepoOwner,
      validRepoName,
      validPullRequestNumber,
      validPullRequestReviewNumber,
      headerUserAgent
    )

  }

}
