/*
 * Copyright 2016-2018 47 Degrees, LLC. <http://www.47deg.com>
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

package github4s.unit

import github4s.api._
import github4s.free.domain.{GistFile, Pagination}
import github4s.utils.{DummyGithubUrls, MockGithubApiServer, TestUtilsJVM}
import org.scalatest._
import cats.implicits._

import scalaj.http._
import cats.Id
import github4s.jvm.ImplicitsJVM

class ApiSpec
    extends FlatSpec
    with Matchers
    with TestUtilsJVM
    with MockGithubApiServer
    with DummyGithubUrls
    with ImplicitsJVM {

  val auth          = new Auth[HttpResponse[String], Id]
  val repos         = new Repos[HttpResponse[String], Id]
  val users         = new Users[HttpResponse[String], Id]
  val gists         = new Gists[HttpResponse[String], Id]
  val gitData       = new GitData[HttpResponse[String], Id]
  val pullRequests  = new PullRequests[HttpResponse[String], Id]
  val issues        = new Issues[HttpResponse[String], Id]
  val activities    = new Activities[HttpResponse[String], Id]
  val organizations = new Organizations[HttpResponse[String], Id]

  "Auth >> NewAuth" should "return a valid token when valid credential is provided" in {
    val response = auth.newAuth(
      validUsername,
      "",
      validScopes,
      validNote,
      validClientId,
      "",
      headers = headerUserAgent)
    response should be('right)

    response.toOption map { r ⇒
      r.result.token.nonEmpty shouldBe true
      r.statusCode shouldBe okStatusCode
    }
  }
  it should "return error on Left when invalid credential is provided" in {
    val response =
      auth.newAuth(
        validUsername,
        invalidPassword,
        validScopes,
        validNote,
        validClientId,
        "",
        headers = headerUserAgent)
    response should be('left)
  }

  "Auth >> AuthorizeUrl" should "return the expected URL for valid username" in {
    val response =
      auth.authorizeUrl(validClientId, validRedirectUri, validScopes)
    response should be('right)

    response.toOption map { r ⇒
      r.result.url.contains(validRedirectUri) shouldBe true
      r.statusCode shouldBe okStatusCode
    }
  }

  "Auth >> GetAccessToken" should "return a valid access_token when a valid code is provided" in {
    val response =
      auth.getAccessToken("", "", validCode, "", "", headers = headerUserAgent)
    response should be('right)

    response.toOption map { r ⇒
      r.result.access_token.nonEmpty shouldBe true
      r.statusCode shouldBe okStatusCode
    }
  }
  it should "return error on Left when invalid code is provided" in {
    val response = auth.getAccessToken("", "", invalidCode, "", "", headers = headerUserAgent)
    response should be('left)
  }

  "Repos >> Get" should "return the expected name when valid repo is provided" in {
    val response =
      repos.get(accessToken, headerUserAgent, validRepoOwner, validRepoName)
    response should be('right)

    response.toOption map { r ⇒
      r.result.name shouldBe validRepoName
      r.statusCode shouldBe okStatusCode
    }
  }
  it should "return error when an invalid repo name is passed" in {
    val response =
      repos.get(accessToken, headerUserAgent, validRepoOwner, invalidRepoName)
    response should be('left)
  }

  "Repos >> ListOrgRepos" should "return the expected repos when a valid org is provided" in {
    val response = repos.listOrgRepos(
      accessToken,
      headerUserAgent,
      validRepoOwner,
      None,
      Option(Pagination(validPage, validPerPage)))
    response should be('right)

    response.toOption map { r ⇒
      r.result.nonEmpty shouldBe true
      r.statusCode shouldBe okStatusCode
    }
  }
  it should "return an empty list of repos for invalid page parameter" in {
    val response = repos.listOrgRepos(
      accessToken,
      headerUserAgent,
      validRepoOwner,
      None,
      Option(Pagination(invalidPage, validPerPage)))
    response should be('right)

    response.toOption map { r ⇒
      r.result.isEmpty shouldBe true
      r.statusCode shouldBe okStatusCode
    }
  }
  it should "return error when an invalid org is passed" in {
    val response =
      repos.listOrgRepos(accessToken, headerUserAgent, invalidRepoName)
    response should be('left)
  }

  "Repos >> GetContents" should "return the expected contents when valid repo and a valid file path is provided" in {
    val response =
      repos.getContents(accessToken, headerUserAgent, validRepoOwner, validRepoName, validFilePath)
    response should be('right)

    response.toOption map { r ⇒
      r.result.head.path shouldBe validFilePath
      r.result.tail.isEmpty shouldBe true
      r.statusCode shouldBe okStatusCode
    }
  }
  it should "return the expected contents when valid repo and a valid dir path is provided" in {
    val response =
      repos.getContents(accessToken, headerUserAgent, validRepoOwner, validRepoName, validDirPath)
    response should be('right)

    response.toOption map { r ⇒
      r.result.head.path.startsWith(validDirPath) shouldBe true
      r.result.tail.nonEmpty shouldBe true
      r.statusCode shouldBe okStatusCode
    }
  }
  it should "return the expected contents when valid repo and a valid symlink path is provided" in {
    val response =
      repos.getContents(
        accessToken,
        headerUserAgent,
        validRepoOwner,
        validRepoName,
        validSymlinkPath)
    response should be('right)

    response.toOption map { r ⇒
      r.result.head.path shouldBe validSymlinkPath
      r.result.tail.isEmpty shouldBe true
      r.statusCode shouldBe okStatusCode
    }
  }
  it should "return the expected contents when valid repo and a valid submodule path is provided" in {
    val response =
      repos.getContents(
        accessToken,
        headerUserAgent,
        validRepoOwner,
        validRepoName,
        validSubmodulePath)
    response should be('right)

    response.toOption map { r ⇒
      r.result.head.path shouldBe validSubmodulePath
      r.result.tail.isEmpty shouldBe true
      r.statusCode shouldBe okStatusCode
    }
  }
  it should "return error when an invalid repo name is passed" in {
    val response =
      repos.get(accessToken, headerUserAgent, validRepoOwner, invalidRepoName)
    response should be('left)
  }

  "Repos >> ListCommits" should "return the expected list of commits for valid data" in {
    val response = repos.listCommits(
      accessToken = accessToken,
      headers = headerUserAgent,
      owner = validRepoOwner,
      repo = validRepoName,
      pagination = Option(Pagination(validPage, validPerPage))
    )
    response should be('right)

    response.toOption map { r ⇒
      r.result.nonEmpty shouldBe true
      r.statusCode shouldBe okStatusCode
    }
  }
  it should "return an empty list of commits for invalid page parameter" in {
    val response = repos.listCommits(
      accessToken = accessToken,
      headers = headerUserAgent,
      owner = validRepoOwner,
      repo = validRepoName,
      pagination = Option(Pagination(invalidPage, validPerPage))
    )

    response should be('right)

    response.toOption map { r ⇒
      r.result.isEmpty shouldBe true
      r.statusCode shouldBe okStatusCode
    }

  }
  it should "return error for invalid repo name" in {
    val response = repos.listCommits(accessToken, headerUserAgent, validRepoOwner, invalidRepoName)
    response should be('left)
  }

  "Repos >> ListContributors" should "return the expected list of contributors for valid data" in {
    val response = repos.listContributors(
      accessToken = accessToken,
      headers = headerUserAgent,
      owner = validRepoOwner,
      repo = validRepoName
    )

    response should be('right)

    response.toOption map { r ⇒
      r.result shouldNot be(empty)
      r.statusCode shouldBe okStatusCode
    }
  }
  it should "return the expected list of contributors for valid data, including a valid anon parameter" in {
    val response = repos.listContributors(
      accessToken = accessToken,
      headers = headerUserAgent,
      owner = validRepoOwner,
      repo = validRepoName,
      anon = Option(validAnonParameter)
    )

    response should be('right)
    response.toOption map { r ⇒
      r.result shouldNot be(empty)
      r.statusCode shouldBe okStatusCode
    }
  }
  it should "return an empty list of contributors for invalid anon parameter" in {
    val response = repos.listContributors(
      accessToken = accessToken,
      headers = headerUserAgent,
      owner = validRepoOwner,
      repo = validRepoName,
      anon = Some(invalidAnonParameter)
    )

    response should be('right)

    response.toOption map { r ⇒
      r.result shouldBe empty
      r.statusCode shouldBe okStatusCode
    }
  }
  it should "return error for invalid repo name" in {
    val response =
      repos.listContributors(accessToken, headerUserAgent, validRepoOwner, invalidRepoName)
    response should be('left)
  }

  "Repos >> CreateRelease" should "return the created release" in {
    val response =
      repos.createRelease(
        accessToken,
        headerUserAgent,
        validRepoOwner,
        validRepoName,
        validTagTitle,
        validTagTitle,
        validNote)
    response should be('right)

    response.toOption map { r =>
      r.statusCode shouldBe createdStatusCode
    }
  }
  it should "return error Left for non authenticated request" in {
    val response =
      repos.createRelease(
        None,
        headerUserAgent,
        validRepoOwner,
        validRepoName,
        validTagTitle,
        validTagTitle,
        validNote)
    response should be('left)
  }

  "Repos >> GetStatus" should "return the expected combined status when a valid ref is provided" in {
    val response =
      repos.getStatus(accessToken, headerUserAgent, validRepoOwner, validRepoName, validRefSingle)
    response should be('right)

    response.toOption map { r ⇒
      r.statusCode shouldBe okStatusCode
    }
  }
  it should "return an error if no tokens are provided" in {
    val response =
      repos.getStatus(None, headerUserAgent, validRepoOwner, validRepoName, validRefSingle)
    response should be('left)
  }
  it should "return an error if an invalid ref is passed" in {
    val response =
      repos.getStatus(accessToken, headerUserAgent, validRepoOwner, validRepoName, invalidRef)
    response should be('left)
  }

  "Repos >> ListStatus" should "return the expected statuses when a valid ref is provided" in {
    val response = repos.listStatuses(
      accessToken,
      headerUserAgent,
      validRepoOwner,
      validRepoName,
      validRefSingle)
    response should be('right)

    response.toOption map { r ⇒
      r.result.nonEmpty shouldBe true
      r.statusCode shouldBe okStatusCode
    }
  }
  it should "return an error if no tokens are provided" in {
    val response =
      repos.listStatuses(None, headerUserAgent, validRepoOwner, validRepoName, validRefSingle)
    response should be('left)
  }
  it should "return an empty list when an invalid ref is passed" in {
    val response =
      repos.listStatuses(accessToken, headerUserAgent, validRepoOwner, validRepoName, invalidRef)
    response should be('right)

    response.toOption map { r ⇒
      r.result.isEmpty shouldBe true
      r.statusCode shouldBe okStatusCode
    }
  }

  "Repos >> CreateStatus" should "return the created status if a valid sha is provided" in {
    val response = repos.createStatus(
      accessToken,
      headerUserAgent,
      validRepoOwner,
      validRepoName,
      validCommitSha,
      validStatusState,
      None,
      None,
      None)
    response should be('right)

    response.toOption map { r =>
      r.statusCode shouldBe createdStatusCode
    }
  }
  it should "return an error if no tokens are provided" in {
    val response = repos.createStatus(
      None,
      headerUserAgent,
      validRepoOwner,
      validRepoName,
      validCommitSha,
      validStatusState,
      None,
      None,
      None)
    response should be('left)
  }
  it should "return an error when an invalid sha is passed" in {
    val response = repos.createStatus(
      accessToken,
      headerUserAgent,
      validRepoOwner,
      validRepoName,
      invalidCommitSha,
      validStatusState,
      None,
      None,
      None)
    response should be('left)
  }

  "Users >> Get" should "return the expected login for a valid username" in {
    val response = users.get(accessToken, headerUserAgent, validUsername)

    response should be('right)
    response.toOption map { r ⇒
      r.result.login shouldBe validUsername
      r.statusCode shouldBe okStatusCode
    }
  }
  it should "return error on Left for invalid username" in {
    val response = users.get(accessToken, headerUserAgent, invalidUsername)
    response should be('left)
  }
  "Users >> GetAuth" should "return the expected login for a valid accessToken" in {
    val response = users.getAuth(accessToken, headerUserAgent)
    response should be('right)

    response.toOption map { r ⇒
      r.result.login shouldBe validUsername
      r.statusCode shouldBe okStatusCode
    }
  }
  it should "return error on Left when no accessToken is provided" in {
    val response = users.getAuth(None, headerUserAgent)
    response should be('left)
  }

  "Users >> GetUsers" should "return users for a valid since value" in {
    val response = users.getUsers(accessToken, headerUserAgent, validSinceInt)
    response should be('right)

    response.toOption map { r ⇒
      r.result.nonEmpty shouldBe true
      r.statusCode shouldBe okStatusCode
    }
  }
  it should "return an empty list when a invalid since value is provided" in {
    val response =
      users.getUsers(accessToken, headerUserAgent, invalidSinceInt)
    response should be('right)

    response.toOption map { r ⇒
      r.result.isEmpty shouldBe true
      r.statusCode shouldBe okStatusCode
    }
  }

  "Gists >> PostGist" should "return the provided gist for a valid request" in {
    val response =
      gists.newGist(
        validGistDescription,
        validGistPublic,
        Map(validGistFilename -> GistFile(validGistFileContent)),
        headerUserAgent,
        accessToken)
    response should be('right)

    response.toOption map { r ⇒
      r.statusCode shouldBe createdStatusCode
    }
  }

  "GitData >> GetReference" should "return the single reference" in {
    val response =
      gitData.reference(
        accessToken,
        headerUserAgent,
        validRepoOwner,
        validRepoName,
        validRefSingle)
    response should be('right)

    response.toOption map { r =>
      r.statusCode shouldBe okStatusCode
    }
  }
  it should "return multiple references" in {
    val response =
      gitData.reference(
        accessToken,
        headerUserAgent,
        validRepoOwner,
        validRepoName,
        validRefMultiple)
    response should be('right)

    response.toOption map { r =>
      r.statusCode shouldBe okStatusCode
    }
  }
  it should "return error Left for non existent reference" in {
    val response =
      gitData.reference(accessToken, headerUserAgent, validRepoOwner, validRepoName, invalidRef)
    response should be('left)
  }

  "GitData >> CreateReference" should "return the single reference" in {
    val response =
      gitData.createReference(
        accessToken,
        headerUserAgent,
        validRepoOwner,
        validRepoName,
        s"refs/$validRefSingle",
        validCommitSha)
    response should be('right)

    response.toOption map { r =>
      r.statusCode shouldBe createdStatusCode
    }
  }
  it should "return error Left for non authenticated request" in {
    val response =
      gitData.updateReference(
        None,
        headerUserAgent,
        validRepoOwner,
        validRepoName,
        validRefSingle,
        validCommitSha)
    response should be('left)
  }

  "GitData >> UpdateReference" should "return the single reference" in {
    val response =
      gitData.updateReference(
        accessToken,
        headerUserAgent,
        validRepoOwner,
        validRepoName,
        validRefSingle,
        validCommitSha)
    response should be('right)

    response.toOption map { r =>
      r.statusCode shouldBe okStatusCode
    }
  }
  it should "return error Left for non authenticated request" in {
    val response =
      gitData.updateReference(
        None,
        headerUserAgent,
        validRepoOwner,
        validRepoName,
        validRefSingle,
        validCommitSha)
    response should be('left)
  }

  "GitData >> GetCommit" should "return the single commit" in {
    val response =
      gitData.commit(accessToken, headerUserAgent, validRepoOwner, validRepoName, validCommitSha)
    response should be('right)

    response.toOption map { r =>
      r.statusCode shouldBe okStatusCode
    }
  }
  it should "return error Left for non existent commit" in {
    val response =
      gitData.commit(accessToken, headerUserAgent, validRepoOwner, validRepoName, invalidCommitSha)
    response should be('left)
  }

  "GitData >> CreateCommit" should "return the single commit" in {
    val response =
      gitData.createCommit(
        accessToken,
        headerUserAgent,
        validRepoOwner,
        validRepoName,
        validNote,
        validTreeSha,
        List(validCommitSha))
    response should be('right)

    response.toOption map { r =>
      r.statusCode shouldBe createdStatusCode
    }
  }
  it should "return error Left for non authenticated request" in {
    val response =
      gitData.createCommit(
        None,
        headerUserAgent,
        validRepoOwner,
        validRepoName,
        validNote,
        validTreeSha,
        List(validCommitSha))
    response should be('left)
  }

  "GitData >> CreateBlob" should "return the created blob" in {
    val response =
      gitData.createBlob(accessToken, headerUserAgent, validRepoOwner, validRepoName, validNote)
    response should be('right)

    response.toOption map { r =>
      r.statusCode shouldBe createdStatusCode
    }
  }
  it should "return error Left for non authenticated request" in {
    val response =
      gitData.createBlob(None, headerUserAgent, validRepoOwner, validRepoName, validNote)
    response should be('left)
  }

  "GitData >> CreateTree" should "return the created tree" in {
    val response =
      gitData.createTree(
        accessToken,
        headerUserAgent,
        validRepoOwner,
        validRepoName,
        Some(validTreeSha),
        treeDataList)
    response should be('right)

    response.toOption map { r =>
      r.statusCode shouldBe createdStatusCode
    }
  }
  it should "return error Left for non authenticated request" in {
    val response =
      gitData.createTree(
        None,
        headerUserAgent,
        validRepoOwner,
        validRepoName,
        Some(validTreeSha),
        treeDataList)
    response should be('left)
  }

  "PullRequests >> List" should "return the expected pull request list when valid repo is provided" in {
    val response =
      pullRequests.list(accessToken, headerUserAgent, validRepoOwner, validRepoName)
    response should be('right)

    response.toOption map { r ⇒
      r.result.nonEmpty shouldBe true
      r.statusCode shouldBe okStatusCode
    }
  }
  it should "return error when an invalid repo name is passed" in {
    val response =
      pullRequests.list(accessToken, headerUserAgent, validRepoOwner, invalidRepoName)
    response should be('left)
  }

  "PullRequests >> ListFiles" should "return the expected files when a valid repo is provided" in {
    val response = pullRequests.listFiles(
      accessToken,
      headerUserAgent,
      validRepoOwner,
      validRepoName,
      validPullRequestNumber)
    response should be('right)

    response.toOption map { r ⇒
      r.result.nonEmpty shouldBe true
      r.statusCode shouldBe okStatusCode
    }
  }
  it should "return an error when an invalid repo name is passed" in {
    val response = pullRequests.listFiles(
      accessToken,
      headerUserAgent,
      validRepoOwner,
      invalidRepoName,
      validPullRequestNumber)
    response should be('left)
  }

  "PullRequests >> Create PullRequestData" should "return the pull request when a valid data is provided" in {
    val response = pullRequests.create(
      accessToken,
      headerUserAgent,
      validRepoOwner,
      validRepoName,
      validNewPullRequestData,
      validHead,
      validBase)
    response should be('right)
  }
  it should "return an error when an invalid data is passed" in {
    val response = pullRequests.create(
      accessToken,
      headerUserAgent,
      validRepoOwner,
      validRepoName,
      invalidNewPullRequestData,
      invalidHead,
      invalidBase)
    response should be('left)
  }

  "PullRequests >> Create PullRequestIssue" should "return the pull request when a valid data is provided" in {
    val response = pullRequests.create(
      accessToken,
      headerUserAgent,
      validRepoOwner,
      validRepoName,
      validNewPullRequestIssue,
      validHead,
      validBase)
    response should be('right)
  }
  it should "return an error when an invalid data is passed" in {
    val response = pullRequests.create(
      accessToken,
      headerUserAgent,
      validRepoOwner,
      validRepoName,
      invalidNewPullRequestIssue,
      invalidHead,
      invalidBase)
    response should be('left)
  }

  "PullRequests >> List PullRequestReviews" should "return a list of reviews when valid data is provided" in {
    val response = pullRequests.listReviews(
      accessToken,
      headerUserAgent,
      validRepoOwner,
      validRepoName,
      validPullRequestNumber)
    response should be('right)
  }
  it should "return an error when invalid data is passed" in {
    val response = pullRequests.listReviews(
      accessToken,
      headerUserAgent,
      validRepoOwner,
      invalidRepoName,
      validPullRequestNumber)
    response should be('left)
  }

  "PullRequests >> Get PullRequestReview" should "return a single review when valid data is provided" in {
    val response = pullRequests.getReview(
      accessToken,
      headerUserAgent,
      validRepoOwner,
      validRepoName,
      validPullRequestNumber,
      validPullRequestReviewNumber)
    response should be('right)
  }
  it should "return an error when invalid data is passed" in {
    val response = pullRequests.getReview(
      accessToken,
      headerUserAgent,
      validRepoOwner,
      invalidRepoName,
      validPullRequestNumber,
      validPullRequestReviewNumber)
    response should be('left)
  }

  "Issues >> List" should "return the expected issues when a valid owner/repo is provided" in {
    val response =
      issues.list(accessToken, headerUserAgent, validRepoOwner, validRepoName)
    response should be('right)

    response.toOption map { r ⇒
      r.result.nonEmpty shouldBe true
      r.statusCode shouldBe okStatusCode
    }
  }
  it should "return an error if invalid data is provided" in {
    val response =
      issues.list(accessToken, headerUserAgent, validRepoOwner, invalidRepoName)
    response should be('left)
  }
  it should "return an error if no tokens are provided" in {
    val response =
      issues.list(None, headerUserAgent, validRepoOwner, validRepoName)
    response should be('left)
  }

  "Issues >> Create" should "return the created issue if valid data is provided" in {
    val response = issues.create(
      accessToken,
      headerUserAgent,
      validRepoOwner,
      validRepoName,
      validIssueTitle,
      validIssueBody,
      None,
      List.empty,
      List.empty)
    response should be('right)

    response.toOption map { r =>
      r.statusCode shouldBe createdStatusCode
    }
  }
  it should "return an error if invalid data is provided" in {
    val response = issues.create(
      accessToken,
      headerUserAgent,
      validRepoOwner,
      invalidRepoName,
      validIssueTitle,
      validIssueBody,
      None,
      List.empty,
      List.empty)
    response should be('left)
  }
  it should "return an error if no tokens are provided" in {
    val response = issues.create(
      None,
      headerUserAgent,
      validRepoOwner,
      validRepoName,
      validIssueTitle,
      validIssueBody,
      None,
      List.empty,
      List.empty)
    response should be('left)
  }

  "Issues >> Edit" should "return the edited issue if valid data is provided" in {
    val response = issues.edit(
      accessToken,
      headerUserAgent,
      validRepoOwner,
      validRepoName,
      validIssueNumber,
      validIssueState,
      validIssueTitle,
      validIssueBody,
      None,
      List.empty,
      List.empty)
    response should be('right)

    response.toOption map { r =>
      r.statusCode shouldBe okStatusCode
    }
  }
  it should "return an error if invalid data is provided" in {
    val response = issues.edit(
      accessToken,
      headerUserAgent,
      validRepoOwner,
      invalidRepoName,
      validIssueNumber,
      validIssueState,
      validIssueTitle,
      validIssueBody,
      None,
      List.empty,
      List.empty)
    response should be('left)
  }
  it should "return an error if no tokens are provided" in {
    val response = issues.edit(
      None,
      headerUserAgent,
      validRepoOwner,
      validRepoName,
      validIssueNumber,
      validIssueState,
      validIssueTitle,
      validIssueBody,
      None,
      List.empty,
      List.empty)
    response should be('left)
  }

  "Issues >> Search" should "return the search result if valid data is provided" in {
    val response = issues.search(accessToken, headerUserAgent, validSearchQuery, List.empty)
    response should be('right)

    response.toOption map { r =>
      r.statusCode shouldBe okStatusCode
    }
  }
  it should "return an empty result if a search query matching nothing is provided" in {
    val response =
      issues.search(accessToken, headerUserAgent, nonExistentSearchQuery, List.empty)
    response should be('right)

    response.toOption map { r ⇒
      r.result.items.isEmpty shouldBe true
      r.statusCode shouldBe okStatusCode
    }
  }

  "Issues >> Create a Comment" should "return the comment created when a valid issue number is provided" in {
    val response =
      issues.createComment(
        accessToken,
        headerUserAgent,
        validRepoOwner,
        validRepoName,
        validIssueNumber,
        validCommentBody)
    response should be('right)
  }
  it should "return an error when an valid issue number is passed without authorization" in {
    val response =
      issues.createComment(
        None,
        headerUserAgent,
        validRepoOwner,
        validRepoName,
        validIssueNumber,
        validCommentBody)
    response should be('left)
  }
  it should "return an error when an invalid issue number is passed" in {
    val response =
      issues.createComment(
        accessToken,
        headerUserAgent,
        validRepoOwner,
        validRepoName,
        invalidIssueNumber,
        validCommentBody)
    response should be('left)
  }

  "Issues >> Edit a Comment" should "return the edited comment when a valid comment id is provided" in {
    val response =
      issues.editComment(
        accessToken,
        headerUserAgent,
        validRepoOwner,
        validRepoName,
        validCommentId,
        validCommentBody)
    response should be('right)
  }
  it should "return an error when an valid comment id is passed without authorization" in {
    val response =
      issues.editComment(
        None,
        headerUserAgent,
        validRepoOwner,
        validRepoName,
        validCommentId,
        validCommentBody)
    response should be('left)
  }
  it should "return an error when an invalid comment id is passed" in {
    val response =
      issues.editComment(
        accessToken,
        headerUserAgent,
        validRepoOwner,
        validRepoName,
        invalidCommentId,
        validCommentBody)
    response should be('left)
  }

  "Issues >> Delete a Comment" should "return deleted status when a valid comment id is provided" in {
    val response =
      issues.deleteComment(
        accessToken,
        headerUserAgent,
        validRepoOwner,
        validRepoName,
        validCommentId)
    response should be('right)
  }
  it should "return an error when an valid comment id is passed without authorization" in {
    val response =
      issues.deleteComment(None, headerUserAgent, validRepoOwner, validRepoName, validCommentId)
    response should be('left)
  }
  it should "return an error when an invalid comment id is passed" in {
    val response =
      issues.deleteComment(
        accessToken,
        headerUserAgent,
        validRepoOwner,
        validRepoName,
        invalidCommentId)
    response should be('left)
  }

  "Activities >> Set a Thread Subscription" should "return the subscription when a valid thread id is provided" in {
    val response =
      activities.setThreadSub(accessToken, headerUserAgent, validThreadId, true, false)
    response should be('right)
  }
  it should "return an error when an valid thread id is passed without authorization" in {
    val response =
      activities.setThreadSub(None, headerUserAgent, validThreadId, true, false)
    response should be('left)
  }
  it should "return an error when an invalid thread id is passed" in {
    val response =
      activities.setThreadSub(accessToken, headerUserAgent, invalidThreadId, true, false)
    response should be('left)
  }

  "Activities >> ListStargazers" should "return the expected list of stargazers for valid data" in {
    val response = activities.listStargazers(
      accessToken = accessToken,
      headers = headerUserAgent,
      owner = validRepoOwner,
      repo = validRepoName,
      timeline = false,
      pagination = Option(Pagination(validPage, validPerPage))
    )
    response should be('right)

    response.toOption map { r ⇒
      r.result.nonEmpty shouldBe true
      r.statusCode shouldBe okStatusCode
    }
  }
  it should "return an empty list of stargazers for invalid page parameter" in {
    val response = activities.listStargazers(
      accessToken = accessToken,
      headers = headerUserAgent,
      owner = validRepoOwner,
      repo = validRepoName,
      timeline = false,
      pagination = Option(Pagination(invalidPage, validPerPage))
    )

    response should be('right)

    response.toOption map { r ⇒
      r.result.isEmpty shouldBe true
      r.statusCode shouldBe okStatusCode
    }
  }
  it should "return error for invalid repo name" in {
    val response = activities.listStargazers(
      accessToken,
      headerUserAgent,
      validRepoOwner,
      invalidRepoName,
      false)
    response should be('left)
  }

  "Activities >> ListStarredRepositories" should "return the expected list of repos" in {
    val response = activities.listStarredRepositories(
      accessToken = accessToken,
      headers = headerUserAgent,
      username = validUsername,
      timeline = false,
      pagination = Option(Pagination(validPage, validPerPage))
    )
    response should be('right)

    response.toOption map { r ⇒
      r.result.nonEmpty shouldBe true
      r.statusCode shouldBe okStatusCode
    }
  }
  it should "return an empty list of repos for invalid page parameter" in {
    val response = activities.listStarredRepositories(
      accessToken = accessToken,
      headers = headerUserAgent,
      username = validUsername,
      timeline = false,
      pagination = Option(Pagination(invalidPage, validPerPage))
    )

    response should be('right)

    response.toOption map { r ⇒
      r.result.isEmpty shouldBe true
      r.statusCode shouldBe okStatusCode
    }
  }
  it should "return error for invalid username" in {
    val response =
      activities.listStarredRepositories(accessToken, headerUserAgent, invalidUsername, false)
    response should be('left)
  }

  "Organizations >> ListMembers" should "return the expected list of users" in {
    val response = organizations.listMembers(
      accessToken = accessToken,
      headers = headerUserAgent,
      org = validRepoOwner,
      pagination = Option(Pagination(validPage, validPerPage))
    )
    response should be('right)

    response.toOption map { r ⇒
      r.result.nonEmpty shouldBe true
      r.statusCode shouldBe okStatusCode
    }
  }
  it should "return an empty list of users for an invalid page parameter" in {
    val response = organizations.listMembers(
      accessToken = accessToken,
      headers = headerUserAgent,
      org = validRepoOwner,
      pagination = Option(Pagination(invalidPage, validPerPage))
    )

    response should be('right)

    response.toOption map { r ⇒
      r.result.isEmpty shouldBe true
      r.statusCode shouldBe okStatusCode
    }
  }
  it should "return error for an invalid organization" in {
    val response = organizations.listMembers(accessToken, headerUserAgent, invalidUsername)
    response should be('left)
  }

}
