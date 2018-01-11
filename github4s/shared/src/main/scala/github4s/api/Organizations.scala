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
import github4s.free.domain.{Pagination, User}
import github4s.free.interpreters.Capture
import io.circe.generic.auto._

/** Factory to encapsulate calls related to Organizations operations  */
class Organizations[C, M[_]](
    implicit urls: GithubApiUrls,
    C: Capture[M],
    httpClientImpl: HttpRequestBuilderExtension[C, M]) {

  val httpClient = new HttpClient[C, M]

  /**
   * List the users belonging to a specific organization
   *
   * @param accessToken To identify the authenticated user
   * @param headers Optional user headers to include in the request
   * @param org Organization for which we want to retrieve the members
   * @param filter To retrieve "all" or only "2fa_disabled" users
   * @param role To retrieve "all", only non-owners ("member") or only owners ("admin")
   * @param pagination Limit and Offset for pagination
   * @return GHResponse with the list of users belonging to this organization
   */
  def listMembers(
      accessToken: Option[String] = None,
      headers: Map[String, String] = Map(),
      org: String,
      filter: Option[String] = None,
      role: Option[String] = None,
      pagination: Option[Pagination] = Some(httpClient.defaultPagination)
  ): M[GHResponse[List[User]]] =
    httpClient.get[List[User]](
      accessToken,
      s"orgs/$org/members",
      headers,
      Map(
        "filter" → filter,
        "role"   → role
      ).collect { case (key, Some(value)) ⇒ key → value },
      pagination = pagination
    )

}
