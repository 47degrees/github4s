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

package github4s.unit

import cats.effect.IO
import github4s.Encoders._
import github4s.Decoders._
import github4s.domain._
import github4s.http.HttpClient
import github4s.interpreters.IssuesInterpreter
import github4s.utils.BaseSpec
import org.http4s

import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

class IssuesSpec extends BaseSpec {

  "Issues.listIssues" should "call httpClient.get with the right parameters" in {

    implicit val httpClientMock: HttpClient[IO] = httpClientMockGet[List[Issue]](
      url = s"repos/$validRepoOwner/$validRepoName/issues",
      response = IO.pure(List(issue))
    )

    val issues = new IssuesInterpreter[IO]

    issues.listIssues(validRepoOwner, validRepoName, None, headerUserAgent).shouldNotFail

  }

  "Issues.getIssue" should "call httpClient.get with the right parameters" in {

    implicit val httpClientMock: HttpClient[IO] = httpClientMockGet[Issue](
      url = s"repos/$validRepoOwner/$validRepoName/issues/$validIssueNumber",
      response = IO.pure(issue)
    )

    val issues = new IssuesInterpreter[IO]

    issues.getIssue(validRepoOwner, validRepoName, validIssueNumber, headerUserAgent).shouldNotFail
  }

  "Issues.searchIssues" should "call htppClient.get with the right parameters" in {

    implicit val httpClientMock: HttpClient[IO] = httpClientMockGet[SearchIssuesResult](
      url = s"search/issues",
      response = IO.pure(searchIssuesResult),
      params = Map("q" -> s"+${validSearchParams.map(_.value).mkString("+")}")
    )

    val issues = new IssuesInterpreter[IO]

    issues.searchIssues("", validSearchParams, None, headerUserAgent).shouldNotFail
  }

  "Issues.createIssue" should "call httpClient.post with the right parameters" in {

    val request = NewIssueRequest(validIssueTitle, validIssueBody, List.empty, List.empty)

    implicit val httpClientMock: HttpClient[IO] = httpClientMockPost[NewIssueRequest, Issue](
      url = s"repos/$validRepoOwner/$validRepoName/issues",
      req = request,
      response = IO.pure(issue)
    )

    val issues = new IssuesInterpreter[IO]

    issues
      .createIssue(
        validRepoOwner,
        validRepoName,
        validIssueTitle,
        validIssueBody,
        None,
        List.empty,
        List.empty,
        headerUserAgent
      )
      .shouldNotFail
  }

  "Issues.editIssue" should "call httpClient.patch with the right parameters" in {

    val request = EditIssueRequest(
      validIssueState,
      validIssueTitle,
      validIssueBody,
      List.empty,
      List.empty
    )

    implicit val httpClientMock: HttpClient[IO] = httpClientMockPatch[EditIssueRequest, Issue](
      url = s"repos/$validRepoOwner/$validRepoName/issues/$validIssueNumber",
      req = request,
      response = IO.pure(issue)
    )

    val issues = new IssuesInterpreter[IO]
    issues
      .editIssue(
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
      .shouldNotFail
  }

  "Issues.ListComments" should "call httpClient.get with the right parameters" in {

    implicit val httpClientMock: HttpClient[IO] = httpClientMockGet[List[Comment]](
      url = s"repos/$validRepoOwner/$validRepoName/issues/$validIssueNumber/comments",
      response = IO.pure(List(comment))
    )

    val issues = new IssuesInterpreter[IO]
    issues
      .listComments(validRepoOwner, validRepoName, validIssueNumber, None, headerUserAgent)
      .shouldNotFail
  }

  "Issue.CreateComment" should "call to httpClient.post with the right parameters" in {

    val request = CommentData(validCommentBody)

    implicit val httpClientMock: HttpClient[IO] = httpClientMockPost[CommentData, Comment](
      url = s"repos/$validRepoOwner/$validRepoName/issues/$validIssueNumber/comments",
      req = request,
      response = IO.pure(comment)
    )

    val issues = new IssuesInterpreter[IO]
    issues
      .createComment(
        validRepoOwner,
        validRepoName,
        validIssueNumber,
        validCommentBody,
        headerUserAgent
      )
      .shouldNotFail
  }

  "Issue.EditComment" should "call to httpClient.patch with the right parameters" in {

    val request = CommentData(validCommentBody)

    implicit val httpClientMock: HttpClient[IO] = httpClientMockPatch[CommentData, Comment](
      url = s"repos/$validRepoOwner/$validRepoName/issues/comments/$validCommentId",
      req = request,
      response = IO.pure(comment)
    )

    val issues = new IssuesInterpreter[IO]
    issues
      .editComment(
        validRepoOwner,
        validRepoName,
        validCommentId,
        validCommentBody,
        headerUserAgent
      )
      .shouldNotFail
  }

  "Issue.DeleteComment" should "call to httpClient.delete with the right parameters" in {

    implicit val httpClientMock: HttpClient[IO] = httpClientMockDelete(
      url = s"repos/$validRepoOwner/$validRepoName/issues/comments/$validCommentId",
      response = IO.unit,
      responseStatus = http4s.Status.NoContent
    )

    val issues = new IssuesInterpreter[IO]
    issues
      .deleteComment(validRepoOwner, validRepoName, validCommentId, headerUserAgent)
      .shouldNotFail
  }

  "Issues.ListLabelsRepository" should "call httpClient.get with the right parameters" in {

    implicit val httpClientMock: HttpClient[IO] = httpClientMockGet[List[Label]](
      url = s"repos/$validRepoOwner/$validRepoName/labels",
      response = IO.pure(List(label))
    )

    val issues = new IssuesInterpreter[IO]
    issues.listLabelsRepository(validRepoOwner, validRepoName, None, headerUserAgent).shouldNotFail
  }

  "Issues.ListLabels" should "call httpClient.get with the right parameters" in {

    implicit val httpClientMock: HttpClient[IO] = httpClientMockGet[List[Label]](
      url = s"repos/$validRepoOwner/$validRepoName/issues/$validIssueNumber/labels",
      response = IO.pure(List(label))
    )

    val issues = new IssuesInterpreter[IO]
    issues
      .listLabels(validRepoOwner, validRepoName, validIssueNumber, None, headerUserAgent)
      .shouldNotFail
  }

  "Issues.CreateLabel" should "call httpClient.post with the right parameters" in {

    val request = validRepoLabel

    implicit val httpClientMock: HttpClient[IO] = httpClientMockPost[Label, Label](
      url = s"repos/$validRepoOwner/$validRepoName/labels",
      req = request,
      response = IO.pure(validRepoLabel)
    )

    val issues = new IssuesInterpreter[IO]
    issues
      .createLabel(
        validRepoOwner,
        validRepoName,
        validRepoLabel,
        headerUserAgent
      )
      .shouldNotFail
  }

  "Issues.UpdateLabel" should "call httpClient.patch with the right parameters" in {

    val request = validRepoLabel

    implicit val httpClientMock: HttpClient[IO] = httpClientMockPatch[Label, Label](
      url = s"repos/$validRepoOwner/$validRepoName/labels/${validRepoLabel.name}",
      req = request,
      response = IO.pure(validRepoLabel)
    )

    val issues = new IssuesInterpreter[IO]
    issues
      .updateLabel(
        validRepoOwner,
        validRepoName,
        validRepoLabel,
        headerUserAgent
      )
      .shouldNotFail
  }

  "Issues.DeleteLabel" should "call httpClient.delete with the right parameters" in {

    implicit val httpClientMock: HttpClient[IO] = httpClientMockDelete(
      url = s"repos/$validRepoOwner/$validRepoName/labels/${validRepoLabel.name}",
      response = IO.unit,
      responseStatus = http4s.Status.NoContent
    )

    val issues = new IssuesInterpreter[IO]
    issues
      .deleteLabel(
        validRepoOwner,
        validRepoName,
        validRepoLabel.name,
        headerUserAgent
      )
      .shouldNotFail
  }

  "Issues.AddLabels" should "call httpClient.post with the right parameters" in {

    val request = validIssueLabel

    implicit val httpClientMock: HttpClient[IO] = httpClientMockPost[List[String], List[Label]](
      url = s"repos/$validRepoOwner/$validRepoName/issues/$validIssueNumber/labels",
      req = request,
      response = IO.pure(List(label))
    )

    val issues = new IssuesInterpreter[IO]
    issues
      .addLabels(
        validRepoOwner,
        validRepoName,
        validIssueNumber,
        validIssueLabel,
        headerUserAgent
      )
      .shouldNotFail
  }

  "Issues.RemoveLabel" should "call httpClient.delete with the right parameters" in {

    implicit val httpClientMock: HttpClient[IO] = httpClientMockDeleteWithResponse[List[Label]](
      url =
        s"repos/$validRepoOwner/$validRepoName/issues/$validIssueNumber/labels/${validIssueLabel.head}",
      response = IO.pure(List(label))
    )

    val issues = new IssuesInterpreter[IO]
    issues
      .removeLabel(
        validRepoOwner,
        validRepoName,
        validIssueNumber,
        validIssueLabel.head,
        headerUserAgent
      )
      .shouldNotFail
  }

  "Issues.listAvailableAssignees" should "call httpClient.get with the right parameters" in {

    implicit val httpClientMock: HttpClient[IO] = httpClientMockGet[List[User]](
      url = s"repos/$validRepoOwner/$validRepoName/assignees",
      response = IO.pure(List(user))
    )

    val issues = new IssuesInterpreter[IO]
    issues
      .listAvailableAssignees(
        validRepoOwner,
        validRepoName,
        Some(Pagination(validPage, validPerPage)),
        headerUserAgent
      )
      .shouldNotFail
  }

  "Issues.listMilestone" should "call httpClient.get with the right parameters" in {

    implicit val httpClientMock: HttpClient[IO] = httpClientMockGet[List[Milestone]](
      url = s"repos/$validRepoOwner/$validRepoName/milestones",
      response = IO.pure(Nil)
    )

    val issues = new IssuesInterpreter[IO]
    issues
      .listMilestones(
        validRepoOwner,
        validRepoName,
        None,
        None,
        None,
        Some(Pagination(validPage, validPerPage)),
        headerUserAgent
      )
      .shouldNotFail
  }

  "Issues.createMilestone" should "call httpClient.post with the right parameters" in {

    val request = MilestoneData(
      validIssueTitle,
      None,
      None,
      Some(ZonedDateTime.parse(validMilestoneDueOn, DateTimeFormatter.ISO_ZONED_DATE_TIME))
    )

    implicit val httpClientMock: HttpClient[IO] = httpClientMockPost[MilestoneData, Milestone](
      url = s"repos/$validRepoOwner/$validRepoName/milestones",
      req = request,
      response = IO.pure(milestone),
      responseStatus = http4s.Status.Created
    )

    val issues = new IssuesInterpreter[IO]

    issues
      .createMilestone(
        validRepoOwner,
        validRepoName,
        validMilestoneTitle,
        None,
        None,
        Some(ZonedDateTime.parse(validMilestoneDueOn, DateTimeFormatter.ISO_ZONED_DATE_TIME)),
        headerUserAgent
      )
      .shouldNotFail
  }

  "Issues.getMilestone" should "call httpClient.get with the right parameters" in {

    implicit val httpClientMock: HttpClient[IO] = httpClientMockGet[Milestone](
      url = s"repos/$validRepoOwner/$validRepoName/milestones/$validMilestoneNumber",
      response = IO.pure(milestone)
    )

    val issues = new IssuesInterpreter[IO]

    issues
      .getMilestone(validRepoOwner, validRepoName, validMilestoneNumber, headerUserAgent)
      .shouldNotFail
  }
  "Issues.updateMilestone" should "call httpClient.patch with the right parameters" in {

    val request = MilestoneData(
      validMilestoneTitle,
      None,
      None,
      None
    )

    implicit val httpClientMock: HttpClient[IO] = httpClientMockPatch[MilestoneData, Milestone](
      url = s"repos/$validRepoOwner/$validRepoName/milestones/$validMilestoneNumber",
      req = request,
      response = IO.pure(milestone)
    )

    val issues = new IssuesInterpreter[IO]
    issues
      .updateMilestone(
        validRepoOwner,
        validRepoName,
        validMilestoneNumber,
        validMilestoneTitle,
        None,
        None,
        None,
        headerUserAgent
      )
      .shouldNotFail
  }

  "Issue.DeleteMilestone" should "call to httpClient.delete with the right parameters" in {

    implicit val httpClientMock: HttpClient[IO] = httpClientMockDelete(
      url = s"repos/$validRepoOwner/$validRepoName/milestones/$validMilestoneNumber",
      response = IO.unit,
      responseStatus = http4s.Status.NoContent
    )

    val issues = new IssuesInterpreter[IO]
    issues
      .deleteMilestone(validRepoOwner, validRepoName, validMilestoneNumber, headerUserAgent)
      .shouldNotFail
  }
}
