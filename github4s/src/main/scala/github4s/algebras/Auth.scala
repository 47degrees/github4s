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

package github4s.algebras

import github4s.GHResponse
import github4s.domain._

trait Auth[F[_]] {

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
  ): F[GHResponse[Authorization]]

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
  ): F[GHResponse[Authorize]]

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
  ): F[GHResponse[OAuthToken]]
}
