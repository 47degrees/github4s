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

package github4s.interpreters

import github4s.http.HttpClient
import github4s.algebras.Users
import github4s.GHResponse
import github4s.domain._
import github4s.Decoders._

class UsersInterpreter[F[_]](implicit client: HttpClient[F], accessToken: Option[String])
    extends Users[F] {

  override def get(username: String, headers: Map[String, String] = Map()): F[GHResponse[User]] =
    client.get[User](accessToken, s"users/$username", headers)

  override def getAuth(headers: Map[String, String] = Map()): F[GHResponse[User]] =
    client.get[User](accessToken, "user", headers)

  override def getUsers(
      since: Int,
      pagination: Option[Pagination] = None,
      headers: Map[String, String] = Map()
  ): F[GHResponse[List[User]]] =
    client
      .get[List[User]](accessToken, "users", headers, Map("since" -> since.toString), pagination)

  override def getFollowing(
      username: String,
      pagination: Option[Pagination] = None,
      headers: Map[String, String] = Map()
  ): F[GHResponse[List[User]]] =
    client
      .get[List[User]](accessToken, s"users/$username/following", headers, pagination = pagination)
}
