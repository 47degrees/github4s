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
import github4s.domain.{Column, Project}
import github4s.utils.{BaseIntegrationSpec, Integration}

trait GHProjectsSpec extends BaseIntegrationSpec {

  "Project >> ListProjects" should "return the expected projects when a valid org is provided" taggedAs Integration in {
    val response =
      Github[IO](accessToken).projects
        .listProjects(validRepoOwner, headers = headerUserAgent ++ headerAccept)
        .unsafeRunSync()

    testIsRight[List[Project]](response, { r =>
      r.result.nonEmpty shouldBe true
      r.statusCode shouldBe okStatusCode
    })
  }

  it should "return error when an invalid org is passed" taggedAs Integration in {
    val response =
      Github[IO](accessToken).projects
        .listProjects(invalidRepoName, headers = headerUserAgent ++ headerAccept)
        .unsafeRunSync()

    testIsLeft(response)
  }

  "Project >> ListColumns" should "return the expected column when a valid project id is provided" taggedAs Integration in {
    val response =
      Github[IO](accessToken).projects
        .listColumns(validProjectId, headers = headerUserAgent ++ headerAccept)
        .unsafeRunSync()

    testIsRight[List[Column]](response, { r =>
      r.result.nonEmpty shouldBe true
      r.statusCode shouldBe okStatusCode
    })
  }

  it should "return error when an invalid project id is passed" taggedAs Integration in {
    val response =
      Github[IO](accessToken).projects
        .listColumns(invalidProjectId, headers = headerUserAgent ++ headerAccept)
        .unsafeRunSync()

    testIsLeft(response)
  }

}
