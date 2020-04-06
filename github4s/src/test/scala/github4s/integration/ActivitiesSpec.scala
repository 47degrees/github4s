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
import github4s.Github
import github4s.domain._
import github4s.utils.{BaseIntegrationSpec, Integration}

trait ActivitiesSpec extends BaseIntegrationSpec {

  "Activity >> Set a thread subscription" should "return expected response when a valid thread id is provided" taggedAs Integration in {
    val response = clientResource
      .use { client =>
        Github[IO](client, accessToken).activities
          .setThreadSub(validThreadId, true, false, headerUserAgent)
      }
      .unsafeRunSync()

    testIsRight[Subscription](response)
    response.statusCode shouldBe okStatusCode
  }

  it should "return error when an invalid thread id is passed" taggedAs Integration in {
    val response = clientResource
      .use { client =>
        Github[IO](client, accessToken).activities
          .setThreadSub(invalidThreadId, true, false, headerUserAgent)
      }
      .unsafeRunSync()

    testIsLeft(response)
    response.statusCode shouldBe notFoundStatusCode
  }

  "Activity >> ListStargazers" should "return the expected list of starrers for valid data" taggedAs Integration in {
    val response = clientResource
      .use { client =>
        Github[IO](client, accessToken).activities
          .listStargazers(validRepoOwner, validRepoName, false, None, headerUserAgent)
      }
      .unsafeRunSync()

    testIsRight[List[Stargazer]](response, { r =>
      r.nonEmpty shouldBe true
      forAll(r)(s => s.starred_at shouldBe None)
    })
    response.statusCode shouldBe okStatusCode
  }

  it should "return the expected list of starrers for valid data with dates if timeline" taggedAs Integration in {
    val response = clientResource
      .use { client =>
        Github[IO](client, accessToken).activities
          .listStargazers(validRepoOwner, validRepoName, true, None, headerUserAgent)
      }
      .unsafeRunSync()

    testIsRight[List[Stargazer]](response, { r =>
      r.nonEmpty shouldBe true
      forAll(r)(s => s.starred_at shouldBe defined)
    })
    response.statusCode shouldBe okStatusCode
  }

  it should "return error for invalid repo name" taggedAs Integration in {
    val response = clientResource
      .use { client =>
        Github[IO](client, accessToken).activities
          .listStargazers(invalidRepoName, validRepoName, false, None, headerUserAgent)
      }
      .unsafeRunSync()

    testIsLeft(response)
    response.statusCode shouldBe notFoundStatusCode
  }

  "Activity >> ListStarredRepositories" should "return the expected list of starred repos" taggedAs Integration in {
    val response = clientResource
      .use { client =>
        Github[IO](client, accessToken).activities
          .listStarredRepositories(validUsername, false, headers = headerUserAgent)
      }
      .unsafeRunSync()

    testIsRight[List[StarredRepository]](response, { r =>
      r.nonEmpty shouldBe true
      forAll(r)(s => s.starred_at shouldBe None)
    })
    response.statusCode shouldBe okStatusCode
  }

  it should "return the expected list of starred repos with dates if timeline" taggedAs Integration in {
    val response = clientResource
      .use { client =>
        Github[IO](client, accessToken).activities
          .listStarredRepositories(validUsername, true, headers = headerUserAgent)
      }
      .unsafeRunSync()

    testIsRight[List[StarredRepository]](response, { r =>
      r.nonEmpty shouldBe true
      forAll(r)(s => s.starred_at shouldBe defined)
    })
    response.statusCode shouldBe okStatusCode
  }

  it should "return error for invalid username" taggedAs Integration in {
    val response = clientResource
      .use { client =>
        Github[IO](client, accessToken).activities
          .listStarredRepositories(invalidUsername, false, headers = headerUserAgent)
      }
      .unsafeRunSync()

    testIsLeft(response)
    response.statusCode shouldBe notFoundStatusCode
  }
}
