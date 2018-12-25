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

package github4s.free.algebra

import cats.InjectK
import cats.free.Free
import github4s.GithubResponses._
import github4s.free.domain.{EditGistFile, Gist, GistFile}

/**
 * Gist ops ADT
 */
sealed trait GistOp[A]

final case class NewGist(
    description: String,
    public: Boolean,
    files: Map[String, GistFile],
    accessToken: Option[String] = None
) extends GistOp[GHResponse[Gist]]

final case class GetGist(
    gistId: String,
    sha: Option[String] = None,
    accessToken: Option[String] = None
) extends GistOp[GHResponse[Gist]]

final case class EditGist(
    gistId: String,
    description: String,
    files: Map[String, Option[EditGistFile]],
    accessToken: Option[String] = None
) extends GistOp[GHResponse[Gist]]

/**
 * Exposes Gists operations as a Free monadic algebra that may be combined with other Algebras via
 * Coproduct
 */
class GistOps[F[_]](implicit I: InjectK[GistOp, F]) {
  def newGist(
      description: String,
      public: Boolean,
      files: Map[String, GistFile],
      accessToken: Option[String] = None
  ): Free[F, GHResponse[Gist]] =
    Free.inject[GistOp, F](NewGist(description, public, files, accessToken))

  def getGist(
      gistId: String,
      sha: Option[String] = None,
      accessToken: Option[String] = None
  ): Free[F, GHResponse[Gist]] =
    Free.inject[GistOp, F](GetGist(gistId, sha, accessToken))

  def editGist(
      gistId: String,
      description: String,
      files: Map[String, Option[EditGistFile]],
      accessToken: Option[String] = None
  ): Free[F, GHResponse[Gist]] =
    Free.inject[GistOp, F](EditGist(gistId, description, files, accessToken))
}

/**
 * Default implicit based DI factory from which instances of the GistOps may be obtained
 */
object GistOps {

  implicit def instance[F[_]](implicit I: InjectK[GistOp, F]): GistOps[F] = new GistOps[F]

}
