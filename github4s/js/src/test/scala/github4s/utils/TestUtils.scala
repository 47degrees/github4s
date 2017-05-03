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

package github4s.utils

import org.scalatest.{Assertion, Matchers}
import cats.implicits._
import github4s.GithubResponses.{GHResponse, GHResult}
import github4s.free.domain._

import scala.concurrent.Future

trait TestUtils extends Matchers with TestData {

  def testFutureIsLeft[A](response: Future[GHResponse[A]])(
      implicit ec: scala.concurrent.ExecutionContext): Future[Assertion] = {
    response map { r =>
      r.isLeft should be(true)
    }
  }

  def testFutureIsRight[A](response: Future[GHResponse[A]], f: (GHResult[A]) => Assertion)(
      implicit ec: scala.concurrent.ExecutionContext): Future[Assertion] = {
    response map { r ⇒
      {
        r.isRight should be(true)

        r.toOption map { rr =>
          f(rr)
        } match {
          case _ => succeed
        }
      }
    }
  }

  val accessToken: Option[String] = Option(github4s.BuildInfo.token)
  def tokenHeader: String         = "token " + accessToken.getOrElse("")

}
