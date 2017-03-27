/*
 * Copyright (c) 2016 47 Degrees, LLC. <http://www.47deg.com>
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
 * the Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
 * FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
 * IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
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

  val validCommitSha = "d3b048c1f500ee5450e5d7b3d1921ed3e7645891"
  val validCommitMsg = "Add SBT project settings"
}
