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
import github4s.domain.{Card, Column, Project}
import github4s.utils.{BaseIntegrationSpec, Integration}

trait ProjectsSpec extends BaseIntegrationSpec {

  "Project >> ListProjects" should "return the expected projects when a valid org is provided" taggedAs Integration in {
    val response = clientResource
      .use { client =>
        Github[IO](client, accessToken).projects
          .listProjects(validRepoOwner, headers = headerUserAgent ++ headerAccept)
      }
      .unsafeRunSync()

    testIsRight[List[Project]](response, r => r.nonEmpty shouldBe true)
    response.statusCode shouldBe okStatusCode
  }

  it should "return error when an invalid org is passed" taggedAs Integration in {
    val response = clientResource
      .use { client =>
        Github[IO](client, accessToken).projects
          .listProjects(invalidRepoName, headers = headerUserAgent ++ headerAccept)
      }
      .unsafeRunSync()

    testIsLeft(response)
  }

  "Project >> ListProjectsRepository" should "return the expected projects when a valid owner and repo are provided" taggedAs Integration in {
    val response = clientResource
      .use { client =>
        Github[IO](client, accessToken).projects
          .listProjectsRepository(
            validRepoOwner,
            validRepoName,
            headers = headerUserAgent ++ headerAccept
          )
      }
      .unsafeRunSync()

    testIsRight[List[Project]](response, r => r.nonEmpty shouldBe true)
    response.statusCode shouldBe okStatusCode
  }

  it should "return error when an invalid repo is passed" taggedAs Integration in {
    val response = clientResource
      .use { client =>
        Github[IO](client, accessToken).projects
          .listProjectsRepository(
            validRepoOwner,
            invalidRepoName,
            headers = headerUserAgent ++ headerAccept
          )
      }
      .unsafeRunSync()

    testIsLeft(response)
  }

  it should "return error when an invalid owner is passed" taggedAs Integration in {
    val response = clientResource
      .use { client =>
        Github[IO](client, accessToken).projects
          .listProjectsRepository(
            invalidRepoOwner,
            validRepoName,
            headers = headerUserAgent ++ headerAccept
          )
      }
      .unsafeRunSync()

    testIsLeft(response)
    response.statusCode shouldBe notFoundStatusCode
  }

  "Project >> ListColumns" should "return the expected column when a valid project id is provided" taggedAs Integration in {
    val response = clientResource
      .use { client =>
        Github[IO](client, accessToken).projects
          .listColumns(validProjectId, headers = headerUserAgent ++ headerAccept)
      }
      .unsafeRunSync()

    testIsRight[List[Column]](response, r => r.nonEmpty shouldBe true)
    response.statusCode shouldBe okStatusCode
  }

  it should "return error when an invalid project id is passed" taggedAs Integration in {
    val response = clientResource
      .use { client =>
        Github[IO](client, accessToken).projects
          .listColumns(invalidProjectId, headers = headerUserAgent ++ headerAccept)
      }
      .unsafeRunSync()

    testIsLeft(response)
    response.statusCode shouldBe notFoundStatusCode
  }

  "Project >> ListCards" should "return the expected cards when a valid column id is provided" taggedAs Integration in {
    val response = clientResource
      .use { client =>
        Github[IO](client, accessToken).projects
          .listCards(validColumnId, headers = headerUserAgent ++ headerAccept)
      }
      .unsafeRunSync()

    testIsRight[List[Card]](response, r => r.nonEmpty shouldBe true)
    response.statusCode shouldBe okStatusCode
  }

  it should "return error when an invalid column id is passed" taggedAs Integration in {
    val response = clientResource
      .use { client =>
        Github[IO](client, accessToken).projects
          .listCards(invalidColumnId, headers = headerUserAgent ++ headerAccept)
      }
      .unsafeRunSync()

    testIsLeft(response)
    response.statusCode shouldBe notFoundStatusCode
  }
}
