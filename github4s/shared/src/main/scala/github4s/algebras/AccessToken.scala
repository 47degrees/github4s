/*
 * Copyright 2016-2023 47 Degrees Open Source <https://www.47deg.com>
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

/**
 * Source of static or expiring github tokens
 *
 * For github app authentication you'd want to create a token source which calls github's
 * installation authentication api with a jwt token, generated from a private key These tokens have
 * a 1h lifetime, so it's a good idea to handle expired tokens here as well
 *
 * @see
 *   https://docs.github.com/en/free-pro-team@latest/developers/apps/authenticating-with-github-apps
 */
trait AccessToken[F[_]] {

  def withAccessToken[T](f: Option[String] => F[GHResponse[T]]): F[GHResponse[T]]
}
