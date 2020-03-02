/*
 * Copyright 2016-2020 47 Degrees, LLC. <http://www.47deg.com>
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

import cats.effect.IO
import github4s.GithubResponses.{GHResponse, GHResult}
import github4s.integration._
import org.scalatest.flatspec.AsyncFlatSpec
import org.scalatest.matchers.should.Matchers
import org.scalatest.{Assertion, Ignore, Inspectors, Tag}

import scala.concurrent.ExecutionContext

class IntegrationSpec
    extends BaseIntegrationSpec
    with GHActivitiesSpec
    with GHAuthSpec
    with GHGitDataSpec
    with GHIssuesSpec
    with GHOrganizationsSpec
    with GHPullRequestsSpec
    with GHReposSpec
    with GHUsersSpec
    with GHTeamsSpec
    with GHProjectsSpec

object Integration
    extends Tag(
      if (sys.env.get("GITHUB4S_ACCESS_TOKEN").isDefined) ""
      else classOf[Ignore].getName
    )

abstract class BaseIntegrationSpec
    extends AsyncFlatSpec
    with Matchers
    with Inspectors
    with TestData {
  override implicit val executionContext: ExecutionContext =
    scala.concurrent.ExecutionContext.Implicits.global

  implicit val ioContextShift = IO.contextShift(executionContext)

  def accessToken: Option[String] = sys.env.get("GITHUB4S_ACCESS_TOKEN")

  def testIsRight[A](response: GHResponse[A], f: (GHResult[A]) => Assertion): Assertion = {
    response.isRight shouldBe true
    response.toOption map (f(_)) match {
      case _ => succeed
    }
  }

  def testIsLeft[A](response: GHResponse[A]): Assertion =
    response.isLeft shouldBe true

}
