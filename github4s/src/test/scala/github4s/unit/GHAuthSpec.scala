/*
 * Copyright 2016-2020 47 Degrees, LLC. <http://www.47deg.com>
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
import github4s.GHAuth
import github4s.app.GitHub4s
import github4s.free.algebra.AuthOps
import github4s.free.domain._
import github4s.utils.BaseSpec

class GHAuthSpec extends BaseSpec {

  "Auth.newAuth" should "call to AuthOps with the right parameters" in {

    val response: Free[GitHub4s, GHResponse[Authorization]] =
      Free.pure(Right(GHResult(authorization, okStatusCode, Map.empty)))

    implicit val authOps: AuthOps[GitHub4s] = mock[AuthOpsTest]
    (authOps.newAuth _)
      .expects(
        validUsername,
        invalidPassword,
        validScopes,
        validNote,
        validClientId,
        invalidClientSecret)
      .returns(response)

    val ghAuth = new GHAuth
    ghAuth.newAuth(
      validUsername,
      invalidPassword,
      validScopes,
      validNote,
      validClientId,
      invalidClientSecret)
  }

  "Auth.getAccessToken" should "call to AuthOps with the right parameters" in {

    val response: Free[GitHub4s, GHResponse[OAuthToken]] =
      Free.pure(Right(GHResult(oAuthToken, okStatusCode, Map.empty)))

    implicit val authOps: AuthOps[GitHub4s] = mock[AuthOpsTest]
    (authOps.getAccessToken _)
      .expects(validClientId, invalidClientSecret, validCode, validRedirectUri, validAuthState)
      .returns(response)

    val ghAuth = new GHAuth
    ghAuth.getAccessToken(
      validClientId,
      invalidClientSecret,
      validCode,
      validRedirectUri,
      validAuthState)
  }

}
