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

package github4s.interpreters

import cats.Applicative
import cats.implicits._
import com.github.marklister.base64.Base64.Encoder
import github4s.algebras.Auth
import github4s.Decoders._
import github4s.domain._
import github4s.Encoders._
import github4s.GithubResponses.{GHResponse, GHResult}
import github4s.http.HttpClient
import java.util.UUID

class AuthInterpreter[F[_]: Applicative](
    implicit client: HttpClient[F],
    accessToken: Option[String])
    extends Auth[F] {

  override def newAuth(
      username: String,
      password: String,
      scopes: List[String],
      note: String,
      client_id: String,
      client_secret: String,
      headers: Map[String, String] = Map()): F[GHResponse[Authorization]] =
    client.postAuth[NewAuthRequest, Authorization](
      method = "authorizations",
      headers = Map("Authorization" -> s"Basic ${s"$username:$password".getBytes.toBase64}") ++ headers,
      data = NewAuthRequest(scopes, note, client_id, client_secret)
    )

  override def authorizeUrl(
      client_id: String,
      redirect_uri: String,
      scopes: List[String]): F[GHResponse[Authorize]] = {
    val state = UUID.randomUUID().toString
    val result: GHResponse[Authorize] =
      Either.right(
        GHResult(
          result = Authorize(
            client.urls.authorizeUrl.format(client_id, redirect_uri, scopes.mkString(","), state),
            state),
          statusCode = 200,
          headers = Map.empty
        )
      )
    result.pure[F]
  }

  override def getAccessToken(
      client_id: String,
      client_secret: String,
      code: String,
      redirect_uri: String,
      state: String,
      headers: Map[String, String] = Map()): F[GHResponse[OAuthToken]] =
    client.postOAuth[NewOAuthRequest, OAuthToken](
      url = client.urls.accessTokenUrl,
      headers = headers,
      data = NewOAuthRequest(client_id, client_secret, code, redirect_uri, state)
    )
}
