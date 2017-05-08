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
import github4s.GithubResponses.GHResponse
import github4s.free.domain.Comment

/** Comments ops ADT */
sealed trait CommentOp[A]

final case class CreateComment(
    owner: String,
    repo: String,
    number: Int,
    body: String,
    accessToken: Option[String] = None
) extends CommentOp[GHResponse[Comment]]

final case class EditComment(
    owner: String,
    repo: String,
    id: Int,
    body: String,
    accessToken: Option[String] = None
) extends CommentOp[GHResponse[Comment]]

final case class DeleteComment(
    owner: String,
    repo: String,
    id: Int,
    accessToken: Option[String] = None
) extends CommentOp[GHResponse[Unit]]

/**
 * Exposes Comments operations as a Free monadic algebra that may be combined with other Coproducts
 */
class CommentOps[F[_]](implicit I: Inject[CommentOp, F]) {

  def createComment(
      owner: String,
      repo: String,
      number: Int,
      body: String,
      accessToken: Option[String] = None
  ): Free[F, GHResponse[Comment]] =
    Free.inject[CommentOp, F](CreateComment(owner, repo, number, body, accessToken))

  def editComment(
      owner: String,
      repo: String,
      id: Int,
      body: String,
      accessToken: Option[String] = None
  ): Free[F, GHResponse[Comment]] =
    Free.inject[CommentOp, F](EditComment(owner, repo, id, body, accessToken))

  def deleteComment(
      owner: String,
      repo: String,
      id: Int,
      accessToken: Option[String] = None
  ): Free[F, GHResponse[Unit]] =
    Free.inject[CommentOp, F](DeleteComment(owner, repo, id, accessToken))

}

/** Default implicit based DI factory from which instances of the CommentOps may be obtained */
object CommentOps {
  implicit def instance[F[_]](implicit I: Inject[CommentOp, F]): CommentOps[F] =
    new CommentOps[F]
}
