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

import github4s.Decoders._
import github4s.GHResponse
import github4s.algebras.Search
import github4s.domain._
import github4s.http.HttpClient

class SearchInterpreter[F[_]](implicit client: HttpClient[F]) extends Search[F] {

  private val textMatchesHeader = "Accept" -> "application/vnd.github.text-match+json"

  override def searchCode(
      query: String,
      searchParams: List[SearchCodeParam] = Nil,
      textMatches: Boolean = false,
      pagination: Option[Pagination] = None,
      headers: Map[String, String] = Map.empty
  ): F[GHResponse[SearchCodeResult]] =
    client.get[SearchCodeResult](
      method = s"search/code",
      if (textMatches) headers + textMatchesHeader else headers,
      params = Map("q" -> s"$query+${searchParams.map(_.value).mkString("+")}"),
      pagination
    )
}
