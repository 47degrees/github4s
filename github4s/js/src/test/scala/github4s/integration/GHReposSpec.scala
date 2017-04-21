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

package github4s.integration

import cats.data.NonEmptyList
import github4s.Github._
import github4s.Github
import github4s.utils.TestUtils
import org.scalatest.{AsyncFlatSpec, Matchers}
import github4s.free.domain.{Commit, Content, Repository, User}
import github4s.js.Implicits._

import scala.concurrent.ExecutionContext

class GHReposSpec extends AsyncFlatSpec with Matchers with TestUtils {

  override implicit val executionContext: ExecutionContext =
    scala.concurrent.ExecutionContext.Implicits.global

  "Repos >> Get" should "return the expected name when a valid repo is provided" in {

    val response =
      Github(accessToken).repos
        .get(validRepoOwner, validRepoName)
        .execFuture(headerUserAgent)

    testFutureIsRight[Repository](response, { r =>
      r.result.name shouldBe validRepoName
      r.statusCode shouldBe okStatusCode
    })
  }

  it should "return error when an invalid repo name is passed" in {
    val response =
      Github(accessToken).repos
        .get(validRepoOwner, invalidRepoName)
        .execFuture(headerUserAgent)

    testFutureIsLeft(response)
  }

  "Repos >> GetContents" should "return the expected contents when valid path is provided" in {

    val response =
      Github(accessToken).repos
        .getContents(validRepoOwner, validRepoName, validFilePath)
        .execFuture(headerUserAgent)

    testFutureIsRight[NonEmptyList[Content]](response, { r =>
      r.result.head.path shouldBe validFilePath
      r.statusCode shouldBe okStatusCode
    })
  }

  it should "return error when an invalid path is passed" in {
    val response =
      Github(accessToken).repos
        .getContents(validRepoOwner, validRepoName, invalidFilePath)
        .execFuture(headerUserAgent)
    testFutureIsLeft(response)
  }

  "Repos >> ListCommits" should "return the expected list of commits for valid data" in {
    val response =
      Github(accessToken).repos
        .listCommits(validRepoOwner, validRepoName)
        .execFuture(headerUserAgent)

    testFutureIsRight[List[Commit]](response, { r =>
      r.result.nonEmpty shouldBe true
      r.statusCode shouldBe okStatusCode
    })
  }

  it should "return error for invalid repo name" in {
    val response =
      Github(accessToken).repos
        .listCommits(invalidRepoName, validRepoName)
        .execFuture(headerUserAgent)

    testFutureIsLeft(response)
  }

  "Repos >> ListContributors" should "return the expected list of contributors for valid data" in {
    val response =
      Github(accessToken).repos
        .listContributors(validRepoOwner, validRepoName)
        .execFuture(headerUserAgent)

    testFutureIsRight[List[User]](response, { r =>
      r.result shouldNot be(empty)
      r.statusCode shouldBe okStatusCode
    })
  }

  it should "return error for invalid repo name" in {
    val response =
      Github(accessToken).repos
        .listContributors(invalidRepoName, validRepoName)
        .execFuture(headerUserAgent)

    testFutureIsLeft(response)
  }

}
