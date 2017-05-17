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
import github4s.free.domain._
import java.util.UUID

trait TestData extends DummyGithubUrls {

  val sampleToken: Option[String]          = Some("token")
  val headerUserAgent: Map[String, String] = Map("user-agent" -> "github4s")

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

  val validRepoOwner     = "47deg"
  val validRepoName      = "github4s"
  val invalidRepoName    = "GHInvalidRepoName"
  val validRedirectUri   = "http://localhost:9000/_oauth-callback"
  val validPage          = 1
  val invalidPage        = 999
  val validPerPage       = 100
  val validFilePath      = "README.md"
  val invalidFilePath    = "NON_EXISTENT_FILE_IN_REPOSITORY"
  val validDirPath       = "lib"
  val validSymlinkPath   = "bin/some-symlink"
  val validSubmodulePath = "test/qunit"

  val validSinceInt   = 100
  val invalidSinceInt = 999999999

  val okStatusCode           = 200
  val createdStatusCode      = 201
  val deletedStatusCode      = 204
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

  val validIssueNumber = 48
  val validIssueTitle  = "Sample Title"
  val validIssueBody   = "Sample Body"
  val validIssueState  = "closed"
  val validIssueLabel  = List("bug", "code review")
  val validAssignees   = List(validUsername)

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

  val validPullRequestFileSha = "f80f79cafbe3f2ba71311b82e1171e73bd37a470"
  val validPullRequestNumber  = 1

  val validHead   = "test-pr-issue"
  val invalidHead = ""

  val validBase   = "master"
  val invalidBase = ""

  val validNewPullRequestData   = NewPullRequestData("Amazing new feature", "Please pull this in!")
  val invalidNewPullRequestData = NewPullRequestData("", "")

  val validNewPullRequestIssue   = NewPullRequestIssue(31)
  val invalidNewPullRequestIssue = NewPullRequestIssue(5)

  val validPath = "project/plugins.sbt"

  val validStatusState = "success"
  val validMode        = "100644"
  val validBlobType    = "blob"

  val treeDataList: List[TreeData] = List(
    TreeDataSha(validPath, validMode, validBlobType, validTreeSha))
  val treeDataResult = List(
    TreeDataResult(
      path = validPath,
      mode = validMode,
      `type` = validBlobType,
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

  val issue = Issue(
    id = 1,
    title = validIssueTitle,
    body = validIssueBody,
    url = githubApiUrl,
    repository_url = githubApiUrl,
    labels_url = githubApiUrl,
    comments_url = githubApiUrl,
    events_url = githubApiUrl,
    html_url = githubApiUrl,
    number = validIssueNumber,
    state = validIssueState,
    user = user,
    assignee = None,
    labels = List.empty,
    locked = None,
    comments = 1,
    pull_request = None,
    closed_at = None,
    created_at = "2011-04-10T20:09:31Z",
    updated_at = "2011-04-10T20:09:31Z"
  )

  val searchIssuesResult = SearchIssuesResult(
    total_count = 1,
    incomplete_results = false,
    items = List(issue)
  )

  val pullRequest = PullRequest(
    id = 1,
    number = validPullRequestNumber,
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
    head = None,
    user = None,
    assignee = None
  )

  val pullRequestFile = PullRequestFile(
    sha = validPullRequestFileSha,
    filename = validPath,
    status = "modified",
    additions = 3,
    deletions = 1,
    changes = 4,
    blob_url = githubApiUrl,
    raw_url = githubApiUrl,
    contents_url = githubApiUrl,
    patch = "",
    previous_filename = None
  )

  val tag = Tag(
    tag = validTagTitle,
    sha = validTagSha,
    url = githubApiUrl,
    message = validNote,
    tagger = refCommitAuthor,
    `object` = RefObject(commitType, validCommitSha, githubApiUrl)
  )

  val release = Release(
    id = 1,
    tag_name = validTagTitle,
    target_commitish = "master",
    name = validTagTitle,
    body = validNote,
    draft = false,
    prerelease = false,
    created_at = "2011-04-10T20:09:31Z",
    published_at = "2011-04-10T20:09:31Z",
    author = user,
    url = githubApiUrl,
    html_url = githubApiUrl,
    assets_url = githubApiUrl,
    upload_url = githubApiUrl,
    tarball_url = githubApiUrl,
    zipball_url = githubApiUrl
  )

  val content = Content(
    `type` = "file",
    encoding = Some("base64"),
    target = None,
    submodule_git_url = None,
    size = validSinceInt,
    name = validFilePath,
    path = validFilePath,
    content = Some(validGistFileContent.getBytes.toBase64),
    sha = invalidCommitSha,
    url = githubApiUrl,
    git_url = githubApiUrl,
    html_url = githubApiUrl,
    download_url = Some(githubApiUrl)
  )

  val status = Status(
    id = 1,
    url = githubApiUrl,
    state = validStatusState,
    target_url = None,
    description = None,
    context = None,
    creator = Some(user),
    created_at = "2011-04-10T20:09:31Z",
    updated_at = "2011-04-10T20:09:31Z"
  )

  val combinedStatus = CombinedStatus(
    url = githubApiUrl,
    state = validStatusState,
    commit_url = githubApiUrl,
    sha = validCommitSha,
    total_count = 1,
    statuses = List(status),
    repository = StatusRepository(
      id = 1,
      name = validRepoName,
      full_name = s"$validRepoOwner/$validRepoName",
      owner = user,
      `private` = false,
      description = None,
      fork = false,
      urls = Map()
    )
  )
  val validThreadId = 219647953

  val invalidThreadId = 0

  val subscription = Subscription(
    subscribed = true,
    ignored = false,
    reason = null,
    created_at = "2012-10-06T21:34:12Z",
    url = "https://api.github.com/notifications/threads/1/subscription",
    thread_url = "https://api.github.com/notifications/threads/1"
  )
  val validCommentBody   = "the comment"
  val invalidIssueNumber = 0
  val validCommentId     = 1
  val invalidCommentId   = 0

  val comment = Comment(
    validCommentId,
    "https://api.github.com/repos/octocat/Hello-World/issues/comments/1",
    "https: //github.com/octocat/Hello-World/issues/1347#issuecomment-1",
    validCommentBody,
    user,
    "2011-04-14T16:00:49Z",
    "2011-04-14T16:00:49Z"
  )
  val repo = Repository(
    1296269,
    validRepoName,
    s"$validRepoOwner/$validRepoName",
    user,
    false,
    Some(validNote),
    false,
    RepoUrls(
      s"https://api.github.com/repos/$validRepoOwner/$validRepoName",
      s"https://github.com/$validRepoOwner/$validRepoName",
      s"git:github.com/$validRepoOwner/$validRepoName.git",
      s"git@github.com:$validRepoOwner/$validRepoName.git",
      s"https://github.com/$validRepoOwner/$validRepoName.git",
      s"https://svn.github.com/$validRepoOwner/$validRepoName",
      Map.empty
    ),
    "2011-01-26T19:01:12Z",
    "2011-01-26T19:14:43Z",
    "2011-01-26T19:06:43Z",
    None,
    None,
    RepoStatus(108, 80, 80, 9, 0, None, None, None, None, true, true, false, true),
    None
  )

  val commit = Commit(
    validCommitSha,
    validNote,
    "2011-01-26T19:01:12Z",
    s"https://github.com/$validRepoOwner/$validRepoName/commit/$validCommitSha",
    None,
    None,
    None
  )
  val validTokenType = "bearer"
  val validAuthState = UUID.randomUUID().toString
  val user           = User(1, validUsername, githubApiUrl, githubApiUrl)

  val authorization = Authorization(1, validRedirectUri, "token")
  val authorize     = Authorize(validRedirectUri, validAuthState)

  val oAuthToken = OAuthToken("token", validTokenType, "public_repo")
}
