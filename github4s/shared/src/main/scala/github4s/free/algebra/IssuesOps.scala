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

import cats.InjectK
import cats.free.Free
import github4s.GithubResponses._
import github4s.free.domain.{Comment, Issue, SearchIssuesResult, SearchParam}

/**
 * Issues ops ADT
 */
sealed trait IssueOp[A]

final case class ListIssues(
    owner: String,
    repo: String,
    accessToken: Option[String] = None
) extends IssueOp[GHResponse[List[Issue]]]

final case class SearchIssues(
    query: String,
    searchParams: List[SearchParam],
    accessToken: Option[String] = None
) extends IssueOp[GHResponse[SearchIssuesResult]]

final case class CreateIssue(
    owner: String,
    repo: String,
    title: String,
    body: String,
    milestone: Option[Int],
    labels: List[String],
    assignees: List[String],
    accessToken: Option[String] = None
) extends IssueOp[GHResponse[Issue]]

final case class EditIssue(
    owner: String,
    repo: String,
    issue: Int,
    state: String,
    title: String,
    body: String,
    milestone: Option[Int],
    labels: List[String],
    assignees: List[String],
    accessToken: Option[String] = None
) extends IssueOp[GHResponse[Issue]]

final case class CreateComment(
    owner: String,
    repo: String,
    number: Int,
    body: String,
    accessToken: Option[String] = None
) extends IssueOp[GHResponse[Comment]]

final case class EditComment(
    owner: String,
    repo: String,
    id: Int,
    body: String,
    accessToken: Option[String] = None
) extends IssueOp[GHResponse[Comment]]

final case class DeleteComment(
    owner: String,
    repo: String,
    id: Int,
    accessToken: Option[String] = None
) extends IssueOp[GHResponse[Unit]]

/**
 * Exposes Issue operations as a Free monadic algebra that may be combined with other Algebras via
 * Coproduct
 */
class IssueOps[F[_]](implicit I: InjectK[IssueOp, F]) {

  def listIssues(
      owner: String,
      repo: String,
      accessToken: Option[String] = None
  ): Free[F, GHResponse[List[Issue]]] =
    Free.inject[IssueOp, F](ListIssues(owner, repo, accessToken))

  def searchIssues(
      query: String,
      searchParams: List[SearchParam],
      accessToken: Option[String] = None
  ): Free[F, GHResponse[SearchIssuesResult]] =
    Free.inject[IssueOp, F](SearchIssues(query, searchParams, accessToken))

  def createIssue(
      owner: String,
      repo: String,
      title: String,
      body: String,
      milestone: Option[Int],
      labels: List[String],
      assignees: List[String],
      accessToken: Option[String] = None
  ): Free[F, GHResponse[Issue]] =
    Free.inject[IssueOp, F](
      CreateIssue(owner, repo, title, body, milestone, labels, assignees, accessToken))

  def editIssue(
      owner: String,
      repo: String,
      issue: Int,
      state: String,
      title: String,
      body: String,
      milestone: Option[Int],
      labels: List[String],
      assignees: List[String],
      accessToken: Option[String] = None
  ): Free[F, GHResponse[Issue]] =
    Free.inject[IssueOp, F](
      EditIssue(owner, repo, issue, state, title, body, milestone, labels, assignees, accessToken))

  def createComment(
      owner: String,
      repo: String,
      number: Int,
      body: String,
      accessToken: Option[String] = None
  ): Free[F, GHResponse[Comment]] =
    Free.inject[IssueOp, F](CreateComment(owner, repo, number, body, accessToken))

  def editComment(
      owner: String,
      repo: String,
      id: Int,
      body: String,
      accessToken: Option[String] = None
  ): Free[F, GHResponse[Comment]] =
    Free.inject[IssueOp, F](EditComment(owner, repo, id, body, accessToken))

  def deleteComment(
      owner: String,
      repo: String,
      id: Int,
      accessToken: Option[String] = None
  ): Free[F, GHResponse[Unit]] =
    Free.inject[IssueOp, F](DeleteComment(owner, repo, id, accessToken))

}

/**
 * Default implicit based DI factory from which instances of the IssueOps may be obtained
 */
object IssueOps {

  implicit def instance[F[_]](implicit I: InjectK[IssueOp, F]): IssueOps[F] = new IssueOps[F]

}
