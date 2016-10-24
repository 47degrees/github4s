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
import github4s.free.domain.{Pagination, Commit, Repository, User}
import github4s.{GithubApiUrls, Decoders, HttpClient}
import io.circe.generic.auto._

/** Factory to encapsulate calls related to Repositories operations  */
class Repos(implicit urls: GithubApiUrls) {

  import Decoders._

  val httpClient = new HttpClient

  /**
    * Get information of a particular repository
    *
    * @param accessToken to identify the authenticated user
    * @param owner of the repo
    * @param repo name of the repo
    * @return GHResponse[Repository] repository details
    */
  def get(accessToken: Option[String] = None,
          owner: String,
          repo: String): GHResponse[Repository] =
    httpClient.get[Repository](accessToken, s"repos/$owner/$repo")

  /**
    * Retrieve the list of commits for a particular repo
    *
    * @param accessToken to identify the authenticated user
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
      owner: String,
      repo: String,
      sha: Option[String] = None,
      path: Option[String] = None,
      author: Option[String] = None,
      since: Option[String] = None,
      until: Option[String] = None,
      pagination: Option[Pagination] = Some(httpClient.defaultPagination)
  ): GHResponse[List[Commit]] =
    httpClient.get[List[Commit]](accessToken,
                                 s"repos/$owner/$repo/commits",
                                 Map(
                                   "sha"    → sha,
                                   "path"   → path,
                                   "author" → author,
                                   "since"  → since,
                                   "until"  → until
                                 ).collect {
                                   case (key, Some(value)) ⇒ key → value
                                 },
                                 pagination)

  /**
    * Fetch contributors list for the the specified repository,
    * sorted by the number of commits per contributor in descending order.
    *
    * @param accessToken to identify the authenticated user
    * @param owner of the repo
    * @param repo name of the repo
    * @param anon Set to 1 or true to include anonymous contributors in results
    * @return GHResponse[List[User]\] List of contributors associated with the specified repository.
    */
  def listContributors(
      accessToken: Option[String] = None,
      owner: String,
      repo: String,
      anon: Option[String] = None
  ): GHResponse[List[User]] =
    httpClient.get[List[User]](accessToken,
                               s"repos/$owner/$repo/contributors",
                               Map(
                                 "anon" → anon
                               ).collect {
                                 case (key, Some(value)) ⇒ key → value
                               })

}
