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

import cats.syntax.either._
import github4s.GithubResponses.{GHResponse, GHResult}
import github4s.free.interpreters.{Capture, Interpreters}
import github4s.HttpRequestBuilderExtension
import org.scalatest.{Assertion, AsyncFlatSpec, Matchers}

import scala.concurrent.{ExecutionContext, Future}

abstract class BaseIntegrationSpec[T] extends AsyncFlatSpec with Matchers with TestData {

  override implicit val executionContext: ExecutionContext =
    scala.concurrent.ExecutionContext.Implicits.global

  implicit def futureInterpreters: Interpreters[Future, T]
  implicit def extension(implicit capture: Capture[Future]): HttpRequestBuilderExtension[T, Future]

  def accessToken: Option[String]

  def testFutureIsLeft[A](response: Future[GHResponse[A]]): Future[Assertion] = {
    response map { r =>
      r.isLeft shouldBe true
    }
  }

  def testFutureIsRight[A](
      response: Future[GHResponse[A]],
      f: (GHResult[A]) => Assertion): Future[Assertion] = {
    response map { r ⇒
      r.isRight shouldBe true
      r.toOption map (f(_)) match {
        case _ => succeed
      }
    }
  }

}
