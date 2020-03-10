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

trait GHTeamsSpec extends BaseIntegrationSpec {
  "Team >> ListTeams" should "return the expected teams when a valid org is provided" taggedAs Integration in {
    val response =
      Github[IO](accessToken).teams
        .listTeams(validRepoOwner, headers = headerUserAgent)
        .unsafeRunSync()

    testIsRight[List[Team]](response, r => r.nonEmpty shouldBe true)
    response.statusCode shouldBe okStatusCode
  }

  it should "return error when an invalid org is passed" taggedAs Integration in {
    val response =
      Github[IO](accessToken).teams
        .listTeams(invalidRepoName, headers = headerUserAgent)
        .unsafeRunSync()
    testIsLeft(response)
    response.statusCode shouldBe notFoundStatusCode
  }

}
