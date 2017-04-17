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

import cats.Id
import cats.implicits._
import github4s.Github
import github4s.Github._
import github4s.jvm.Implicits._
import github4s.utils.TestUtils
import org.scalatest._

import scalaj.http.HttpResponse

class GHStatusesSpec extends FlatSpec with Matchers with TestUtils {
  "Statuses >> Get" should "return a combined status" in {
    val response = Github(accessToken).statuses
      .getCombinedStatus(validRepoOwner, validRepoName, validRefSingle)
      .exec[Id, HttpResponse[String]](headerUserAgent)

    response should be('right)
    response.toOption map { r =>
      r.result.repository.full_name shouldBe s"$validRepoOwner/$validRepoName"
      r.statusCode shouldBe okStatusCode
    }
  }

  it should "return an error when an invalid ref is passed" in {
    val response = Github(accessToken).statuses
      .getCombinedStatus(validRepoOwner, validRepoName, invalidRef)
      .exec[Id, HttpResponse[String]](headerUserAgent)
    response should be('left)
  }

  "Statuses >> List" should "return a list of statuses" in {
    val response = Github(accessToken).statuses
      .listStatuses(validRepoOwner, validRepoName, validCommitSha)
      .exec[Id, HttpResponse[String]](headerUserAgent)

    response should be('right)
    response.toOption map { r =>
      r.result.nonEmpty shouldBe true
      r.statusCode shouldBe okStatusCode
    }
  }

  it should "return an empty list when an invalid ref is passed" in {
    val response = Github(accessToken).statuses
      .listStatuses(validRepoOwner, validRepoName, invalidRef)
      .exec[Id, HttpResponse[String]](headerUserAgent)
    response should be('right)
    response.toOption map { r =>
      r.result.isEmpty shouldBe true
      r.statusCode shouldBe okStatusCode
    }
  }

  "Statuses >> Create" should "create a status" in {
    val response = Github(accessToken).statuses
      .createStatus(
        validRepoOwner,
        validRepoName,
        validCommitSha,
        validStatusState,
        None,
        None,
        None)
      .exec[Id, HttpResponse[String]](headerUserAgent)

    response should be('right)
    response.toOption map { r ⇒
      r.result.state shouldBe validStatusState
      r.statusCode shouldBe createdStatusCode
    }
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
      .exec[Id, HttpResponse[String]](headerUserAgent)
    response should be('left)
  }
}
