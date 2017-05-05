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
import github4s.api.PullRequests
import github4s.free.domain._
import github4s.utils.BaseSpec

class PullRequestsSpec extends BaseSpec {

  "PullRequests.list" should "call to httpClient.get with the right parameters" in {

    val response: GHResponse[List[PullRequest]] =
      Right(GHResult(List(pullRequest), okStatusCode, Map.empty))

    val httpClientMock = httpClientMockGet[List[PullRequest]](
      url = s"repos/$validRepoOwner/$validRepoName/pulls",
      response = response
    )
    val pullRequests = new PullRequests[String, Id] {
      override val httpClient: HttpClient[String, Id] = httpClientMock
    }
    pullRequests.list(sampleToken, headerUserAgent, validRepoOwner, validRepoName, Nil)
  }

  "PullRequests.listFiles" should "call to httpClient.get with the right parameters" in {

    val response: GHResponse[List[PullRequestFile]] =
      Right(GHResult(List(pullRequestFile), okStatusCode, Map.empty))

    val httpClientMock = httpClientMockGet[List[PullRequestFile]](
      url = s"repos/$validRepoOwner/$validRepoName/pulls/$validPullRequestNumber/files",
      response = response
    )
    val pullRequests = new PullRequests[String, Id] {
      override val httpClient: HttpClient[String, Id] = httpClientMock
    }
    pullRequests
      .listFiles(
        sampleToken,
        headerUserAgent,
        validRepoOwner,
        validRepoName,
        validPullRequestNumber)
  }

}
