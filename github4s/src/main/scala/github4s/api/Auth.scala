/*
 * Copyright (c) 2016 47 Degrees, LLC. <http://www.47deg.com>
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
 * the Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
 * FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
 * IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package github4s.api

import java.util.UUID
import github4s.GithubResponses.{GHResult, GHResponse}
import github4s.free.domain._
import github4s.{GithubApiUrls, HttpClient}
import io.circe.generic.auto._
import io.circe.syntax._
import cats.implicits._
import scalaj.http.HttpConstants._

/** Factory to encapsulate calls related to Auth operations  */
class Auth(implicit urls: GithubApiUrls) {

  val httpClient = new HttpClient

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
    * @return GHResponse[Authorization] new authorization with access_token
    */
  def newAuth(
      username: String,
      password: String,
      scopes: List[String],
      note: String,
      client_id: String,
      client_secret: String
  ): GHResponse[Authorization] =
    httpClient.postAuth[Authorization](
      method = "authorizations",
      headers = Map("Authorization" → s"Basic ${base64(s"$username:$password")}"),
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
  ): GHResponse[Authorize] = {
    val state = UUID.randomUUID().toString
    Either.right(
      GHResult(
        result =
          Authorize(authorizeUrl.format(client_id, redirect_uri, scopes.mkString(","), state),
                    state),
        statusCode = 200,
        headers = Map.empty
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
    * @return GHResponse[OAuthToken] new access_token: second step oAuth
    */
  def getAccessToken(
      client_id: String,
      client_secret: String,
      code: String,
      redirect_uri: String,
      state: String
  ): GHResponse[OAuthToken] = httpClient.postOAuth[OAuthToken](
    url = accessTokenUrl,
    data = NewOAuthRequest(client_id, client_secret, code, redirect_uri, state).asJson.noSpaces
  )

}
