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
import github4s.{Decoders, Encoders, GithubApiUrls, HttpClient, HttpRequestBuilderExtension}
import github4s.util.URLEncoder
import io.circe.generic.auto._
import io.circe.syntax._

import scala.language.higherKinds

/** Factory to encapsulate calls related to PullRequests operations  */
class PullRequests[C, M[_]](
    implicit urls: GithubApiUrls,
    C: Capture[M],
    httpClientImpl: HttpRequestBuilderExtension[C, M]) {

  import Decoders._
  import Encoders._

  val httpClient = new HttpClient[C, M]

  /**
   * List pull requests for a repository
   *
   * @param accessToken to identify the authenticated user
   * @param headers optional user headers to include in the request
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
   * @return a GHResponse with the pull request list.
   */
  def list(
      accessToken: Option[String] = None,
      headers: Map[String, String] = Map(),
      owner: String,
      repo: String,
      filters: List[PRFilter] = Nil): M[GHResponse[List[PullRequest]]] =
    httpClient.get[List[PullRequest]](
      accessToken,
      s"repos/$owner/$repo/pulls",
      headers,
      filters.map(_.tupled).toMap)

  /**
   * List files for a specific pull request
   *
   * @param accessToken to identify the authenticated user
   * @param headers optional user headers to include in the request
   * @param owner of the repo
   * @param repo name of the repo
   * @param number of the pull request for which we want to list the files
   * @return a GHResponse with the list of files affected by the pull request identified by number.
   */
  def listFiles(
      accessToken: Option[String] = None,
      headers: Map[String, String] = Map(),
      owner: String,
      repo: String,
      number: Int): M[GHResponse[List[PullRequestFile]]] =
    httpClient
      .get[List[PullRequestFile]](accessToken, s"repos/$owner/$repo/pulls/$number/files", headers)

  /**
   * Create a pull request
   *
   * @param accessToken Token to identify the authenticated user
   * @param headers Optional user headers to include in the request
   * @param owner Owner of the repo
   * @param repo Name of the repo
   * @param newPullRequest The title and body parameters or the issue parameter
   * @param head The name of the branch where your changes are implemented. For cross-repository pull
   *             requests in the same network, namespace head with a user like this: username:branch.
   * @param base The name of the branch you want the changes pulled into. This should be an existing branch
   *             on the current repository. You cannot submit a pull request to one repository that
   * @param maintainerCanModify Indicates whether maintainers can modify the pull request, Default:Some(true).
   */
  def create(
      accessToken: Option[String] = None,
      headers: Map[String, String] = Map(),
      owner: String,
      repo: String,
      newPullRequest: NewPullRequest,
      head: String,
      base: String,
      maintainerCanModify: Option[Boolean] = Some(true)): M[GHResponse[PullRequest]] = {
    val data: CreatePullRequest = newPullRequest match {
      case NewPullRequestData(title, body) ⇒
        CreatePullRequestData(title, head, base, body, maintainerCanModify)
      case NewPullRequestIssue(issue) ⇒
        CreatePullRequestIssue(issue, head, base, maintainerCanModify)
    }
    httpClient
      .post[PullRequest](accessToken, s"repos/$owner/$repo/pulls", headers, data.asJson.noSpaces)
  }
}
