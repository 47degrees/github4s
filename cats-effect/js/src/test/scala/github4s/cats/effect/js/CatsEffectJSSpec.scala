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

package github4s.cats.effect.js

import cats.effect.IO
import github4s.Github
import github4s.Github._
import github4s.cats.effect.js.Implicits._
import org.scalatest.{Assertion, AsyncFunSuite, Matchers}
import fr.hmil.roshttp.response.SimpleHttpResponse
import github4s.GithubResponses.GHResponse
import github4s.free.domain.User

import scala.concurrent.{ExecutionContext, ExecutionContextExecutor}
import scala.scalajs.js

class CatsEffectJSSpec extends AsyncFunSuite with Matchers {

  implicit override def executionContext: ExecutionContextExecutor = ExecutionContext.global

  val accessToken =
    js.Dynamic.global.process.env.GITHUB4S_ACCESS_TOKEN.asInstanceOf[js.UndefOr[String]].toOption
  val headerUserAgent = Map("user-agent" -> "github4s")
  val validUsername   = "rafaparadela"
  val invalidUsername = "GHInvalidUserName"
  val okStatusCode    = 200

  test("return a succeded result for a valid call") {
    val response = Github(accessToken).users
      .get(validUsername)
      .exec[IO, SimpleHttpResponse](headerUserAgent)

    response
      .unsafeToFuture()
      .map { r: GHResponse[User] =>
        r.isRight shouldBe true
        r.right.map(_.statusCode) shouldBe Right(okStatusCode)
      }
  }

  test("return a failed result for an invalid call") {
    val response = Github(accessToken).users
      .get(invalidUsername)
      .exec[IO, SimpleHttpResponse](headerUserAgent)

    response
      .unsafeToFuture()
      .map { r: GHResponse[User] =>
        r.isLeft shouldBe true
      }
  }

  // only here for the 80% coverage, to remove once JS makes use of Captures
  test("IOCapture == IO.apply") {
    syncCaptureInstance[IO].capture("a").unsafeRunSync shouldBe IO("a").unsafeRunSync
  }
}
