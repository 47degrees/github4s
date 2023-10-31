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

package github4s.interpreters

import github4s.GHResponse
import github4s.algebras.AccessToken

/**
 * A simple static version
 */
class StaticAccessToken[F[_]](accessToken: Option[String]) extends AccessToken[F] {

  override def withAccessToken[T](f: Option[String] => F[GHResponse[T]]): F[GHResponse[T]] =
    f(accessToken)
}

object StaticAccessToken {
  def noToken[F[_]] = new StaticAccessToken[F](None)
}
