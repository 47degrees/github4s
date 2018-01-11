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

import cats.free.Free
import github4s.GithubResponses.{GHResponse, GHResult}
import github4s.{GHUsers, HttpClient}
import github4s.app.GitHub4s
import github4s.free.domain._
import github4s.utils.BaseSpec

class GHUserSpec extends BaseSpec {

  "User.getUser" should "call to UserOps with the right parameters" in {

    val response: Free[GitHub4s, GHResponse[User]] =
      Free.pure(Right(GHResult(user, okStatusCode, Map.empty)))

    val userOps = mock[UserOpsTest]
    (userOps.getUser _)
      .expects(validUsername, sampleToken)
      .returns(response)

    val ghUsers = new GHUsers(sampleToken)(userOps)
    ghUsers.get(validUsername)
  }

  "User.getAuthUser" should "call to UserOps with the right parameters" in {

    val response: Free[GitHub4s, GHResponse[User]] =
      Free.pure(Right(GHResult(user, okStatusCode, Map.empty)))

    val userOps = mock[UserOpsTest]
    (userOps.getAuthUser _)
      .expects(sampleToken)
      .returns(response)

    val ghUsers = new GHUsers(sampleToken)(userOps)
    ghUsers.getAuth
  }

  "User.getUsers" should "call to UserOps with the right parameters" in {

    val response: Free[GitHub4s, GHResponse[List[User]]] =
      Free.pure(Right(GHResult(List(user), okStatusCode, Map.empty)))

    val userOps = mock[UserOpsTest]
    (userOps.getUsers _)
      .expects(1, None, sampleToken)
      .returns(response)

    val ghUsers = new GHUsers(sampleToken)(userOps)
    ghUsers.getUsers(1)
  }

}
