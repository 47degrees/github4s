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

package github4s.unit

import cats.Id
import github4s.GithubResponses.{GHResponse, GHResult}
import github4s.HttpClient
import github4s.api.Comments
import github4s.free.domain._
import github4s.utils.BaseSpec

class CommentsSpec extends BaseSpec {

  "Comment.CreateComment" should "call to httpClient.post with the right parameters" in {

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

    val comments = new Comments[String, Id] {
      override val httpClient: HttpClient[String, Id] = httpClientMock
    }
    comments.create(
      sampleToken,
      headerUserAgent,
      validRepoOwner,
      validRepoName,
      validIssueNumber,
      validCommentBody)
  }

  "Comment.EditComment" should "call to httpClient.patch with the right parameters" in {

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

    val comments = new Comments[String, Id] {
      override val httpClient: HttpClient[String, Id] = httpClientMock
    }
    comments.edit(
      sampleToken,
      headerUserAgent,
      validRepoOwner,
      validRepoName,
      validCommentId,
      validCommentBody)
  }

  "Comment.DeleteComment" should "call to httpClient.delete with the right parameters" in {

    val response: GHResponse[Unit] =
      Right(GHResult((): Unit, deletedStatusCode, Map.empty))

    val httpClientMock = httpClientMockDelete[Unit](
      url = s"repos/$validRepoOwner/$validRepoName/issues/comments/$validCommentId",
      response = response
    )

    val comments = new Comments[String, Id] {
      override val httpClient: HttpClient[String, Id] = httpClientMock
    }
    comments.delete(sampleToken, headerUserAgent, validRepoOwner, validRepoName, validCommentId)
  }

}
