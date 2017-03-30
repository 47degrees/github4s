/*
 * Copyright (c) 2016 47 Degrees, LLC. <http://www.47deg.com>
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
 * the Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
 * FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
 * IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
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
