/*
 * Copyright 2016-2020 47 Degrees Open Source <https://www.47deg.com>
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

package github4s.algebras

import github4s.GHResponse
import github4s.domain._

trait Teams[F[_]] {

  /**
   * List the teams for a particular organization
   *
   * @param org organization for which we wish to retrieve the teams
   * @param pagination Limit and Offset for pagination
   * @param headers optional user headers to include in the request
   * @return GHResponse[List[Team]] the list of teams for this organization
   */
  def listTeams(
      org: String,
      pagination: Option[Pagination] = None,
      headers: Map[String, String] = Map()
  ): F[GHResponse[List[Team]]]

}
