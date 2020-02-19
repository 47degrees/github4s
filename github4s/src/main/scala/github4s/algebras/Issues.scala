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

package github4s.algebras

import github4s.GithubResponses.GHResponse
import github4s.domain._

trait Issues[F[_]] {

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
   * @param owner of the repo
   * @param repo name of the repo
   * @param headers optional user headers to include in the request
   * @return a GHResponse with the issue list.
   */
  def listIssues(
      owner: String,
      repo: String,
      headers: Map[String, String] = Map()
  ): F[GHResponse[List[Issue]]]

  /**
   * Get a single issue of a repository
   *
   * Note: In the past, pull requests and issues were more closely aligned than they are now.
   * As far as the API is concerned, every pull request is an issue, but not every issue is a
   * pull request.
   *
   * This endpoint may also return pull requests in the response. If an issue is a pull request,
   * the object will include a `pull_request` key.
   *
   * @param owner of the repo
   * @param repo name of the repo
   * @param number Issue number
   * @param headers optional user headers to include in the request
   * @return a GHResponse with the issue list.
   */
  def getIssue(
      owner: String,
      repo: String,
      number: Int,
      headers: Map[String, String] = Map()
  ): F[GHResponse[Issue]]

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
   * @param query the query string for the search
   * @param searchParams list of search params
   * @param headers optional user headers to include in the request
   * @return a GHResponse with the result of the search.
   */
  def searchIssues(
      query: String,
      searchParams: List[SearchParam],
      headers: Map[String, String] = Map()
  ): F[GHResponse[SearchIssuesResult]]

  /**
   * Create an issue
   *
   * @param owner of the repo
   * @param repo name of the repo
   * @param title The title of the issue.
   * @param body The contents of the issue.
   * @param milestone The number of the milestone to associate this issue with.
   * @param labels Labels to associate with this issue.
   * @param assignees Logins for Users to assign to this issue.
   * @param headers optional user headers to include in the request
   */
  def createIssue(
      owner: String,
      repo: String,
      title: String,
      body: String,
      milestone: Option[Int],
      labels: List[String],
      assignees: List[String],
      headers: Map[String, String] = Map()
  ): F[GHResponse[Issue]]

  /**
   * Edit an issue
   *
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
   * @param headers optional user headers to include in the request
   */
  def editIssue(
      owner: String,
      repo: String,
      issue: Int,
      state: String,
      title: String,
      body: String,
      milestone: Option[Int],
      labels: List[String],
      assignees: List[String],
      headers: Map[String, String] = Map()
  ): F[GHResponse[Issue]]

  /**
   * List comments to an Issue
   *
   * @param owner of the repo
   * @param repo name of the repo
   * @param number Issue number
   * @param headers optional user headers to include in the request
   * @return a GHResponse with the comment list of the Issue.
   */
  def listComments(
      owner: String,
      repo: String,
      number: Int,
      headers: Map[String, String] = Map()
  ): F[GHResponse[List[Comment]]]

  /**
   * Create a comment
   *
   * @param owner of the repo
   * @param repo name of the repo
   * @param number Issue number
   * @param body Comment body
   * @param headers optional user headers to include in the request
   * @return a GHResponse with the created Comment
   */
  def createComment(
      owner: String,
      repo: String,
      number: Int,
      body: String,
      headers: Map[String, String] = Map()
  ): F[GHResponse[Comment]]

  /**
   * Edit a comment
   *
   * @param owner of the repo
   * @param repo name of the repo
   * @param id Comment id
   * @param body Comment body
   * @param headers optional user headers to include in the request
   * @return a GHResponse with the edited Comment
   */
  def editComment(
      owner: String,
      repo: String,
      id: Int,
      body: String,
      headers: Map[String, String] = Map()
  ): F[GHResponse[Comment]]

  /**
   * Delete a comment
   *
   * @param owner of the repo
   * @param repo name of the repo
   * @param id Comment id
   * @param headers optional user headers to include in the request
   * @return a unit GHResponse
   */
  def deleteComment(
      owner: String,
      repo: String,
      id: Int,
      headers: Map[String, String] = Map()
  ): F[GHResponse[Unit]]

  /**
   * List the labels assigned to an Issue
   *
   * @param owner of the repo
   * @param repo name of the repo
   * @param number Issue number
   * @param headers optional user headers to include in the request
   * @return a GHResponse with the list of labels for the Issue.
   */
  def listLabels(
      owner: String,
      repo: String,
      number: Int,
      headers: Map[String, String] = Map()
  ): F[GHResponse[List[Label]]]

  /**
   * Add the specified labels to an Issue
   *
   * @param owner of the repo
   * @param repo name of the repo
   * @param number Issue number
   * @param labels the list of labels to add to the issue
   * @param headers optional user headers to include in the request
   * @return a GHResponse with the list of labels added to the Issue.
   */
  def addLabels(
      owner: String,
      repo: String,
      number: Int,
      labels: List[String],
      headers: Map[String, String] = Map()
  ): F[GHResponse[List[Label]]]

  /**
   * Remove the specified label from an Issue
   *
   * @param owner of the repo
   * @param repo name of the repo
   * @param number Issue number
   * @param label the name of the label to remove from the issue
   * @param headers optional user headers to include in the request
   * @return a GHResponse with the list of labels removed from the Issue.
   */
  def removeLabel(
      owner: String,
      repo: String,
      number: Int,
      label: String,
      headers: Map[String, String] = Map()
  ): F[GHResponse[List[Label]]]

  /**
   * List available assignees for issues
   *
   * @param owner repo owner
   * @param repo repo name
   * @param pagination Limit and Offset for pagination
   * @param headers optional user headers to include in the request
   * @return a GHResponse with the list of available assignees for issues in specified repository
   */
  def listAvailableAssignees(
      owner: String,
      repo: String,
      pagination: Option[Pagination] = None,
      headers: Map[String, String] = Map()
  ): F[GHResponse[List[User]]]
}
