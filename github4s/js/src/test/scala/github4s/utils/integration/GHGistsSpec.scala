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

package github4s.integration

import github4s.Github._
import github4s.Github
import github4s.utils.TestUtils
import org.scalatest._
import fr.hmil.roshttp.response.SimpleHttpResponse
import github4s.free.domain.{Gist, GistFile}
import github4s.js.Implicits._
import scala.concurrent.Future

class GHGistsSpec extends AsyncFlatSpec with Matchers with TestUtils {

  override implicit val executionContext =
    scala.concurrent.ExecutionContext.Implicits.global

  "Gists >> Post" should "return the provided gist" in {
    val response = Github(accessToken).gists
      .newGist(
        validGistDescription,
        validGistPublic,
        Map(validGistFilename -> GistFile(validGistFileContent)))
      .execFuture(headerUserAgent)

    testFutureIsRight[Gist](response, { r =>
      r.result.description shouldBe validGistDescription
      r.statusCode shouldBe createdStatusCode
    })
  }
}
