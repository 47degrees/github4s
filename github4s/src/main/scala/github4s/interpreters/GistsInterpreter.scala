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

package github4s.interpreters

import github4s.algebras.Gists
import github4s.Decoders._
import github4s.domain._
import github4s.Encoders._
import github4s.GithubResponses.GHResponse
import github4s.http.HttpClient

class GistsInterpreter[F[_]](implicit client: HttpClient[F], accessToken: Option[String])
    extends Gists[F] {

  override def newGist(
      description: String,
      public: Boolean,
      files: Map[String, GistFile],
      headers: Map[String, String] = Map()
  ): F[GHResponse[Gist]] =
    client.post[NewGistRequest, Gist](
      accessToken,
      "gists",
      headers,
      data = NewGistRequest(description, public, files)
    )

  override def getGist(
      gistId: String,
      sha: Option[String],
      headers: Map[String, String] = Map()
  ): F[GHResponse[Gist]] =
    client.get[Gist](
      accessToken,
      ("gists" :: gistId :: sha.toList).mkString("/"),
      headers
    )

  override def editGist(
      gistId: String,
      description: String,
      files: Map[String, Option[EditGistFile]],
      headers: Map[String, String] = Map()
  ): F[GHResponse[Gist]] =
    client.patch[EditGistRequest, Gist](
      accessToken,
      s"gists/$gistId",
      headers,
      data = EditGistRequest(description, files)
    )
}
