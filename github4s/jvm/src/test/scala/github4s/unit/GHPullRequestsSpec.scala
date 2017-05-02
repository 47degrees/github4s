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
import github4s.GHPullRequests
import github4s.GithubResponses.{GHResponse, GHResult}
import github4s.app.GitHub4s
import github4s.free.algebra.PullRequestOps
import github4s.free.domain._
import github4s.utils.TestUtils
import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito._
import org.scalatest.mockito.MockitoSugar.mock
import org.scalatest.{FlatSpec, Matchers}

class GHPullRequestsSpec extends FlatSpec with Matchers with TestUtils {

  "GHPullRequests.list" should "call to PullRequestOps with the right parameters" in {

    val response: Free[GitHub4s, GHResponse[List[PullRequest]]] =
      Free.pure(Right(GHResult(List(pullRequest), okStatusCode, Map.empty)))

    val pullRequestOps = mock[PullRequestOps[GitHub4s]]
    when(
      pullRequestOps
        .listPullRequests(any[String], any[String], any[List[PRFilter]], any[Option[String]]))
      .thenReturn(response)

    val token          = Some("token")
    val ghPullRequests = new GHPullRequests(token)(pullRequestOps)
    ghPullRequests.list(validRepoOwner, validRepoName)

    verify(pullRequestOps).listPullRequests(validRepoOwner, validRepoName, Nil, token)
  }

  "GHPullRequests.listFiles" should "call to PullRequestOps with the right parameters" in {

    val response: Free[GitHub4s, GHResponse[List[PullRequestFile]]] =
      Free.pure(Right(GHResult(List(pullRequestFile), okStatusCode, Map.empty)))

    val pullRequestOps = mock[PullRequestOps[GitHub4s]]
    when(
      pullRequestOps
        .listPullRequestFiles(any[String], any[String], any[Int], any[Option[String]]))
      .thenReturn(response)

    val token          = Some("token")
    val ghPullRequests = new GHPullRequests(token)(pullRequestOps)
    ghPullRequests.listFiles(validRepoOwner, validRepoName, validPullRequestNumber)

    verify(pullRequestOps)
      .listPullRequestFiles(validRepoOwner, validRepoName, validPullRequestNumber, token)
  }

}
