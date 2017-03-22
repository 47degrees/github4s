/*
 * Copyright (c) 2016 47 Degrees, LLC. <http://www.47deg.com>
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
 * the Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
 * FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
 * IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
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
