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

package github4s

import cats.effect.Concurrent
import github4s.algebras._
import github4s.interpreters.StaticAccessToken
import github4s.modules._
import org.http4s.client.Client

private[github4s] class GithubClient[F[_]: Concurrent](
    client: Client[F],
    authHeader: AccessHeader[F]
)(implicit config: GithubConfig)
    extends GithubAPIs[F] {

  private lazy val module: GithubAPIs[F] = new GithubAPIsV3[F](client, config, authHeader)

  lazy val users: Users[F]                 = module.users
  lazy val repos: Repositories[F]          = module.repos
  lazy val auth: Auth[F]                   = module.auth
  lazy val gists: Gists[F]                 = module.gists
  lazy val issues: Issues[F]               = module.issues
  lazy val activities: Activities[F]       = module.activities
  lazy val gitData: GitData[F]             = module.gitData
  lazy val pullRequests: PullRequests[F]   = module.pullRequests
  lazy val organizations: Organizations[F] = module.organizations
  lazy val teams: Teams[F]                 = module.teams
  lazy val projects: Projects[F]           = module.projects
  lazy val search: Search[F]               = module.search
}

object GithubClient {

  def apply[F[_]: Concurrent](
      client: Client[F],
      accessToken: Option[String] = None
  )(implicit config: GithubConfig): GithubAPIs[F] =
    new GithubClient[F](client, AccessHeader.from(new StaticAccessToken(accessToken)))

  def apply[F[_]: Concurrent](
      client: Client[F],
      authHeader: AccessHeader[F]
  )(implicit config: GithubConfig): GithubAPIs[F] =
    new GithubClient[F](client, authHeader)
}
