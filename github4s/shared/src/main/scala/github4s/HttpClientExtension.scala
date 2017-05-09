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

package github4s

import github4s.GithubResponses.GHResponse
import io.circe.Decoder

import scala.language.higherKinds

trait HttpRequestBuilderExtension[C, M[_]] {
  def run[A](rb: HttpRequestBuilder[C, M])(implicit D: Decoder[A]): M[GHResponse[A]]

  def runEmpty(rb: HttpRequestBuilder[C, M]): M[GHResponse[Unit]]
}
