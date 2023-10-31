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

package github4s.unit

import cats.effect.IO
import github4s.Decoders._
import github4s.Encoders._
import github4s.domain._
import github4s.http.HttpClient
import github4s.interpreters.ActivitiesInterpreter
import github4s.utils.BaseSpec

class ActivitiesSpec extends BaseSpec {

  "Activity.setThreadSub" should "call to httpClient.put with the right parameters" in {

    val request = SubscriptionRequest(true, false)

    implicit val httpClientMock: HttpClient[IO] =
      httpClientMockPut[SubscriptionRequest, Subscription](
        url = s"notifications/threads/$validThreadId/subscription",
        req = request,
        response = IO.pure(subscription)
      )

    val activities = new ActivitiesInterpreter[IO]

    activities.setThreadSub(validThreadId, true, false, headerUserAgent).shouldNotFail
  }

  "Activity.listStargazers" should "call to httpClient.get with the right parameters" in {

    implicit val httpClientMock: HttpClient[IO] = httpClientMockGet[List[Stargazer]](
      url = s"repos/$validRepoOwner/$validRepoName/stargazers",
      response = IO.pure(List(stargazer))
    )

    val activities = new ActivitiesInterpreter[IO]

    activities
      .listStargazers(validRepoOwner, validRepoName, false, None, headerUserAgent)
      .shouldNotFail
  }

  "Activity.listStarredRepositories" should "call to httpClient.get with the right parameters" in {

    implicit val httpClientMock: HttpClient[IO] = httpClientMockGet[List[StarredRepository]](
      url = s"users/$validUsername/starred",
      response = IO.pure(List(starredRepository))
    )

    val activities = new ActivitiesInterpreter[IO]

    activities
      .listStarredRepositories(validUsername, false, None, None, None, headerUserAgent)
      .shouldNotFail
  }

  "Activity.listPublicOrganizationEvents" should "call to httpClient.get with the right parameters" in {

    implicit val httpClientMock: HttpClient[IO] = httpClientMockGet[List[PublicGitHubEvent]](
      url = s"orgs/$validOrganizationName/events",
      response = IO.pure(List(publicGitHubEvent))
    )

    val activities = new ActivitiesInterpreter[IO]

    activities
      .listPublicOrganizationEvents(validUsername, None, headerUserAgent)
      .shouldNotFail
  }

  "Activity.listPublicRepositoryEvents" should "call to httpClient.get with the right parameters" in {

    implicit val httpClientMock: HttpClient[IO] = httpClientMockGet[List[PublicGitHubEvent]](
      url = s"orgs/$validRepoOwner/$validRepoName/events",
      response = IO.pure(List(publicGitHubEvent))
    )

    val activities = new ActivitiesInterpreter[IO]

    activities
      .listPublicRepositoryEvents(validRepoOwner, validRepoName, None, headerUserAgent)
      .shouldNotFail
  }

}
