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

package github4s.interpreters

import github4s.algebras.Activities
import github4s.Decoders._
import github4s.domain._
import github4s.Encoders._
import github4s.GHResponse
import github4s.http.HttpClient

class ActivitiesInterpreter[F[_]](implicit client: HttpClient[F], accessToken: Option[String])
    extends Activities[F] {

  private val timelineHeader = ("Accept" -> "application/vnd.github.v3.star+json")

  override def setThreadSub(
      id: Int,
      subscribed: Boolean,
      ignored: Boolean,
      headers: Map[String, String]
  ): F[GHResponse[Subscription]] =
    client.put[SubscriptionRequest, Subscription](
      accessToken = accessToken,
      url = s"notifications/threads/$id/subscription",
      headers = headers,
      data = SubscriptionRequest(subscribed, ignored)
    )

  override def listStargazers(
      owner: String,
      repo: String,
      timeline: Boolean,
      pagination: Option[Pagination],
      headers: Map[String, String]
  ): F[GHResponse[List[Stargazer]]] =
    client.get[List[Stargazer]](
      accessToken,
      s"repos/$owner/$repo/stargazers",
      if (timeline) headers + timelineHeader else headers,
      pagination = pagination
    )

  override def listStarredRepositories(
      username: String,
      timeline: Boolean,
      sort: Option[String],
      direction: Option[String],
      pagination: Option[Pagination],
      headers: Map[String, String]
  ): F[GHResponse[List[StarredRepository]]] =
    client.get[List[StarredRepository]](
      accessToken,
      s"users/$username/starred",
      if (timeline) headers + timelineHeader else headers,
      Map(
        "sort"      -> sort,
        "direction" -> direction
      ).collect { case (key, Some(value)) => key -> value },
      pagination = pagination
    )
}
