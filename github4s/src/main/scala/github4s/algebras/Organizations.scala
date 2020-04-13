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

import github4s.GithubResponses.GHResponse
import github4s.domain._

trait Organizations[F[_]] {

  /**
   * List the users belonging to a specific organization
   *
   * @param org Organization for which we want to retrieve the members
   * @param filter To retrieve "all" or only "2fa_disabled" users
   * @param role To retrieve "all", only non-owners ("member") or only owners ("admin")
   * @param pagination Limit and Offset for pagination
   * @param headers Optional user headers to include in the request
   * @return GHResponse with the list of users belonging to this organization
   */
  def listMembers(
      org: String,
      filter: Option[String] = None,
      role: Option[String] = None,
      pagination: Option[Pagination] = None,
      headers: Map[String, String] = Map()
  ): F[GHResponse[List[User]]]

  /**
   * List users who are outside collaborators
   *
   * @param org Organization for which we want to retrieve collaborators
   * @param filter To retrieve "all" or only "2fa_disabled" users
   * @param pagination Limit and Offset for pagination
   * @param headers Optional user headers to include in the request
   * @return GHResponse with outside collaborators
   */
  def listOutsideCollaborators(
      org: String,
      filter: Option[String] = None,
      pagination: Option[Pagination] = None,
      headers: Map[String, String] = Map()
  ): F[GHResponse[List[User]]]
}
