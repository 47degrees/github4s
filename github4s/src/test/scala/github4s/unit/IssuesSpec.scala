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

package github4s.unit

import cats.effect.IO
import github4s.GithubResponses.{GHResponse, GHResult}
import github4s.domain._
import github4s.interpreters.IssuesInterpreter
import github4s.utils.BaseSpec

class IssuesSpec extends BaseSpec {

  implicit val token = sampleToken

  "Issues.listIssues" should "call httpClient.get with the right parameters" in {
    val response: IO[GHResponse[List[Issue]]] =
      IO(Right(GHResult(List(issue), okStatusCode, Map.empty)))

    implicit val httpClientMock = httpClientMockGet[List[Issue]](
      url = s"repos/$validRepoOwner/$validRepoName/issues",
      response = response
    )

    val issues = new IssuesInterpreter[IO]

    issues.listIssues(validRepoOwner, validRepoName, headerUserAgent)

  }

  "Issues.getIssue" should "call httpClient.get with the right parameters" in {
    val response: IO[GHResponse[Issue]] =
      IO(Right(GHResult(issue, okStatusCode, Map.empty)))

    implicit val httpClientMock = httpClientMockGet[Issue](
      url = s"repos/$validRepoOwner/$validRepoName/issues/$validIssueNumber",
      response = response
    )

    val issues = new IssuesInterpreter[IO]

    issues.getIssue(validRepoOwner, validRepoName, validIssueNumber, headerUserAgent)

  }

  "Issues.searchIssues" should "call htppClient.get with the right parameters" in {
    val response: IO[GHResponse[SearchIssuesResult]] =
      IO(Right(GHResult(searchIssuesResult, okStatusCode, Map.empty)))

    implicit val httpClientMock = httpClientMockGet[SearchIssuesResult](
      url = s"search/issues",
      response = response,
      params = Map("q" -> s"+${validSearchParams.map(_.value).mkString("+")}")
    )

    val issues = new IssuesInterpreter[IO]

    issues.searchIssues("", validSearchParams, headerUserAgent)
  }

  "Issues.createIssue" should "call httpClient.post with the right parameters" in {
    val response: IO[GHResponse[Issue]] = IO(Right(GHResult(issue, createdStatusCode, Map.empty)))

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
    val response: IO[GHResponse[Issue]] = IO(Right(GHResult(issue, okStatusCode, Map.empty)))

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
      IO(Right(GHResult(List(comment), okStatusCode, Map.empty)))

    implicit val httpClientMock = httpClientMockGet[List[Comment]](
      url = s"repos/$validRepoOwner/$validRepoName/issues/$validIssueNumber/comments",
      response = response
    )

    val issues = new IssuesInterpreter[IO]
    issues.listComments(validRepoOwner, validRepoName, validIssueNumber, headerUserAgent)
  }

  "Issue.CreateComment" should "call to httpClient.post with the right parameters" in {

    val response: IO[GHResponse[Comment]] =
      IO(Right(GHResult(comment, createdStatusCode, Map.empty)))

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
      IO(Right(GHResult(comment, okStatusCode, Map.empty)))

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
      IO(Right(GHResult((): Unit, deletedStatusCode, Map.empty)))

    implicit val httpClientMock = httpClientMockDelete(
      url = s"repos/$validRepoOwner/$validRepoName/issues/comments/$validCommentId",
      response = response
    )

    val issues = new IssuesInterpreter[IO]
    issues.deleteComment(validRepoOwner, validRepoName, validCommentId, headerUserAgent)
  }

  "Issues.ListLabels" should "call httpClient.get with the right parameters" in {
    val response: IO[GHResponse[List[Label]]] =
      IO(Right(GHResult(List(label), okStatusCode, Map.empty)))

    implicit val httpClientMock = httpClientMockGet[List[Label]](
      url = s"repos/$validRepoOwner/$validRepoName/issues/$validIssueNumber/labels",
      response = response
    )

    val issues = new IssuesInterpreter[IO]
    issues.listLabels(validRepoOwner, validRepoName, validIssueNumber, headerUserAgent)
  }

  "Issues.AddLabels" should "call httpClient.post with the right parameters" in {
    val response: IO[GHResponse[List[Label]]] =
      IO(Right(GHResult(List(label), okStatusCode, Map.empty)))

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
      IO(Right(GHResult(List(label), okStatusCode, Map.empty)))

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
      IO(Right(GHResult(List(user), okStatusCode, Map.empty)))

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

}
