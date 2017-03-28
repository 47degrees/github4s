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

import cats.data.NonEmptyList
import cats.free.{Free, Inject}
import github4s.GithubResponses._
import github4s.free.domain._

/**
 * Git ops ADT
 */
sealed trait GitDataOp[A]

final case class GetReference(
    owner: String,
    repo: String,
    ref: String,
    accessToken: Option[String] = None
) extends GitDataOp[GHResponse[NonEmptyList[Ref]]]

final case class UpdateReference(
  owner: String,
  repo: String,
  ref: String,
  sha: String,
  force: Option[Boolean],
  accessToken: Option[String]
) extends GitDataOp[GHResponse[Ref]]

final case class GetCommit(
    owner: String,
    repo: String,
    sha: String,
    accessToken: Option[String] = None
) extends GitDataOp[GHResponse[RefCommit]]

final case class CreateCommit(
    owner: String,
    repo: String,
    message: String,
    tree: String,
    parents: List[String],
    author: Option[RefCommitAuthor],
    accessToken: Option[String]
) extends GitDataOp[GHResponse[RefCommit]]

final case class CreateBlob(
    owner: String,
    repo: String,
    content: String,
    encoding: Option[String],
    accessToken: Option[String] = None
) extends GitDataOp[GHResponse[RefInfo]]

final case class CreateTree(
    owner: String,
    repo: String,
    baseTree: Option[String],
    treeDataList: List[TreeData],
    accessToken: Option[String] = None
) extends GitDataOp[GHResponse[TreeResult]]

/**
 * Exposes Git Data operations as a Free monadic algebra that may be combined with other Algebras via
 * Coproduct
 */
class GitDataOps[F[_]](implicit I: Inject[GitDataOp, F]) {

  def getReference(
      owner: String,
      repo: String,
      ref: String,
      accessToken: Option[String] = None
  ): Free[F, GHResponse[NonEmptyList[Ref]]] =
    Free.inject[GitDataOp, F](GetReference(owner, repo, ref, accessToken))

  def updateReference(
      owner: String,
      repo: String,
      ref: String,
      sha: String,
    force: Option[Boolean],
      accessToken: Option[String] = None
  ): Free[F, GHResponse[Ref]] =
    Free.inject[GitDataOp, F](UpdateReference(owner, repo, ref, sha, force, accessToken))

  def getCommit(
      owner: String,
      repo: String,
      sha: String,
      accessToken: Option[String] = None
  ): Free[F, GHResponse[RefCommit]] =
    Free.inject[GitDataOp, F](GetCommit(owner, repo, sha, accessToken))

  def createCommit(
      owner: String,
      repo: String,
      message: String,
      tree: String,
      parents: List[String],
      author: Option[RefCommitAuthor],
      accessToken: Option[String] = None
  ): Free[F, GHResponse[RefCommit]] =
    Free.inject[GitDataOp, F](CreateCommit(owner, repo, message, tree, parents, author, accessToken))

  def createBlob(
      owner: String,
      repo: String,
      content: String,
      encoding: Option[String],
      accessToken: Option[String] = None
  ): Free[F, GHResponse[RefInfo]] =
    Free.inject[GitDataOp, F](CreateBlob(owner, repo, content, encoding, accessToken))

  def createTree(
      owner: String,
      repo: String,
      baseTree: Option[String],
      treeDataList: List[TreeData],
      accessToken: Option[String] = None
  ): Free[F, GHResponse[TreeResult]] =
    Free.inject[GitDataOp, F](CreateTree(owner, repo, baseTree, treeDataList, accessToken))
}

/**
 * Default implicit based DI factory from which instances of the GitOps may be obtained
 */
object GitDataOps {

  implicit def instance[F[_]](implicit I: Inject[GitDataOp, F]): GitDataOps[F] = new GitDataOps[F]

}
