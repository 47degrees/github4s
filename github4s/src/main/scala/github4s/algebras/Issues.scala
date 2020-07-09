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

import java.time.ZonedDateTime

import github4s.GHResponse
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
   * @param pagination Limit and Offset for pagination
   * @param headers optional user headers to include in the request
   * @return a GHResponse with the issue list.
   */
  def listIssues(
      owner: String,
      repo: String,
      pagination: Option[Pagination] = None,
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
   * @param pagination Limit and Offset for pagination
   * @param headers optional user headers to include in the request
   * @return a GHResponse with the comment list of the Issue.
   */
  def listComments(
      owner: String,
      repo: String,
      number: Int,
      pagination: Option[Pagination] = None,
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
      id: Long,
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
      id: Long,
      headers: Map[String, String] = Map()
  ): F[GHResponse[Unit]]

  /**
   * List the labels assigned to a Repository
   *
   * @param owner of the repo
   * @param repo name of the repo
   * @param pagination Limit and Offset for pagination, optional.
   * @param headers optional user headers to include in the request
   * @return a GHResponse with the list of labels for the Repository.
   */
  def listLabelsRepository(
      owner: String,
      repo: String,
      pagination: Option[Pagination] = None,
      headers: Map[String, String] = Map()
  ): F[GHResponse[List[Label]]]

  /**
   * List the labels assigned to an Issue
   *
   * @param owner of the repo
   * @param repo name of the repo
   * @param number Issue number
   * @param pagination Limit and Offset for pagination, optional.
   * @param headers optional user headers to include in the request
   * @return a GHResponse with the list of labels for the Issue.
   */
  def listLabels(
      owner: String,
      repo: String,
      number: Int,
      pagination: Option[Pagination] = None,
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

  /**
   * List milestone in specified repository
   *
   * @param owner repo owner
   * @param repo repo name
   * @param state filter milestones returned by their state. Can be either `open`, `closed`, `all`. Default: `open`
   * @param sort What to sort results by. Either due_on or completeness. Default: due_on
   * @param direction The direction of the sort. Either asc or desc. Default: asc
   * @param pagination Limit and Offset for pagination
   * @param headers optional user headers to include in the request
   * @return a GHResponse with the list of milestones in specified repository
   */
  def listMilestones(
      owner: String,
      repo: String,
      state: Option[String],
      sort: Option[String],
      direction: Option[String],
      pagination: Option[Pagination] = None,
      headers: Map[String, String] = Map()
  ): F[GHResponse[List[Milestone]]]

  /**
   * Create a milestone
   *
   * @param owner repo owner
   * @param repo repo name
   * @param title The title of the milestone.
   * @param state The state of the milestone. Either open or closed. Default: open
   * @param description A description of the milestone.
   * @param due_on 	The milestone due date. This is a timestamp in ISO 8601 format: YYYY-MM-DDTHH:MM:SSZ.
   * @param headers optional user headers to include in the request
   * @return a GHResponse with the created Milestone
   */
  def createMilestone(
      owner: String,
      repo: String,
      title: String,
      state: Option[String],
      description: Option[String],
      due_on: Option[ZonedDateTime],
      headers: Map[String, String] = Map()
  ): F[GHResponse[Milestone]]

  /**
   * Get a single milestone
   *
   * @param owner repo owner
   * @param repo repo name
   * @param number Milestone number
   * @param headers optional user headers to include in the request
   * @return a GHResponse with a Milestone
   */
  def getMilestone(
      owner: String,
      repo: String,
      number: Int,
      headers: Map[String, String] = Map()
  ): F[GHResponse[Milestone]]

  /**
   * Update a milestone
   *
   * @param owner repo owner
   * @param repo repo name
   * @param milestone_number number of milestone
   * @param title The title of the milestone.
   * @param state The state of the milestone. Either open or closed. Default: open
   * @param description A description of the milestone.
   * @param due_on 	The milestone due date. This is a timestamp in ISO 8601 format: YYYY-MM-DDTHH:MM:SSZ.
   * @param headers optional user headers to include in the request
   * @return a GHResponse with the updated Milestone
   */
  def updateMilestone(
      owner: String,
      repo: String,
      milestone_number: Int,
      title: String,
      state: Option[String],
      description: Option[String],
      due_on: Option[ZonedDateTime],
      headers: Map[String, String] = Map()
  ): F[GHResponse[Milestone]]

  /**
   * Delete a milestone
   *
   * @param owner repo owner
   * @param repo repo name
   * @param milestone_number number of milestone
   * @param headers optional user headers to include in the request
   * @return a Unit GHResponse
   */
  def deleteMilestone(
      owner: String,
      repo: String,
      milestone_number: Int,
      headers: Map[String, String] = Map()
  ): F[GHResponse[Unit]]
}
