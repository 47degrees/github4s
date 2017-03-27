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

package github4s.unit

import github4s.free.domain.{Commit, Repository}
import github4s.utils.FakeResponses
import github4s.Decoders._
import org.scalatest._
import io.circe.parser._
import cats.implicits._

class DecodersSpec extends FlatSpec with Matchers with FakeResponses {

  "Commit decoder" should "return a list of commits when the JSON is valid" in {
    decode[List[Commit]](listCommitsValidResponse) should be('right)
  }

  it should "return an empty list for an empty JSON" in {
    decode[List[Commit]](emptyListResponse).toOption map (_.isEmpty shouldBe true)
  }

  "Repository decoder" should "return a valid repo for a valid JSON" in {
    decode[Repository](getRepoResponse) should be('right)
  }

  it should "return error for an empty JSON" in {
    decode[Repository](emptyListResponse) should be('left)
  }

}
