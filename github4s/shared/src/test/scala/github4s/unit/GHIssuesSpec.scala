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

import cats.free.Free
import github4s.GHIssues
import github4s.GithubResponses.{GHResponse, GHResult}
import github4s.app.GitHub4s
import github4s.free.domain.{Comment, Issue, Label, SearchIssuesResult}
import github4s.utils.BaseSpec

class GHIssuesSpec extends BaseSpec {

  "GHIssues.listIssues" should "call to IssuesOps with the right parameters" in {
    val response: Free[GitHub4s, GHResponse[List[Issue]]] =
      Free.pure(Right(GHResult(List(issue), okStatusCode, Map.empty)))

    val issuesOps = mock[IssueOpsTest]
    (issuesOps.listIssues _)
      .expects(validRepoOwner, validRepoName, sampleToken)
      .returns(response)
    val ghIssues = new GHIssues(sampleToken)(issuesOps)
    ghIssues.listIssues(validRepoOwner, validRepoName)
  }

  "GHIssues.getIssue" should "call to IssuesOps with the right parameters" in {
    val response: Free[GitHub4s, GHResponse[Issue]] =
      Free.pure(Right(GHResult(issue, okStatusCode, Map.empty)))

    val issuesOps = mock[IssueOpsTest]
    (issuesOps.getIssue _)
      .expects(validRepoOwner, validRepoName, validIssueNumber, sampleToken)
      .returns(response)
    val ghIssues = new GHIssues(sampleToken)(issuesOps)
    ghIssues.getIssue(validRepoOwner, validRepoName, validIssueNumber)
  }

  "GHIssues.searchIssues" should "call to IssuesOps with the right parameters" in {
    val response: Free[GitHub4s, GHResponse[SearchIssuesResult]] =
      Free.pure(Right(GHResult(searchIssuesResult, okStatusCode, Map.empty)))

    val issuesOps = mock[IssueOpsTest]
    (issuesOps.searchIssues _)
      .expects(validSearchQuery, validSearchParams, sampleToken)
      .returns(response)
    val ghIssues = new GHIssues(sampleToken)(issuesOps)
    ghIssues.searchIssues(validSearchQuery, validSearchParams)
  }

  "GHIssues.createIssue" should "call to IssuesOps with the right parameters" in {
    val response: Free[GitHub4s, GHResponse[Issue]] =
      Free.pure(Right(GHResult(issue, createdStatusCode, Map.empty)))

    val issuesOps = mock[IssueOpsTest]
    (issuesOps.createIssue _)
      .expects(
        validRepoOwner,
        validRepoName,
        validIssueTitle,
        validIssueBody,
        None,
        List.empty[String],
        List.empty[String],
        sampleToken)
      .returns(response)
    val ghIssues = new GHIssues(sampleToken)(issuesOps)
    ghIssues
      .createIssue(
        validRepoOwner,
        validRepoName,
        validIssueTitle,
        validIssueBody,
        None,
        List.empty,
        List.empty)
  }

  "GHIssues.editIssue" should "call to IssuesOps with the right parameters" in {
    val response: Free[GitHub4s, GHResponse[Issue]] =
      Free.pure(Right(GHResult(issue, okStatusCode, Map.empty)))

    val issuesOps = mock[IssueOpsTest]
    (issuesOps.editIssue _)
      .expects(
        validRepoOwner,
        validRepoName,
        validIssueNumber,
        validIssueState,
        validIssueTitle,
        validIssueBody,
        None,
        List.empty[String],
        List.empty[String],
        sampleToken)
      .returns(response)
    val ghIssues = new GHIssues(sampleToken)(issuesOps)
    ghIssues
      .editIssue(
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

  "Issues.ListComments" should "call to IssuesOps with the right parameters" in {
    val response: Free[GitHub4s, GHResponse[List[Comment]]] =
      Free.pure(Right(GHResult(List(comment), okStatusCode, Map.empty)))

    val commentOps = mock[IssueOpsTest]
    (commentOps.listComments _)
      .expects(validRepoOwner, validRepoName, validIssueNumber, sampleToken)
      .returns(response)

    val ghIssues = new GHIssues(sampleToken)(commentOps)
    ghIssues.listComments(validRepoOwner, validRepoName, validIssueNumber)
  }

  "Issues.CreateComment" should "call to IssueOps with the right parameters" in {

    val response: Free[GitHub4s, GHResponse[Comment]] =
      Free.pure(Right(GHResult(comment, createdStatusCode, Map.empty)))

    val commentOps = mock[IssueOpsTest]
    (commentOps.createComment _)
      .expects(validRepoOwner, validRepoName, validIssueNumber, validCommentBody, sampleToken)
      .returns(response)

    val ghIssues = new GHIssues(sampleToken)(commentOps)
    ghIssues.createComment(validRepoOwner, validRepoName, validIssueNumber, validCommentBody)
  }

  "Issues.EditComment" should "call to IssueOps with the right parameters" in {

    val response: Free[GitHub4s, GHResponse[Comment]] =
      Free.pure(Right(GHResult(comment, okStatusCode, Map.empty)))

    val commentOps = mock[IssueOpsTest]
    (commentOps.editComment _)
      .expects(validRepoOwner, validRepoName, validCommentId, validCommentBody, sampleToken)
      .returns(response)

    val ghIssues = new GHIssues(sampleToken)(commentOps)
    ghIssues.editComment(validRepoOwner, validRepoName, validCommentId, validCommentBody)
  }

  "Issues.DeleteComment" should "call to IssueOps with the right parameters" in {

    val response: Free[GitHub4s, GHResponse[Unit]] =
      Free.pure(Right(GHResult((): Unit, deletedStatusCode, Map.empty)))

    val commentOps = mock[IssueOpsTest]
    (commentOps.deleteComment _)
      .expects(validRepoOwner, validRepoName, validCommentId, sampleToken)
      .returns(response)

    val ghIssues = new GHIssues(sampleToken)(commentOps)
    ghIssues.deleteComment(validRepoOwner, validRepoName, validCommentId)
  }

  "Issues.ListLabels" should "call to IssuesOps with the right parameters" in {
    val response: Free[GitHub4s, GHResponse[List[Label]]] =
      Free.pure(Right(GHResult(List(label), okStatusCode, Map.empty)))

    val commentOps = mock[IssueOpsTest]
    (commentOps.listLabels _)
      .expects(validRepoOwner, validRepoName, validIssueNumber, sampleToken)
      .returns(response)

    val ghIssues = new GHIssues(sampleToken)(commentOps)
    ghIssues.listLabels(validRepoOwner, validRepoName, validIssueNumber)
  }

}
