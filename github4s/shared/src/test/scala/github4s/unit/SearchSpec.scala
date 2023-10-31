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

package github4s.unit

import cats.effect.IO
import github4s.Encoders._
import github4s.domain.SearchCodeParam._
import github4s.domain._
import github4s.http.HttpClient
import github4s.interpreters.SearchInterpreter
import github4s.utils.BaseSpec

class SearchSpec extends BaseSpec {

  "Search.searchCode" should "call to httpClient.get with the right parameters" in {

    val query = "foobar"
    val searchParams = List(
      In(Set(In.File, In.Path)),
      Filename(validFilePath),
      Size(Some(LesserThan), 1024)
    )

    implicit val httpClientMock: HttpClient[IO] = httpClientMockGet[SearchCodeResult](
      url = s"search/code",
      params = Map("q" -> s"$query+${searchParams.map(_.value).mkString("+")}"),
      response = IO.pure(SearchCodeResult(total_count = 0, incomplete_results = false, items = Nil))
    )

    val search = new SearchInterpreter[IO]

    search
      .searchCode("foobar", searchParams, headers = headerUserAgent)
      .shouldNotFail
  }

}
