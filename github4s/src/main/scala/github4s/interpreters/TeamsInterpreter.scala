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

package github4s.interpreters

import github4s.Decoders._
import github4s.GithubResponses.GHResponse
import github4s.algebras.Teams
import github4s.domain._
import github4s.http.HttpClient

class TeamsInterpreter[F[_]](implicit client: HttpClient[F], accessToken: Option[String])
    extends Teams[F] {

  override def listTeams(
      org: String,
      pagination: Option[Pagination],
      headers: Map[String, String]
  ): F[GHResponse[List[Team]]] =
    client.get[List[Team]](accessToken, method = s"orgs/$org/teams", headers, Map.empty, pagination)
}
