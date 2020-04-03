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

package github4s.modules

import cats.effect.Sync
import github4s.GithubConfig
import github4s.algebras._
import github4s.http.HttpClient
import github4s.interpreters._
import org.http4s.client.Client

class GithubAPIv3[F[_]: Sync](
    client: Client[F],
    config: GithubConfig,
    accessToken: Option[String] = None
) extends GithubAPIs[F] {

  implicit val httpClient = new HttpClient[F](client, config)
  implicit val at         = accessToken

  override val users: Users[F]                 = new UsersInterpreter[F]
  override val repos: Repositories[F]          = new RepositoriesInterpreter[F]
  override val auth: Auth[F]                   = new AuthInterpreter[F]
  override val gists: Gists[F]                 = new GistsInterpreter[F]
  override val issues: Issues[F]               = new IssuesInterpreter[F]
  override val activities: Activities[F]       = new ActivitiesInterpreter[F]
  override val gitData: GitData[F]             = new GitDataInterpreter[F]
  override val pullRequests: PullRequests[F]   = new PullRequestsInterpreter[F]
  override val organizations: Organizations[F] = new OrganizationsInterpreter[F]
  override val teams: Teams[F]                 = new TeamsInterpreter[F]
  override val projects: Projects[F]           = new ProjectsInterpreter[F]

}
