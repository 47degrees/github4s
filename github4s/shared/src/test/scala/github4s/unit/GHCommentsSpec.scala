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

import cats.free.Free
import github4s.GithubResponses.{GHResponse, GHResult}
import github4s.{GHComments, HttpClient}
import github4s.api.Comments
import github4s.app.GitHub4s
import github4s.free.domain._
import github4s.utils.BaseSpec

class GHCommentsSpec extends BaseSpec {

  "Comments.CreateComment" should "call to CommentOps with the right parameters" in {

    val response: Free[GitHub4s, GHResponse[Comment]] =
      Free.pure(Right(GHResult(comment, createdStatusCode, Map.empty)))

    val commentOps = mock[CommentOpsTest]
    (commentOps.createComment _)
      .expects(validRepoOwner, validRepoName, validIssueNumber, validCommentBody, sampleToken)
      .returns(response)

    val ghComments = new GHComments(sampleToken)(commentOps)
    ghComments.create(validRepoOwner, validRepoName, validIssueNumber, validCommentBody)
  }
  "Comments.EditComment" should "call to CommentOps with the right parameters" in {

    val response: Free[GitHub4s, GHResponse[Comment]] =
      Free.pure(Right(GHResult(comment, okStatusCode, Map.empty)))

    val commentOps = mock[CommentOpsTest]
    (commentOps.editComment _)
      .expects(validRepoOwner, validRepoName, validCommentId, validCommentBody, sampleToken)
      .returns(response)

    val ghComments = new GHComments(sampleToken)(commentOps)
    ghComments.edit(validRepoOwner, validRepoName, validCommentId, validCommentBody)
  }
  "Comments.DeleteComment" should "call to CommentOps with the right parameters" in {

    val response: Free[GitHub4s, GHResponse[Unit]] =
      Free.pure(Right(GHResult((): Unit, deletedStatusCode, Map.empty)))

    val commentOps = mock[CommentOpsTest]
    (commentOps.deleteComment _)
      .expects(validRepoOwner, validRepoName, validCommentId, sampleToken)
      .returns(response)

    val ghComments = new GHComments(sampleToken)(commentOps)
    ghComments.delete(validRepoOwner, validRepoName, validCommentId)
  }

}
