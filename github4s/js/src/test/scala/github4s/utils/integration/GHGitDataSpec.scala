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

package github4s.utils.integration

import cats.data.NonEmptyList
import github4s.Github
import github4s.Github._
import github4s.free.domain.{Ref, RefCommit}
import github4s.js.Implicits._
import github4s.utils.TestUtils
import org.scalatest._

class GHGitDataSpec extends AsyncFlatSpec with Matchers with TestUtils {

  override implicit val executionContext =
    scala.concurrent.ExecutionContext.Implicits.global

  "GitData >> GetReference" should "return a list of references" in {
    val response = Github(accessToken).gitData
      .getReference(validRepoOwner, validRepoName, "heads")
      .execFuture(headerUserAgent)

    testFutureIsRight[NonEmptyList[Ref]](response, { r =>
      r.result.tail.nonEmpty shouldBe true
      r.statusCode shouldBe okStatusCode
    })
  }

  it should "return at least one reference" in {
    val response = Github(accessToken).gitData
      .getReference(validRepoOwner, validRepoName, validRefSingle)
      .execFuture(headerUserAgent)

    testFutureIsRight[NonEmptyList[Ref]](response, { r =>
      r.result.head.ref.contains(validRefSingle) shouldBe true
      r.statusCode shouldBe okStatusCode
    })
  }

  "GitData >> GetCommit" should "return one commit" in {
    val response = Github(accessToken).gitData
      .getCommit(validRepoOwner, validRepoName, validCommitSha)
      .execFuture(headerUserAgent)

    testFutureIsRight[RefCommit](response, { r =>
      r.result.message shouldBe validCommitMsg
      r.statusCode shouldBe okStatusCode
    })
  }
}
