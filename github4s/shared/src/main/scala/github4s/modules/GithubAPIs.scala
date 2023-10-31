/*
 * Copyright 2016-2023 47 Degrees Open Source <https://www.47deg.com>
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

import cats.effect.kernel.Concurrent
import github4s.GithubConfig
import github4s.algebras._
import github4s.http.HttpClient
import github4s.interpreters._
import org.http4s.client.Client

class GithubAPIv3[F[_]: Concurrent](
    client: Client[F],
    config: GithubConfig,
    accessToken: AccessToken[F]
) extends GithubAPIs[F] {

  implicit val httpClient: HttpClient[F] = new HttpClient[F](client, config, accessToken)

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
  override val search: Search[F]               = new SearchInterpreter[F]

}

object GithubAPIv3 {

  def noAuth[F[_]: Concurrent](client: Client[F], config: GithubConfig): GithubAPIv3[F] =
    new GithubAPIv3[F](client, config, StaticAccessToken.noToken)
}
