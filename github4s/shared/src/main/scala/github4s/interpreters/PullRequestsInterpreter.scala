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

package github4s.interpreters

import github4s.Decoders._
import github4s.Encoders._
import github4s.GHResponse
import github4s.algebras.PullRequests
import github4s.domain._
import github4s.http.HttpClient

class PullRequestsInterpreter[F[_]](implicit client: HttpClient[F]) extends PullRequests[F] {

  override def getPullRequest(
      owner: String,
      repo: String,
      number: Int,
      headers: Map[String, String]
  ): F[GHResponse[PullRequest]] =
    client.get[PullRequest](s"repos/$owner/$repo/pulls/$number", headers)

  override def listPullRequests(
      owner: String,
      repo: String,
      filters: List[PRFilter],
      pagination: Option[Pagination],
      headers: Map[String, String]
  ): F[GHResponse[List[PullRequest]]] =
    client.get[List[PullRequest]](
      s"repos/$owner/$repo/pulls",
      headers,
      filters.map(_.tupled).toMap,
      pagination
    )

  override def listFiles(
      owner: String,
      repo: String,
      number: Int,
      pagination: Option[Pagination],
      headers: Map[String, String]
  ): F[GHResponse[List[PullRequestFile]]] =
    client
      .get[List[PullRequestFile]](
        s"repos/$owner/$repo/pulls/$number/files",
        headers,
        Map.empty,
        pagination
      )

  override def createPullRequest(
      owner: String,
      repo: String,
      newPullRequest: NewPullRequest,
      head: String,
      base: String,
      maintainerCanModify: Option[Boolean],
      headers: Map[String, String]
  ): F[GHResponse[PullRequest]] = {
    val data: CreatePullRequest = newPullRequest match {
      case NewPullRequestData(title, body, draft) =>
        CreatePullRequestData(title, head, base, body, maintainerCanModify, draft)
      case NewPullRequestIssue(issue) =>
        CreatePullRequestIssue(issue, head, base, maintainerCanModify)
    }
    client
      .post[CreatePullRequest, PullRequest](s"repos/$owner/$repo/pulls", headers, data)
  }

  override def listReviews(
      owner: String,
      repo: String,
      pullRequest: Int,
      pagination: Option[Pagination],
      headers: Map[String, String]
  ): F[GHResponse[List[PullRequestReview]]] =
    client.get[List[PullRequestReview]](
      s"repos/$owner/$repo/pulls/$pullRequest/reviews",
      headers,
      Map.empty,
      pagination
    )

  override def getReview(
      owner: String,
      repo: String,
      pullRequest: Int,
      review: Long,
      headers: Map[String, String]
  ): F[GHResponse[PullRequestReview]] =
    client.get[PullRequestReview](
      s"repos/$owner/$repo/pulls/$pullRequest/reviews/$review",
      headers
    )

  override def createReview(
      owner: String,
      repo: String,
      pullRequest: Int,
      createPRReviewRequest: CreatePRReviewRequest,
      headers: Map[String, String]
  ): F[GHResponse[PullRequestReview]] =
    client
      .post[CreatePRReviewRequest, PullRequestReview](
        s"repos/$owner/$repo/pulls/$pullRequest/reviews",
        headers,
        createPRReviewRequest
      )

  override def listReviewers(
      owner: String,
      repo: String,
      pullRequest: Int,
      pagination: Option[Pagination],
      headers: Map[String, String]
  ): F[GHResponse[ReviewersResponse]] =
    client.get[ReviewersResponse](
      s"repos/$owner/$repo/pulls/$pullRequest/requested_reviewers",
      headers,
      Map.empty,
      pagination
    )

  override def addReviewers(
      owner: String,
      repo: String,
      pullRequest: Int,
      reviewers: ReviewersRequest,
      headers: Map[String, String]
  ): F[GHResponse[PullRequest]] =
    client.post[ReviewersRequest, PullRequest](
      s"repos/$owner/$repo/pulls/$pullRequest/requested_reviewers",
      headers,
      reviewers
    )

  override def removeReviewers(
      owner: String,
      repo: String,
      pullRequest: Int,
      reviewers: ReviewersRequest,
      headers: Map[String, String]
  ): F[GHResponse[PullRequest]] =
    client.deleteWithBody[ReviewersRequest, PullRequest](
      s"repos/$owner/$repo/pulls/$pullRequest/requested_reviewers",
      headers,
      reviewers
    )

  override def updateBranch(
      owner: String,
      repo: String,
      pullRequest: Int,
      expectedHeadSha: Option[String] = None,
      headers: Map[String, String] = Map()
  ): F[GHResponse[BranchUpdateResponse]] =
    client.put[BranchUpdateRequest, BranchUpdateResponse](
      s"repos/$owner/$repo/pulls/$pullRequest/update-branch",
      headers ++ Seq("Accept" -> "application/vnd.github.lydian-preview+json"),
      BranchUpdateRequest(expectedHeadSha)
    )
}
