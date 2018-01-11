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

import github4s.GithubResponses.GHResponse
import github4s.{GithubApiUrls, HttpClient, HttpRequestBuilderExtension}
import github4s.free.domain._
import github4s.free.interpreters.Capture
import github4s.util.URLEncoder
import io.circe.syntax._
import io.circe.generic.auto._

/** Factory to encapsulate calls related to Issues operations  */
class Issues[C, M[_]](
    implicit urls: GithubApiUrls,
    C: Capture[M],
    httpClientImpl: HttpRequestBuilderExtension[C, M]) {

  val httpClient = new HttpClient[C, M]

  /**
   * List issues for a repository
   *
   * Note: In the past, pull requests and issues were more closely aligned than they are now.
   * As far as the API is concerned, every pull request is an issue, but not every issue is a
   * pull request.
   *
   * This endpoint may also return pull requests in the response. If an issue is a pull request,
   * the object will include a `pull_request` key.
   *
   * @param accessToken to identify the authenticated user
   * @param headers optional user headers to include in the request
   * @param owner of the repo
   * @param repo name of the repo
   * @return a GHResponse with the issue list.
   */
  def list(
      accessToken: Option[String] = None,
      headers: Map[String, String] = Map(),
      owner: String,
      repo: String): M[GHResponse[List[Issue]]] =
    httpClient.get[List[Issue]](accessToken, s"repos/$owner/$repo/issues", headers)

  /**
   * Search for issues
   *
   * Note: In the past, pull requests and issues were more closely aligned than they are now.
   * As far as the API is concerned, every pull request is an issue, but not every issue is a
   * pull request.
   *
   * This endpoint may also return pull requests in the response. If an issue is a pull request,
   * the object will include a `pull_request` key.
   *
   * @param accessToken to identify the authenticated user
   * @param headers optional user headers to include in the request
   * @param query the query string for the search
   * @param searchParams list of search params
   * @return a GHResponse with the result of the search.
   */
  def search(
      accessToken: Option[String] = None,
      headers: Map[String, String] = Map(),
      query: String,
      searchParams: List[SearchParam]): M[GHResponse[SearchIssuesResult]] = {
    val queryString = s"${URLEncoder.encode(query)}+${searchParams.map(_.value).mkString("+")}"
    httpClient
      .get[SearchIssuesResult](accessToken, "search/issues", headers, Map("q" -> queryString))
  }

  /**
   * Create an issue
   *
   * @param accessToken to identify the authenticated user
   * @param headers optional user headers to include in the request
   * @param owner of the repo
   * @param repo name of the repo
   * @param title The title of the issue.
   * @param body The contents of the issue.
   * @param milestone The number of the milestone to associate this issue with.
   * @param labels Labels to associate with this issue.
   * @param assignees Logins for Users to assign to this issue.
   */
  def create(
      accessToken: Option[String] = None,
      headers: Map[String, String] = Map(),
      owner: String,
      repo: String,
      title: String,
      body: String,
      milestone: Option[Int],
      labels: List[String],
      assignees: List[String]): M[GHResponse[Issue]] =
    httpClient.post[Issue](
      accessToken,
      s"repos/$owner/$repo/issues",
      headers,
      data = NewIssueRequest(title, body, milestone, labels, assignees).asJson.noSpaces)

  /**
   * Edit an issue
   *
   * @param accessToken to identify the authenticated user
   * @param headers optional user headers to include in the request
   * @param owner of the repo
   * @param repo name of the repo
   * @param issue number
   * @param state State of the issue. Either open or closed.
   * @param title The title of the issue.
   * @param body The contents of the issue.
   * @param milestone The number of the milestone to associate this issue with.
   * @param labels Labels to associate with this issue.
   *               Pass one or more Labels to replace the set of Labels on this Issue.
   *               Send an empty list to clear all Labels from the Issue.
   * @param assignees Logins for Users to assign to this issue.
   *                  Pass one or more user logins to replace the set of assignees on this Issue.
   *                  Send an empty list to clear all assignees from the Issue.
   */
  def edit(
      accessToken: Option[String] = None,
      headers: Map[String, String] = Map(),
      owner: String,
      repo: String,
      issue: Int,
      state: String,
      title: String,
      body: String,
      milestone: Option[Int],
      labels: List[String],
      assignees: List[String]): M[GHResponse[Issue]] =
    httpClient.patch[Issue](
      accessToken,
      s"repos/$owner/$repo/issues/$issue",
      headers,
      data = EditIssueRequest(state, title, body, milestone, labels, assignees).asJson.noSpaces)

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
  def createComment(
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
  def editComment(
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
  def deleteComment(
      accessToken: Option[String] = None,
      headers: Map[String, String] = Map(),
      owner: String,
      repo: String,
      id: Int): M[GHResponse[Unit]] =
    httpClient.delete(accessToken, s"repos/$owner/$repo/issues/comments/$id", headers)

}
