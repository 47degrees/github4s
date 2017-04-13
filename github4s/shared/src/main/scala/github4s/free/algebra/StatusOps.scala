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

package github4s.free.algebra

import cats.free.{Free, Inject}
import github4s.GithubResponses.GHResponse
import github4s.free.domain.{CombinedStatus, Status}

/** Statuses ops ADT */
sealed trait StatusOp[A]

final case class GetCombinedStatus(
  owner: String,
  repo: String,
  ref: String,
  accessToken: Option[String] = None
) extends StatusOp[GHResponse[CombinedStatus]]

final case class ListStatuses(
  owner: String,
  repo: String,
  ref: String,
  accessToken: Option[String] = None
) extends StatusOp[GHResponse[List[Status]]]

final case class CreateStatus(
  owner: String,
  repo: String,
  sha: String,
  state: String,
  target_url: Option[String],
  description: Option[String],
  context: Option[String],
  accessToken: Option[String] = None
) extends StatusOp[GHResponse[Status]]

/**
  * Exposes Status operations as a Free monadic algebra that may be combined with other Coproducts
  */
class StatusOps[F[_]](implicit I: Inject[StatusOp, F]) {
  def getCombinedStatus(
    owner: String,
    repo: String,
    ref: String,
    accessToken: Option[String] = None
  ): Free[F, GHResponse[CombinedStatus]] =
    Free.inject[StatusOp, F](GetCombinedStatus(owner, repo, ref, accessToken))

  def listStatuses(
    owner: String,
    repo: String,
    ref: String,
    accessToken: Option[String] = None
  ): Free[F, GHResponse[List[Status]]] =
    Free.inject[StatusOp, F](ListStatuses(owner, repo, ref, accessToken))

  def createStatus(
    owner: String,
    repo: String,
    sha: String,
    state: String,
    target_url: Option[String],
    description: Option[String],
    context: Option[String],
    accessToken: Option[String] = None
  ): Free[F, GHResponse[Status]] =
    Free.inject[StatusOp, F](
      CreateStatus(owner, repo, sha, state, target_url, description, context, accessToken))
}

/** Default implicit based DI factory from which instances of the StatusOps may be obtained */
object StatusOps {
  implicit def instance[F[_]](implicit I: Inject[StatusOp, F]): StatusOps[F] =
    new StatusOps[F]
}