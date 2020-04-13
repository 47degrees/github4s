/*
 * Copyright 2016-2020 47 Degrees Open Source <https://www.47deg.com>
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

package github4s.interpreters

import github4s.http.HttpClient
import github4s.algebras.Issues
import github4s.GithubResponses.GHResponse
import github4s.domain._
import java.net.URLEncoder.encode
import java.time.ZonedDateTime

import github4s.Decoders._
import github4s.Encoders._

class IssuesInterpreter[F[_]](implicit client: HttpClient[F], accessToken: Option[String])
    extends Issues[F] {

  override def listIssues(
      owner: String,
      repo: String,
      headers: Map[String, String] = Map()
  ): F[GHResponse[List[Issue]]] =
    client.get[List[Issue]](accessToken, s"repos/$owner/$repo/issues", headers)

  override def getIssue(
      owner: String,
      repo: String,
      number: Int,
      headers: Map[String, String] = Map()
  ): F[GHResponse[Issue]] =
    client.get[Issue](accessToken, s"repos/$owner/$repo/issues/$number", headers)

  override def searchIssues(
      query: String,
      searchParams: List[SearchParam],
      headers: Map[String, String] = Map()
  ): F[GHResponse[SearchIssuesResult]] =
    client.get[SearchIssuesResult](
      accessToken = accessToken,
      method = "search/issues",
      headers = headers,
      params = Map("q" -> s"${encode(query, "utf-8")}+${searchParams.map(_.value).mkString("+")}")
    )

  override def createIssue(
      owner: String,
      repo: String,
      title: String,
      body: String,
      milestone: Option[Int],
      labels: List[String],
      assignees: List[String],
      headers: Map[String, String] = Map()
  ): F[GHResponse[Issue]] =
    client.post[NewIssueRequest, Issue](
      accessToken,
      s"repos/$owner/$repo/issues",
      headers,
      data = NewIssueRequest(title, body, milestone, labels, assignees)
    )

  override def editIssue(
      owner: String,
      repo: String,
      issue: Int,
      state: String,
      title: String,
      body: String,
      milestone: Option[Int],
      labels: List[String],
      assignees: List[String],
      headers: Map[String, String] = Map()
  ): F[GHResponse[Issue]] =
    client.patch[EditIssueRequest, Issue](
      accessToken,
      s"repos/$owner/$repo/issues/$issue",
      headers,
      data = EditIssueRequest(state, title, body, milestone, labels, assignees)
    )

  override def listComments(
      owner: String,
      repo: String,
      number: Int,
      headers: Map[String, String] = Map()
  ): F[GHResponse[List[Comment]]] =
    client
      .get[List[Comment]](accessToken, s"repos/$owner/$repo/issues/$number/comments", headers)

  override def createComment(
      owner: String,
      repo: String,
      number: Int,
      body: String,
      headers: Map[String, String] = Map()
  ): F[GHResponse[Comment]] =
    client.post[CommentData, Comment](
      accessToken,
      s"repos/$owner/$repo/issues/$number/comments",
      headers,
      CommentData(body)
    )

  override def editComment(
      owner: String,
      repo: String,
      id: Int,
      body: String,
      headers: Map[String, String] = Map()
  ): F[GHResponse[Comment]] =
    client
      .patch[CommentData, Comment](
        accessToken,
        s"repos/$owner/$repo/issues/comments/$id",
        headers,
        CommentData(body)
      )

  override def deleteComment(
      owner: String,
      repo: String,
      id: Int,
      headers: Map[String, String] = Map()
  ): F[GHResponse[Unit]] =
    client.delete(accessToken, s"repos/$owner/$repo/issues/comments/$id", headers)

  override def listLabelsRepository(
      owner: String,
      repo: String,
      headers: Map[String, String],
      pagination: Option[Pagination]
  ): F[GHResponse[List[Label]]] =
    client.get[List[Label]](
      accessToken,
      s"repos/$owner/$repo/labels",
      headers = headers,
      pagination = pagination
    )

  override def listLabels(
      owner: String,
      repo: String,
      number: Int,
      headers: Map[String, String] = Map()
  ): F[GHResponse[List[Label]]] =
    client.get[List[Label]](accessToken, s"repos/$owner/$repo/issues/$number/labels", headers)

  override def addLabels(
      owner: String,
      repo: String,
      number: Int,
      labels: List[String],
      headers: Map[String, String] = Map()
  ): F[GHResponse[List[Label]]] =
    client.post[List[String], List[Label]](
      accessToken,
      s"repos/$owner/$repo/issues/$number/labels",
      headers,
      labels
    )

  override def removeLabel(
      owner: String,
      repo: String,
      number: Int,
      label: String,
      headers: Map[String, String] = Map()
  ): F[GHResponse[List[Label]]] =
    client.deleteWithResponse[List[Label]](
      accessToken,
      s"repos/$owner/$repo/issues/$number/labels/$label",
      headers
    )

  override def listAvailableAssignees(
      owner: String,
      repo: String,
      pagination: Option[Pagination],
      headers: Map[String, String] = Map()
  ): F[GHResponse[List[User]]] =
    client.get[List[User]](
      accessToken,
      s"repos/$owner/$repo/assignees",
      headers,
      pagination = pagination
    )

  override def listMilestones(
      owner: String,
      repo: String,
      state: Option[String],
      sort: Option[String],
      direction: Option[String],
      pagination: Option[Pagination],
      headers: Map[String, String]
  ): F[GHResponse[List[Milestone]]] =
    client.get[List[Milestone]](
      accessToken,
      s"repos/$owner/$repo/milestones",
      headers,
      pagination = pagination,
      params =
        List(state.map("state" -> _), sort.map("sort" -> _), direction.map("direction" -> _)).flatten.toMap
    )

  override def createMilestone(
      owner: String,
      repo: String,
      title: String,
      state: Option[String],
      description: Option[String],
      due_on: Option[ZonedDateTime],
      headers: Map[String, String]
  ): F[GHResponse[Milestone]] =
    client.post[MilestoneData, Milestone](
      accessToken,
      s"repos/$owner/$repo/milestones",
      headers,
      MilestoneData(title, state, description, due_on)
    )

  override def getMilestone(
      owner: String,
      repo: String,
      number: Int,
      headers: Map[String, String]
  ): F[GHResponse[Milestone]] =
    client.get[Milestone](accessToken, s"repos/$owner/$repo/milestones/$number", headers)

  override def updateMilestone(
      owner: String,
      repo: String,
      milestone_number: Int,
      title: String,
      state: Option[String],
      description: Option[String],
      due_on: Option[ZonedDateTime],
      headers: Map[String, String]
  ): F[GHResponse[Milestone]] = client.patch[MilestoneData, Milestone](
    accessToken,
    s"repos/$owner/$repo/milestones/$milestone_number",
    headers,
    data = MilestoneData(title, state, description, due_on)
  )

  override def deleteMilestone(
      owner: String,
      repo: String,
      milestone_number: Int,
      headers: Map[String, String]
  ): F[GHResponse[Unit]] =
    client.delete(accessToken, s"repos/$owner/$repo/milestones/$milestone_number", headers)
}
