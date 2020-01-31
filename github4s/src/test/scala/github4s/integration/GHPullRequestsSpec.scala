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

package github4s.integration

import cats.effect.IO
import github4s.GithubIOSyntax._
import github4s.Github
import github4s.domain._
import github4s.utils.{BaseIntegrationSpec, Integration}

trait GHPullRequestsSpec extends BaseIntegrationSpec {

  "PullRequests >> Get" should "return a right response when a valid pr number is provided" taggedAs Integration in {
    val response =
      Github[IO](accessToken).pullRequests
        .get(validRepoOwner, validRepoName, validPullRequestNumber, headers = headerUserAgent)
        .toFuture

    testFutureIsRight[PullRequest](response, { r =>
      r.statusCode shouldBe okStatusCode
    })
  }

  it should "return an error when a valid issue number is provided" taggedAs Integration in {
    val response =
      Github[IO](accessToken).pullRequests
        .get(validRepoOwner, validRepoName, validIssueNumber, headers = headerUserAgent)
        .toFuture

    testFutureIsLeft(response)
  }

  it should "return an error when an invalid repo name is passed" taggedAs Integration in {
    val response =
      Github[IO](accessToken).pullRequests
        .get(validRepoOwner, invalidRepoName, validPullRequestNumber, headers = headerUserAgent)
        .toFuture

    testFutureIsLeft(response)
  }

  "PullRequests >> List" should "return a right response when valid repo is provided" taggedAs Integration in {
    val response =
      Github[IO](accessToken).pullRequests
        .list(
          validRepoOwner,
          validRepoName,
          pagination = Some(Pagination(1, 10)),
          headers = headerUserAgent)
        .toFuture

    testFutureIsRight[List[PullRequest]](response, { r =>
      r.statusCode shouldBe okStatusCode
    })
  }

  it should "return a right response when a valid repo is provided but not all pull requests have body" taggedAs Integration in {
    val response =
      Github[IO](accessToken).pullRequests
        .list("lloydmeta", "gh-test-repo", List(PRFilterOpen), headers = headerUserAgent)
        .toFuture

    testFutureIsRight[List[PullRequest]](response, { r =>
      r.result.nonEmpty shouldBe true
      r.statusCode shouldBe okStatusCode
    })
  }

  it should "return a non empty list when valid repo and some filters are provided" taggedAs Integration in {
    val response =
      Github[IO](accessToken).pullRequests
        .list(
          validRepoOwner,
          validRepoName,
          List(PRFilterAll, PRFilterSortCreated, PRFilterOrderAsc),
          headers = headerUserAgent)
        .toFuture

    testFutureIsRight[List[PullRequest]](response, { r =>
      r.result.nonEmpty shouldBe true
      r.statusCode shouldBe okStatusCode
    })
  }

  it should "return error when an invalid repo name is passed" taggedAs Integration in {
    val response =
      Github[IO](accessToken).pullRequests
        .list(validRepoOwner, invalidRepoName, headers = headerUserAgent)
        .toFuture

    testFutureIsLeft(response)
  }

  "PullRequests >> ListFiles" should "return a right response when a valid repo is provided" taggedAs Integration in {
    val response =
      Github[IO](accessToken).pullRequests
        .listFiles(validRepoOwner, validRepoName, validPullRequestNumber, headers = headerUserAgent)
        .toFuture

    testFutureIsRight[List[PullRequestFile]](response, { r =>
      r.result.nonEmpty shouldBe true
      r.statusCode shouldBe okStatusCode
    })
  }

  it should "return a right response when a valid repo is provided and not all files have 'patch'" taggedAs Integration in {
    val response =
      Github[IO](accessToken).pullRequests
        .listFiles("scala", "scala", 4877, headers = headerUserAgent)
        .toFuture

    testFutureIsRight[List[PullRequestFile]](response, { r =>
      r.result.nonEmpty shouldBe true
      r.statusCode shouldBe okStatusCode
    })
  }

  it should "return error when an invalid repo name is passed" taggedAs Integration in {
    val response =
      Github[IO](accessToken).pullRequests
        .listFiles(
          validRepoOwner,
          invalidRepoName,
          validPullRequestNumber,
          headers = headerUserAgent)
        .toFuture

    testFutureIsLeft(response)
  }

  "PullRequests >> ListReviews" should "return a right response when a valid pr is provided" taggedAs Integration in {
    val response =
      Github[IO](accessToken).pullRequests
        .listReviews(
          validRepoOwner,
          validRepoName,
          validPullRequestNumber,
          headers = headerUserAgent)
        .toFuture

    testFutureIsRight[List[PullRequestReview]](response, { r =>
      r.result.nonEmpty shouldBe true
      r.statusCode shouldBe okStatusCode
    })
  }

  it should "return error when an invalid repo name is passed" taggedAs Integration in {
    val response =
      Github[IO](accessToken).pullRequests
        .listReviews(
          validRepoOwner,
          invalidRepoName,
          validPullRequestNumber,
          headers = headerUserAgent)
        .toFuture

    testFutureIsLeft(response)
  }

  "PullRequests >> GetReview" should "return a right response when a valid pr review is provided" taggedAs Integration in {
    val response =
      Github[IO](accessToken).pullRequests
        .getReview(
          validRepoOwner,
          validRepoName,
          validPullRequestNumber,
          validPullRequestReviewNumber,
          headers = headerUserAgent)
        .toFuture

    testFutureIsRight[PullRequestReview](response, { r =>
      r.result.id shouldBe validPullRequestReviewNumber
      r.statusCode shouldBe okStatusCode
    })
  }

  it should "return error when an invalid repo name is passed" taggedAs Integration in {
    val response =
      Github[IO](accessToken).pullRequests
        .getReview(
          validRepoOwner,
          invalidRepoName,
          validPullRequestNumber,
          validPullRequestReviewNumber,
          headers = headerUserAgent)
        .toFuture

    testFutureIsLeft(response)
  }

}
