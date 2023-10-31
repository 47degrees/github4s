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
import github4s.domain._

trait Search[F[_]] {

  /**
   * Search code
   *
   * @param query query string
   * @param searchParams search parameters
   * @param textMatches enable text matches
   * @param pagination Limit and Offset for pagination
   * @param headers optional user headers to include in the request
   * @return GHResponse[SearchCodeResult] the search results
   */
  def searchCode(
      query: String,
      searchParams: List[SearchCodeParam],
      textMatches: Boolean = false,
      pagination: Option[Pagination] = None,
      headers: Map[String, String] = Map()
  ): F[GHResponse[SearchCodeResult]]

}
