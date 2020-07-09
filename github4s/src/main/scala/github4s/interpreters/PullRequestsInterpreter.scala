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
import github4s.algebras.PullRequests
import github4s.GHResponse
import github4s.domain._
import github4s.Decoders._
import github4s.Encoders._

class PullRequestsInterpreter[F[_]](implicit client: HttpClient[F], accessToken: Option[String])
    extends PullRequests[F] {

  override def getPullRequest(
      owner: String,
      repo: String,
      number: Int,
      headers: Map[String, String]
  ): F[GHResponse[PullRequest]] =
    client.get[PullRequest](accessToken, s"repos/$owner/$repo/pulls/$number", headers)

  override def listPullRequests(
      owner: String,
      repo: String,
      filters: List[PRFilter],
      pagination: Option[Pagination],
      headers: Map[String, String]
  ): F[GHResponse[List[PullRequest]]] =
    client.get[List[PullRequest]](
      accessToken,
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
        accessToken,
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
      case NewPullRequestData(title, body) =>
        CreatePullRequestData(title, head, base, body, maintainerCanModify)
      case NewPullRequestIssue(issue) =>
        CreatePullRequestIssue(issue, head, base, maintainerCanModify)
    }
    client
      .post[CreatePullRequest, PullRequest](accessToken, s"repos/$owner/$repo/pulls", headers, data)
  }

  override def listReviews(
      owner: String,
      repo: String,
      pullRequest: Int,
      pagination: Option[Pagination],
      headers: Map[String, String]
  ): F[GHResponse[List[PullRequestReview]]] =
    client.get[List[PullRequestReview]](
      accessToken,
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
      accessToken,
      s"repos/$owner/$repo/pulls/$pullRequest/reviews/$review",
      headers
    )
}
