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
import github4s.api.Auth
import github4s.free.domain._
import github4s.utils.BaseSpec
import com.github.marklister.base64.Base64.Encoder

class AuthSpec extends BaseSpec {

  "Auth.newAuth" should "call to httpClient.postAuth with the right parameters" in {

    val response: GHResponse[Authorization] =
      Right(GHResult(authorization, okStatusCode, Map.empty))

    val request =
      """
        |{
        |"scopes": ["public_repo"],
        |"note": "New access token",
        |"client_id": "e8e39175648c9db8c280",
        |"client_secret": "1234567890"
        |}""".stripMargin

    val httpClientMock = httpClientMockPostAuth[Authorization](
      url = "authorizations",
      headers =
        Map("Authorization" → s"Basic ${s"rafaparadela:invalidPassword".getBytes.toBase64}"),
      json = request,
      response = response
    )

    val auth = new Auth[String, Id] {
      override val httpClient: HttpClient[String, Id] = httpClientMock
    }

    auth.newAuth(
      validUsername,
      invalidPassword,
      validScopes,
      validNote,
      validClientId,
      invalidClientSecret,
      headerUserAgent)
  }

  "Auth.getAccessToken" should "call to httpClient.postOAuth with the right parameters" in {

    val response: GHResponse[OAuthToken] =
      Right(GHResult(oAuthToken, okStatusCode, Map.empty))

    val request =
      s"""
        |{
        |"client_id": "e8e39175648c9db8c280",
        |"client_secret": "1234567890",
        |"code": "code",
        |"redirect_uri": "http://localhost:9000/_oauth-callback",
        |"state": "$validAuthState"
        |}""".stripMargin

    val httpClientMock = httpClientMockPostOAuth[OAuthToken](
      url = "http://127.0.0.1:9999/login/oauth/access_token",
      json = request,
      response = response
    )

    val auth = new Auth[String, Id] {
      override val httpClient: HttpClient[String, Id] = httpClientMock
    }

    auth.getAccessToken(
      validClientId,
      invalidClientSecret,
      validCode,
      validRedirectUri,
      validAuthState,
      headerUserAgent)
  }
}
