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
import github4s.Github._
import github4s.Github
import github4s.jvm.Implicits._
import github4s.utils.TestUtils
import org.scalatest.{FlatSpec, Matchers}

import scalaj.http.HttpResponse

class GHReposSpec extends FlatSpec with Matchers with TestUtils {

  "Repos >> Get" should "return the expected name when a valid repo is provided" in {

    val response =
      Github(accessToken).repos
        .get(validRepoOwner, validRepoName)
        .exec[Id, HttpResponse[String]](headerUserAgent)
    response should be('right)
    response.toOption map { r ⇒
      r.result.name shouldBe validRepoName
      r.statusCode shouldBe okStatusCode
    }
  }

  it should "return error when an invalid repo name is passed" in {
    val response =
      Github(accessToken).repos
        .get(validRepoOwner, invalidRepoName)
        .exec[Id, HttpResponse[String]](headerUserAgent)
    response should be('left)
  }

  "Repos >> GetContents" should "return the expected contents when valid path is provided" in {

    val response =
      Github(accessToken).repos
        .getContents(validRepoOwner, validRepoName, validFilePath)
        .exec[Id, HttpResponse[String]](headerUserAgent)
    response should be('right)
    response.toOption map { r ⇒
      r.result.head.path shouldBe validFilePath
      r.statusCode shouldBe okStatusCode
    }
  }

  it should "return error when an invalid path is passed" in {
    val response =
      Github(accessToken).repos
        .getContents(validRepoOwner, validRepoName, invalidFilePath)
        .exec[Id, HttpResponse[String]](headerUserAgent)
    response should be('left)
  }

  "Repos >> ListCommits" should "return the expected list of commits for valid data" in {
    val response = Github(accessToken).repos
      .listCommits(validRepoOwner, validRepoName)
      .exec[Id, HttpResponse[String]](headerUserAgent)
    response should be('right)

    response.toOption map { r ⇒
      r.result.nonEmpty shouldBe true
      r.statusCode shouldBe okStatusCode
    }
  }

  it should "return error for invalid repo name" in {
    val response = Github(accessToken).repos
      .listCommits(invalidRepoName, validRepoName)
      .exec[Id, HttpResponse[String]](headerUserAgent)
    response should be('left)
  }

  "Repos >> ListContributors" should "return the expected list of contributors for valid data" in {
    val response =
      Github(accessToken).repos
        .listContributors(validRepoOwner, validRepoName)
        .exec[Id, HttpResponse[String]](headerUserAgent)
    response should be('right)

    response.toOption map { r ⇒
      r.result shouldNot be(empty)
      r.statusCode shouldBe okStatusCode
    }

  }

  it should "return error for invalid repo name" in {
    val response =
      Github(accessToken).repos
        .listContributors(invalidRepoName, validRepoName)
        .exec[Id, HttpResponse[String]](headerUserAgent)
    response should be('left)
  }

}
