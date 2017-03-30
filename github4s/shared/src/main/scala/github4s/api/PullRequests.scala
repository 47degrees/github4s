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
import github4s.free.domain._
import github4s.free.interpreters.Capture
import github4s.{Decoders, GithubApiUrls, HttpClient, HttpRequestBuilderExtension}
import io.circe.generic.auto._
import io.circe.syntax._

/** Factory to encapsulate calls related to PullRequests operations  */
class PullRequests[C, M[_]](
    implicit urls: GithubApiUrls,
    C: Capture[M],
    httpClientImpl: HttpRequestBuilderExtension[C, M]) {

  import Decoders._

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

}
