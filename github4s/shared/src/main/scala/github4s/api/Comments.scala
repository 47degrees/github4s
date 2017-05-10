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

package github4s.api

import github4s._
import github4s.GithubResponses.GHResponse
import github4s.{GithubApiUrls, HttpClient, HttpRequestBuilderExtension}
import github4s.free.domain.{Comment, CommentData}
import github4s.free.interpreters.Capture
import io.circe.generic.auto._
import io.circe.syntax._

/** Factory to encapsulate calls related to Comments operations */
class Comments[C, M[_]](
    implicit urls: GithubApiUrls,
    C: Capture[M],
    httpClientImpl: HttpRequestBuilderExtension[C, M]) {

  import Decoders._

  val httpClient = new HttpClient[C, M]

  /**
   *
   * Create a comment
   *
   * @param accessToken to identify the authenticated user
   * @param headers optional user headers to include in the request
   * @param owner of the repo
   * @param repo name of the repo
   * @param number Issue number
   * @param body Comment body
   * @return a GHResponse with the created Comment
   */
  def create(
      accessToken: Option[String] = None,
      headers: Map[String, String] = Map(),
      owner: String,
      repo: String,
      number: Int,
      body: String): M[GHResponse[Comment]] =
    httpClient.post[Comment](
      accessToken,
      s"repos/$owner/$repo/issues/$number/comments",
      headers,
      CommentData(body).asJson.noSpaces)

  /**
   * Edit a comment
   *
   * @param accessToken to identify the authenticated user
   * @param headers optional user headers to include in the request
   * @param owner of the repo
   * @param repo name of the repo
   * @param id Comment id
   * @param body Comment body
   * @return a GHResponse with the edited Comment
   */
  def edit(
      accessToken: Option[String] = None,
      headers: Map[String, String] = Map(),
      owner: String,
      repo: String,
      id: Int,
      body: String): M[GHResponse[Comment]] =
    httpClient
      .patch[Comment](
        accessToken,
        s"repos/$owner/$repo/issues/comments/$id",
        headers,
        CommentData(body).asJson.noSpaces)

  /**
   * Delete a comment
   *
   * @param accessToken to identify the authenticated user
   * @param headers optional user headers to include in the request
   * @param owner of the repo
   * @param repo name of the repo
   * @param id Comment id
   * @return a unit GHResponse
   */
  def delete(
      accessToken: Option[String] = None,
      headers: Map[String, String] = Map(),
      owner: String,
      repo: String,
      id: Int): M[GHResponse[Unit]] =
    httpClient.delete(accessToken, s"repos/$owner/$repo/issues/comments/$id", headers)

}
