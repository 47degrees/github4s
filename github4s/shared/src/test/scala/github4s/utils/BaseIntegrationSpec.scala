/*
 * Copyright 2016-2023 47 Degrees Open Source <https://www.47deg.com>
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

import cats.effect.{unsafe, IO, Resource}
import github4s.integration._
import github4s.{GHError, GHResponse, IOAssertions}
import org.http4s.blaze.client.BlazeClientBuilder
import org.http4s.client.Client
import org.scalatest.flatspec.AsyncFlatSpec
import org.scalatest.matchers.should.Matchers
import org.scalatest.{Assertion, Ignore, Inspectors, Tag}

import scala.concurrent.ExecutionContext
import scala.reflect.ClassTag

class IntegrationSpec
    extends BaseIntegrationSpec
    with ActivitiesSpec
    with AuthSpec
    with GitDataSpec
    with IssuesSpec
    with OrganizationsSpec
    with PullRequestsSpec
    with ReposSpec
    with UsersSpec
    with TeamsSpec
    with ProjectsSpec
    with SearchSpec

object Integration
    extends Tag(
      if (sys.env.get("GITHUB_TOKEN").exists(_.nonEmpty)) ""
      else classOf[Ignore].getName
    )

abstract class BaseIntegrationSpec
    extends AsyncFlatSpec
    with Matchers
    with Inspectors
    with TestData
    with IOAssertions {

  override val executionContext: ExecutionContext = TestContext.context

  protected implicit val ioRuntime: unsafe.IORuntime = unsafe.IORuntime.global

  val clientResource: Resource[IO, Client[IO]] = BlazeClientBuilder[IO].resource

  def accessToken: Option[String] = sys.env.get("GITHUB_TOKEN")

  def testIsRight[A](response: GHResponse[A], f: A => Assertion = (_: A) => succeed): Assertion = {
    withClue(response.result) {
      response.result match {
        case Left(error)  => fail(error.getMessage())
        case Right(value) => f(value)
      }
    }
  }

  def testIsLeft[E <: GHError: ClassTag, A](response: GHResponse[A]): Assertion = {
    val ct = implicitly[ClassTag[E]]
    response.result match {
      case Left(ct(_)) => succeed
      case Left(l)     => fail(s"Left-side is not of type $ct, but of type ${l.getClass}")
      case Right(r)    => fail(s"It should be a left but was right $r")
    }
  }
}
