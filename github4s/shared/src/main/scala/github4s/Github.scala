/*
 * Copyright 2016-2024 47 Degrees Open Source <https://www.47deg.com>
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

package github4s

import cats.effect.Concurrent
import github4s.algebras._
import github4s.interpreters.StaticAccessToken
import org.http4s.client.Client

@deprecated("Use github4s.GithubClient instead", "0.33.0")
class Github[F[_]: Concurrent](
    client: Client[F],
    accessToken: AccessToken[F]
)(implicit config: GithubConfig)
    extends GithubClient[F](client, AccessHeader.from(accessToken))

object Github {

  @deprecated("Use github4s.GithubClient instead", "0.33.0")
  def apply[F[_]: Concurrent](
      client: Client[F],
      accessToken: Option[String] = None
  )(implicit config: GithubConfig): Github[F] =
    new Github[F](client, new StaticAccessToken(accessToken))
}
