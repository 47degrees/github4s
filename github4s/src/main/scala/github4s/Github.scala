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

import java.util.concurrent.TimeUnit.MILLISECONDS
import cats.effect.ConcurrentEffect
import github4s.algebras._
import github4s.modules._
import scala.concurrent.ExecutionContext
import scala.concurrent.duration.Duration

class Github[F[_]: ConcurrentEffect](accessToken: Option[String], timeout: Option[Duration])(
    implicit ec: ExecutionContext) {

  val module: GithubAPIs[F] =
    new GithubAPIv3[F](accessToken, timeout.getOrElse(Duration(1000l, MILLISECONDS)))

  val users: Users[F]                 = module.users
  val repos: Repositories[F]          = module.repos
  val auth: Auth[F]                   = module.auth
  val gists: Gists[F]                 = module.gists
  val issues: Issues[F]               = module.issues
  val activities: Activities[F]       = module.activities
  val gitData: GitData[F]             = module.gitData
  val pullRequests: PullRequests[F]   = module.pullRequests
  val organizations: Organizations[F] = module.organizations
  val teams: Teams[F]                 = module.teams

}

object Github {

  def apply[F[_]: ConcurrentEffect](
      accessToken: Option[String] = None,
      timeout: Option[Duration] = None)(implicit ec: ExecutionContext): Github[F] =
    new Github[F](accessToken, timeout)

}
