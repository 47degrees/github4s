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
import github4s.Encoders._
import github4s.domain._
import github4s.http.HttpClient
import github4s.interpreters.UsersInterpreter
import github4s.utils.BaseSpec

class UserSpec extends BaseSpec {

  "UsersInterpreter.get" should "call to httpClient.get with the right parameters" in {

    implicit val httpClientMock: HttpClient[IO] = httpClientMockGet[User](
      url = s"users/$validUsername",
      response = IO.pure(user)
    )

    val users = new UsersInterpreter[IO]
    users.get(validUsername, headerUserAgent).shouldNotFail
  }

  "User.getAuth" should "call to httpClient.get with the right parameters" in {

    implicit val httpClientMock: HttpClient[IO] = httpClientMockGet[User](
      url = "user",
      response = IO.pure(user)
    )

    val users = new UsersInterpreter[IO]
    users.getAuth(headerUserAgent).shouldNotFail
  }

  "User.getUsers" should "call to httpClient.get with the right parameters" in {

    val request = Map("since" -> 1.toString)

    implicit val httpClientMock: HttpClient[IO] = httpClientMockGet[List[User]](
      url = "users",
      params = request,
      response = IO.pure(List(user))
    )

    val users = new UsersInterpreter[IO]
    users.getUsers(1, None, headerUserAgent).shouldNotFail
  }

  "User.getFollowing" should "call to httpClient.get with the right parameters" in {

    implicit val httpClientMock: HttpClient[IO] = httpClientMockGet[List[User]](
      url = s"users/$validUsername/following",
      response = IO.pure(List(user))
    )

    val users = new UsersInterpreter[IO]
    users.getFollowing(validUsername, None, headerUserAgent).shouldNotFail
  }

}
