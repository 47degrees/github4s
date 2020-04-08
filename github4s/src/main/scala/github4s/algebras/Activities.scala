/*
 * Copyright 2016-2020 47 Degrees <https://www.47deg.com>
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

trait Activities[F[_]] {

  /**
   * Set a thread subscription
   *
   * @param id Conversation id for subscribe or unsubscribe
   * @param ignored Determines if all notifications should be blocked from this thread
   * @param subscribed Determines if notifications should be received from this thread
   * @param headers Optional user headers to include in the request
   * @return GHResponse with the Subscription
   */
  def setThreadSub(
      id: Int,
      subscribed: Boolean,
      ignored: Boolean,
      headers: Map[String, String] = Map()
  ): F[GHResponse[Subscription]]

  /**
   * List the users having starred a particular repository
   *
   * @param owner of the repo
   * @param repo name of the repo
   * @param timeline Whether or not to include the date at which point a user starred the repo
   * @param pagination Limit and Offset for pagination
   * @param headers Optional user headers to include in the request
   * @return GHResponse with the list of users starring this repo
   */
  def listStargazers(
      owner: String,
      repo: String,
      timeline: Boolean,
      pagination: Option[Pagination] = None,
      headers: Map[String, String] = Map()
  ): F[GHResponse[List[Stargazer]]]

  /**
   * List the repositories starred by a particular user
   *
   * @param username User for which we want to retrieve the starred repositories
   * @param timeline Whether or not to include the date at which point a user starred the repo
   * @param sort How to sort the result, can be "created" (when the repo was starred) or "updated"
   * (when the repo was last pushed to)
   * @param direction In which direction the results are sorted, can be "asc" or "desc"
   * @param pagination Limit and Offset for pagination
   * @param headers Optional user headers to include in the request
   * @return GHResponse with the list of starred repositories for this user
   */
  def listStarredRepositories(
      username: String,
      timeline: Boolean,
      sort: Option[String] = None,
      direction: Option[String] = None,
      pagination: Option[Pagination] = None,
      headers: Map[String, String] = Map()
  ): F[GHResponse[List[StarredRepository]]]

}
