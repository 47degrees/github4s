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

import cats.data.NonEmptyList
import github4s.GithubResponses.GHResponse
import github4s.domain._

trait Repositories[F[_]] {

  /**
   * Get information of a particular repository
   *
   * @param owner of the repo
   * @param repo name of the repo
   * @param headers optional user headers to include in the request
   * @return GHResponse[Repository] repository details
   */
  def get(
      owner: String,
      repo: String,
      headers: Map[String, String] = Map()
  ): F[GHResponse[Repository]]

  /**
   * List the repositories for a particular organization
   *
   * @param org organization for which we wish to retrieve the repositories
   * @param `type` visibility of the retrieved repositories, can be "all", "public", "private",
   * "forks", "sources" or "member"
   * @param headers optional user headers to include in the request
   * @return GHResponse[List[Repository]] the list of repositories for this organization
   */
  def listOrgRepos(
      org: String,
      `type`: Option[String] = None,
      pagination: Option[Pagination] = None,
      headers: Map[String, String] = Map()
  ): F[GHResponse[List[Repository]]]

  /**
   * List the repositories for a particular user
   *
   * @param user user for which we wish to retrieve the repositories
   * @param `type` visibility of the retrieved repositories, can be "all", "public", "private",
   * "forks", "sources" or "member"
   * @param headers optional user headers to include in the request
   * @return GHResponse[List[Repository]] the list of repositories for this user
   */
  def listUserRepos(
      user: String,
      `type`: Option[String] = None,
      pagination: Option[Pagination] = None,
      headers: Map[String, String] = Map()
  ): F[GHResponse[List[Repository]]]

  /**
   * Get the contents of a file or directory in a repository.
   *
   * The response could be a:
   *  - file
   *  - directory
   *   The response will be an array of objects, one object for each item in the directory.
   *   When listing the contents of a directory, submodules have their "type" specified as "file".
   *  - symlink
   *   If the requested :path points to a symlink, and the symlink's target is a normal file in the repository,
   *   then the API responds with the content of the file.
   *   Otherwise, the API responds with an object describing the symlink itself.
   *  - submodule
   *   The submodule_git_url identifies the location of the submodule repository,
   *   and the sha identifies a specific commit within the submodule repository.
   *   Git uses the given URL when cloning the submodule repository,
   *   and checks out the submodule at that specific commit.
   *   If the submodule repository is not hosted on github.com, the Git URLs (git_url and _links["git"])
   *   and the github.com URLs (html_url and _links["html"]) will have null values
   *
   * @param owner of the repo
   * @param repo name of the repo
   * @param path the content path
   * @param ref the name of the commit/branch/tag. Default: the repository’s default branch (usually `master`)
   * @param headers optional user headers to include in the request
   * @return GHResponse with the content defails
   */
  def getContents(
      owner: String,
      repo: String,
      path: String,
      ref: Option[String] = None,
      headers: Map[String, String] = Map()
  ): F[GHResponse[NonEmptyList[Content]]]

  /**
   * Retrieve the list of commits for a particular repo
   *
   * @param owner of the repo
   * @param repo name of the repo
   * @param sha branch to start listing commits from
   * @param path commits containing this file path will be returned
   * @param author GitHub login or email address by which to filter by commit author.
   * @param since Only commits after this date will be returned
   * @param until Only commits before this date will be returned
   * @param pagination Limit and Offset for pagination
   * @param headers optional user headers to include in the request
   * @return GHResponse[List[Commit]\] List of commit's details
   */
  def listCommits(
      owner: String,
      repo: String,
      sha: Option[String] = None,
      path: Option[String] = None,
      author: Option[String] = None,
      since: Option[String] = None,
      until: Option[String] = None,
      pagination: Option[Pagination] = None,
      headers: Map[String, String] = Map()
  ): F[GHResponse[List[Commit]]]

  /**
   * Retrieve list of branches for a repo
   *
   * @param owner of the repo
   * @param repo name of the repo
   * @param onlyProtected Setting to true returns only protected branches
   * @param headers optional user headers to include in the request
   * @return GHResponse[List[Branch]\] List of branches
   */
  def listBranches(
      owner: String,
      repo: String,
      onlyProtected: Option[Boolean] = None,
      headers: Map[String, String] = Map()
  ): F[GHResponse[List[Branch]]]

  /**
   * Fetch contributors list for the the specified repository,
   * sorted by the number of commits per contributor in descending order.
   *
   * @param owner of the repo
   * @param repo name of the repo
   * @param anon Set to 1 or true to include anonymous contributors in results
   * @param headers optional user headers to include in the request
   * @return GHResponse[List[User]\] List of contributors associated with the specified repository.
   */
  def listContributors(
      owner: String,
      repo: String,
      anon: Option[String] = None,
      headers: Map[String, String] = Map()
  ): F[GHResponse[List[User]]]

  /**
   * Fetch list of collaborators for the specified repository.
   * For organization-owned repositories, the list of collaborators includes outside collaborators,
   * organization members that are direct collaborators, organization members with access through team memberships,
   * organization members with access through default organization permissions, and organization owners.
   *
   * @param owner of the repo
   * @param repo name of the repo
   * @param affiliation Filter collaborators returned by their affiliation. Can be one of `outside`, `direct`, `all`.
   *                    Default: `all`
   * @param headers optional user headers to include in the request
   * @return GHResponse[List[User]\] List of collaborators within the specified repository
   */
  def listCollaborators(
      owner: String,
      repo: String,
      affiliation: Option[String] = None,
      headers: Map[String, String] = Map()
  ): F[GHResponse[List[User]]]

  /**
   * Create a new release
   *
   * @param owner of the repo
   * @param repo name of the repo
   * @param tagName the name of the tag.
   * @param name the name of the release.
   * @param body text describing the contents of the tag.
   * @param targetCommitish specifies the commitish value that determines where the Git tag is created from.
   * Can be any branch or commit SHA. Unused if the Git tag already exists.
   * Default: the repository's default branch (usually `master`).
   * @param draft `true` to create a draft (unpublished) release, `false` to createStatus a published one.
   * Default: `false`
   * @param prerelease `true` to identify the release as a prerelease.
   * `false` to identify the release as a full release.
   * Default: `false`
   * @param headers optional user headers to include in the request
   * @return a GHResponse with Release
   */
  def createRelease(
      owner: String,
      repo: String,
      tagName: String,
      name: String,
      body: String,
      targetCommitish: Option[String] = None,
      draft: Option[Boolean] = None,
      prerelease: Option[Boolean] = None,
      headers: Map[String, String] = Map()
  ): F[GHResponse[Release]]

  /**
   * Get the combined status for a specific ref
   *
   * @param owner of the repo
   * @param repo name of the commit
   * @param ref commit SHA, branch name or tag name
   * @param headers optional user headers to include in the request
   * @return a GHResponse with the combined status
   */
  def getCombinedStatus(
      owner: String,
      repo: String,
      ref: String,
      headers: Map[String, String] = Map()
  ): F[GHResponse[CombinedStatus]]

  /**
   * List statuses for a commit
   *
   * @param owner of the repo
   * @param repo name of the repo
   * @param ref commit SHA, branch name or tag name
   * @param headers optional user headers to include in the request
   * @return a GHResponse with the status list
   */
  def listStatuses(
      owner: String,
      repo: String,
      ref: String,
      headers: Map[String, String] = Map()
  ): F[GHResponse[List[Status]]]

  /**
   * Create a status
   *
   * @param owner of the repo
   * @param repo name of the repo
   * @param sha commit sha to create the status on
   * @param target_url url to associate with the status, will appear in the GitHub UI
   * @param state of the status: pending, success, error, or failure
   * @param description of the status
   * @param context identifier of the status maker
   * @param headers optional user headers to include in the request
   * @return a GHResopnse with the created Status
   */
  def createStatus(
      owner: String,
      repo: String,
      sha: String,
      state: String,
      target_url: Option[String],
      description: Option[String],
      context: Option[String],
      headers: Map[String, String] = Map()
  ): F[GHResponse[Status]]
}
