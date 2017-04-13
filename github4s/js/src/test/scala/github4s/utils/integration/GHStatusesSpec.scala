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

package github4s.utils.integration

import github4s.Decoders._
import github4s.Github
import github4s.Github._
import github4s.free.domain.{CombinedStatus, Status}
import github4s.js.Implicits._
import github4s.utils.TestUtils
import org.scalatest.{AsyncFlatSpec, Matchers}

class GHStatusesSpec extends AsyncFlatSpec with Matchers with TestUtils {

  override implicit val executionContext = scala.concurrent.ExecutionContext.Implicits.global

  "Statuses >> Get" should "return a combined status" in {
    val response = Github(accessToken).statuses
      .getCombinedStatus(validRepoOwner, validRepoName, validRefSingle)
      .execFuture(headerUserAgent)

    testFutureIsRight[CombinedStatus](response, { r =>
      r.result.repository.full_name shouldBe s"$validRepoOwner/$validRepoName"
      r.statusCode shouldBe okStatusCode
    })
  }

  it should "return an error when an invalid ref is passed" in {
    val response = Github(accessToken).statuses
      .getCombinedStatus(validRepoOwner, validRepoName, invalidRef)
      .execFuture(headerUserAgent)
    response should be('left)
  }

  "Statuses >> List" should "return a non empty list when a valid ref is provided" in {
    val response = Github(accessToken).statuses
      .listStatuses(validRepoOwner, validRepoName, validRefSingle)
      .execFuture(headerUserAgent)

    testFutureIsRight[List[Status]](response, { r =>
      r.result.nonEmpty shouldBe true
      r.statusCode shouldBe okStatusCode
    })
  }

  it should "return an empty list when an invalid ref is provided" in {
    val response = Github(accessToken).statuses
      .listStatuses(validRepoOwner, validRepoName, invalidRef)
      .execFuture(headerUserAgent)

    testFutureIsRight[List[Status]](response, { r =>
      r.result.isEmpty shouldBe true
      r.statusCode shouldBe okStatusCode
    })
  }

  "Status >> Create" should "create a status" in {
    val response = Github(accessToken).statuses
      .createStatus(
        validRepoOwner,
        validRepoName,
        validCommitSha,
        validStatusState,
        None,
        None,
        None)
      .execFuture(headerUserAgent)

    testFutureIsRight[Status](response, { r =>
      r.result.state shouldBe validStatusState
      r.statusCode shouldBe createdStatusCode
    })
  }

  it should "return an error when an invalid sha is passed" in {
    val response = Github(accessToken).statuses
      .createStatus(
        validRepoOwner,
        validRepoName,
        invalidCommitSha,
        validStatusState,
        None,
        None,
        None)
      .execFuture(headerUserAgent)

    testFutureIsLeft(response)
  }
}
