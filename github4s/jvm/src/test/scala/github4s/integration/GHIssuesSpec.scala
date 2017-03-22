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

import cats.Id
import cats.implicits._
import github4s.Github
import github4s.Github._
import github4s.jvm.Implicits._
import github4s.utils.TestUtils
import org.scalatest._

import scalaj.http.HttpResponse

class GHIssuesSpec extends FlatSpec with Matchers with TestUtils {

  "Issues >> List" should "return a list of issues" in {
    val response = Github(accessToken).issues
      .listIssues(validRepoOwner, validRepoName)
      .exec[Id, HttpResponse[String]](headerUserAgent)

    response should be('right)
    response.toOption map { r ⇒
      r.result.nonEmpty shouldBe true
      r.statusCode shouldBe okStatusCode
    }
  }

  "Issues >> Search" should "return at least one issue for a valid query" in {
    val response = Github(accessToken).issues
      .searchIssues(validSearchQuery, validSearchParams)
      .exec[Id, HttpResponse[String]](headerUserAgent)

    response should be('right)
    response.toOption map { r ⇒
      r.result.total_count > 0 shouldBe true
      r.result.items.nonEmpty shouldBe true
      r.statusCode shouldBe okStatusCode
    }
  }

  it should "return an empty result for a non existent query string" in {
    val response = Github(accessToken).issues
      .searchIssues(nonExistentSearchQuery, validSearchParams)
      .exec[Id, HttpResponse[String]](headerUserAgent)

    response should be('right)
    response.toOption map { r ⇒
      r.result.total_count shouldBe 0
      r.result.items.nonEmpty shouldBe false
      r.statusCode shouldBe okStatusCode
    }
  }

  "Issues >> Edit" should "edit the specified issue" in {
    val response = Github(accessToken).issues
      .editIssue(
        validRepoOwner,
        validRepoName,
        validIssue,
        validIssueState,
        validIssueTitle,
        validIssueBody,
        None,
        validIssueLabel,
        validAssignees)
      .exec[Id, HttpResponse[String]](headerUserAgent)

    response should be('right)
    response.toOption map { r ⇒
      r.result.state shouldBe validIssueState
      r.result.title shouldBe validIssueTitle
      r.statusCode shouldBe okStatusCode
    }
  }
}
