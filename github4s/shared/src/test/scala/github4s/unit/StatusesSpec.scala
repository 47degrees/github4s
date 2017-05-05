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
import github4s.api.Statuses
import github4s.free.domain._
import github4s.utils.BaseSpec

class StatusesSpec extends BaseSpec {

  "Statuses.get" should "call httpClient.get with the right parameters" in {
    val response: GHResponse[CombinedStatus] =
      Right(GHResult(combinedStatus, okStatusCode, Map.empty))

    val httpClientMock = httpClientMockGet[CombinedStatus](
      url = s"repos/$validRepoOwner/$validRepoName/commits/$validRefSingle/status",
      response = response
    )

    val statuses = new Statuses[String, Id] {
      override val httpClient: HttpClient[String, Id] = httpClientMock
    }
    statuses.get(sampleToken, headerUserAgent, validRepoOwner, validRepoName, validRefSingle)
  }

  "Statuses.list" should "call htppClient.get with the right parameters" in {
    val response: GHResponse[List[Status]] = Right(GHResult(List(status), okStatusCode, Map.empty))

    val httpClientMock = httpClientMockGet[List[Status]](
      url = s"repos/$validRepoOwner/$validRepoName/commits/$validRefSingle/statuses",
      response = response
    )

    val statuses = new Statuses[String, Id] {
      override val httpClient: HttpClient[String, Id] = httpClientMock
    }
    statuses.list(sampleToken, headerUserAgent, validRepoOwner, validRepoName, validRefSingle)
  }

  "Statuses.create" should "call httpClient.post with the right parameters" in {
    val response: GHResponse[Status] = Right(GHResult(status, createdStatusCode, Map.empty))

    val httpClientMock = httpClientMockPost[Status](
      url = s"repos/$validRepoOwner/$validRepoName/statuses/$validCommitSha",
      json = s"""{"state":"$validStatusState"}""",
      response = response
    )

    val statuses = new Statuses[String, Id] {
      override val httpClient: HttpClient[String, Id] = httpClientMock
    }
    statuses.create(
      sampleToken,
      headerUserAgent,
      validRepoOwner,
      validRepoName,
      validCommitSha,
      validStatusState,
      None,
      None,
      None)
  }
}
