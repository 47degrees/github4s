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
import github4s.free.domain.{Pagination, User}

/**
  * Users ops ADT
  */
sealed trait UserOp[A]
final case class GetUser(username: String, accessToken: Option[String] = None)
    extends UserOp[GHResponse[User]]
final case class GetAuthUser(accessToken: Option[String] = None) extends UserOp[GHResponse[User]]
final case class GetUsers(since: Int,
                          pagination: Option[Pagination] = None,
                          accessToken: Option[String] = None)
    extends UserOp[GHResponse[List[User]]]

/**
  * Exposes Users operations as a Free monadic algebra that may be combined with other Algebras via
  * Coproduct
  */
class UserOps[F[_]](implicit I: Inject[UserOp, F]) {

  def getUser(username: String, accessToken: Option[String] = None): Free[F, GHResponse[User]] =
    Free.inject[UserOp, F](GetUser(username, accessToken))

  def getAuthUser(accessToken: Option[String] = None): Free[F, GHResponse[User]] =
    Free.inject[UserOp, F](GetAuthUser(accessToken))

  def getUsers(since: Int,
               pagination: Option[Pagination] = None,
               accessToken: Option[String] = None): Free[F, GHResponse[List[User]]] =
    Free.inject[UserOp, F](GetUsers(since, pagination, accessToken))

}

/**
  * Default implicit based DI factory from which instances of the UserOps may be obtained
  */
object UserOps {

  implicit def instance[F[_]](implicit I: Inject[UserOp, F]): UserOps[F] = new UserOps[F]

}
