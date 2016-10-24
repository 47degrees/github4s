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

import github4s.GithubResponses.GHResponse
import github4s.{GithubApiUrls, HttpClient}
import github4s.free.domain.{Pagination, User}
import io.circe.generic.auto._

/** Factory to encapsulate calls related to Users operations  */
class Users(implicit urls: GithubApiUrls) {

  val httpClient = new HttpClient

  /**
    * Get information for a particular user
    *
    * @param accessToken to identify the authenticated user
    * @param username of the user to retrieve
    * @return GHResponse[User] User details
    */
  def get(accessToken: Option[String] = None, username: String): GHResponse[User] =
    httpClient.get[User](accessToken, s"users/$username")

  /**
    * Get information of the authenticated user
    * @param accessToken to identify the authenticated user
    * @return GHResponse[User] User details
    */
  def getAuth(accessToken: Option[String] = None): GHResponse[User] =
    httpClient.get[User](accessToken, "user")

  /**
    * Get users
    *
    * @param accessToken to identify the authenticated user
    * @param since The integer ID of the last User that you've seen.
    * @param pagination Limit and Offset for pagination
    * @return GHResponse[List[User] ] List of user's details
    */
  def getUsers(
      accessToken: Option[String] = None,
      since: Int,
      pagination: Option[Pagination] = None
  ): GHResponse[List[User]] =
    httpClient.get[List[User]](accessToken, "users", Map("since" → since.toString), pagination)

}
