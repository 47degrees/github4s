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
import github4s.algebras.Projects
import github4s.domain.{Card, Column, Pagination, Project}
import github4s.http.HttpClient
import github4s.Decoders._

class ProjectsInterpreter[F[_]](
    implicit client: HttpClient[F],
    accessToken: Option[String]
) extends Projects[F] {

  override def listProjects(
      org: String,
      state: Option[String],
      pagination: Option[Pagination] = None,
      headers: Map[String, String] = Map()
  ): F[GHResponse[List[Project]]] =
    client.get[List[Project]](
      accessToken,
      s"orgs/$org/projects",
      headers,
      state.fold(Map.empty[String, String])(s => Map("state" -> s)),
      pagination
    )

  override def listProjectsRepository(
      owner: String,
      repo: String,
      state: Option[String],
      pagination: Option[Pagination],
      headers: Map[String, String]
  ): F[GHResponse[List[Project]]] =
    client.get[List[Project]](
      accessToken,
      s"repos/$owner/$repo/projects",
      headers,
      state.fold(Map.empty[String, String])(s => Map("state" -> s)),
      pagination
    )

  override def listColumns(
      project_id: Int,
      pagination: Option[Pagination],
      headers: Map[String, String]
  ): F[GHResponse[List[Column]]] = client.get[List[Column]](
    accessToken,
    s"projects/$project_id/columns",
    headers,
    Map(),
    pagination
  )

  override def listCards(
      column_id: Int,
      archived_state: Option[String],
      pagination: Option[Pagination],
      headers: Map[String, String]
  ): F[GHResponse[List[Card]]] =
    client.get[List[Card]](
      accessToken,
      s"projects/columns/$column_id/cards",
      headers,
      archived_state.fold(Map.empty[String, String])(s => Map("archived_state" -> s)),
      pagination
    )

}
