/*
 * Copyright 2016-2020 47 Degrees <https://www.47deg.com>
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

trait GithubAPIs[F[_]] {

  /**
   * User-related operations.
   */
  def users: Users[F]

  /**
   * Repository-related operations.
   */
  def repos: Repositories[F]

  /**
   * Authentication-related operations.
   */
  def auth: Auth[F]

  /**
   * Gist-related operations.
   */
  def gists: Gists[F]

  /**
   * Issue-related operations. Covers operations commons to both issues and pull requests.
   */
  def issues: Issues[F]

  /**
   * Activities-related operations.
   */
  def activities: Activities[F]

  /**
   * Git operations.
   */
  def gitData: GitData[F]

  /**
   * Pull Request-related operations. Pull Requests are issues, so operations not specific
   * to pull requests, such as label queries, will be found under [[#issues]]
   * @return
   */
  def pullRequests: PullRequests[F]

  /**
   * Organization-related operations.
   */
  def organizations: Organizations[F]

  /**
   * Team-related operations.
   */
  def teams: Teams[F]

  /**
   * Project-related operations.
   */
  def projects: Projects[F]
}
