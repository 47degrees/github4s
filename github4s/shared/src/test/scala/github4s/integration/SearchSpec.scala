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

package github4s.integration

import cats.effect.IO
import github4s.Github
import github4s.domain._
import github4s.utils.{BaseIntegrationSpec, Integration}

trait SearchSpec extends BaseIntegrationSpec {

  behavior of "Search >> Code"

  it should "return zero match for a non existent search query" taggedAs Integration in {
    val response = clientResource
      .use { client =>
        Github[IO](client, accessToken).search
          .searchCode(
            query = nonExistentSearchQuery,
            searchParams = List(
              SearchCodeParam.Repository(validRepoOwner, validRepoName),
              SearchCodeParam.Extension("xml")
            ),
            headers = headerUserAgent
          )
      }
      .unsafeRunSync()

    testIsRight[SearchCodeResult](
      response,
      { r =>
        r.total_count shouldBe 0
        r.items shouldBe empty
      }
    )
    response.statusCode shouldBe okStatusCode
  }

  it should "return at least one match for a valid query" taggedAs Integration in {
    val response = clientResource
      .use { client =>
        Github[IO](client, accessToken).search
          .searchCode(
            query = "github",
            searchParams = List(
              SearchCodeParam.Repository(validRepoOwner, validRepoName),
              SearchCodeParam.Extension("scala")
            ),
            headers = headerUserAgent
          )
      }
      .unsafeRunSync()

    testIsRight[SearchCodeResult](
      response,
      { r =>
        r.total_count > 0 shouldBe true
        r.items.nonEmpty shouldBe true
      }
    )
    response.statusCode shouldBe okStatusCode
  }

  it should "return at least one match for a valid query with text matches enabled" taggedAs Integration in {
    val response = clientResource
      .use { client =>
        Github[IO](client, accessToken).search
          .searchCode(
            query = "github",
            searchParams = List(
              SearchCodeParam.Repository(validRepoOwner, validRepoName),
              SearchCodeParam.Extension("md")
            ),
            textMatches = true,
            headers = headerUserAgent
          )
      }
      .unsafeRunSync()

    testIsRight[SearchCodeResult](
      response,
      { r =>
        r.total_count > 0 shouldBe true
        r.items.nonEmpty shouldBe true
        forEvery(r.items.map(_.text_matches)) { textMatches =>
          textMatches.isDefined shouldBe true
          textMatches.get should not be empty
        }
      }
    )
    response.statusCode shouldBe okStatusCode
  }
}
