/*
 * Copyright 2016-2018 47 Degrees, LLC. <http://www.47deg.com>
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

package github4s.api

import github4s.GithubResponses.GHResponse
import github4s._
import github4s.free.domain._
import github4s.free.interpreters.Capture
import github4s.util.URLEncoder
import io.circe.syntax._
import io.circe.generic.auto._

/** Factory to encapsulate calls related to Activities operations  */
class Activities[C, M[_]](
    implicit urls: GithubApiUrls,
    C: Capture[M],
    httpClientImpl: HttpRequestBuilderExtension[C, M]) {
  import Decoders._

  val httpClient = new HttpClient[C, M]

  private val timelineHeader = ("Accept" -> "application/vnd.github.v3.star+json")

  /**
   * Set a thread subscription
   *
   * @param accessToken Token to identify the authenticated user
   * @param headers Optional user headers to include in the request
   * @param id Conversation id for subscribe or unsubscribe
   * @param subscribed Determines if notifications should be received from this thread
   * @param ignored Determines if all notifications should be blocked from this thread
   */
  def setThreadSub(
      accessToken: Option[String] = None,
      headers: Map[String, String] = Map(),
      id: Int,
      subscribed: Boolean,
      ignored: Boolean): M[GHResponse[Subscription]] =
    httpClient.put[Subscription](
      accessToken,
      s"notifications/threads/$id/subscription",
      headers,
      data = SubscriptionRequest(subscribed, ignored).asJson.noSpaces)

  /**
   * List the users having starred a particular repository
   *
   * @param accessToken To identify the authenticated user
   * @param headers Optional user headers to include in the request
   * @param owner of the repo
   * @param repo name of the repo
   * @param timeline Whether or not to include the date at which point a user starred the repo
   * @param pagination Limit and Offset for pagination
   * @return GHResponse with the list of users starring this repo
   */
  def listStargazers(
      accessToken: Option[String] = None,
      headers: Map[String, String] = Map(),
      owner: String,
      repo: String,
      timeline: Boolean,
      pagination: Option[Pagination] = Some(httpClient.defaultPagination)
  ): M[GHResponse[List[Stargazer]]] =
    httpClient.get[List[Stargazer]](
      accessToken,
      s"repos/$owner/$repo/stargazers",
      if (timeline) headers + timelineHeader else headers,
      pagination = pagination
    )

  /**
   * List the repositories starred by a particular user
   *
   * @param accessToken To identify the authenticated user
   * @param headers Optional user headers to include in the request
   * @param username User for which we want to retrieve the starred repositories
   * @param timeline Whether or not to include the date at which point a user starred the repo
   * @param sort How to sort the result, can be "created" (when the repo was starred) or "updated"
   * (when the repo was last pushed to)
   * @param direction In which direction the results are sorted, can be "asc" or "desc"
   * @param pagination Limit and Offset for pagination
   * @return GHResponse with the list of starred repositories for this user
   */
  def listStarredRepositories(
      accessToken: Option[String] = None,
      headers: Map[String, String] = Map(),
      username: String,
      timeline: Boolean,
      sort: Option[String] = None,
      direction: Option[String] = None,
      pagination: Option[Pagination] = Some(httpClient.defaultPagination)
  ): M[GHResponse[List[StarredRepository]]] =
    httpClient.get[List[StarredRepository]](
      accessToken,
      s"users/$username/starred",
      if (timeline) headers + timelineHeader else headers,
      Map(
        "sort"      → sort,
        "direction" → direction
      ).collect { case (key, Some(value)) ⇒ key → value },
      pagination = pagination
    )

}
