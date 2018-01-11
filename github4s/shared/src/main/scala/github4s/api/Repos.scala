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

import cats.data.NonEmptyList
import github4s.GithubResponses.GHResponse
import github4s.free.domain._
import github4s.free.interpreters.Capture
import github4s._
import io.circe.syntax._
import io.circe.generic.auto._

/** Factory to encapsulate calls related to Repositories operations  */
class Repos[C, M[_]](
    implicit urls: GithubApiUrls,
    C: Capture[M],
    httpClientImpl: HttpRequestBuilderExtension[C, M]) {

  import Decoders._

  val httpClient = new HttpClient[C, M]

  /**
   * Get information of a particular repository
   *
   * @param accessToken to identify the authenticated user
   * @param headers optional user headers to include in the request
   * @param owner of the repo
   * @param repo name of the repo
   * @return GHResponse[Repository] repository details
   */
  def get(
      accessToken: Option[String] = None,
      headers: Map[String, String] = Map(),
      owner: String,
      repo: String): M[GHResponse[Repository]] =
    httpClient.get[Repository](accessToken, s"repos/$owner/$repo", headers)

  /**
   * List the repositories for a particular organization
   *
   * @param accessToken to identify the authenticated user
   * @param headers optional user headers to include in the request
   * @param org organization for which we wish to retrieve the repositories
   * @param `type` visibility of the retrieved repositories, can be "all", "public", "private",
   * "forks", "sources" or "member"
   * @return GHResponse[List[Repository]] the list of repositories for this organization
   */
  def listOrgRepos(
      accessToken: Option[String] = None,
      headers: Map[String, String] = Map(),
      org: String,
      `type`: Option[String] = None,
      pagination: Option[Pagination] = Some(httpClient.defaultPagination)
  ): M[GHResponse[List[Repository]]] =
    httpClient.get[List[Repository]](
      accessToken,
      s"orgs/$org/repos",
      headers,
      params = `type`.map(t => Map("type" -> t)).getOrElse(Map.empty),
      pagination = pagination
    )

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
   * @param accessToken to identify the authenticated user
   * @param headers optional user headers to include in the request
   * @param owner of the repo
   * @param repo name of the repo
   * @param path the content path
   * @param ref the name of the commit/branch/tag. Default: the repository’s default branch (usually `master`)
   * @return GHResponse with the content defails
   */
  def getContents(
      accessToken: Option[String] = None,
      headers: Map[String, String] = Map(),
      owner: String,
      repo: String,
      path: String,
      ref: Option[String] = None): M[GHResponse[NonEmptyList[Content]]] =
    httpClient.get[NonEmptyList[Content]](
      accessToken,
      s"repos/$owner/$repo/contents/$path",
      headers,
      params = ref map (r => Map("ref" -> r)) getOrElse Map.empty)

  /**
   * Retrieve the list of commits for a particular repo
   *
   * @param accessToken to identify the authenticated user
   * @param headers optional user headers to include in the request
   * @param owner of the repo
   * @param repo name of the repo
   * @param sha branch to start listing commits from
   * @param path commits containing this file path will be returned
   * @param author GitHub login or email address by which to filter by commit author.
   * @param since Only commits after this date will be returned
   * @param until Only commits before this date will be returned
   * @param pagination Limit and Offset for pagination
   * @return GHResponse[List[Commit]\] List of commit's details
   */
  def listCommits(
      accessToken: Option[String] = None,
      headers: Map[String, String] = Map(),
      owner: String,
      repo: String,
      sha: Option[String] = None,
      path: Option[String] = None,
      author: Option[String] = None,
      since: Option[String] = None,
      until: Option[String] = None,
      pagination: Option[Pagination] = Some(httpClient.defaultPagination)
  ): M[GHResponse[List[Commit]]] =
    httpClient.get[List[Commit]](
      accessToken,
      s"repos/$owner/$repo/commits",
      headers,
      Map(
        "sha"    → sha,
        "path"   → path,
        "author" → author,
        "since"  → since,
        "until"  → until
      ).collect {
        case (key, Some(value)) ⇒ key → value
      },
      pagination
    )

  /**
   * Fetch contributors list for the the specified repository,
   * sorted by the number of commits per contributor in descending order.
   *
   * @param accessToken to identify the authenticated user
   * @param headers optional user headers to include in the request
   * @param owner of the repo
   * @param repo name of the repo
   * @param anon Set to 1 or true to include anonymous contributors in results
   * @return GHResponse[List[User]\] List of contributors associated with the specified repository.
   */
  def listContributors(
      accessToken: Option[String] = None,
      headers: Map[String, String] = Map(),
      owner: String,
      repo: String,
      anon: Option[String] = None
  ): M[GHResponse[List[User]]] =
    httpClient.get[List[User]](
      accessToken,
      s"repos/$owner/$repo/contributors",
      headers,
      Map(
        "anon" → anon
      ).collect {
        case (key, Some(value)) ⇒ key → value
      })

  /**
   * Create a new release
   *
   * @param accessToken to identify the authenticated user
   * @param headers optional user headers to include in the request
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
   * @return a GHResponse with Release
   */
  def createRelease(
      accessToken: Option[String] = None,
      headers: Map[String, String] = Map(),
      owner: String,
      repo: String,
      tagName: String,
      name: String,
      body: String,
      targetCommitish: Option[String] = None,
      draft: Option[Boolean] = None,
      prerelease: Option[Boolean] = None): M[GHResponse[Release]] =
    httpClient.post[Release](
      accessToken,
      s"repos/$owner/$repo/releases",
      headers,
      dropNullPrint(
        NewReleaseRequest(tagName, name, body, targetCommitish, draft, prerelease).asJson))

  /**
   * Get the combined status for a specific ref
   *
   * @param accessToken to identify the authenticated user
   * @param headers optional user headers to include in the request
   * @param owner of the repo
   * @param repo name of the commit
   * @param ref commit SHA, branch name or tag name
   * @return a GHResponse with the combined status
   */
  def getStatus(
      accessToken: Option[String] = None,
      headers: Map[String, String] = Map(),
      owner: String,
      repo: String,
      ref: String): M[GHResponse[CombinedStatus]] =
    httpClient.get[CombinedStatus](accessToken, s"repos/$owner/$repo/commits/$ref/status", headers)

  /**
   * List statuses for a commit
   *
   * @param accessToken to identify the authenticated user
   * @param headers optional user headers to include in the request
   * @param owner of the repo
   * @param repo name of the repo
   * @param ref commit SHA, branch name or tag name
   * @return a GHResponse with the status list
   */
  def listStatuses(
      accessToken: Option[String] = None,
      headers: Map[String, String] = Map(),
      owner: String,
      repo: String,
      ref: String): M[GHResponse[List[Status]]] =
    httpClient.get[List[Status]](accessToken, s"repos/$owner/$repo/commits/$ref/statuses", headers)

  /**
   * Create a status
   *
   * @param accessToken to identify the authenticated user
   * @param headers optional user headers to include in the request
   * @param owner of the repo
   * @param repo name of the repo
   * @param sha commit sha to create the status on
   * @param target_url url to associate with the status, will appear in the GitHub UI
   * @param state of the status: pending, success, error, or failure
   * @param description of the status
   * @param context identifier of the status maker
   * @return a GHResopnse with the created Status
   */
  def createStatus(
      accessToken: Option[String] = None,
      headers: Map[String, String] = Map(),
      owner: String,
      repo: String,
      sha: String,
      state: String,
      target_url: Option[String],
      description: Option[String],
      context: Option[String]): M[GHResponse[Status]] =
    httpClient.post[Status](
      accessToken,
      s"repos/$owner/$repo/statuses/$sha",
      headers,
      dropNullPrint(NewStatusRequest(state, target_url, description, context).asJson))
}
