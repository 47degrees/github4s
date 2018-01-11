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

import github4s.Github
import github4s.Github._
import github4s.free.domain.{Stargazer, StarredRepository, Subscription}
import github4s.implicits._
import github4s.utils.BaseIntegrationSpec

trait GHActivitiesSpec[T] extends BaseIntegrationSpec[T] {

  "Activity >> Set a thread subscription" should "return expected response when a valid thread id is provided" in {
    val response =
      Github(accessToken).activities
        .setThreadSub(validThreadId, true, false)
        .execFuture[T](headerUserAgent)

    testFutureIsRight[Subscription](response, { r =>
      r.statusCode shouldBe okStatusCode
    })
  }

  it should "return error when an invalid thread id is passed" in {
    val response =
      Github(accessToken).activities
        .setThreadSub(invalidThreadId, true, false)
        .execFuture[T](headerUserAgent)

    testFutureIsLeft(response)
  }


  "Activity >> ListStargazers" should "return the expected list of starrers for valid data" in {
    val response =
      Github(accessToken).activities
        .listStargazers(validRepoOwner, validRepoName, false)
        .execFuture[T](headerUserAgent)

    testFutureIsRight[List[Stargazer]](response, { r =>
      r.result.nonEmpty shouldBe true
      forAll(r.result) { s => s.starred_at shouldBe None }
      r.statusCode shouldBe okStatusCode
    })
  }

  it should "return the expected list of starrers for valid data with dates if timeline" in {
    val response =
      Github(accessToken).activities
        .listStargazers(validRepoOwner, validRepoName, true)
        .execFuture[T](headerUserAgent)

    testFutureIsRight[List[Stargazer]](response, { r =>
      r.result.nonEmpty shouldBe true
      forAll(r.result) { s => s.starred_at shouldBe defined }
      r.statusCode shouldBe okStatusCode
    })
  }

  it should "return error for invalid repo name" in {
    val response =
      Github(accessToken).activities
        .listStargazers(invalidRepoName, validRepoName, false)
        .execFuture[T](headerUserAgent)

    testFutureIsLeft(response)
  }

  "Activity >> ListStarredRepositories" should "return the expected list of starred repos" in {
    val response =
      Github(accessToken).activities
        .listStarredRepositories(validUsername, false)
        .execFuture[T](headerUserAgent)

    testFutureIsRight[List[StarredRepository]](response, { r =>
      r.result.nonEmpty shouldBe true
      forAll(r.result) { s => s.starred_at shouldBe None }
      r.statusCode shouldBe okStatusCode
    })
  }

  it should "return the expected list of starred repos with dates if timeline" in {
    val response =
      Github(accessToken).activities
        .listStarredRepositories(validUsername, true)
        .execFuture[T](headerUserAgent)

    testFutureIsRight[List[StarredRepository]](response, { r =>
      r.result.nonEmpty shouldBe true
      forAll(r.result) { s => s.starred_at shouldBe defined }
      r.statusCode shouldBe okStatusCode
    })
  }

  it should "return error for invalid username" in {
    val response =
      Github(accessToken).activities
        .listStarredRepositories(invalidUsername, false)
        .execFuture[T](headerUserAgent)

    testFutureIsLeft(response)
  }
}
