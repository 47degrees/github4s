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

package github4s.cats.effect.js

import cats.effect.IO
import github4s.Github
import github4s.Github._
import github4s.cats.effect.js.Implicits._
import org.scalatest.{Assertion, AsyncFunSuite, Matchers}
import fr.hmil.roshttp.response.SimpleHttpResponse
import github4s.GithubResponses.GHResponse
import github4s.free.domain.User
import org.scalactic.source.Position

import scala.concurrent.{ExecutionContext, ExecutionContextExecutor, Future, Promise}
import scala.util.Try

class CatsEffectJSSpec extends AsyncFunSuite with Matchers {

  implicit override def executionContext: ExecutionContextExecutor = ExecutionContext.global

  def testEffectOnRunAsync[A](source: IO[GHResponse[A]], f: GHResponse[A] => Assertion)(
      implicit pos: Position): Future[Assertion] = {

    val effect  = Promise[GHResponse[A]]()
    val attempt = Promise[Try[GHResponse[A]]]()
    effect.future.onComplete(attempt.success)

    val io = source.runAsync {
      case Right(s) => IO(effect.success(s))
      case Left(e)  => IO(effect.failure(e))
    }

    for (_ <- io.unsafeToFuture(); v <- attempt.future) yield {
      v.toOption
        .map { result =>
          f(result)
        }
        .getOrElse(fail("effect attempt failed"))
    }
  }

  val accessToken     = sys.env.get("GITHUB4S_ACCESS_TOKEN")
  val headerUserAgent = Map("user-agent" -> "github4s")
  val validUsername   = "rafaparadela"
  val invalidUsername = "GHInvalidUserName"
  val okStatusCode    = 200

  test("return a succeded result for a valid call") {
    val response = Github(accessToken).users
      .get(validUsername)
      .exec[IO, SimpleHttpResponse](headerUserAgent)

    testEffectOnRunAsync(response, { r: GHResponse[User] =>
      r.isRight shouldBe true
      r.right.map(_.statusCode) shouldBe Right(okStatusCode)
    })
  }

  test("return a failed result for an invalid call") {
    val response = Github(accessToken).users
      .get(invalidUsername)
      .exec[IO, SimpleHttpResponse](headerUserAgent)

    testEffectOnRunAsync(response, { r: GHResponse[User] =>
      r.isLeft shouldBe true
    })
  }

  // only here for the 80% coverage, to remove once JS makes use of Captures
  test("IOCapture == IO.pure") {
    ioCaptureInstance.capture("a") shouldBe IO.pure("a")
  }
}
