/*
 * Copyright 2016-2018 47 Degrees, LLC. <http://www.47deg.com>
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

package github4s.cats.effect.jvm

import cats.effect.IO
import github4s.Github
import github4s.Github._
import github4s.cats.effect.jvm.Implicits._
import org.scalatest.{FlatSpec, Matchers}
import scalaj.http.HttpResponse

class CatsEffectJVMSpec extends FlatSpec with Matchers {
  val accessToken     = sys.env.get("GITHUB4S_ACCESS_TOKEN")
  val headerUserAgent = Map("user-agent" -> "github4s")
  val validUsername   = "rafaparadela"
  val invalidUsername = "GHInvalidUserName"
  val okStatusCode    = 200

  "cats-effect jvm integration" should "return a succeded result for a valid call" in {
    val response =
      Github(accessToken).users.get(validUsername).exec[IO, HttpResponse[String]](headerUserAgent)

    val res = response.unsafeRunSync
    res.isRight shouldBe true
    res.right.map(_.statusCode) shouldBe Right(okStatusCode)
  }

  it should "return a failed result for an invalid call" in {
    val response =
      Github(accessToken).users
        .get(invalidUsername)
        .exec[IO, HttpResponse[String]](headerUserAgent)

    val res = response.unsafeRunSync
    res.isLeft shouldBe true
  }
}
