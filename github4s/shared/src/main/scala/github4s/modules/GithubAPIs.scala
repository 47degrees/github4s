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

package github4s.modules

import cats.effect.kernel.Concurrent
import github4s.GithubConfig
import github4s.algebras._
import github4s.interpreters._
import org.http4s.client.Client

@deprecated("Use github4s.modules.GithubAPIsV3 instead", "0.33.0")
class GithubAPIv3[F[_]: Concurrent](
    client: Client[F],
    config: GithubConfig,
    accessToken: AccessToken[F]
) extends GithubAPIsV3[F](client, config, AccessHeader.from(accessToken))

object GithubAPIv3 {
  @deprecated("Use github4s.modules.GithubAPIsV3.noAuth instead", "0.33.0")
  def noAuth[F[_]: Concurrent](client: Client[F], config: GithubConfig): GithubAPIv3[F] =
    new GithubAPIv3[F](client, config, StaticAccessToken.noToken)
}
