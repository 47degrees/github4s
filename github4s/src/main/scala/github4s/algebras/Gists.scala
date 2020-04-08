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

import github4s.GHResponse
import github4s.domain._

trait Gists[F[_]] {

  /**
   * Create a new gist
   *
   * @param description of the gist
   * @param public boolean value that describes if the Gist should be public or not
   * @param files map describing the filenames of the Gist and its contents
   * @param headers optional user headers to include in the request
   */
  def newGist(
      description: String,
      public: Boolean,
      files: Map[String, GistFile],
      headers: Map[String, String] = Map()
  ): F[GHResponse[Gist]]

  /**
   * Get a single gist or a specific revision of a gist
   *
   * @param gistId of the gist
   * @param sha optional sha of a revision
   * @param headers optional user headers to include in the request
   */
  def getGist(
      gistId: String,
      sha: Option[String] = None,
      headers: Map[String, String] = Map()
  ): F[GHResponse[Gist]]

  /**
   * Edit a gist
   *
   * @param gistId of the gist
   * @param files map describing the filenames of the Gist and its contents and/or new filenames
   * @param headers optional user headers to include in the request
   */
  def editGist(
      gistId: String,
      description: String,
      files: Map[String, Option[EditGistFile]],
      headers: Map[String, String] = Map()
  ): F[GHResponse[Gist]]
}
