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

import com.github.marklister.base64.Base64.Encoder
import github4s.free.domain._
import io.circe.parser.parse
import org.mockito.ArgumentMatcher

trait TestUtils {

  case class JsonArgMatcher(json: String) extends ArgumentMatcher[String] {
    override def matches(argument: String): Boolean = parse(json) == parse(argument)
  }

  val accessToken: Option[String] = sys.env.get("GITHUB4S_ACCESS_TOKEN")
  def tokenHeader: String         = "token " + accessToken.getOrElse("")
  val headerUserAgent             = Map("user-agent" -> "github4s")

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
  val validGistPublic      = true
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

  val githubApiUrl = "http://api.github.com"
  val encoding     = Some("utf-8")

  val validRefSingle   = "heads/master"
  val validRefMultiple = "heads/feature"
  val invalidRef       = "heads/feature-branch-that-no-longer-exists"

  val validCommitSha   = "d3b048c1f500ee5450e5d7b3d1921ed3e7645891"
  val validCommitMsg   = "Add SBT project settings"
  val commitType       = "commit"
  val invalidCommitSha = "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"

  val validTreeSha   = "827efc6d56897b048c772eb4087f854f46256132"
  val invalidTreeSha = "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"

  val validTagTitle = "v0.1.1"
  val validTagSha   = "c3d0be41ecbe669545ee3e94d31ed9a4bc91ee3c"

  val validPath = "project/plugins.sbt"

  val treeDataList: List[TreeData] = List(TreeDataSha(validPath, "100644", "blob", validTreeSha))
  val treeDataResult = List(
    TreeDataResult(
      path = validPath,
      mode = "100644",
      `type` = "blob",
      size = Some(100),
      sha = validTreeSha,
      url = githubApiUrl))

  val refObject = RefObject(commitType, validCommitSha, githubApiUrl)
  val ref       = Ref("XXXX", githubApiUrl, refObject)

  val refCommitAuthor =
    RefAuthor("2014-11-07T22:01:45Z", validUsername, "developer@47deg.com")
  val refInfo = new RefInfo(validTreeSha, githubApiUrl)
  val refCommit = RefCommit(
    sha = validCommitSha,
    url = githubApiUrl,
    author = refCommitAuthor,
    committer = refCommitAuthor,
    message = validNote,
    tree = refInfo,
    parents = List(refInfo))

  val pullRequest = PullRequest(
    id = 1,
    number = 1,
    state = "open",
    title = "Title",
    body = "Body",
    locked = false,
    html_url = githubApiUrl,
    created_at = "2011-04-10T20:09:31Z",
    updated_at = None,
    closed_at = None,
    merged_at = None,
    base = None,
    user = None,
    assignee = None
  )

  val tag = Tag(
    tag = validTagTitle,
    sha = validTagSha,
    url = githubApiUrl,
    message = validNote,
    tagger = refCommitAuthor,
    `object` = RefObject(commitType, validCommitSha, githubApiUrl)
  )
}
