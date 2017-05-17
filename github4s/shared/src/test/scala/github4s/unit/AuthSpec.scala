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

class AuthSpec extends BaseSpec {

  "Auth.newAuth" should "call to httpClient.post with the right parameters" in {

    val response: GHResponse[Authorization] =
      Right(GHResult(authorization, okStatusCode, Map.empty))

    val request =
      """
        |{
        |"username": "rafaparadela",
        |"password": "invalidPassword",
        |"scopes": List("public_repo"),
        |"note": "New access token",
        |"client_id": "e8e39175648c9db8c280",
        |"client_secret": "1234567890"
        |}""".stripMargin

    val httpClientMock = httpClientMockPost[Authorization](
      url = s"authorizations",
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
      invalidClientSecret)
  }

  "Auth.authorizeUrl" should "call to httpClient.post with the right parameters" in {

    val response: GHResponse[Authorize] =
      Right(GHResult(authorize, okStatusCode, Map.empty))

    val auth = new Auth[String, Id]
    auth.authorizeUrl(validClientId, validRedirectUri, validScopes)
    //TODO shouldBe validRedirectUri

  }

  "Auth.getAccessToken" should "call to httpClient.post with the right parameters" in {

    val response: GHResponse[OAuthToken] =
      Right(GHResult(oAuthToken, okStatusCode, Map.empty))

    val request =
      s"""
        |{
        |"client_id": "e8e39175648c9db8c280",
        |"client_secret": "1234567890",
        |"code": "code",
        |"redirect_uri": "http://localhost:9000/_oauth-callback",
        |"state": $validAuthState,
        |"headers": Map()
        |}""".stripMargin

    val httpClientMock = httpClientMockPost[OAuthToken](
      url = s"https://github.com/login/oauth/access_token",
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
      Map.empty)
  }
}
