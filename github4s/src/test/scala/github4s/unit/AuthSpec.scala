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

import cats.effect.IO
import github4s.GithubResponses.{GHResponse, GHResult}
import github4s.utils.BaseSpec
import com.github.marklister.base64.Base64.Encoder
import github4s.domain._
import github4s.interpreters.AuthInterpreter

class AuthSpec extends BaseSpec {

  implicit val token = sampleToken

  "Auth.newAuth" should "call to httpClient.postAuth with the right parameters" in {

    val response: IO[GHResponse[Authorization]] =
      IO(Right(GHResult(authorization, okStatusCode, Map.empty)))

    val request = NewAuthRequest(validScopes, validNote, validClientId, invalidClientSecret)

    implicit val httpClientMock = httpClientMockPostAuth[NewAuthRequest, Authorization](
      url = "authorizations",
      headers =
        Map("Authorization" -> s"Basic ${s"rafaparadela:invalidPassword".getBytes.toBase64}"),
      req = request,
      response = response
    )

    val auth = new AuthInterpreter[IO]

    auth.newAuth(
      validUsername,
      invalidPassword,
      validScopes,
      validNote,
      validClientId,
      invalidClientSecret,
      headerUserAgent
    )

  }

  "Auth.getAccessToken" should "call to httpClient.postOAuth with the right parameters" in {

    val response: IO[GHResponse[OAuthToken]] =
      IO(Right(GHResult(oAuthToken, okStatusCode, Map.empty)))

    val request = NewOAuthRequest(
      validClientId,
      invalidClientSecret,
      validCode,
      validRedirectUri,
      validAuthState
    )

    implicit val httpClientMock = httpClientMockPostOAuth[NewOAuthRequest, OAuthToken](
      url = "https://github.com/login/oauth/access_token",
      req = request,
      response = response
    )

    val auth = new AuthInterpreter[IO]

    auth.getAccessToken(
      validClientId,
      invalidClientSecret,
      validCode,
      validRedirectUri,
      validAuthState,
      headerUserAgent
    )
  }
}
