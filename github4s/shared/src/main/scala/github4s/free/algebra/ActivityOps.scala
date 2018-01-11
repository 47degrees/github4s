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

package github4s.free.algebra

import cats.InjectK
import cats.free.Free
import github4s.GithubResponses._
import github4s.free.domain._

/**
 * Activities ops ADT
 */
sealed trait ActivityOp[A]

final case class SetThreadSub(
    id: Int,
    subscribed: Boolean,
    ignored: Boolean,
    accessToken: Option[String] = None
) extends ActivityOp[GHResponse[Subscription]]

final case class ListStargazers(
    owner: String,
    repo: String,
    timeline: Boolean,
    pagination: Option[Pagination] = None,
    accessToken: Option[String] = None
) extends ActivityOp[GHResponse[List[Stargazer]]]

final case class ListStarredRepositories(
    username: String,
    timeline: Boolean,
    sort: Option[String] = None,
    direction: Option[String] = None,
    pagination: Option[Pagination] = None,
    accessToken: Option[String] = None
) extends ActivityOp[GHResponse[List[StarredRepository]]]

/**
 * Exposes Activity operations as a Free monadic algebra that may be combined with other Algebras via
 * Coproduct
 */
class ActivityOps[F[_]](implicit I: InjectK[ActivityOp, F]) {

  def setThreadSub(
      id: Int,
      subscribed: Boolean,
      ignored: Boolean,
      accessToken: Option[String] = None): Free[F, GHResponse[Subscription]] =
    Free.inject[ActivityOp, F](SetThreadSub(id, subscribed, ignored, accessToken))

  def listStargazers(
      owner: String,
      repo: String,
      timeline: Boolean,
      pagination: Option[Pagination] = None,
      accessToken: Option[String] = None): Free[F, GHResponse[List[Stargazer]]] =
    Free.inject[ActivityOp, F](ListStargazers(owner, repo, timeline, pagination, accessToken))

  def listStarredRepositories(
      username: String,
      timeline: Boolean,
      sort: Option[String] = None,
      direction: Option[String] = None,
      pagination: Option[Pagination] = None,
      accessToken: Option[String] = None): Free[F, GHResponse[List[StarredRepository]]] =
    Free.inject[ActivityOp, F](
      ListStarredRepositories(username, timeline, sort, direction, pagination, accessToken))
}

/**
 * Default implicit based DI factory from which instances of the ActivityOps may be obtained
 */
object ActivityOps {

  implicit def instance[F[_]](implicit I: InjectK[ActivityOp, F]): ActivityOps[F] =
    new ActivityOps[F]

}
