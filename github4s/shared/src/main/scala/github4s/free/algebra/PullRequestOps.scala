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
import github4s.free.domain._

/**
 * PullRequests ops ADT
 */
sealed trait PullRequestOp[A]

final case class ListPullRequests(
    owner: String,
    repo: String,
    filters: List[PRFilter] = Nil,
    accessToken: Option[String] = None
) extends PullRequestOp[GHResponse[List[PullRequest]]]

/**
 * Exposes Pull Request operations as a Free monadic algebra that may be combined with other Algebras via
 * Coproduct
 */
class PullRequestOps[F[_]](implicit I: Inject[PullRequestOp, F]) {

  def listPullRequests(
      owner: String,
      repo: String,
      filters: List[PRFilter] = Nil,
      accessToken: Option[String] = None
  ): Free[F, GHResponse[List[PullRequest]]] =
    Free.inject[PullRequestOp, F](ListPullRequests(owner, repo, filters, accessToken))
}

/**
 * Default implicit based DI factory from which instances of the PullRequestOps may be obtained
 */
object PullRequestOps {

  implicit def instance[F[_]](implicit I: Inject[PullRequestOp, F]): PullRequestOps[F] =
    new PullRequestOps[F]

}
