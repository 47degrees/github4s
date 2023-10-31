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

package github4s.integration

import cats.effect.IO
import github4s.GHError.{JsonParsingError, NotFoundError}
import github4s.Github
import github4s.domain._
import github4s.utils.{BaseIntegrationSpec, Integration}

trait PullRequestsSpec extends BaseIntegrationSpec {

  "PullRequests >> Get" should "return a right response when a valid pr number is provided" taggedAs Integration in {
    val response = clientResource
      .use { client =>
        Github[IO](client, accessToken).pullRequests
          .getPullRequest(
            validRepoOwner,
            validRepoName,
            validPullRequestNumber,
            headers = headerUserAgent
          )
      }
      .unsafeRunSync()

    testIsRight[PullRequest](response)
    response.statusCode shouldBe okStatusCode
  }

  it should "return an error when a valid issue number is provided" taggedAs Integration in {
    val response = clientResource
      .use { client =>
        Github[IO](client, accessToken).pullRequests
          .getPullRequest(
            validRepoOwner,
            validRepoName,
            validIssueNumber,
            headers = headerUserAgent
          )
      }
      .unsafeRunSync()

    testIsLeft[NotFoundError, PullRequest](response)
    response.statusCode shouldBe notFoundStatusCode
  }

  it should "return an error when an invalid repo name is passed" taggedAs Integration in {
    val response = clientResource
      .use { client =>
        Github[IO](client, accessToken).pullRequests
          .getPullRequest(
            validRepoOwner,
            invalidRepoName,
            validPullRequestNumber,
            headers = headerUserAgent
          )
      }
      .unsafeRunSync()

    testIsLeft[NotFoundError, PullRequest](response)
    response.statusCode shouldBe notFoundStatusCode
  }

  "PullRequests >> List" should "return a right response when valid repo is provided" taggedAs Integration in {
    val response = clientResource
      .use { client =>
        Github[IO](client, accessToken).pullRequests
          .listPullRequests(
            validRepoOwner,
            validRepoName,
            pagination = Some(Pagination(1, 10)),
            headers = headerUserAgent
          )
      }
      .unsafeRunSync()

    testIsRight[List[PullRequest]](response)
    response.statusCode shouldBe okStatusCode
  }

  it should "return a right response when a valid repo is provided but not all pull requests have body" taggedAs Integration in {
    val response = clientResource
      .use { client =>
        Github[IO](client, accessToken).pullRequests
          .listPullRequests(
            "lloydmeta",
            "gh-test-repo",
            List(PRFilterOpen),
            headers = headerUserAgent
          )
      }
      .unsafeRunSync()

    testIsRight[List[PullRequest]](response, r => r.nonEmpty shouldBe true)
    response.statusCode shouldBe okStatusCode
  }

  it should "return a non empty list when valid repo and some filters are provided" taggedAs Integration in {
    val response = clientResource
      .use { client =>
        Github[IO](client, accessToken).pullRequests
          .listPullRequests(
            validRepoOwner,
            validRepoName,
            List(PRFilterAll, PRFilterSortCreated, PRFilterOrderAsc),
            headers = headerUserAgent
          )
      }
      .unsafeRunSync()

    testIsRight[List[PullRequest]](response, r => r.nonEmpty shouldBe true)
    response.statusCode shouldBe okStatusCode
  }

  it should "return error when an invalid repo name is passed" taggedAs Integration in {
    val response = clientResource
      .use { client =>
        Github[IO](client, accessToken).pullRequests
          .listPullRequests(validRepoOwner, invalidRepoName, headers = headerUserAgent)
      }
      .unsafeRunSync()

    testIsLeft[NotFoundError, List[PullRequest]](response)
    response.statusCode shouldBe notFoundStatusCode
  }

  "PullRequests >> ListFiles" should "return a right response when a valid repo is provided" taggedAs Integration in {
    val response = clientResource
      .use { client =>
        Github[IO](client, accessToken).pullRequests
          .listFiles(
            validRepoOwner,
            validRepoName,
            validPullRequestNumber,
            headers = headerUserAgent
          )
      }
      .unsafeRunSync()

    testIsRight[List[PullRequestFile]](response, r => r.nonEmpty shouldBe true)
    response.statusCode shouldBe okStatusCode
  }

  it should "return a right response when a valid repo is provided and not all files have 'patch'" taggedAs Integration in {
    val response = clientResource
      .use { client =>
        Github[IO](client, accessToken).pullRequests
          .listFiles("scala", "scala", 4877, headers = headerUserAgent)
      }
      .unsafeRunSync()

    testIsRight[List[PullRequestFile]](response, r => r.nonEmpty shouldBe true)
    response.statusCode shouldBe okStatusCode
  }

  it should "return error when an invalid repo name is passed" taggedAs Integration in {
    val response = clientResource
      .use { client =>
        Github[IO](client, accessToken).pullRequests
          .listFiles(
            validRepoOwner,
            invalidRepoName,
            validPullRequestNumber,
            headers = headerUserAgent
          )
      }
      .unsafeRunSync()

    testIsLeft[NotFoundError, List[PullRequestFile]](response)
    response.statusCode shouldBe notFoundStatusCode
  }

  "PullRequests >> ListReviews" should "return a right response when a valid pr is provided" taggedAs Integration in {
    val response = clientResource
      .use { client =>
        Github[IO](client, accessToken).pullRequests
          .listReviews(
            validRepoOwner,
            validRepoName,
            validPullRequestNumber,
            headers = headerUserAgent
          )
      }
      .unsafeRunSync()

    testIsRight[List[PullRequestReview]](response, r => r.nonEmpty shouldBe true)
    response.statusCode shouldBe okStatusCode
  }

  it should "return error when an invalid repo name is passed" taggedAs Integration in {
    val response = clientResource
      .use { client =>
        Github[IO](client, accessToken).pullRequests
          .listReviews(
            validRepoOwner,
            invalidRepoName,
            validPullRequestNumber,
            headers = headerUserAgent
          )
      }
      .unsafeRunSync()

    testIsLeft[NotFoundError, List[PullRequestReview]](response)
    response.statusCode shouldBe notFoundStatusCode
  }

  "PullRequests >> GetReview" should "return a right response when a valid pr review is provided" taggedAs Integration in {
    val response = clientResource
      .use { client =>
        Github[IO](client, accessToken).pullRequests
          .getReview(
            validRepoOwner,
            validRepoName,
            validPullRequestReview,
            validPullRequestReviewNumber,
            headers = headerUserAgent
          )
      }
      .unsafeRunSync()

    testIsRight[PullRequestReview](response, r => r.id shouldBe validPullRequestReviewNumber)
    response.statusCode shouldBe okStatusCode
  }

  it should "return error when an invalid repo name is passed" taggedAs Integration in {
    val response = clientResource
      .use { client =>
        Github[IO](client, accessToken).pullRequests
          .getReview(
            validRepoOwner,
            invalidRepoName,
            validPullRequestNumber,
            validPullRequestReviewNumber,
            headers = headerUserAgent
          )
      }
      .unsafeRunSync()

    testIsLeft[NotFoundError, PullRequestReview](response)
    response.statusCode shouldBe notFoundStatusCode
  }

  "PullRequests >> CreateReview" should "return a created review" taggedAs Integration in {
    val response = clientResource
      .use { client =>
        Github[IO](client, accessToken).pullRequests
          .createReview(
            validRepoOwner,
            validRepoName,
            validPullRequestReview,
            validCreatePRReviewRequest,
            headers = headerUserAgent
          )
      }
      .unsafeRunSync()

    testIsRight[PullRequestReview](
      response,
      r => {
        r.body shouldBe validCreatePRReviewRequest.body
        r.state shouldBe PRRStateApproved
      }
    )
    response.statusCode shouldBe okStatusCode
  }

  it should "return an error when invalid review data was passed" taggedAs Integration in {
    val response = clientResource
      .use { client =>
        Github[IO](client, accessToken).pullRequests
          .createReview(
            validRepoOwner,
            validRepoName,
            validPullRequestReview,
            invalidCreatePRReviewRequest,
            headers = headerUserAgent
          )
      }
      .unsafeRunSync()

    testIsLeft[JsonParsingError, PullRequestReview](response)
    response.statusCode shouldBe unprocessableEntityStatusCode
  }

  "PullRequests >> Add/List/Remove Reviewers" should "return the proper reviewers" taggedAs Integration ignore {
    val addReviewersResponse = clientResource
      .use { client =>
        Github[IO](client, accessToken).pullRequests
          .addReviewers(
            validRepoOwner,
            validRepoName,
            validPullRequestNumber,
            validReviewers,
            headers = headerUserAgent
          )
      }
      .unsafeRunSync()

    testIsRight[PullRequest](
      addReviewersResponse,
      r => r.body shouldBe Some(validCreatePRReviewRequest.body)
    )
    addReviewersResponse.statusCode shouldBe okStatusCode

    val getReviewersResponse = clientResource
      .use { client =>
        Github[IO](client, accessToken).pullRequests
          .listReviewers(
            validRepoOwner,
            validRepoName,
            validPullRequestNumber,
            headers = headerUserAgent
          )
      }
      .unsafeRunSync()

    testIsRight[ReviewersResponse](
      getReviewersResponse,
      r => {
        r.users.map(_.login) shouldBe List(validUsername)
        r.teams.map(_.slug) shouldBe List(validSlug)
      }
    )
    getReviewersResponse.statusCode shouldBe okStatusCode

    val removeReviewersResponse = clientResource
      .use { client =>
        Github[IO](client, accessToken).pullRequests
          .removeReviewers(
            validRepoOwner,
            validRepoName,
            validPullRequestNumber,
            validReviewers,
            headers = headerUserAgent
          )
      }
      .unsafeRunSync()

    testIsRight[PullRequest](
      addReviewersResponse,
      r => r.body shouldBe Some(validCreatePRReviewRequest.body)
    )
    removeReviewersResponse.statusCode shouldBe okStatusCode
  }

  "PullRequests >> Update Branch" should "merge target branch's head into selected" taggedAs Integration ignore {
    val response = clientResource
      .use { client =>
        Github[IO](client, accessToken).pullRequests
          .updateBranch(
            validRepoOwner,
            validRepoName,
            692,
            headers = headerUserAgent
          )
      }
      .unsafeRunSync()

    testIsRight[BranchUpdateResponse](response)
    response.statusCode shouldBe acceptedStatusCode
  }
}
