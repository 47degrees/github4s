/*
 * Copyright (c) 2016 47 Degrees, LLC. <http://www.47deg.com>
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
 * the Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
 * FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
 * IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
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

}
