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
import github4s.api.Activities
import github4s.free.domain._
import github4s.utils.BaseSpec

class ActivitiesSpec extends BaseSpec {

  "Activity.setThreadSub" should "call to httpClient.put with the right parameters" in {

    val response: GHResponse[Subscription] =
      Right(GHResult(subscription, okStatusCode, Map.empty))

    val request =
      """
        |{
        |  "subscribed": true,
        |  "ignored": false
        |}""".stripMargin

    val httpClientMock = httpClientMockPut[Subscription](
      url = s"notifications/threads/$validThreadId/subscription",
      json = request,
      response = response
    )

    val activities = new Activities[String, Id] {
      override val httpClient: HttpClient[String, Id] = httpClientMock
    }
    activities.setThreadSub(sampleToken, headerUserAgent, validThreadId, true, false)
  }

}
