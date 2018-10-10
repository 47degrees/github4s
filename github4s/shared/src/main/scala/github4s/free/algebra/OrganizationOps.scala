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
import github4s.free.domain.{Pagination, User}

/**
 * Organizations ops ADT
 */
sealed trait OrganizationOp[A]

final case class ListMembers(
    org: String,
    filter: Option[String] = None,
    role: Option[String] = None,
    pagination: Option[Pagination] = None,
    accessToken: Option[String] = None
) extends OrganizationOp[GHResponse[List[User]]]

final case class ListOutsideCollaborators(
    org: String,
    filter: Option[String] = None,
    pagination: Option[Pagination] = None,
    accessToken: Option[String] = None
) extends OrganizationOp[GHResponse[List[User]]]

/**
 * Exposes Organization operations as a Free monadic algebra that may be combined with other
 * Algebras via Coproduct
 */
class OrganizationOps[F[_]](implicit I: InjectK[OrganizationOp, F]) {

  def listMembers(
      org: String,
      filter: Option[String] = None,
      role: Option[String] = None,
      pagination: Option[Pagination] = None,
      accessToken: Option[String] = None): Free[F, GHResponse[List[User]]] =
    Free.inject[OrganizationOp, F](
      ListMembers(org, filter, role, pagination, accessToken))

  def listOutsideCollaborators(
      org: String,
      filter: Option[String] = None,
      pagination: Option[Pagination] = None,
      accessToken: Option[String] = None): Free[F, GHResponse[List[User]]] =
    Free.inject[OrganizationOp, F](
      ListOutsideCollaborators(org, filter, pagination, accessToken))

}

/**
 * Default implicit based DI factory from which instances of the OrganizationOps may be obtained
 */
object OrganizationOps {

  implicit def instance[F[_]](implicit I: InjectK[OrganizationOp, F]): OrganizationOps[F] =
    new OrganizationOps[F]

}
