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
import org.scalatest._

import scalaj.http.HttpResponse

class GHUsersSpec extends FlatSpec with Matchers with TestUtils {

  "Users >> Get" should "return the expected login for a valid username" in {
    val response =
      Github(accessToken).users
        .get(validUsername)
        .exec[Id, HttpResponse[String]](headerUserAgent)

    response should be('right)
    response.toOption map { r ⇒
      r.result.login shouldBe validUsername
      r.statusCode shouldBe okStatusCode
    }
  }

  it should "return error on Left for invalid username" in {
    val response = Github(accessToken).users
      .get(invalidUsername)
      .exec[Id, HttpResponse[String]](headerUserAgent)

    response should be('left)
  }

  "Users >> GetAuth" should "return error on Left when no accessToken is provided" in {
    val response =
      Github().users.getAuth.exec[Id, HttpResponse[String]](headerUserAgent)
    response should be('left)
  }

  "Users >> GetUsers" should "return users for a valid since value" in {
    val response = Github(accessToken).users
      .getUsers(validSinceInt)
      .exec[Id, HttpResponse[String]](headerUserAgent)
    response should be('right)

    response.toOption map { r ⇒
      r.result.nonEmpty shouldBe true
      r.statusCode shouldBe okStatusCode
    }
  }

  it should "return an empty list when a invalid since value is provided" in {
    val response =
      Github(accessToken).users
        .getUsers(invalidSinceInt)
        .exec[Id, HttpResponse[String]](headerUserAgent)
    response should be('right)

    response.toOption map { r ⇒
      r.result.isEmpty shouldBe true
      r.statusCode shouldBe okStatusCode
    }
  }

}
