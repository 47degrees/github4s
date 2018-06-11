/*
 * Copyright 2016-2018 47 Degrees, LLC. <http://www.47deg.com>
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

import cats.Id
import github4s.GithubResponses.{GHResponse, GHResult}
import github4s.HttpClient
import github4s.api.Issues
import github4s.free.domain.{Comment, Issue, Label, SearchIssuesResult}
import github4s.utils.BaseSpec

class IssuesSpec extends BaseSpec {

  "Issues.list" should "call httpClient.get with the right parameters" in {
    val response: GHResponse[List[Issue]] =
      Right(GHResult(List(issue), okStatusCode, Map.empty))

    val httpClientMock = httpClientMockGet[List[Issue]](
      url = s"repos/$validRepoOwner/$validRepoName/issues",
      response = response
    )

    val issues = new Issues[String, Id] {
      override val httpClient: HttpClient[String, Id] = httpClientMock
    }
    issues.list(sampleToken, headerUserAgent, validRepoOwner, validRepoName)
  }

  "Issues.get" should "call httpClient.get with the right parameters" in {
    val response: GHResponse[Issue] =
      Right(GHResult(issue, okStatusCode, Map.empty))

    val httpClientMock = httpClientMockGet[Issue](
      url = s"repos/$validRepoOwner/$validRepoName/issues/$validIssueNumber",
      response = response
    )

    val issues = new Issues[String, Id] {
      override val httpClient: HttpClient[String, Id] = httpClientMock
    }
    issues.get(sampleToken, headerUserAgent, validRepoOwner, validRepoName, validIssueNumber)
  }

  "Issues.search" should "call htppClient.get with the right parameters" in {
    val response: GHResponse[SearchIssuesResult] =
      Right(GHResult(searchIssuesResult, okStatusCode, Map.empty))

    val httpClientMock = httpClientMockGet[SearchIssuesResult](
      url = s"search/issues",
      response = response,
      params = Map("q" -> s"+${validSearchParams.map(_.value).mkString("+")}")
    )

    val issues = new Issues[String, Id] {
      override val httpClient: HttpClient[String, Id] = httpClientMock
    }
    issues.search(sampleToken, headerUserAgent, "", validSearchParams)
  }

  "Issues.create" should "call httpClient.post with the right parameters" in {
    val response: GHResponse[Issue] = Right(GHResult(issue, createdStatusCode, Map.empty))

    val request =
      s"""
         |{
         |  "title": "$validIssueTitle",
         |  "body": "$validIssueBody",
         |  "milestone": null,
         |  "labels": [],
         |  "assignees": []
         |}
       """.stripMargin

    val httpClientMock = httpClientMockPost[Issue](
      url = s"repos/$validRepoOwner/$validRepoName/issues",
      json = request,
      response = response
    )

    val issues = new Issues[String, Id] {
      override val httpClient: HttpClient[String, Id] = httpClientMock
    }
    issues.create(
      sampleToken,
      headerUserAgent,
      validRepoOwner,
      validRepoName,
      validIssueTitle,
      validIssueBody,
      None,
      List.empty,
      List.empty)
  }

  "Issues.edit" should "call httpClient.patch with the right parameters" in {
    val response: GHResponse[Issue] = Right(GHResult(issue, okStatusCode, Map.empty))

    val request =
      s"""
         |{
         |  "state": "$validIssueState",
         |  "title": "$validIssueTitle",
         |  "body": "$validIssueBody",
         |  "milestone": null,
         |  "labels": [],
         |  "assignees": []
         |}
       """.stripMargin

    val httpClientMock = httpClientMockPatch[Issue](
      url = s"repos/$validRepoOwner/$validRepoName/issues/$validIssueNumber",
      json = request,
      response = response
    )

    val issues = new Issues[String, Id] {
      override val httpClient: HttpClient[String, Id] = httpClientMock
    }
    issues.edit(
      sampleToken,
      headerUserAgent,
      validRepoOwner,
      validRepoName,
      validIssueNumber,
      validIssueState,
      validIssueTitle,
      validIssueBody,
      None,
      List.empty,
      List.empty)
  }

  "Issues.ListComments" should "call httpClient.get with the right parameters" in {
    val response: GHResponse[List[Comment]] =
      Right(GHResult(List(comment), okStatusCode, Map.empty))

    val httpClientMock = httpClientMockGet[List[Comment]](
      url = s"repos/$validRepoOwner/$validRepoName/issues/$validIssueNumber/comments",
      response = response
    )

    val issues = new Issues[String, Id] {
      override val httpClient: HttpClient[String, Id] = httpClientMock
    }
    issues.listComments(
      sampleToken,
      headerUserAgent,
      validRepoOwner,
      validRepoName,
      validIssueNumber)
  }

  "Issue.CreateComment" should "call to httpClient.post with the right parameters" in {

    val response: GHResponse[Comment] =
      Right(GHResult(comment, createdStatusCode, Map.empty))

    val request =
      """
        |{
        |  "body": "the comment"
        |}""".stripMargin

    val httpClientMock = httpClientMockPost[Comment](
      url = s"repos/$validRepoOwner/$validRepoName/issues/$validIssueNumber/comments",
      json = request,
      response = response
    )

    val issues = new Issues[String, Id] {
      override val httpClient: HttpClient[String, Id] = httpClientMock
    }
    issues.createComment(
      sampleToken,
      headerUserAgent,
      validRepoOwner,
      validRepoName,
      validIssueNumber,
      validCommentBody)
  }

  "Issue.EditComment" should "call to httpClient.patch with the right parameters" in {

    val response: GHResponse[Comment] =
      Right(GHResult(comment, okStatusCode, Map.empty))

    val request =
      """
        |{
        |  "body": "the comment"
        |}""".stripMargin

    val httpClientMock = httpClientMockPatch[Comment](
      url = s"repos/$validRepoOwner/$validRepoName/issues/comments/$validCommentId",
      json = request,
      response = response
    )

    val issues = new Issues[String, Id] {
      override val httpClient: HttpClient[String, Id] = httpClientMock
    }
    issues.editComment(
      sampleToken,
      headerUserAgent,
      validRepoOwner,
      validRepoName,
      validCommentId,
      validCommentBody)
  }

  "Issue.DeleteComment" should "call to httpClient.delete with the right parameters" in {

    val response: GHResponse[Unit] =
      Right(GHResult((): Unit, deletedStatusCode, Map.empty))

    val httpClientMock = httpClientMockDelete(
      url = s"repos/$validRepoOwner/$validRepoName/issues/comments/$validCommentId",
      response = response
    )

    val issues = new Issues[String, Id] {
      override val httpClient: HttpClient[String, Id] = httpClientMock
    }
    issues.deleteComment(
      sampleToken,
      headerUserAgent,
      validRepoOwner,
      validRepoName,
      validCommentId)
  }

  "Issues.ListLabels" should "call httpClient.get with the right parameters" in {
    val response: GHResponse[List[Label]] =
      Right(GHResult(List(label), okStatusCode, Map.empty))

    val httpClientMock = httpClientMockGet[List[Label]](
      url = s"repos/$validRepoOwner/$validRepoName/issues/$validIssueNumber/labels",
      response = response
    )

    val issues = new Issues[String, Id] {
      override val httpClient: HttpClient[String, Id] = httpClientMock
    }
    issues.listLabels(
      sampleToken,
      headerUserAgent,
      validRepoOwner,
      validRepoName,
      validIssueNumber)
  }

  "Issues.AddLabels" should "call httpClient.post with the right parameters" in {
    val response: GHResponse[List[Label]] =
      Right(GHResult(List(label), okStatusCode, Map.empty))

    val request =
      """
        |[
        |  "bug",
        |  "code review"
        |]""".stripMargin

    val httpClientMock = httpClientMockPost[List[Label]](
      url = s"repos/$validRepoOwner/$validRepoName/issues/$validIssueNumber/labels",
      json = request,
      response = response
    )

    val issues = new Issues[String, Id] {
      override val httpClient: HttpClient[String, Id] = httpClientMock
    }
    issues.addLabels(
      sampleToken,
      headerUserAgent,
      validRepoOwner,
      validRepoName,
      validIssueNumber,
      validIssueLabel)
  }
}
