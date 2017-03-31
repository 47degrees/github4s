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

import github4s.Github
import github4s.Github._
import github4s.free.domain.{Issue, SearchIssuesResult}
import github4s.js.Implicits._
import github4s.utils.TestUtils
import org.scalatest._

class GHIssuesSpec extends AsyncFlatSpec with Matchers with TestUtils {

  override implicit val executionContext =
    scala.concurrent.ExecutionContext.Implicits.global

  "Issues >> List" should "return a list of issues" in {
    val response = Github(accessToken).issues
      .listIssues(validRepoOwner, validRepoName)
      .execFuture(headerUserAgent)

    testFutureIsRight[List[Issue]](response, { r =>
      r.result.nonEmpty shouldBe true
      r.statusCode shouldBe okStatusCode
    })
  }

  "Issues >> Search" should "return at least one issue for a valid query" in {
    val response = Github(accessToken).issues
      .searchIssues(validSearchQuery, validSearchParams)
      .execFuture(headerUserAgent)

    testFutureIsRight[SearchIssuesResult](response, { r =>
      r.result.total_count > 0 shouldBe true
      r.result.items.nonEmpty shouldBe true
      r.statusCode shouldBe okStatusCode
    })
  }

  it should "return an empty result for a non existent query string" in {
    val response = Github(accessToken).issues
      .searchIssues(nonExistentSearchQuery, validSearchParams)
      .execFuture(headerUserAgent)

    testFutureIsRight[SearchIssuesResult](response, { r =>
      r.result.total_count shouldBe 0
      r.result.items.nonEmpty shouldBe false
      r.statusCode shouldBe okStatusCode
    })
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
      .execFuture(headerUserAgent)

    testFutureIsRight[Issue](response, { r =>
      r.result.state shouldBe validIssueState
      r.result.title shouldBe validIssueTitle
      r.statusCode shouldBe okStatusCode
    })
  }
}
