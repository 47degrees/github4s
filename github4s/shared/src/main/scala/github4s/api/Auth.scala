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

package github4s.api

import java.util.UUID

import github4s.GithubResponses.{GHResponse, GHResult}
import github4s.free.domain._
import github4s.{GithubApiUrls, HttpClient, HttpRequestBuilderExtension}
import io.circe.generic.auto._
import io.circe.syntax._
import cats.implicits._
import github4s.free.interpreters.Capture
import com.github.marklister.base64.Base64.Encoder

/** Factory to encapsulate calls related to Auth operations  */
class Auth[C, M[_]](
    implicit urls: GithubApiUrls,
    C: Capture[M],
    httpClientImpl: HttpRequestBuilderExtension[C, M]) {

  val httpClient = new HttpClient[C, M]

  val authorizeUrl   = urls.authorizeUrl
  val accessTokenUrl = urls.accessTokenUrl

  /**
   * Call to request a new authorization given a basic authentication, the returned object Authorization includes an
   * access token
   *
   * @param username the username of the user
   * @param password the password of the user
   * @param scopes attached to the token
   * @param note to remind you what the OAuth token is for
   * @param client_id the 20 character OAuth app client key for which to create the token
   * @param client_secret the 40 character OAuth app client secret for which to create the token
   * @param headers optional user headers to include in the request
   * @return GHResponse[Authorization] new authorization with access_token
   */
  def newAuth(
      username: String,
      password: String,
      scopes: List[String],
      note: String,
      client_id: String,
      client_secret: String,
      headers: Map[String, String] = Map()
  ): M[GHResponse[Authorization]] =
    httpClient.postAuth[Authorization](
      method = "authorizations",
      headers = Map("Authorization" → s"Basic ${s"$username:$password".getBytes.toBase64}") ++ headers,
      data = NewAuthRequest(scopes, note, client_id, client_secret).asJson.noSpaces
    )

  /**
   * Generates the authorize url with a random state, both are returned within Authorize object
   *
   * @param client_id the 20 character OAuth app client key for which to create the token
   * @param redirect_uri the URL in your app where users will be sent after authorization
   * @param scopes attached to the token
   * @return GHResponse[Authorize] new state: first step oAuth
   */
  def authorizeUrl(
      client_id: String,
      redirect_uri: String,
      scopes: List[String]
  ): M[GHResponse[Authorize]] = {
    val state = UUID.randomUUID().toString
    C.capture(
      Either.right(
        GHResult(
          result = Authorize(
            authorizeUrl.format(client_id, redirect_uri, scopes.mkString(","), state),
            state),
          statusCode = 200,
          headers = Map.empty
        )
      )
    )
  }

  /**
   * Requests an access token based on the code retrieved in the first step of the oAuth process
   *
   * @param client_id the 20 character OAuth app client key for which to create the token
   * @param client_secret the 40 character OAuth app client secret for which to create the token
   * @param code the code you received as a response to Step 1
   * @param redirect_uri the URL in your app where users will be sent after authorization
   * @param state the unguessable random string you optionally provided in Step 1
   * @param headers optional user headers to include in the request
   * @return GHResponse[OAuthToken] new access_token: second step oAuth
   */
  def getAccessToken(
      client_id: String,
      client_secret: String,
      code: String,
      redirect_uri: String,
      state: String,
      headers: Map[String, String] = Map()
  ): M[GHResponse[OAuthToken]] = httpClient.postOAuth[OAuthToken](
    url = accessTokenUrl,
    headers = headers,
    data = NewOAuthRequest(client_id, client_secret, code, redirect_uri, state).asJson.noSpaces
  )

}
