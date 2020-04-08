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

package github4s.interpreters

import github4s.GithubResponses.GHResponse
import github4s.http.HttpClient
import github4s.algebras.Organizations
import github4s.domain._
import github4s.Decoders._

class OrganizationsInterpreter[F[_]](implicit client: HttpClient[F], accessToken: Option[String])
    extends Organizations[F] {

  override def listMembers(
      org: String,
      filter: Option[String],
      role: Option[String],
      pagination: Option[Pagination],
      headers: Map[String, String] = Map()
  ): F[GHResponse[List[User]]] =
    client.get[List[User]](
      accessToken,
      s"orgs/$org/members",
      headers,
      Map(
        "filter" -> filter,
        "role"   -> role
      ).collect { case (key, Some(value)) => key -> value },
      pagination
    )

  override def listOutsideCollaborators(
      org: String,
      filter: Option[String],
      pagination: Option[Pagination],
      headers: Map[String, String] = Map()
  ): F[GHResponse[List[User]]] =
    client.get[List[User]](
      accessToken,
      s"orgs/$org/outside_collaborators",
      headers,
      Map("filter" -> filter).collect { case (key, Some(value)) => key -> value },
      pagination
    )
}
