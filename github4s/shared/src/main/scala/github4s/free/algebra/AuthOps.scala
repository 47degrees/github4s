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

package github4s.free.algebra

import cats.InjectK
import cats.free.Free
import github4s.GithubResponses._
import github4s.free.domain.{OAuthToken, Authorize, Authorization}

/**
  * Auths ops ADT
  */
sealed trait AuthOp[A]

final case class NewAuth(
    username: String,
    password: String,
    scopes: List[String],
    note: String,
    client_id: String,
    client_secret: String
) extends AuthOp[GHResponse[Authorization]]

final case class AuthorizeUrl(
    client_id: String,
    redirect_uri: String,
    scopes: List[String]
) extends AuthOp[GHResponse[Authorize]]

final case class GetAccessToken(
    client_id: String,
    client_secret: String,
    code: String,
    redirect_uri: String,
    state: String
) extends AuthOp[GHResponse[OAuthToken]]

/**
  * Exposes Auths operations as a Free monadic algebra that may be combined with other Algebras via
  * Coproduct
  */
class AuthOps[F[_]](implicit I: InjectK[AuthOp, F]) {

  def newAuth(
      username: String,
      password: String,
      scopes: List[String],
      note: String,
      client_id: String,
      client_secret: String
  ): Free[F, GHResponse[Authorization]] =
    Free.inject[AuthOp, F](NewAuth(username, password, scopes, note, client_id, client_secret))

  def authorizeUrl(
      client_id: String,
      redirect_uri: String,
      scopes: List[String]
  ): Free[F, GHResponse[Authorize]] =
    Free.inject[AuthOp, F](AuthorizeUrl(client_id, redirect_uri, scopes))

  def getAccessToken(
      client_id: String,
      client_secret: String,
      code: String,
      redirect_uri: String,
      state: String
  ): Free[F, GHResponse[OAuthToken]] =
    Free.inject[AuthOp, F](GetAccessToken(client_id, client_secret, code, redirect_uri, state))

}

/**
  * Default implicit based DI factory from which instances of the AuthOps may be obtained
  */
object AuthOps {

  implicit def instance[F[_]](implicit I: InjectK[AuthOp, F]): AuthOps[F] = new AuthOps[F]

}
