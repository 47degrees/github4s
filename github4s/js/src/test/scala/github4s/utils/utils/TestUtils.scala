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

import com.github.marklister.base64.Base64._
import org.scalatest.{Assertion, Matchers}
import cats.implicits._
import github4s.GithubResponses.{GHResponse, GHResult}
import github4s.free.domain.{IssueTypeIssue, OwnerParamInRepository, SearchIn, SearchInTitle}

import scala.concurrent.Future

trait TestUtils extends Matchers {
  def testFutureIsLeft[A](response: Future[GHResponse[A]])(
      implicit ec: scala.concurrent.ExecutionContext) = {
    response map { r =>
      r.isLeft should be(true)
    }
  }

  def testFutureIsRight[A](response: Future[GHResponse[A]], f: (GHResult[A]) => Assertion)(
      implicit ec: scala.concurrent.ExecutionContext) = {
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

  val accessToken     = Option(github4s.BuildInfo.token)
  def tokenHeader     = "token " + accessToken.getOrElse("")
  val headerUserAgent = Map("user-agent" -> "github4s")

  val validUsername   = "rafaparadela"
  val invalidUsername = "GHInvalidaUserName"
  val invalidPassword = "invalidPassword"

  def validBasicAuth = s"Basic ${s"$validUsername:".getBytes.toBase64}"
  def invalidBasicAuth =
    s"Basic ${s"$validUsername:$invalidPassword".getBytes.toBase64}"

  val validScopes         = List("public_repo")
  val validNote           = "New access token"
  val validClientId       = "e8e39175648c9db8c280"
  val invalidClientSecret = "1234567890"
  val validCode           = "code"
  val invalidCode         = "invalid-code"

  val validRepoOwner   = "47deg"
  val validRepoName    = "github4s"
  val validFilePath    = "README.md"
  val invalidFilePath  = "NON_EXISTENT_FILE_IN_REPOSITORY"
  val invalidRepoName  = "GHInvalidRepoName"
  val validRedirectUri = "http://localhost:9000/_oauth-callback"
  val validPage        = 1
  val invalidPage      = 999
  val validPerPage     = 100

  val validSinceInt   = 100
  val invalidSinceInt = 999999999

  val okStatusCode           = 200
  val createdStatusCode      = 201
  val unauthorizedStatusCode = 401
  val notFoundStatusCode     = 404

  val validAnonParameter   = "true"
  val invalidAnonParameter = "X"

  val validGistDescription = "A Gist"
  val validGistPublic      = false
  val validGistFileContent = "val meaningOfLife = 42"
  val validGistFilename    = "test.scala"

  val validSearchQuery       = "Scala 2.12"
  val nonExistentSearchQuery = "nonExistentSearchQueryString"
  val validSearchParams = List(
    OwnerParamInRepository(s"$validRepoOwner/$validRepoName"),
    IssueTypeIssue,
    SearchIn(Set(SearchInTitle))
  )

  val validIssue      = 48
  val validIssueTitle = "Sample Title"
  val validIssueBody  = "Sample Body"
  val validIssueState = "closed"
  val validIssueLabel = List("bug", "code review")
  val validAssignees  = List(validUsername)

  val validRefSingle = "heads/master"
  val invalidRef     = "heads/feature-branch-that-no-longer-exists"

  val validCommitSha   = "d3b048c1f500ee5450e5d7b3d1921ed3e7645891"
  val invalidCommitSha = "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"
  val validCommitMsg   = "Add SBT project settings"

  val validStatusState = "success"
}
