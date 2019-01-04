/*
 * Copyright 2016-2019 47 Degrees, LLC. <http://www.47deg.com>
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
import github4s.free.domain.{Issue, Label, SearchIssuesResult, User}
import github4s.implicits._
import github4s.utils.BaseIntegrationSpec

trait GHIssuesSpec[T] extends BaseIntegrationSpec[T] {

  "Issues >> List" should "return a list of issues" in {
    val response = Github(accessToken).issues
      .listIssues(validRepoOwner, validRepoName)
      .execFuture[T](headerUserAgent)

    testFutureIsRight[List[Issue]](response, { r =>
      r.result.nonEmpty shouldBe true
      r.statusCode shouldBe okStatusCode
    })
  }

  "Issues >> Get" should "return an issue which is a PR" ignore {
    val response = Github(accessToken).issues
      .getIssue(validRepoOwner, validRepoName, validPullRequestNumber)
      .execFuture[T](headerUserAgent)

    testFutureIsRight[Issue](response, { r =>
      r.result.body.isEmpty shouldBe true
      r.statusCode shouldBe okStatusCode
    })
  }

  "Issues >> Search" should "return at least one issue for a valid query" in {
    val response = Github(accessToken).issues
      .searchIssues(validSearchQuery, validSearchParams)
      .execFuture[T](headerUserAgent)

    testFutureIsRight[SearchIssuesResult](response, { r =>
      r.result.total_count > 0 shouldBe true
      r.result.items.nonEmpty shouldBe true
      r.statusCode shouldBe okStatusCode
    })
  }

  it should "return an empty result for a non existent query string" in {
    val response = Github(accessToken).issues
      .searchIssues(nonExistentSearchQuery, validSearchParams)
      .execFuture[T](headerUserAgent)

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
        validIssueNumber,
        validIssueState,
        validIssueTitle,
        validIssueBody,
        None,
        validIssueLabel,
        validAssignees)
      .execFuture[T](headerUserAgent)

    testFutureIsRight[Issue](response, { r =>
      r.result.state shouldBe validIssueState
      r.result.title shouldBe validIssueTitle
      r.statusCode shouldBe okStatusCode
    })
  }

  "Issues >> ListLabels" should "return a list of labels" in {
    val response = Github(accessToken).issues
      .listLabels(validRepoOwner, validRepoName, validIssueNumber)
      .execFuture[T](headerUserAgent)

    testFutureIsRight[List[Label]](response, { r =>
      r.result.nonEmpty shouldBe true
      r.statusCode shouldBe okStatusCode
    })
  }

  "Issues >> RemoveLabel" should "return a list of removed labels" in {
    val response = Github(accessToken).issues
      .removeLabel(validRepoOwner, validRepoName, validIssueNumber, validIssueLabel.head)
      .execFuture[T](headerUserAgent)

    testFutureIsRight[List[Label]](response, { r =>
      r.result.nonEmpty shouldBe true
      r.statusCode shouldBe okStatusCode
    })
  }

  "Issues >> AddLabels" should "return a list of labels" in {
    val response = Github(accessToken).issues
      .addLabels(validRepoOwner, validRepoName, validIssueNumber, validIssueLabel)
      .execFuture[T](headerUserAgent)

    testFutureIsRight[List[Label]](response, { r =>
      r.result.nonEmpty shouldBe true
      r.statusCode shouldBe okStatusCode
    })
  }

  "GHIssues >> ListAvailableAssignees" should "return a list of users" in {
    val response = Github(accessToken).issues
      .listAvailableAssignees(validRepoOwner, validRepoName)
      .execFuture[T](headerUserAgent)

    testFutureIsRight[List[User]](response, { r =>
      r.result.nonEmpty shouldBe true
      r.statusCode shouldBe okStatusCode
    })
  }

  it should "return error for an invalid repo owner" in {
    val response = Github(accessToken).issues
      .listAvailableAssignees(invalidRepoOwner, validRepoName)
      .execFuture[T](headerUserAgent)

    testFutureIsLeft(response)
  }

  it should "return error for an invalid repo name" in {
    val response = Github(accessToken).issues
      .listAvailableAssignees(validRepoOwner, invalidRepoName)
      .execFuture[T](headerUserAgent)

    testFutureIsLeft(response)
  }

}
