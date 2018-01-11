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

import github4s.free.domain._
import github4s._
import io.circe.generic.auto._
import io.circe.syntax._
import github4s.GithubResponses.GHResponse
import github4s.free.interpreters.Capture

import scala.language.higherKinds

/** Factory to encapsulate calls related to Repositories operations  */
class Gists[C, M[_]](
    implicit urls: GithubApiUrls,
    C: Capture[M],
    httpClientImpl: HttpRequestBuilderExtension[C, M]) {
  import Decoders._

  val httpClient = new HttpClient[C, M]

  /**
   * Create a new gist
   *
   * @param description of the gist
   * @param public boolean value that describes if the Gist should be public or not
   * @param files map describing the filenames of the Gist and its contents
   * @param headers optional user headers to include in the request
   * @param accessToken to identify the authenticated user
   */
  def newGist(
      description: String,
      public: Boolean,
      files: Map[String, GistFile],
      headers: Map[String, String] = Map(),
      accessToken: Option[String] = None): M[GHResponse[Gist]] =
    httpClient.post[Gist](
      accessToken,
      "gists",
      headers,
      data = NewGistRequest(description, public, files).asJson.noSpaces
    )

}
