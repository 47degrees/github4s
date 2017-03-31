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

class GHGitDataSpec extends FlatSpec with Matchers with TestUtils {

  "GitData >> GetReference" should "return a list of references" in {
    val response = Github(accessToken).gitData
      .getReference(validRepoOwner, validRepoName, "heads")
      .exec[Id, HttpResponse[String]](headerUserAgent)

    response should be('right)
    response.toOption map { r ⇒
      r.result.tail.nonEmpty shouldBe true
      r.statusCode shouldBe okStatusCode
    }
  }

  it should "return at least one reference" in {
    val response = Github(accessToken).gitData
      .getReference(validRepoOwner, validRepoName, validRefSingle)
      .exec[Id, HttpResponse[String]](headerUserAgent)

    response should be('right)
    response.toOption map { r ⇒
      r.result.head.ref.contains(validRefSingle) shouldBe true
      r.statusCode shouldBe okStatusCode
    }
  }

  "GitData >> GetCommit" should "return one commit" in {
    val response = Github(accessToken).gitData
      .getCommit(validRepoOwner, validRepoName, validCommitSha)
      .exec[Id, HttpResponse[String]](headerUserAgent)

    response should be('right)
    response.toOption map { r ⇒
      r.result.message shouldBe validCommitMsg
      r.statusCode shouldBe okStatusCode
    }
  }
}
