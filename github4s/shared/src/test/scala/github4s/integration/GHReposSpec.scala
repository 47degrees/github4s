/*
 * Copyright 2016-2018 47 Degrees, LLC. <http://www.47deg.com>
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

import cats.data.NonEmptyList
import github4s.Github
import github4s.Github._
import github4s.free.domain._
import github4s.implicits._
import github4s.utils.BaseIntegrationSpec

trait GHReposSpec[T] extends BaseIntegrationSpec[T] {

  "Repos >> Get" should "return the expected name when a valid repo is provided" in {
    val response =
      Github(accessToken).repos
        .get(validRepoOwner, validRepoName)
        .execFuture[T](headerUserAgent)

    testFutureIsRight[Repository](response, { r =>
      r.result.name shouldBe validRepoName
      r.statusCode shouldBe okStatusCode
    })
  }

  it should "return error when an invalid repo name is passed" in {
    val response =
      Github(accessToken).repos
        .get(validRepoOwner, invalidRepoName)
        .execFuture[T](headerUserAgent)

    testFutureIsLeft(response)
  }

  "Repos >> ListOrgRepos" should "return the expected repos when a valid org is provided" in {
    val response =
      Github(accessToken).repos
        .listOrgRepos(validRepoOwner)
        .execFuture[T](headerUserAgent)

    testFutureIsRight[List[Repository]](response, { r =>
      r.result.nonEmpty shouldBe true
      r.statusCode shouldBe okStatusCode
    })
  }

  it should "return error when an invalid org is passed" in {
    val response =
      Github(accessToken).repos
        .listOrgRepos(invalidRepoName)
        .execFuture[T](headerUserAgent)

    testFutureIsLeft(response)
  }

  "Repos >> GetContents" should "return the expected contents when valid path is provided" in {
    val response =
      Github(accessToken).repos
        .getContents(validRepoOwner, validRepoName, validFilePath)
        .execFuture[T](headerUserAgent)

    testFutureIsRight[NonEmptyList[Content]](response, { r =>
      r.result.head.path shouldBe validFilePath
      r.statusCode shouldBe okStatusCode
    })
  }

  it should "return error when an invalid path is passed" in {
    val response =
      Github(accessToken).repos
        .getContents(validRepoOwner, validRepoName, invalidFilePath)
        .execFuture[T](headerUserAgent)
    testFutureIsLeft(response)
  }

  "Repos >> ListCommits" should "return the expected list of commits for valid data" in {
    val response =
      Github(accessToken).repos
        .listCommits(validRepoOwner, validRepoName)
        .execFuture[T](headerUserAgent)

    testFutureIsRight[List[Commit]](response, { r =>
      r.result.nonEmpty shouldBe true
      r.statusCode shouldBe okStatusCode
    })
  }

  it should "return error for invalid repo name" in {
    val response =
      Github(accessToken).repos
        .listCommits(invalidRepoName, validRepoName)
        .execFuture[T](headerUserAgent)

    testFutureIsLeft(response)
  }

  "Repos >> ListContributors" should "return the expected list of contributors for valid data" in {
    val response =
      Github(accessToken).repos
        .listContributors(validRepoOwner, validRepoName)
        .execFuture[T](headerUserAgent)

    testFutureIsRight[List[User]](response, { r =>
      r.result shouldNot be(empty)
      r.statusCode shouldBe okStatusCode
    })
  }

  it should "return error for invalid repo name" in {
    val response =
      Github(accessToken).repos
        .listContributors(invalidRepoName, validRepoName)
        .execFuture[T](headerUserAgent)

    testFutureIsLeft(response)
  }

  "Repos >> GetStatus" should "return a combined status" in {
    val response = Github(accessToken).repos
      .getCombinedStatus(validRepoOwner, validRepoName, validRefSingle)
      .execFuture[T](headerUserAgent)

    testFutureIsRight[CombinedStatus](response, { r =>
      r.result.repository.full_name shouldBe s"$validRepoOwner/$validRepoName"
      r.statusCode shouldBe okStatusCode
    })
  }

  it should "return an error when an invalid ref is passed" in {
    val response = Github(accessToken).repos
      .getCombinedStatus(validRepoOwner, validRepoName, invalidRef)
      .execFuture[T](headerUserAgent)
    testFutureIsLeft(response)
  }

  "Repos >> ListStatus" should "return a non empty list when a valid ref is provided" in {
    val response = Github(accessToken).repos
      .listStatuses(validRepoOwner, validRepoName, validCommitSha)
      .execFuture[T](headerUserAgent)

    testFutureIsRight[List[Status]](response, { r =>
      r.result.nonEmpty shouldBe true
      r.statusCode shouldBe okStatusCode
    })
  }

  it should "return an error when an invalid ref is provided" in {
    val response = Github(accessToken).repos
      .listStatuses(validRepoOwner, validRepoName, invalidRef)
      .execFuture[T](headerUserAgent)
    testFutureIsLeft(response)
  }
}
