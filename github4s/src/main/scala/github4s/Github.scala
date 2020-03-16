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

package github4s

import cats.effect.Sync
import github4s.algebras._
import github4s.modules._
import org.http4s.client.Client

class Github[F[_]: Sync](
    client: Client[F],
    accessToken: Option[String]
) {

  private lazy val module: GithubAPIs[F] =
    new GithubAPIv3[F](client, accessToken)

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
}

object Github {

  def apply[F[_]: Sync](
      client: Client[F],
      accessToken: Option[String] = None
  ): Github[F] =
    new Github[F](client, accessToken)

}
