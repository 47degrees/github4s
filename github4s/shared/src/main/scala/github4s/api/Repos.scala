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
   * @param draft `true` to create a draft (unpublished) release, `false` to create a published one.
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
}
