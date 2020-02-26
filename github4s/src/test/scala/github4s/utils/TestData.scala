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

import com.github.marklister.base64.Base64._
import github4s.domain._
import java.util.UUID

import github4s.domain.{Stargazer, StarredRepository, Subscription}

trait TestData extends DummyGithubUrls {

  val sampleToken: Option[String]          = Some("token")
  val headerUserAgent: Map[String, String] = Map("user-agent" -> "github4s")
  val headerAccept: Map[String, String] = Map(
    "Accept" -> "application/vnd.github.inertia-preview+json"
  )

  val validUsername   = "rafaparadela"
  val invalidUsername = "GHInvalidUserName"
  val invalidPassword = "invalidPassword"

  val githubApiUrl = "http://api.github.com"
  val user         = User(1, validUsername, githubApiUrl, githubApiUrl)

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
  val invalidRepoOwner   = "invalid47deg"
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

  val validOrganizationName   = "47deg"
  val invalidOrganizationName = "invalid47deg"

  val validSinceInt   = 100
  val invalidSinceInt = 999999999

  val okStatusCode           = 200
  val createdStatusCode      = 201
  val deletedStatusCode      = 204
  val unauthorizedStatusCode = 401
  val notFoundStatusCode     = 404

  val validAnonParameter   = "true"
  val invalidAnonParameter = "X"

  val validGistDescription     = "A Gist"
  val validGistPublic          = true
  val validGistFileContent     = "val meaningOfLife = 42"
  val validGistFilename        = "test.scala"
  val validGistNewFilename     = "best.scala"
  val validGistOldFilename     = "fest.scala"
  val validGistDeletedFilename = "rest.scala"

  val validSearchQuery       = "Scala 2.12"
  val nonExistentSearchQuery = "nonExistentSearchQueryString"
  val validSearchParams = List(
    OwnerParamInRepository(s"$validRepoOwner/$validRepoName")
  )

  val validIssueNumber = 48
  val validIssueTitle  = "Sample Title"
  val validIssueBody   = "Sample Body"
  val validIssueState  = "closed"
  val validIssueLabel  = List("bug", "code review")
  val validAssignees   = List(validUsername)

  val encoding = Some("utf-8")

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

  val validPullRequestFileSha      = "f80f79cafbe3f2ba71311b82e1171e73bd37a470"
  val validPullRequestNumber       = 1
  val validPullRequestReviewNumber = 39318789
  val validMergeCommitSha          = "e5bd3914e2e596debea16f433f57875b5b90bcd6"

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
  val validAvatarUrl   = "https://github.com/images/error/hubot_happy.gif"
  val validNodeId      = "MDY6U3RhdHVzMQ=="

  val treeDataList: List[TreeData] = List(
    TreeDataSha(validPath, validMode, validBlobType, validTreeSha)
  )
  val treeDataResult = List(
    TreeDataResult(
      path = validPath,
      mode = validMode,
      `type` = validBlobType,
      size = Some(100),
      sha = validTreeSha,
      url = githubApiUrl
    )
  )

  val refObject = RefObject(commitType, validCommitSha, githubApiUrl)
  val ref       = Ref("XXXX", "nodeid", githubApiUrl, refObject)

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
    parents = List(refInfo)
  )

  val issue = Issue(
    id = 1,
    title = validIssueTitle,
    body = Some(validIssueBody),
    url = githubApiUrl,
    repository_url = githubApiUrl,
    labels_url = githubApiUrl,
    comments_url = githubApiUrl,
    events_url = githubApiUrl,
    html_url = githubApiUrl,
    number = validIssueNumber,
    state = validIssueState,
    user = Some(user),
    assignee = None,
    labels = List.empty,
    locked = None,
    comments = 1,
    pull_request = None,
    closed_at = None,
    created_at = "2011-04-10T20:09:31Z",
    updated_at = "2011-04-10T20:09:31Z"
  )

  val label = Label(
    id = Some(1),
    name = validIssueLabel.head,
    url = githubApiUrl,
    color = "",
    default = None
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
    body = Some("Body"),
    locked = false,
    html_url = githubApiUrl,
    created_at = "2011-04-10T20:09:31Z",
    updated_at = None,
    closed_at = None,
    merged_at = None,
    merge_commit_sha = Some(validMergeCommitSha),
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
    patch = None,
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
    author = Some(user),
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
    url = githubApiUrl,
    avatar_url = validAvatarUrl,
    id = 1,
    node_id = validNodeId,
    state = validStatusState,
    target_url = None,
    description = None,
    context = None,
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
      owner = Some(user),
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
    Some(user),
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
  val validBranchName = "master"
  val protectedBranch = Branch(
    name = validBranchName,
    commit = BranchCommit(
      sha = validCommitSha,
      url = s"https://api.github.com/repos/$validRepoOwner/$validRepoName/commits/$validCommitSha"
    ),
    `protected` = Some(true),
    protection_url = Some(
      s"https://api.github.com/repos/$validRepoOwner/$validRepoName/branches/$validBranchName/protection"
    )
  )
  val branch = protectedBranch.copy(`protected` = None, protection_url = None)

  val validTokenType = "bearer"
  val validAuthState = UUID.randomUUID().toString

  val authorization = Authorization(1, validRedirectUri, "token")
  val authorize     = Authorize(validRedirectUri, validAuthState)

  val oAuthToken   = OAuthToken("token", validTokenType, "public_repo")
  val validGistUrl = "https://api.github.com/gists/aa5a315d61ae9438b18d"
  val validGistId  = "aa5a315d61ae9438b18d"
  val gist         = Gist(validGistUrl, validGistId, validGistDescription, validGistPublic, Map())
  val validGistSha = "deadbeef"

  val stargazer         = Stargazer(None, user)
  val starredRepository = StarredRepository(None, repo)

  val pullRequestReview = PullRequestReview(
    id = validPullRequestReviewNumber,
    user = Some(user),
    body = validCommentBody,
    commit_id = validCommitSha,
    state = PRRStateCommented,
    html_url = "",
    pull_request_url = ""
  )

  val validNameTeam = "47 Devs"
  val validSlug     = "47-devs"

  val team = Team(
    name = validNameTeam,
    id = 40235,
    node_id = "MDQ6VGVhbTQwMjM1",
    slug = validSlug,
    description = null,
    privacy = "secret",
    url = "https://api.github.com/organizations/479857/team/40235",
    html_url = "https://github.com/orgs/47deg/teams/47-devs",
    members_url = "https://api.github.com/organizations/479857/team/40235/members{/member}",
    repositories_url = "https://api.github.com/organizations/479857/team/40235/repos",
    permission = "push",
    parent = null
  )

  val validProjectId   = 1903050
  val invalidProjectId = 11111

  val project = Project(
    owner_url = "https://api.github.com/orgs/47deg",
    url = "https://api.github.com/projects/1903050",
    html_url = "https://github.com/orgs/47deg/projects/4",
    columns_url = "https://api.github.com/projects/1903050/columns",
    id = validProjectId,
    node_id = "MDc6UHJvamVjdDE5MDMwNTA=",
    name = "Team Asterism",
    body = Some(
      "Track all things related with the open source initiatives maintained by the Asterism internal tea"
    ),
    number = 4,
    creator = Creator(
      login = "calvellido",
      id = 7753447,
      node_id = "MDQ6VXNlcjc3NTM0NDc=",
      avatar_url = "https://avatars0.githubusercontent.com/u/7753447?v=4",
      gravatar_id = None,
      url = "https://api.github.com/users/calvellido",
      html_url = "https://github.com/calvellido",
      followers_url = "https://api.github.com/users/calvellido/followers",
      following_url = "https://api.github.com/users/calvellido/following{/other_user}",
      gists_url = "https://api.github.com/users/calvellido/gists{/gist_id}",
      starred_url = "https://api.github.com/users/calvellido/starred{/owner}{/repo}",
      subscriptions_url = "https://api.github.com/users/calvellido/subscriptions",
      organizations_url = "https://api.github.com/users/calvellido/orgs",
      repos_url = "https://api.github.com/users/calvellido/repos",
      events_url = "https://api.github.com/users/calvellido/events{/privacy}",
      received_events_url = "https://api.github.com/users/calvellido/received_events",
      `type` = "User",
      site_admin = false
    ),
    created_at = "2018-10-30T14:18:42Z",
    updated_at = "2019-09-30T07:26:21Z",
    organization_permission = Some("read"),
    `private` = Some(true)
  )

  val column = Column(
    url = "https://api.github.com/projects/columns/3724010",
    project_url = "https://api.github.com/projects/1910444",
    cards_url = "https://api.github.com/projects/columns/3724010/cards",
    id = 3724010,
    node_id = "MDEzOlByb2plY3RDb2x1bW4zNzI0MDEw",
    name = "To do",
    created_at = "2018-11-02T09:36:28Z",
    updated_at = "2019-07-04T09:39:01Z"
  )

}
