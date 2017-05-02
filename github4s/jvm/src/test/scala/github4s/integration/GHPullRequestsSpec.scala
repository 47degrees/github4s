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
import github4s.free.domain.{PRFilterAll, PRFilterBase, PRFilterOrderAsc, PRFilterSortCreated}
import github4s.jvm.Implicits._
import github4s.utils.TestUtils
import org.scalatest.{FlatSpec, Matchers}

import scalaj.http.HttpResponse

class GHPullRequestsSpec extends FlatSpec with Matchers with TestUtils {

  "PullRequests >> List" should "return a right response when a valid repo is provided" in {
    val response =
      Github(accessToken).pullRequests
        .list(validRepoOwner, validRepoName)
        .exec[Id, HttpResponse[String]](headerUserAgent)

    response should be('right)
    response.toOption map { r ⇒
      r.statusCode shouldBe okStatusCode
    }
  }

  it should "return a non empty list when a valid repo and some filters are provided" in {
    val response =
      Github(accessToken).pullRequests
        .list(
          validRepoOwner,
          validRepoName,
          List(PRFilterAll, PRFilterSortCreated, PRFilterOrderAsc, PRFilterBase("master")))
        .exec[Id, HttpResponse[String]](headerUserAgent)

    response should be('right)
    response.toOption map { r ⇒
      r.result.nonEmpty shouldBe true
      r.statusCode shouldBe okStatusCode
    }
  }

  it should "return error when an invalid repo name is passed" in {
    val response =
      Github(accessToken).pullRequests
        .list(validRepoOwner, invalidRepoName)
        .exec[Id, HttpResponse[String]](headerUserAgent)

    response should be('left)
  }

  "PullRequests >> ListFiles" should "return a right response when a valid repo is provided" in {
    val response =
      Github(accessToken).pullRequests
        .listFiles(validRepoOwner, validRepoName, validPullRequestNumber)
        .exec[Id, HttpResponse[String]](headerUserAgent)

    response should be('right)
    response.toOption map { r ⇒
      r.result.nonEmpty shouldBe true
      r.statusCode shouldBe okStatusCode
    }
  }

  it should "return error when an invalid repo name is passed" in {
    val response =
      Github(accessToken).pullRequests
        .listFiles(validRepoOwner, invalidRepoName, validPullRequestNumber)
        .exec[Id, HttpResponse[String]](headerUserAgent)

    response should be('left)
  }

}
