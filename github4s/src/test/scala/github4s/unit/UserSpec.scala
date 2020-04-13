/*
 * Copyright 2016-2020 47 Degrees Open Source <https://www.47deg.com>
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
import github4s.interpreters.UsersInterpreter
import github4s.domain._
import github4s.utils.BaseSpec

class UserSpec extends BaseSpec {

  implicit val token = sampleToken

  "UsersInterpreter.get" should "call to httpClient.get with the right parameters" in {
    val response: IO[GHResponse[User]] =
      IO(GHResponse(user.asRight, okStatusCode, Map.empty))

    implicit val httpClientMock = httpClientMockGet[User](
      url = s"users/$validUsername",
      response = response
    )

    val users = new UsersInterpreter[IO]
    users.get(validUsername, headerUserAgent)
  }

  "User.getAuth" should "call to httpClient.get with the right parameters" in {
    val response: IO[GHResponse[User]] =
      IO(GHResponse(user.asRight, okStatusCode, Map.empty))

    implicit val httpClientMock = httpClientMockGet[User](
      url = "user",
      response = response
    )

    val users = new UsersInterpreter[IO]
    users.getAuth(headerUserAgent)
  }

  "User.getUsers" should "call to httpClient.get with the right parameters" in {
    val response: IO[GHResponse[List[User]]] =
      IO(GHResponse(List(user).asRight, okStatusCode, Map.empty))

    val request = Map("since" -> 1.toString)

    implicit val httpClientMock = httpClientMockGet[List[User]](
      url = "users",
      params = request,
      response = response
    )

    val users = new UsersInterpreter[IO]
    users.getUsers(1, None, headerUserAgent)
  }

  "User.getFollowing" should "call to httpClient.get with the right parameters" in {
    val response: IO[GHResponse[List[User]]] =
      IO(GHResponse(List(user).asRight, okStatusCode, Map.empty))

    implicit val httpClientMock = httpClientMockGet[List[User]](
      url = s"users/$validUsername/following",
      response = response
    )

    val users = new UsersInterpreter[IO]
    users.getFollowing(validUsername, headerUserAgent)
  }

}
