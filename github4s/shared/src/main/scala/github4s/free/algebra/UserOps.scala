/*
 * Copyright 2016-2017 47 Degrees, LLC. <http://www.47deg.com>
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

import cats.free.{Free, Inject}
import github4s.GithubResponses._
import github4s.free.domain.{Pagination, User}

/**
 * Users ops ADT
 */
sealed trait UserOp[A]
final case class GetUser(username: String, accessToken: Option[String] = None)
    extends UserOp[GHResponse[User]]
final case class GetAuthUser(accessToken: Option[String] = None) extends UserOp[GHResponse[User]]
final case class GetUsers(
    since: Int,
    pagination: Option[Pagination] = None,
    accessToken: Option[String] = None)
    extends UserOp[GHResponse[List[User]]]

/**
 * Exposes Users operations as a Free monadic algebra that may be combined with other Algebras via
 * Coproduct
 */
class UserOps[F[_]](implicit I: Inject[UserOp, F]) {

  def getUser(username: String, accessToken: Option[String] = None): Free[F, GHResponse[User]] =
    Free.inject[UserOp, F](GetUser(username, accessToken))

  def getAuthUser(accessToken: Option[String] = None): Free[F, GHResponse[User]] =
    Free.inject[UserOp, F](GetAuthUser(accessToken))

  def getUsers(
      since: Int,
      pagination: Option[Pagination] = None,
      accessToken: Option[String] = None): Free[F, GHResponse[List[User]]] =
    Free.inject[UserOp, F](GetUsers(since, pagination, accessToken))

}

/**
 * Default implicit based DI factory from which instances of the UserOps may be obtained
 */
object UserOps {

  implicit def instance[F[_]](implicit I: Inject[UserOp, F]): UserOps[F] = new UserOps[F]

}
