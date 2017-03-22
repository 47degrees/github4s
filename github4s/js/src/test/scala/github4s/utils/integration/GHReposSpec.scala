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
import github4s.GithubResponses._
import github4s.Github
import github4s.utils.TestUtils
import org.scalatest.{AsyncFlatSpec, FlatSpec, Matchers}
import fr.hmil.roshttp.response.SimpleHttpResponse
import github4s.free.domain.{Commit, Repository, User}
import github4s.js.Implicits._
import scala.concurrent.Future

class GHReposSpec extends AsyncFlatSpec with Matchers with TestUtils {

  override implicit val executionContext =
    scala.concurrent.ExecutionContext.Implicits.global

  "Repos >> Get" should "return the expected name when valid repo is provided" in {

    val response =
      Github(accessToken).repos
        .get(validRepoOwner, validRepoName)
        .execFuture(headerUserAgent)

    testFutureIsRight[Repository](response, { r =>
      r.result.name shouldBe validRepoName
      r.statusCode shouldBe okStatusCode
    })
  }

  it should "return error when an invalid repo name is passed" in {
    val response =
      Github(accessToken).repos
        .get(validRepoOwner, invalidRepoName)
        .execFuture(headerUserAgent)

    testFutureIsLeft(response)
  }

  "Repos >> ListCommits" should "return the expected list of commits for valid data" in {
    val response =
      Github(accessToken).repos
        .listCommits(validRepoOwner, validRepoName)
        .execFuture(headerUserAgent)

    testFutureIsRight[List[Commit]](response, { r =>
      r.result.nonEmpty shouldBe true
      r.statusCode shouldBe okStatusCode
    })
  }

  it should "return error for invalid repo name" in {
    val response =
      Github(accessToken).repos
        .listCommits(invalidRepoName, validRepoName)
        .execFuture(headerUserAgent)

    testFutureIsLeft(response)
  }

  "Repos >> ListContributors" should "return the expected list of contributors for valid data" in {
    val response =
      Github(accessToken).repos
        .listContributors(validRepoOwner, validRepoName)
        .execFuture(headerUserAgent)

    testFutureIsRight[List[User]](response, { r =>
      r.result shouldNot be(empty)
      r.statusCode shouldBe okStatusCode
    })
  }

  it should "return error for invalid repo name" in {
    val response =
      Github(accessToken).repos
        .listContributors(invalidRepoName, validRepoName)
        .execFuture(headerUserAgent)

    testFutureIsLeft(response)
  }

}
