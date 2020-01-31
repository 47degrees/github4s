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
import cats.data.NonEmptyList
import github4s.Github
import github4s.domain._
import github4s.utils.{BaseIntegrationSpec, Integration}

trait GHReposSpec extends BaseIntegrationSpec {

  "Repos >> Get" should "return the expected name when a valid repo is provided" taggedAs Integration in {
    val response =
      Github[IO](accessToken).repos
        .get(validRepoOwner, validRepoName, headers = headerUserAgent)
        .toFuture

    testFutureIsRight[Repository](response, { r =>
      r.result.name shouldBe validRepoName
      r.statusCode shouldBe okStatusCode
    })
  }

  it should "return error when an invalid repo name is passed" taggedAs Integration in {
    val response =
      Github[IO](accessToken).repos
        .get(validRepoOwner, invalidRepoName, headers = headerUserAgent)
        .toFuture

    testFutureIsLeft(response)
  }

  "Repos >> ListOrgRepos" should "return the expected repos when a valid org is provided" taggedAs Integration in {
    val response =
      Github[IO](accessToken).repos
        .listOrgRepos(validRepoOwner, headers = headerUserAgent)
        .toFuture

    testFutureIsRight[List[Repository]](response, { r =>
      r.result.nonEmpty shouldBe true
      r.statusCode shouldBe okStatusCode
    })
  }

  it should "return error when an invalid org is passed" taggedAs Integration in {
    val response =
      Github[IO](accessToken).repos
        .listOrgRepos(invalidRepoName, headers = headerUserAgent)
        .toFuture

    testFutureIsLeft(response)
  }

  "Repos >> ListUserRepos" should "return the expected repos when a valid user is provided" taggedAs Integration in {
    val response = Github[IO](accessToken).repos
      .listUserRepos(validUsername, headers = headerUserAgent)
      .toFuture

    testFutureIsRight[List[Repository]](response, { r =>
      r.result.nonEmpty shouldBe true
      r.statusCode shouldBe okStatusCode
    })
  }

  it should "return error when an invalid user is passed" taggedAs Integration in {
    val response = Github[IO](accessToken).repos
      .listUserRepos(invalidUsername, headers = headerUserAgent)
      .toFuture

    testFutureIsLeft(response)
  }

  "Repos >> GetContents" should "return the expected contents when valid path is provided" taggedAs Integration in {
    val response =
      Github[IO](accessToken).repos
        .getContents(validRepoOwner, validRepoName, validFilePath, headers = headerUserAgent)
        .toFuture

    testFutureIsRight[NonEmptyList[Content]](response, { r =>
      r.result.head.path shouldBe validFilePath
      r.statusCode shouldBe okStatusCode
    })
  }

  it should "return error when an invalid path is passed" taggedAs Integration in {
    val response =
      Github[IO](accessToken).repos
        .getContents(validRepoOwner, validRepoName, invalidFilePath, headers = headerUserAgent)
        .toFuture
    testFutureIsLeft(response)
  }

  "Repos >> ListCommits" should "return the expected list of commits for valid data" taggedAs Integration in {
    val response =
      Github[IO](accessToken).repos
        .listCommits(validRepoOwner, validRepoName, headers = headerUserAgent)
        .toFuture

    testFutureIsRight[List[Commit]](response, { r =>
      r.result.nonEmpty shouldBe true
      r.statusCode shouldBe okStatusCode
    })
  }

  it should "return error for invalid repo name" taggedAs Integration in {
    val response =
      Github[IO](accessToken).repos
        .listCommits(invalidRepoName, validRepoName, headers = headerUserAgent)
        .toFuture

    testFutureIsLeft(response)
  }

  "Repos >> ListBranches" should "return the expected list of branches for valid data" taggedAs Integration in {
    val response =
      Github[IO](accessToken).repos
        .listBranches(validRepoOwner, validRepoName, headers = headerUserAgent)
        .toFuture

    testFutureIsRight[List[Branch]](response, { r =>
      r.result.nonEmpty shouldBe true
      r.statusCode shouldBe okStatusCode
    })
  }

  it should "return error for invalid repo name" taggedAs Integration in {
    val response =
      Github[IO](accessToken).repos
        .listBranches(invalidRepoName, validRepoName, headers = headerUserAgent)
        .toFuture

    testFutureIsLeft(response)
  }

  "Repos >> ListContributors" should "return the expected list of contributors for valid data" taggedAs Integration in {
    val response =
      Github[IO](accessToken).repos
        .listContributors(validRepoOwner, validRepoName, headers = headerUserAgent)
        .toFuture

    testFutureIsRight[List[User]](response, { r =>
      r.result shouldNot be(empty)
      r.statusCode shouldBe okStatusCode
    })
  }

  it should "return error for invalid repo name" taggedAs Integration in {
    val response =
      Github[IO](accessToken).repos
        .listContributors(invalidRepoName, validRepoName, headers = headerUserAgent)
        .toFuture

    testFutureIsLeft(response)
  }

  "Repos >> ListCollaborators" should "return the expected list of collaborators for valid data" taggedAs Integration in {
    val response =
      Github[IO](accessToken).repos
        .listCollaborators(validRepoOwner, validRepoName, headers = headerUserAgent)
        .toFuture

    testFutureIsRight[List[User]](response, { r =>
      r.result shouldNot be(empty)
      r.statusCode shouldBe okStatusCode
    })
  }

  it should "return error for invalid repo name" taggedAs Integration in {
    val response =
      Github[IO](accessToken).repos
        .listCollaborators(invalidRepoName, validRepoName, headers = headerUserAgent)
        .toFuture

    testFutureIsLeft(response)
  }

  "Repos >> GetStatus" should "return a combined status" taggedAs Integration in {
    val response = Github[IO](accessToken).repos
      .getCombinedStatus(validRepoOwner, validRepoName, validRefSingle, headers = headerUserAgent)
      .toFuture

    testFutureIsRight[CombinedStatus](response, { r =>
      r.result.repository.full_name shouldBe s"$validRepoOwner/$validRepoName"
      r.statusCode shouldBe okStatusCode
    })
  }

  it should "return an error when an invalid ref is passed" taggedAs Integration in {
    val response = Github[IO](accessToken).repos
      .getCombinedStatus(validRepoOwner, validRepoName, invalidRef, headers = headerUserAgent)
      .toFuture
    testFutureIsLeft(response)
  }

  "Repos >> ListStatus" should "return a non empty list when a valid ref is provided" taggedAs Integration in {
    val response = Github[IO](accessToken).repos
      .listStatuses(validRepoOwner, validRepoName, validCommitSha, headers = headerUserAgent)
      .toFuture

    testFutureIsRight[List[Status]](response, { r =>
      r.result.nonEmpty shouldBe true
      r.statusCode shouldBe okStatusCode
    })
  }

  it should "return an error when an invalid ref is provided" taggedAs Integration in {
    val response = Github[IO](accessToken).repos
      .listStatuses(validRepoOwner, validRepoName, invalidRef, headers = headerUserAgent)
      .toFuture
    testFutureIsLeft(response)
  }
}
