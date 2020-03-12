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

package github4s.unit

import cats.effect.IO
import cats.syntax.either._
import github4s.GithubResponses.GHResponse
import github4s.domain._
import github4s.interpreters.TeamsInterpreter
import github4s.utils.BaseSpec

class TeamsSpec extends BaseSpec {

  implicit val token = sampleToken

  "Teams.listTeam" should "call to httpClient.get with the right parameters" in {
    val response: IO[GHResponse[List[Team]]] =
      IO(GHResponse(List(team).asRight, okStatusCode, Map.empty))

    implicit val httpClientMock = httpClientMockGet[List[Team]](
      url = s"orgs/$validRepoOwner/teams",
      response = response
    )

    val teams = new TeamsInterpreter[IO]

    teams.listTeams(validRepoOwner, headers = headerUserAgent)
  }

}
