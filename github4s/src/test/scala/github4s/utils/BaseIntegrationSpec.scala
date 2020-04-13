/*
 * Copyright 2016-2020 47 Degrees Open Source <https://www.47deg.com>
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

import cats.effect.{ContextShift, IO, Resource}
import github4s.GithubResponses.GHResponse
import github4s.integration._
import org.http4s.client.Client
import org.http4s.client.blaze.BlazeClientBuilder
import org.scalatest.flatspec.AsyncFlatSpec
import org.scalatest.matchers.should.Matchers
import org.scalatest.{Assertion, Ignore, Inspectors, Tag}

import scala.concurrent.ExecutionContext

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

object Integration
    extends Tag(
      if (sys.env.get("GITHUB_TOKEN").exists(_.nonEmpty)) ""
      else classOf[Ignore].getName
    )

abstract class BaseIntegrationSpec
    extends AsyncFlatSpec
    with Matchers
    with Inspectors
    with TestData {

  override val executionContext: ExecutionContext =
    scala.concurrent.ExecutionContext.Implicits.global

  implicit val ioContextShift: ContextShift[IO] = IO.contextShift(executionContext)

  val clientResource: Resource[IO, Client[IO]] = BlazeClientBuilder[IO](executionContext).resource

  def accessToken: Option[String] = sys.env.get("GITHUB_TOKEN")

  def testIsRight[A](response: GHResponse[A], f: A => Assertion = (_: A) => succeed): Assertion = {
    response.result.isRight shouldBe true
    response.result.toOption map (f(_)) match {
      case _ => succeed
    }
  }

  def testIsLeft[A](response: GHResponse[A]): Assertion =
    response.result.isLeft shouldBe true

}
