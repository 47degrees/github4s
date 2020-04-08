/*
 * Copyright 2016-2020 47 Degrees <https://www.47deg.com>
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

import cats.effect.IO
import cats.syntax.either._
import github4s.GithubResponses.GHResponse
import github4s.interpreters.ActivitiesInterpreter
import github4s.domain._
import github4s.utils.BaseSpec

class ActivitiesSpec extends BaseSpec {

  implicit val token = sampleToken

  "Activity.setThreadSub" should "call to httpClient.put with the right parameters" in {

    val response: IO[GHResponse[Subscription]] =
      IO(GHResponse(subscription.asRight, okStatusCode, Map.empty))

    val request = SubscriptionRequest(true, false)

    implicit val httpClientMock = httpClientMockPut[SubscriptionRequest, Subscription](
      url = s"notifications/threads/$validThreadId/subscription",
      req = request,
      response = response
    )

    val activities = new ActivitiesInterpreter[IO]

    activities.setThreadSub(validThreadId, true, false, headerUserAgent)
  }

  "Activity.listStargazers" should "call to httpClient.get with the right parameters" in {

    val response: IO[GHResponse[List[Stargazer]]] =
      IO(GHResponse(List(stargazer).asRight, okStatusCode, Map.empty))

    implicit val httpClientMock = httpClientMockGet[List[Stargazer]](
      url = s"repos/$validRepoOwner/$validRepoName/stargazers",
      response = response
    )

    val activities = new ActivitiesInterpreter[IO]

    activities.listStargazers(validRepoOwner, validRepoName, false, None, headerUserAgent)
  }

  "Activity.listStarredRepositories" should "call to httpClient.get with the right parameters" in {

    val response: IO[GHResponse[List[StarredRepository]]] =
      IO(GHResponse(List(starredRepository).asRight, okStatusCode, Map.empty))

    implicit val httpClientMock = httpClientMockGet[List[StarredRepository]](
      url = s"users/$validUsername/starred",
      response = response
    )

    val activities = new ActivitiesInterpreter[IO]

    activities.listStarredRepositories(validUsername, false, None, None, None, headerUserAgent)
  }

}
