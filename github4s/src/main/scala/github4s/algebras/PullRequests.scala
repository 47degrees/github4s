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

trait PullRequests[F[_]] {

  /**
   * Get a single pull request for a repository
   *
   * @param owner of the repo
   * @param repo name of the repo
   * @param number of the pull request
   * @param headers optional user headers to include in the request
   * @return a GHResponse with the pull request.
   */
  def getPullRequest(
      owner: String,
      repo: String,
      number: Int,
      headers: Map[String, String] = Map()
  ): F[GHResponse[PullRequest]]

  /**
   * List pull requests for a repository
   *
   * @param owner of the repo
   * @param repo name of the repo
   * @param filters define the filter list. Options are:
   *   - state: Either `open`, `closed`, or `all` to filter by state. Default: `open`
   *   - head: Filter pulls by head user and branch name in the format of `user:ref-name`.
   *     Example: `github:new-script-format`.
   *   - base: Filter pulls by base branch name. Example: `gh-pages`.
   *   - sort: What to sort results by. Can be either `created`, `updated`, `popularity` (comment count)
   *     or `long-running` (age, filtering by pulls updated in the last month). Default: `created`
   *   - direction: The direction of the sort. Can be either `asc` or `desc`.
   *     Default: `desc` when sort is created or sort is not specified, otherwise `asc`.
   * @param pagination Limit and Offset for pagination
   * @param headers optional user headers to include in the request
   * @return a GHResponse with the pull request list.
   */
  def listPullRequests(
      owner: String,
      repo: String,
      filters: List[PRFilter] = Nil,
      pagination: Option[Pagination] = None,
      headers: Map[String, String] = Map()
  ): F[GHResponse[List[PullRequest]]]

  /**
   * List files for a specific pull request
   *
   * @param owner of the repo
   * @param repo name of the repo
   * @param number of the pull request for which we want to list the files
   * @param pagination Limit and Offset for pagination
   * @param headers optional user headers to include in the request
   * @return a GHResponse with the list of files affected by the pull request identified by number.
   */
  def listFiles(
      owner: String,
      repo: String,
      number: Int,
      pagination: Option[Pagination] = None,
      headers: Map[String, String] = Map()
  ): F[GHResponse[List[PullRequestFile]]]

  /**
   * Create a pull request
   *
   * @param owner Owner of the repo
   * @param repo Name of the repo
   * @param newPullRequest The title and body parameters or the issue parameter
   * @param head The name of the branch where your changes are implemented. For cross-repository pull
   *             requests in the same network, namespace head with a user like this: username:branch.
   * @param base The name of the branch you want the changes pulled into. This should be an existing branch
   *             on the current repository. You cannot submit a pull request to one repository that
   * @param maintainerCanModify Indicates whether maintainers can modify the pull request, Default:Some(true).
   * @param headers Optional user headers to include in the request
   */
  def createPullRequest(
      owner: String,
      repo: String,
      newPullRequest: NewPullRequest,
      head: String,
      base: String,
      maintainerCanModify: Option[Boolean] = Some(true),
      headers: Map[String, String] = Map()
  ): F[GHResponse[PullRequest]]

  /**
   * List pull request reviews.
   *
   * @param owner Owner of the repo
   * @param repo Name of the repo
   * @param pullRequest ID number of the PR to get reviews for.
   * @param pagination Limit and Offset for pagination
   * @param headers Optional user header to include in the request
   */
  def listReviews(
      owner: String,
      repo: String,
      pullRequest: Int,
      pagination: Option[Pagination] = None,
      headers: Map[String, String] = Map()
  ): F[GHResponse[List[PullRequestReview]]]

  /**
   * Get a specific pull request review.
   *
   * @param owner Owner of the repo
   * @param repo Name of the repo
   * @param pullRequest ID number of the PR to get reviews for
   * @param review ID number of the review to retrieve.
   * @param headers Optional user header to include in the request
   */
  def getReview(
      owner: String,
      repo: String,
      pullRequest: Int,
      review: Int,
      headers: Map[String, String] = Map()
  ): F[GHResponse[PullRequestReview]]

}
