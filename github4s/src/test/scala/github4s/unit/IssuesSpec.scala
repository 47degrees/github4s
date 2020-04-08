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

package github4s.unit

import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

import cats.effect.IO
import cats.syntax.either._
import github4s.GHResponse
import github4s.domain._
import github4s.interpreters.IssuesInterpreter
import github4s.utils.BaseSpec

class IssuesSpec extends BaseSpec {

  implicit val token = sampleToken

  "Issues.listIssues" should "call httpClient.get with the right parameters" in {
    val response: IO[GHResponse[List[Issue]]] =
      IO(GHResponse(List(issue).asRight, okStatusCode, Map.empty))

    implicit val httpClientMock = httpClientMockGet[List[Issue]](
      url = s"repos/$validRepoOwner/$validRepoName/issues",
      response = response
    )

    val issues = new IssuesInterpreter[IO]

    issues.listIssues(validRepoOwner, validRepoName, headerUserAgent)

  }

  "Issues.getIssue" should "call httpClient.get with the right parameters" in {
    val response: IO[GHResponse[Issue]] =
      IO(GHResponse(issue.asRight, okStatusCode, Map.empty))

    implicit val httpClientMock = httpClientMockGet[Issue](
      url = s"repos/$validRepoOwner/$validRepoName/issues/$validIssueNumber",
      response = response
    )

    val issues = new IssuesInterpreter[IO]

    issues.getIssue(validRepoOwner, validRepoName, validIssueNumber, headerUserAgent)

  }

  "Issues.searchIssues" should "call htppClient.get with the right parameters" in {
    val response: IO[GHResponse[SearchIssuesResult]] =
      IO(GHResponse(searchIssuesResult.asRight, okStatusCode, Map.empty))

    implicit val httpClientMock = httpClientMockGet[SearchIssuesResult](
      url = s"search/issues",
      response = response,
      params = Map("q" -> s"+${validSearchParams.map(_.value).mkString("+")}")
    )

    val issues = new IssuesInterpreter[IO]

    issues.searchIssues("", validSearchParams, headerUserAgent)
  }

  "Issues.createIssue" should "call httpClient.post with the right parameters" in {
    val response: IO[GHResponse[Issue]] =
      IO(GHResponse(issue.asRight, createdStatusCode, Map.empty))

    val request = NewIssueRequest(validIssueTitle, validIssueBody, None, List.empty, List.empty)

    implicit val httpClientMock = httpClientMockPost[NewIssueRequest, Issue](
      url = s"repos/$validRepoOwner/$validRepoName/issues",
      req = request,
      response = response
    )

    val issues = new IssuesInterpreter[IO]

    issues.createIssue(
      validRepoOwner,
      validRepoName,
      validIssueTitle,
      validIssueBody,
      None,
      List.empty,
      List.empty,
      headerUserAgent
    )

  }

  "Issues.editIssue" should "call httpClient.patch with the right parameters" in {
    val response: IO[GHResponse[Issue]] = IO(GHResponse(issue.asRight, okStatusCode, Map.empty))

    val request = EditIssueRequest(
      validIssueState,
      validIssueTitle,
      validIssueBody,
      None,
      List.empty,
      List.empty
    )

    implicit val httpClientMock = httpClientMockPatch[EditIssueRequest, Issue](
      url = s"repos/$validRepoOwner/$validRepoName/issues/$validIssueNumber",
      req = request,
      response = response
    )

    val issues = new IssuesInterpreter[IO]
    issues.editIssue(
      validRepoOwner,
      validRepoName,
      validIssueNumber,
      validIssueState,
      validIssueTitle,
      validIssueBody,
      None,
      List.empty,
      List.empty,
      headerUserAgent
    )
  }

  "Issues.ListComments" should "call httpClient.get with the right parameters" in {
    val response: IO[GHResponse[List[Comment]]] =
      IO(GHResponse(List(comment).asRight, okStatusCode, Map.empty))

    implicit val httpClientMock = httpClientMockGet[List[Comment]](
      url = s"repos/$validRepoOwner/$validRepoName/issues/$validIssueNumber/comments",
      response = response
    )

    val issues = new IssuesInterpreter[IO]
    issues.listComments(validRepoOwner, validRepoName, validIssueNumber, headerUserAgent)
  }

  "Issue.CreateComment" should "call to httpClient.post with the right parameters" in {

    val response: IO[GHResponse[Comment]] =
      IO(GHResponse(comment.asRight, createdStatusCode, Map.empty))

    val request = CommentData(validCommentBody)

    implicit val httpClientMock = httpClientMockPost[CommentData, Comment](
      url = s"repos/$validRepoOwner/$validRepoName/issues/$validIssueNumber/comments",
      req = request,
      response = response
    )

    val issues = new IssuesInterpreter[IO]
    issues.createComment(
      validRepoOwner,
      validRepoName,
      validIssueNumber,
      validCommentBody,
      headerUserAgent
    )
  }

  "Issue.EditComment" should "call to httpClient.patch with the right parameters" in {

    val response: IO[GHResponse[Comment]] =
      IO(GHResponse(comment.asRight, okStatusCode, Map.empty))

    val request = CommentData(validCommentBody)

    implicit val httpClientMock = httpClientMockPatch[CommentData, Comment](
      url = s"repos/$validRepoOwner/$validRepoName/issues/comments/$validCommentId",
      req = request,
      response = response
    )

    val issues = new IssuesInterpreter[IO]
    issues.editComment(
      validRepoOwner,
      validRepoName,
      validCommentId,
      validCommentBody,
      headerUserAgent
    )
  }

  "Issue.DeleteComment" should "call to httpClient.delete with the right parameters" in {

    val response: IO[GHResponse[Unit]] =
      IO(GHResponse(().asRight, deletedStatusCode, Map.empty))

    implicit val httpClientMock = httpClientMockDelete(
      url = s"repos/$validRepoOwner/$validRepoName/issues/comments/$validCommentId",
      response = response
    )

    val issues = new IssuesInterpreter[IO]
    issues.deleteComment(validRepoOwner, validRepoName, validCommentId, headerUserAgent)
  }

  "Issues.ListLabelsRepository" should "call httpClient.get with the right parameters" in {
    val response: IO[GHResponse[List[Label]]] =
      IO(GHResponse(List(label).asRight, okStatusCode, Map.empty))

    implicit val httpClientMock = httpClientMockGet[List[Label]](
      url = s"repos/$validRepoOwner/$validRepoName/labels",
      response = response
    )

    val issues = new IssuesInterpreter[IO]
    issues.listLabelsRepository(validRepoOwner, validRepoName, headerUserAgent)
  }

  "Issues.ListLabels" should "call httpClient.get with the right parameters" in {
    val response: IO[GHResponse[List[Label]]] =
      IO(GHResponse(List(label).asRight, okStatusCode, Map.empty))

    implicit val httpClientMock = httpClientMockGet[List[Label]](
      url = s"repos/$validRepoOwner/$validRepoName/issues/$validIssueNumber/labels",
      response = response
    )

    val issues = new IssuesInterpreter[IO]
    issues.listLabels(validRepoOwner, validRepoName, validIssueNumber, headerUserAgent)
  }

  "Issues.AddLabels" should "call httpClient.post with the right parameters" in {
    val response: IO[GHResponse[List[Label]]] =
      IO(GHResponse(List(label).asRight, okStatusCode, Map.empty))

    val request = validIssueLabel

    implicit val httpClientMock = httpClientMockPost[List[String], List[Label]](
      url = s"repos/$validRepoOwner/$validRepoName/issues/$validIssueNumber/labels",
      req = request,
      response = response
    )

    val issues = new IssuesInterpreter[IO]
    issues.addLabels(
      validRepoOwner,
      validRepoName,
      validIssueNumber,
      validIssueLabel,
      headerUserAgent
    )
  }

  "Issues.RemoveLabel" should "call httpClient.delete with the right parameters" in {
    val response: IO[GHResponse[List[Label]]] =
      IO(GHResponse(List(label).asRight, okStatusCode, Map.empty))

    implicit val httpClientMock = httpClientMockDeleteWithResponse[List[Label]](
      url =
        s"repos/$validRepoOwner/$validRepoName/issues/$validIssueNumber/labels/${validIssueLabel.head}",
      response = response
    )

    val issues = new IssuesInterpreter[IO]
    issues.removeLabel(
      validRepoOwner,
      validRepoName,
      validIssueNumber,
      validIssueLabel.head,
      headerUserAgent
    )
  }

  "Issues.listAvailableAssignees" should "call httpClient.get with the right parameters" in {
    val response: IO[GHResponse[List[User]]] =
      IO(GHResponse(List(user).asRight, okStatusCode, Map.empty))

    implicit val httpClientMock = httpClientMockGet[List[User]](
      url = s"repos/$validRepoOwner/$validRepoName/assignees",
      response = response
    )

    val issues = new IssuesInterpreter[IO]
    issues.listAvailableAssignees(
      validRepoOwner,
      validRepoName,
      Some(Pagination(validPage, validPerPage)),
      headerUserAgent
    )
  }

  "Issues.listMilestone" should "call httpClient.get with the right parameters" in {
    val response: IO[GHResponse[List[Milestone]]] =
      IO(GHResponse(Nil.asRight, okStatusCode, Map.empty))

    implicit val httpClientMock = httpClientMockGet[List[Milestone]](
      url = s"repos/$validRepoOwner/$validRepoName/milestones",
      response = response
    )

    val issues = new IssuesInterpreter[IO]
    issues.listMilestones(
      validRepoOwner,
      validRepoName,
      None,
      None,
      None,
      Some(Pagination(validPage, validPerPage)),
      headerUserAgent
    )
  }

  "Issues.createMilestone" should "call httpClient.post with the right parameters" in {
    val response: IO[GHResponse[Milestone]] =
      IO(GHResponse(milestone.asRight, createdStatusCode, Map.empty))

    val request = MilestoneData(
      validIssueTitle,
      None,
      None,
      Some(ZonedDateTime.parse(validMilestoneDueOn, DateTimeFormatter.ISO_ZONED_DATE_TIME))
    )

    implicit val httpClientMock = httpClientMockPost[MilestoneData, Milestone](
      url = s"repos/$validRepoOwner/$validRepoName/milestones",
      req = request,
      response = response
    )

    val issues = new IssuesInterpreter[IO]

    issues.createMilestone(
      validRepoOwner,
      validRepoName,
      validMilestoneTitle,
      None,
      None,
      Some(ZonedDateTime.parse(validMilestoneDueOn, DateTimeFormatter.ISO_ZONED_DATE_TIME)),
      headerUserAgent
    )

  }

  "Issues.getMilestone" should "call httpClient.get with the right parameters" in {
    val response: IO[GHResponse[Milestone]] =
      IO(GHResponse(milestone.asRight, okStatusCode, Map.empty))

    implicit val httpClientMock = httpClientMockGet[Milestone](
      url = s"repos/$validRepoOwner/$validRepoName/milestones/$validMilestoneNumber",
      response = response
    )

    val issues = new IssuesInterpreter[IO]

    issues.getMilestone(validRepoOwner, validRepoName, validMilestoneNumber, headerUserAgent)

  }
  "Issues.updateMilestone" should "call httpClient.patch with the right parameters" in {
    val response: IO[GHResponse[Milestone]] =
      IO(GHResponse(milestone.asRight, okStatusCode, Map.empty))

    val request = MilestoneData(
      validMilestoneTitle,
      None,
      None,
      None
    )

    implicit val httpClientMock = httpClientMockPatch[MilestoneData, Milestone](
      url = s"repos/$validRepoOwner/$validRepoName/milestones/$validMilestoneNumber",
      req = request,
      response = response
    )

    val issues = new IssuesInterpreter[IO]
    issues.updateMilestone(
      validRepoOwner,
      validRepoName,
      validMilestoneNumber,
      validMilestoneTitle,
      None,
      None,
      None,
      headerUserAgent
    )
  }

  "Issue.DeleteMilestone" should "call to httpClient.delete with the right parameters" in {

    val response: IO[GHResponse[Unit]] =
      IO(GHResponse(().asRight, deletedStatusCode, Map.empty))

    implicit val httpClientMock = httpClientMockDelete(
      url = s"repos/$validRepoOwner/$validRepoName/milestones/$validMilestoneNumber",
      response = response
    )

    val issues = new IssuesInterpreter[IO]
    issues.deleteMilestone(validRepoOwner, validRepoName, validMilestoneNumber, headerUserAgent)
  }
}
