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

package github4s.algebras

import github4s.GithubResponses.GHResponse
import github4s.domain._

trait Users[F[_]] {

  /**
   * Get information for a particular user
   *
   * @param username of the user to retrieve
   * @param headers optional user headers to include in the request
   * @return GHResponse[User] User details
   */
  def get(username: String, headers: Map[String, String] = Map()): F[GHResponse[User]]

  /**
   * Get information of the authenticated user
   * @param headers optional user headers to include in the request
   * @return GHResponse[User] User details
   */
  def getAuth(headers: Map[String, String] = Map()): F[GHResponse[User]]

  /**
   * Get users
   *
   * @param since The integer ID of the last User that you've seen.
   * @param pagination Limit and Offset for pagination
   * @param headers optional user headers to include in the request
   * @return GHResponse[List[User] ] List of user's details
   */
  def getUsers(
      since: Int,
      pagination: Option[Pagination] = None,
      headers: Map[String, String] = Map()
  ): F[GHResponse[List[User]]]

  /**
   * Get information for a particular user's list of users they follow
   *
   * @param username of the user to retrieve
   * @param headers optional user headers to include in the request
   * @return GHResponse[User] User details
   */
  def getFollowing(
      username: String,
      headers: Map[String, String] = Map()
  ): F[GHResponse[List[User]]]
}
