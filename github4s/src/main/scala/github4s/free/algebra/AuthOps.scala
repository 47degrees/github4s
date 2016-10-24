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

package github4s.free.algebra

import cats.free.{Free, Inject}
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
class AuthOps[F[_]](implicit I: Inject[AuthOp, F]) {

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

  implicit def instance[F[_]](implicit I: Inject[AuthOp, F]): AuthOps[F] = new AuthOps[F]

}
