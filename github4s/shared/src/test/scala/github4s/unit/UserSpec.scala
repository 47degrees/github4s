/*
 * Copyright 2016-2018 47 Degrees, LLC. <http://www.47deg.com>
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
import github4s.api.Users
import github4s.free.domain._
import github4s.utils.BaseSpec

class UserSpec extends BaseSpec {

  "User.get" should "call to httpClient.get with the right parameters" in {

    val response: GHResponse[User] =
      Right(GHResult(user, okStatusCode, Map.empty))

    val httpClientMock = httpClientMockGet[User](
      url = s"users/$validUsername",
      response = response
    )

    val users = new Users[String, Id] {
      override val httpClient: HttpClient[String, Id] = httpClientMock
    }
    users.get(sampleToken, headerUserAgent, validUsername)
  }

  "User.getAuth" should "call to httpClient.get with the right parameters" in {

    val response: GHResponse[User] =
      Right(GHResult(user, okStatusCode, Map.empty))

    val httpClientMock = httpClientMockGet[User](
      url = "user",
      response = response
    )

    val users = new Users[String, Id] {
      override val httpClient: HttpClient[String, Id] = httpClientMock
    }
    users.getAuth(sampleToken, headerUserAgent)
  }

  "User.getUsers" should "call to httpClient.get with the right parameters" in {

    val response: GHResponse[List[User]] =
      Right(GHResult(List(user), okStatusCode, Map.empty))

    val request = Map("since" → 1.toString)

    val httpClientMock = httpClientMockGet[List[User]](
      url = "users",
      params = request,
      response = response
    )

    val users = new Users[String, Id] {
      override val httpClient: HttpClient[String, Id] = httpClientMock
    }
    users.getUsers(sampleToken, headerUserAgent, 1)
  }

}
