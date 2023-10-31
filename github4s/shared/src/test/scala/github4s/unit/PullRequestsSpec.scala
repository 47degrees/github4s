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

package github4s.unit

import cats.effect.IO
import github4s.Decoders._
import github4s.Encoders._
import github4s.domain._
import github4s.http.HttpClient
import github4s.interpreters.PullRequestsInterpreter
import github4s.utils.BaseSpec

class PullRequestsSpec extends BaseSpec {

  "PullRequests.get" should "call to httpClient.get with the right parameters" in {

    implicit val httpClientMock: HttpClient[IO] = httpClientMockGet[PullRequest](
      url = s"repos/$validRepoOwner/$validRepoName/pulls/$validPullRequestNumber",
      response = IO.pure(pullRequest)
    )
    val pullRequests = new PullRequestsInterpreter[IO]
    pullRequests
      .getPullRequest(
        validRepoOwner,
        validRepoName,
        validPullRequestNumber,
        headerUserAgent
      )
      .shouldNotFail

  }

  "PullRequests.list" should "call to httpClient.get with the right parameters" in {

    implicit val httpClientMock: HttpClient[IO] = httpClientMockGet[List[PullRequest]](
      url = s"repos/$validRepoOwner/$validRepoName/pulls",
      response = IO.pure(List(pullRequest))
    )
    val pullRequests = new PullRequestsInterpreter[IO]
    pullRequests
      .listPullRequests(validRepoOwner, validRepoName, Nil, None, headerUserAgent)
      .shouldNotFail
  }

  "PullRequests.listFiles" should "call to httpClient.get with the right parameters" in {

    implicit val httpClientMock: HttpClient[IO] = httpClientMockGet[List[PullRequestFile]](
      url = s"repos/$validRepoOwner/$validRepoName/pulls/$validPullRequestNumber/files",
      response = IO.pure(List(pullRequestFile))
    )
    val pullRequests = new PullRequestsInterpreter[IO]
    pullRequests
      .listFiles(validRepoOwner, validRepoName, validPullRequestNumber, None, headerUserAgent)
      .shouldNotFail
  }

  "PullRequests.create data" should "call to httpClient.post with the right parameters" in {

    val request = CreatePullRequestData(
      "Amazing new feature",
      validHead,
      validBase,
      "Please pull this in!",
      Some(true),
      draft
    )

    implicit val httpClientMock: HttpClient[IO] =
      httpClientMockPost[CreatePullRequestData, PullRequest](
        url = s"repos/$validRepoOwner/$validRepoName/pulls",
        req = request,
        response = IO.pure(pullRequest)
      )

    val pullRequests = new PullRequestsInterpreter[IO]

    pullRequests
      .createPullRequest(
        validRepoOwner,
        validRepoName,
        validNewPullRequestData,
        validHead,
        validBase,
        Some(true),
        headerUserAgent
      )
      .shouldNotFail

  }

  "PullRequests.create issue" should "call to httpClient.post with the right parameters" in {

    val request = CreatePullRequestIssue(31, validHead, validBase, Some(true))

    implicit val httpClientMock: HttpClient[IO] =
      httpClientMockPost[CreatePullRequestIssue, PullRequest](
        url = s"repos/$validRepoOwner/$validRepoName/pulls",
        req = request,
        response = IO.pure(pullRequest)
      )

    val pullRequests = new PullRequestsInterpreter[IO]

    pullRequests
      .createPullRequest(
        validRepoOwner,
        validRepoName,
        validNewPullRequestIssue,
        validHead,
        validBase,
        Some(true),
        headerUserAgent
      )
      .shouldNotFail

  }

  "PullRequests.listReviews" should "call to httpClient.get with the right parameters" in {

    implicit val httpClientMock: HttpClient[IO] = httpClientMockGet[List[PullRequestReview]](
      url = s"repos/$validRepoOwner/$validRepoName/pulls/$validPullRequestNumber/reviews",
      response = IO.pure(List(pullRequestReview))
    )

    val pullRequests = new PullRequestsInterpreter[IO]

    pullRequests
      .listReviews(
        validRepoOwner,
        validRepoName,
        validPullRequestNumber,
        None,
        headerUserAgent
      )
      .shouldNotFail

  }

  "PullRequests.getReview" should "call to httpClient.get with the right parameters" in {

    implicit val httpClientMock: HttpClient[IO] = httpClientMockGet[PullRequestReview](
      url =
        s"repos/$validRepoOwner/$validRepoName/pulls/$validPullRequestNumber/reviews/$validPullRequestReviewNumber",
      response = IO.pure(pullRequestReview)
    )

    val pullRequests = new PullRequestsInterpreter[IO]

    pullRequests
      .getReview(
        validRepoOwner,
        validRepoName,
        validPullRequestNumber,
        validPullRequestReviewNumber,
        headerUserAgent
      )
      .shouldNotFail

  }

  "PullRequests.createReview" should "call to httpClient.post with the right parameters" in {

    implicit val httpClientMock: HttpClient[IO] =
      httpClientMockPost[CreatePRReviewRequest, PullRequestReview](
        url = s"repos/$validRepoOwner/$validRepoName/pulls/$validPullRequestNumber/reviews",
        req = validCreatePRReviewRequest,
        response = IO.pure(pullRequestReview)
      )

    val pullRequests = new PullRequestsInterpreter[IO]

    pullRequests
      .createReview(
        validRepoOwner,
        validRepoName,
        validPullRequestNumber,
        validCreatePRReviewRequest,
        headerUserAgent
      )
      .shouldNotFail
  }

  "PullRequests.addReviewers" should "call to httpClient.post with the right parameters" in {

    implicit val httpClientMock: HttpClient[IO] =
      httpClientMockPost[ReviewersRequest, PullRequestReview](
        url =
          s"repos/$validRepoOwner/$validRepoName/pulls/$validPullRequestNumber/requested_reviewers",
        req = validRequestedReviewersRequest,
        response = IO.pure(pullRequestReview)
      )

    val pullRequests = new PullRequestsInterpreter[IO]

    pullRequests
      .addReviewers(
        validRepoOwner,
        validRepoName,
        validPullRequestNumber,
        validRequestedReviewersRequest,
        headerUserAgent
      )
      .shouldNotFail
  }

  "PullRequests.removeReviewers" should "call to httpClient.delete with the right parameters" in {

    implicit val httpClientMock: HttpClient[IO] =
      httpClientMockDeleteWithBody[ReviewersRequest, PullRequestReview](
        url =
          s"repos/$validRepoOwner/$validRepoName/pulls/$validPullRequestNumber/requested_reviewers",
        req = validRequestedReviewersRequest,
        response = IO.pure(pullRequestReview)
      )

    val pullRequests = new PullRequestsInterpreter[IO]

    pullRequests
      .removeReviewers(
        validRepoOwner,
        validRepoName,
        validPullRequestNumber,
        validRequestedReviewersRequest,
        headerUserAgent
      )
      .shouldNotFail
  }

  "PullRequests.listReviewers" should "call to httpClient.get with the right parameters" in {

    implicit val httpClientMock: HttpClient[IO] = httpClientMockGet[ReviewersResponse](
      url =
        s"repos/$validRepoOwner/$validRepoName/pulls/$validPullRequestNumber/requested_reviewers",
      response = IO.pure(validRequestedReviewersResponse)
    )

    val pullRequests = new PullRequestsInterpreter[IO]

    pullRequests
      .listReviewers(
        validRepoOwner,
        validRepoName,
        validPullRequestNumber,
        None,
        headerUserAgent
      )
      .shouldNotFail
  }

  "PullRequests.updateBranch" should "call to httpClient.put with the right parameters" in {

    implicit val httpClientMock: HttpClient[IO] =
      httpClientMockPut[BranchUpdateRequest, BranchUpdateResponse](
        url = s"repos/$validRepoOwner/$validRepoName/pulls/$validPullRequestNumber/update-branch",
        req = BranchUpdateRequest(None),
        response = IO.pure(validBranchUpdateResponse)
      )

    val pullRequests = new PullRequestsInterpreter[IO]

    pullRequests
      .updateBranch(
        validRepoOwner,
        validRepoName,
        validPullRequestNumber,
        None,
        headerUserAgent
      )
      .shouldNotFail
  }
}
