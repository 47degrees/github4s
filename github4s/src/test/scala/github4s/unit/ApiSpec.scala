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

/*
package github4s.unit

import cats.effect.IO
import github4s.interpreters._
import github4s.free.domain.{EditGistFile, GistFile, Pagination}
import github4s.utils.{DummyGithubUrls, MockGithubApiServer, TestUtilsJVM}
import cats.Id
import github4s.utils.Integration
import org.scalatest.EitherValues
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class ApiSpec
    extends AnyFlatSpec
    with Matchers
    with TestUtilsJVM
    with MockGithubApiServer
    with DummyGithubUrls
    with EitherValues {

  val auth          = new AuthInterpreter[IO]
  val repos         = new Repos[Id]
  val users         = new Users[Id]
  val gists         = new Gists[Id]
  val gitData       = new GitData[Id]
  val pullRequests  = new PullRequests[Id]
  val issues        = new Issues[Id]
  val activities    = new Activities[Id]
  val organizations = new Organizations[Id]

  "Auth >> NewAuth" should "return a valid token when valid credential is provided" in {
    val response = auth.newAuth(
      validUsername,
      "",
      validScopes,
      validNote,
      validClientId,
      "",
      headers = headerUserAgent)

    response.right.value.result.token should not be empty
    response.right.value.statusCode shouldBe okStatusCode
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
    response.isLeft shouldBe true
  }

  "Auth >> AuthorizeUrl" should "return the expected URL for valid username" in {
    val response =
      auth.authorizeUrl(validClientId, validRedirectUri, validScopes)

    response.right.value.result.url.contains(validRedirectUri) shouldBe true
    response.right.value.statusCode shouldBe okStatusCode
  }

  "Auth >> GetAccessToken" should "return a valid access_token when a valid code is provided" in {
    val response =
      auth.getAccessToken("", "", validCode, "", "", headers = headerUserAgent)

    response.right.value.result.access_token should not be empty
    response.right.value.statusCode shouldBe okStatusCode
  }
  it should "return error on Left when invalid code is provided" in {
    val response = auth.getAccessToken("", "", invalidCode, "", "", headers = headerUserAgent)
    response.isLeft shouldBe true
  }

  "Repos >> Get" should "return the expected name when valid repo is provided" in {
    val response =
      repos.get(accessToken, headerUserAgent, validRepoOwner, validRepoName)

    response.right.value.result.name shouldBe validRepoName
    response.right.value.statusCode shouldBe okStatusCode
  }
  it should "return error when an invalid repo name is passed" in {
    val response =
      repos.get(accessToken, headerUserAgent, validRepoOwner, invalidRepoName)
    response.isLeft shouldBe true
  }

  "Repos >> ListOrgRepos" should "return the expected repos when a valid org is provided" in {
    val response = repos.listOrgRepos(
      accessToken,
      headerUserAgent,
      validRepoOwner,
      None,
      Option(Pagination(validPage, validPerPage)))

    response.right.value.result should not be empty
    response.right.value.statusCode shouldBe okStatusCode
  }
  it should "return an empty list of repos for invalid page parameter" in {
    val response = repos.listOrgRepos(
      accessToken,
      headerUserAgent,
      validRepoOwner,
      None,
      Option(Pagination(invalidPage, validPerPage)))

    response.right.value.result shouldBe empty
    response.right.value.statusCode shouldBe okStatusCode
  }
  it should "return error when an invalid org is passed" in {
    val response =
      repos.listOrgRepos(accessToken, headerUserAgent, invalidRepoName)
    response.isLeft shouldBe true
  }

  "Repos >> ListUserRepos" should "return the expected repos when a valid user is provided" in {
    val response = repos.listUserRepos(
      accessToken,
      headerUserAgent,
      validUsername,
      None,
      Option(Pagination(validPage, validPerPage)))

    response.right.value.result should not be empty
    response.right.value.statusCode shouldBe okStatusCode
  }
  it should "return an empty list of repos for invalid page parameter" in {
    val response = repos.listUserRepos(
      accessToken,
      headerUserAgent,
      validUsername,
      None,
      Option(Pagination(invalidPage, validPerPage)))

    response.right.value.result shouldBe empty
    response.right.value.statusCode shouldBe okStatusCode
  }
  it should "return error when an invalid user is passed" in {
    val response =
      repos.listUserRepos(accessToken, headerUserAgent, invalidUsername)
    response.isLeft shouldBe true
  }

  "Repos >> GetContents" should "return the expected contents when valid repo and a valid file path is provided" in {
    val response =
      repos.getContents(accessToken, headerUserAgent, validRepoOwner, validRepoName, validFilePath)

    response.right.value.result.head.path shouldBe validFilePath
    response.right.value.result.tail shouldBe empty
    response.right.value.statusCode shouldBe okStatusCode
  }
  it should "return the expected contents when valid repo and a valid dir path is provided" in {
    val response =
      repos.getContents(accessToken, headerUserAgent, validRepoOwner, validRepoName, validDirPath)

    response.right.value.result.head.path.startsWith(validDirPath) shouldBe true
    response.right.value.result.tail should not be empty
    response.right.value.statusCode shouldBe okStatusCode
  }
  it should "return the expected contents when valid repo and a valid symlink path is provided" in {
    val response =
      repos.getContents(
        accessToken,
        headerUserAgent,
        validRepoOwner,
        validRepoName,
        validSymlinkPath)

    response.right.value.result.head.path shouldBe validSymlinkPath
    response.right.value.result.tail shouldBe empty
    response.right.value.statusCode shouldBe okStatusCode
  }
  it should "return the expected contents when valid repo and a valid submodule path is provided" in {
    val response =
      repos.getContents(
        accessToken,
        headerUserAgent,
        validRepoOwner,
        validRepoName,
        validSubmodulePath)

    response.right.value.result.head.path shouldBe validSubmodulePath
    response.right.value.result.tail shouldBe empty
    response.right.value.statusCode shouldBe okStatusCode
  }
  it should "return error when an invalid repo name is passed" in {
    val response =
      repos.get(accessToken, headerUserAgent, validRepoOwner, invalidRepoName)
    response.isLeft shouldBe true
  }

  "Repos >> ListCommits" should "return the expected list of commits for valid data" in {
    val response = repos.listCommits(
      accessToken = accessToken,
      headers = headerUserAgent,
      owner = validRepoOwner,
      repo = validRepoName,
      pagination = Option(Pagination(validPage, validPerPage))
    )

    response.right.value.result should not be empty
    response.right.value.statusCode shouldBe okStatusCode
  }
  it should "return an empty list of commits for invalid page parameter" in {
    val response = repos.listCommits(
      accessToken = accessToken,
      headers = headerUserAgent,
      owner = validRepoOwner,
      repo = validRepoName,
      pagination = Option(Pagination(invalidPage, validPerPage))
    )

    response.right.value.result shouldBe empty
    response.right.value.statusCode shouldBe okStatusCode
  }
  it should "return error for invalid repo name" in {
    val response = repos.listCommits(accessToken, headerUserAgent, validRepoOwner, invalidRepoName)
    response.isLeft shouldBe true
  }

  "Repos >> ListContributors" should "return the expected list of contributors for valid data" in {
    val response = repos.listContributors(
      accessToken = accessToken,
      headers = headerUserAgent,
      owner = validRepoOwner,
      repo = validRepoName
    )

    response.right.value.result shouldNot be(empty)
    response.right.value.statusCode shouldBe okStatusCode
  }
  it should "return the expected list of contributors for valid data, including a valid anon parameter" in {
    val response = repos.listContributors(
      accessToken = accessToken,
      headers = headerUserAgent,
      owner = validRepoOwner,
      repo = validRepoName,
      anon = Option(validAnonParameter)
    )

    response.right.value.result shouldNot be(empty)
    response.right.value.statusCode shouldBe okStatusCode
  }
  it should "return an empty list of contributors for invalid anon parameter" in {
    val response = repos.listContributors(
      accessToken = accessToken,
      headers = headerUserAgent,
      owner = validRepoOwner,
      repo = validRepoName,
      anon = Some(invalidAnonParameter)
    )

    response.right.value.result shouldBe empty
    response.right.value.statusCode shouldBe okStatusCode
  }
  it should "return error for invalid repo name" in {
    val response =
      repos.listContributors(accessToken, headerUserAgent, validRepoOwner, invalidRepoName)
    response.isLeft shouldBe true
  }

  "Repos >> CreateRelease" should "return the created release" taggedAs Integration in {
    val response =
      repos.createRelease(
        accessToken,
        headerUserAgent,
        validRepoOwner,
        validRepoName,
        validTagTitle,
        validTagTitle,
        validNote)

    response.right.value.statusCode shouldBe createdStatusCode
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
    response.isLeft shouldBe true
  }

  "Repos >> GetStatus" should "return the expected combined status when a valid ref is provided" taggedAs Integration in {
    val response =
      repos.getStatus(accessToken, headerUserAgent, validRepoOwner, validRepoName, validRefSingle)

    response.right.value.statusCode shouldBe okStatusCode
  }
  it should "return an error if no tokens are provided" in {
    val response =
      repos.getStatus(None, headerUserAgent, validRepoOwner, validRepoName, validRefSingle)
    response.isLeft shouldBe true
  }
  it should "return an error if an invalid ref is passed" in {
    val response =
      repos.getStatus(accessToken, headerUserAgent, validRepoOwner, validRepoName, invalidRef)
    response.isLeft shouldBe true
  }

  "Repos >> ListStatus" should "return the expected statuses when a valid ref is provided" taggedAs Integration in {
    val response = repos.listStatuses(
      accessToken,
      headerUserAgent,
      validRepoOwner,
      validRepoName,
      validRefSingle)

    response.right.value.result should not be empty
    response.right.value.statusCode shouldBe okStatusCode
  }
  it should "return an error if no tokens are provided" in {
    val response =
      repos.listStatuses(None, headerUserAgent, validRepoOwner, validRepoName, validRefSingle)
    response.isLeft shouldBe true
  }
  it should "return an empty list when an invalid ref is passed" taggedAs Integration in {
    val response =
      repos.listStatuses(accessToken, headerUserAgent, validRepoOwner, validRepoName, invalidRef)

    response.right.value.result shouldBe empty
    response.right.value.statusCode shouldBe okStatusCode
  }

  "Repos >> CreateStatus" should "return the created status if a valid sha is provided" taggedAs Integration in {
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

    response.right.value.statusCode shouldBe createdStatusCode
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
    response.isLeft shouldBe true
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
    response.isLeft shouldBe true
  }

  "Users >> Get" should "return the expected login for a valid username" in {
    val response = users.get(accessToken, headerUserAgent, validUsername)

    response.right.value.result.login shouldBe validUsername
    response.right.value.statusCode shouldBe okStatusCode
  }
  it should "return error on Left for invalid username" in {
    val response = users.get(accessToken, headerUserAgent, invalidUsername)
    response.isLeft shouldBe true
  }
  "Users >> GetAuth" should "return the expected login for a valid accessToken" taggedAs Integration in {
    val response = users.getAuth(accessToken, headerUserAgent)

    response.right.value.result.login shouldBe validUsername
    response.right.value.statusCode shouldBe okStatusCode
  }
  it should "return error on Left when no accessToken is provided" in {
    val response = users.getAuth(None, headerUserAgent)
    response.isLeft shouldBe true
  }

  "Users >> GetUsers" should "return users for a valid since value" in {
    val response = users.getUsers(accessToken, headerUserAgent, validSinceInt)

    response.right.value.result should not be empty
    response.right.value.statusCode shouldBe okStatusCode
  }
  it should "return an empty list when a invalid since value is provided" in {
    val response =
      users.getUsers(accessToken, headerUserAgent, invalidSinceInt)

    response.right.value.result shouldBe empty
    response.right.value.statusCode shouldBe okStatusCode
  }

  "Gists >> PostGist" should "return the provided gist for a valid request" taggedAs Integration in {
    val response =
      gists.newGist(
        validGistDescription,
        validGistPublic,
        Map(validGistFilename -> GistFile(validGistFileContent)),
        headerUserAgent,
        accessToken)

    response.right.value.statusCode shouldBe createdStatusCode
  }

  "Gists >> GetGist" should "return the single gist for a valid request" taggedAs Integration in {
    val response =
      gists.getGist(validGistId, sha = None, headerUserAgent, accessToken)

    response.right.value.statusCode shouldBe okStatusCode
  }

  it should "return the specific revision of gist for a valid request" taggedAs Integration in {
    val response =
      gists.getGist(validGistId, sha = Some(validGistSha), headerUserAgent, accessToken)

    response.right.value.statusCode shouldBe okStatusCode
  }

  "Gists >> EditGist" should "return the provided gist for a valid request" taggedAs Integration in {
    val response =
      gists.editGist(
        validGistId,
        validGistDescription,
        Map(
          validGistFilename -> Some(EditGistFile(validGistFileContent)),
          validGistOldFilename -> Some(
            EditGistFile(validGistFileContent, Some(validGistNewFilename))),
          validGistDeletedFilename -> None
        ),
        headerUserAgent,
        accessToken
      )

    response.right.value.statusCode shouldBe okStatusCode
  }

  "GitData >> GetReference" should "return the single reference" in {
    val response =
      gitData.reference(accessToken, headerUserAgent, validRepoOwner, validRepoName, validRefSingle)

    response.right.value.statusCode shouldBe okStatusCode
  }
  it should "return multiple references" in {
    val response =
      gitData.reference(
        accessToken,
        headerUserAgent,
        validRepoOwner,
        validRepoName,
        validRefMultiple)

    response.right.value.statusCode shouldBe okStatusCode
  }
  it should "return error Left for non existent reference" in {
    val response =
      gitData.reference(accessToken, headerUserAgent, validRepoOwner, validRepoName, invalidRef)
    response.isLeft shouldBe true
  }

  "GitData >> CreateReference" should "return the single reference" taggedAs Integration in {
    val response =
      gitData.createReference(
        accessToken,
        headerUserAgent,
        validRepoOwner,
        validRepoName,
        s"refs/$validRefSingle",
        validCommitSha)

    response.right.value.statusCode shouldBe createdStatusCode
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
    response.isLeft shouldBe true
  }

  "GitData >> UpdateReference" should "return the single reference" taggedAs Integration in {
    val response =
      gitData.updateReference(
        accessToken,
        headerUserAgent,
        validRepoOwner,
        validRepoName,
        validRefSingle,
        validCommitSha)

    response.right.value.statusCode shouldBe okStatusCode
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
    response.isLeft shouldBe true
  }

  "GitData >> GetCommit" should "return the single commit" in {
    val response =
      gitData.commit(accessToken, headerUserAgent, validRepoOwner, validRepoName, validCommitSha)

    response.right.value.statusCode shouldBe okStatusCode
  }
  it should "return error Left for non existent commit" in {
    val response =
      gitData.commit(accessToken, headerUserAgent, validRepoOwner, validRepoName, invalidCommitSha)
    response.isLeft shouldBe true
  }

  "GitData >> CreateCommit" should "return the single commit" taggedAs Integration in {
    val response =
      gitData.createCommit(
        accessToken,
        headerUserAgent,
        validRepoOwner,
        validRepoName,
        validNote,
        validTreeSha,
        List(validCommitSha))

    response.right.value.statusCode shouldBe createdStatusCode
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
    response.isLeft shouldBe true
  }

  "GitData >> CreateBlob" should "return the created blob" taggedAs Integration in {
    val response =
      gitData.createBlob(accessToken, headerUserAgent, validRepoOwner, validRepoName, validNote)

    response.right.value.statusCode shouldBe createdStatusCode
  }
  it should "return error Left for non authenticated request" in {
    val response =
      gitData.createBlob(None, headerUserAgent, validRepoOwner, validRepoName, validNote)
    response.isLeft shouldBe true
  }

  "GitData >> CreateTree" should "return the created tree" taggedAs Integration in {
    val response =
      gitData.createTree(
        accessToken,
        headerUserAgent,
        validRepoOwner,
        validRepoName,
        Some(validTreeSha),
        treeDataList)

    response.right.value.statusCode shouldBe createdStatusCode
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
    response.isLeft shouldBe true
  }

  "PullRequests >> Get" should "return the expected pull request when a valid repo is provided" in {
    val response = pullRequests.get(
      accessToken,
      headerUserAgent,
      validRepoOwner,
      validRepoName,
      validPullRequestNumber)

    response.right.value.result.id shouldBe validPullRequestNumber
    response.right.value.statusCode shouldBe okStatusCode
  }
  it should "return error when an invalid repo name is passed" in {
    val response = pullRequests.get(
      accessToken,
      headerUserAgent,
      validRepoOwner,
      invalidRepoName,
      validPullRequestNumber)
    response.isLeft shouldBe true
  }

  "PullRequests >> List" should "return the expected pull request list when a valid repo is provided" in {
    val response =
      pullRequests.list(accessToken, headerUserAgent, validRepoOwner, validRepoName)

    response.right.value.result should not be empty
    response.right.value.statusCode shouldBe okStatusCode
  }
  it should "return error when an invalid repo name is passed" in {
    val response =
      pullRequests.list(accessToken, headerUserAgent, validRepoOwner, invalidRepoName)
    response.isLeft shouldBe true
  }

  "PullRequests >> ListFiles" should "return the expected files when a valid repo is provided" in {
    val response = pullRequests.listFiles(
      accessToken,
      headerUserAgent,
      validRepoOwner,
      validRepoName,
      validPullRequestNumber)

    response.right.value.result should not be empty
    response.right.value.statusCode shouldBe okStatusCode
  }
  it should "return an error when an invalid repo name is passed" in {
    val response = pullRequests.listFiles(
      accessToken,
      headerUserAgent,
      validRepoOwner,
      invalidRepoName,
      validPullRequestNumber)
    response.isLeft shouldBe true
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
    response.isRight shouldBe true
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
    response.isLeft shouldBe true
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
    response.isRight shouldBe true
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
    response.isLeft shouldBe true
  }

  "PullRequests >> List PullRequestReviews" should "return a list of reviews when valid data is provided" taggedAs Integration in {
    val response = pullRequests.listReviews(
      accessToken,
      headerUserAgent,
      validRepoOwner,
      validRepoName,
      validPullRequestNumber)
    response.isRight shouldBe true
  }
  it should "return an error when invalid data is passed" in {
    val response = pullRequests.listReviews(
      accessToken,
      headerUserAgent,
      validRepoOwner,
      invalidRepoName,
      validPullRequestNumber)
    response.isLeft shouldBe true
  }

  "PullRequests >> Get PullRequestReview" should "return a single review when valid data is provided" taggedAs Integration in {
    val response = pullRequests.getReview(
      accessToken,
      headerUserAgent,
      validRepoOwner,
      validRepoName,
      validPullRequestNumber,
      validPullRequestReviewNumber)
    response.isRight shouldBe true
  }
  it should "return an error when invalid data is passed" in {
    val response = pullRequests.getReview(
      accessToken,
      headerUserAgent,
      validRepoOwner,
      invalidRepoName,
      validPullRequestNumber,
      validPullRequestReviewNumber)
    response.isLeft shouldBe true
  }

  "Issues >> List" should "return the expected issues when a valid owner/repo is provided" taggedAs Integration in {
    val response =
      issues.list(accessToken, headerUserAgent, validRepoOwner, validRepoName)

    response.right.value.result should not be empty
    response.right.value.statusCode shouldBe okStatusCode
  }
  it should "return an error if invalid data is provided" in {
    val response =
      issues.list(accessToken, headerUserAgent, validRepoOwner, invalidRepoName)
    response.isLeft shouldBe true
  }
  it should "return an error if no tokens are provided" in {
    val response =
      issues.list(None, headerUserAgent, validRepoOwner, validRepoName)
    response.isLeft shouldBe true
  }

  "Issues >> Get" should "return the expected issue when a valid owner/repo is provided" taggedAs Integration in {
    val response =
      issues.get(accessToken, headerUserAgent, validRepoOwner, validRepoName, validIssueNumber)

    response.right.value.statusCode shouldBe okStatusCode
  }
  it should "return an issue which is just a Pull Request" taggedAs Integration in {
    val response =
      issues.get(
        accessToken,
        headerUserAgent,
        validRepoOwner,
        validRepoName,
        validPullRequestNumber)
    response.isRight shouldBe true
  }
  it should "return an error if an invalid issue number is provided" in {
    val response =
      issues.get(accessToken, headerUserAgent, validRepoOwner, validRepoName, invalidIssueNumber)
    response.isLeft shouldBe true
  }
  it should "return an error if no tokens are provided" in {
    val response =
      issues.get(None, headerUserAgent, validRepoOwner, validRepoName, validIssueNumber)
    response.isLeft shouldBe true
  }

  "Issues >> Create" should "return the created issue if valid data is provided" taggedAs Integration in {
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

    response.right.value.statusCode shouldBe createdStatusCode
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
    response.isLeft shouldBe true
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
    response.isLeft shouldBe true
  }

  "Issues >> Edit" should "return the edited issue if valid data is provided" taggedAs Integration in {
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

    response.right.value.statusCode shouldBe okStatusCode
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
    response.isLeft shouldBe true
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
    response.isLeft shouldBe true
  }

  "Issues >> Search" should "return the search result if valid data is provided" taggedAs Integration in {
    val response = issues.search(accessToken, headerUserAgent, validSearchQuery, List.empty)

    response.right.value.statusCode shouldBe okStatusCode
  }
  it should "return an empty result if a search query matching nothing is provided" taggedAs Integration in {
    val response =
      issues.search(accessToken, headerUserAgent, nonExistentSearchQuery, List.empty)

    response.right.value.result.items shouldBe empty
    response.right.value.statusCode shouldBe okStatusCode
  }

  "Issues >> ListComments" should "return the expected issue comments when a valid issue number is provided" taggedAs Integration in {
    val response =
      issues.listComments(
        accessToken,
        headerUserAgent,
        validRepoOwner,
        validRepoName,
        validIssueNumber)

    response.right.value.result should not be empty
    response.right.value.statusCode shouldBe okStatusCode
  }
  it should "return an error if an invalid issue number is provided" in {
    val response =
      issues.listComments(
        accessToken,
        headerUserAgent,
        validRepoOwner,
        validRepoName,
        invalidIssueNumber)
    response.isLeft shouldBe true
  }
  it should "return an error if no tokens are provided" in {
    val response =
      issues.listComments(None, headerUserAgent, validRepoOwner, validRepoName, validIssueNumber)
    response.isLeft shouldBe true
  }

  "Issues >> Create a Comment" should "return the comment created when a valid issue number is provided" taggedAs Integration in {
    val response =
      issues.createComment(
        accessToken,
        headerUserAgent,
        validRepoOwner,
        validRepoName,
        validIssueNumber,
        validCommentBody)
    response.isRight shouldBe true
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
    response.isLeft shouldBe true
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
    response.isLeft shouldBe true
  }

  "Issues >> Edit a Comment" should "return the edited comment when a valid comment id is provided" taggedAs Integration in {
    val response =
      issues.editComment(
        accessToken,
        headerUserAgent,
        validRepoOwner,
        validRepoName,
        validCommentId,
        validCommentBody)
    response.isRight shouldBe true
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
    response.isLeft shouldBe true
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
    response.isLeft shouldBe true
  }

  "Issues >> Delete a Comment" should "return deleted status when a valid comment id is provided" taggedAs Integration in {
    val response =
      issues.deleteComment(
        accessToken,
        headerUserAgent,
        validRepoOwner,
        validRepoName,
        validCommentId)
    response.isRight shouldBe true
  }
  it should "return an error when an valid comment id is passed without authorization" in {
    val response =
      issues.deleteComment(None, headerUserAgent, validRepoOwner, validRepoName, validCommentId)
    response.isLeft shouldBe true
  }
  it should "return an error when an invalid comment id is passed" in {
    val response =
      issues.deleteComment(
        accessToken,
        headerUserAgent,
        validRepoOwner,
        validRepoName,
        invalidCommentId)
    response.isLeft shouldBe true
  }

  "Issues >> ListLabels" should "return the expected issue labels when a valid issue number is provided" taggedAs Integration in {
    val response =
      issues.listLabels(
        accessToken,
        headerUserAgent,
        validRepoOwner,
        validRepoName,
        validIssueNumber)

    response.right.value.result should not be empty
    response.right.value.statusCode shouldBe okStatusCode
  }
  it should "return an error if an invalid issue number is provided" in {
    val response =
      issues.listLabels(
        accessToken,
        headerUserAgent,
        validRepoOwner,
        validRepoName,
        invalidIssueNumber)
    response.isLeft shouldBe true
  }
  it should "return an error if no tokens are provided" in {
    val response =
      issues.listLabels(None, headerUserAgent, validRepoOwner, validRepoName, validIssueNumber)
    response.isLeft shouldBe true
  }

  "Issues >> RemoveLabel" should "return the removed issue labels when a valid issue number is provided" taggedAs Integration in {
    val response =
      issues.removeLabel(
        accessToken,
        headerUserAgent,
        validRepoOwner,
        validRepoName,
        validIssueNumber,
        validIssueLabel.head)

    response.right.value.result should not be empty
    response.right.value.statusCode shouldBe okStatusCode
  }
  it should "return an error if an invalid issue number is provided" in {
    val response =
      issues.removeLabel(
        accessToken,
        headerUserAgent,
        validRepoOwner,
        validRepoName,
        invalidIssueNumber,
        validIssueLabel.head)
    response.isLeft shouldBe true
  }
  it should "return an error if no tokens are provided" in {
    val response =
      issues.removeLabel(
        None,
        headerUserAgent,
        validRepoOwner,
        validRepoName,
        validIssueNumber,
        validIssueLabel.head)
    response.isLeft shouldBe true
  }

  "Issues >> AddLabels" should "return the assigned issue labels when a valid issue number is provided" taggedAs Integration in {
    val response =
      issues.addLabels(
        accessToken,
        headerUserAgent,
        validRepoOwner,
        validRepoName,
        validIssueNumber,
        validIssueLabel)

    response.right.value.result should not be empty
    response.right.value.statusCode shouldBe okStatusCode
  }
  it should "return an error if an invalid issue number is provided" in {
    val response =
      issues.addLabels(
        accessToken,
        headerUserAgent,
        validRepoOwner,
        validRepoName,
        invalidIssueNumber,
        validIssueLabel)
    response.isLeft shouldBe true
  }
  it should "return an error if no tokens are provided" in {
    val response =
      issues.addLabels(
        None,
        headerUserAgent,
        validRepoOwner,
        validRepoName,
        validIssueNumber,
        validIssueLabel)
    response.isLeft shouldBe true
  }

  "Issues >> ListAvailableAssignees" should "return the expected users" in {
    val response =
      issues.listAvailableAssignees(
        accessToken,
        headerUserAgent,
        validRepoOwner,
        validRepoName,
        pagination = Option(Pagination(validPage, validPerPage)))

    response.right.value.result should not be empty
    response.right.value.statusCode shouldBe okStatusCode
  }
  it should "return an error if an invalid repo owner is provided" in {
    val response =
      issues.listAvailableAssignees(
        accessToken,
        headerUserAgent,
        invalidRepoOwner,
        validRepoName,
        pagination = None)
    response.isLeft shouldBe true
  }
  it should "return an error if invalid repo name is provided" in {
    val response =
      issues.listAvailableAssignees(
        accessToken,
        headerUserAgent,
        validRepoOwner,
        invalidRepoName,
        pagination = None)
    response.isLeft shouldBe true
  }
  it should "return an empty list of users for an invalid page parameter" in {
    val response =
      issues.listAvailableAssignees(
        accessToken = accessToken,
        headers = headerUserAgent,
        validRepoOwner,
        validRepoName,
        pagination = Option(Pagination(invalidPage, validPerPage)))

    response.right.value.result shouldBe empty
    response.right.value.statusCode shouldBe okStatusCode
  }

  "Activities >> Set a Thread Subscription" should "return the subscription when a valid thread id is provided" taggedAs Integration in {
    val response =
      activities.setThreadSub(accessToken, headerUserAgent, validThreadId, true, false)
    response.isRight shouldBe true
  }
  it should "return an error when an valid thread id is passed without authorization" in {
    val response =
      activities.setThreadSub(None, headerUserAgent, validThreadId, true, false)
    response.isLeft shouldBe true
  }
  it should "return an error when an invalid thread id is passed" in {
    val response =
      activities.setThreadSub(accessToken, headerUserAgent, invalidThreadId, true, false)
    response.isLeft shouldBe true
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

    response.right.value.result should not be empty
    response.right.value.statusCode shouldBe okStatusCode
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

    response.right.value.result shouldBe empty
    response.right.value.statusCode shouldBe okStatusCode
  }
  it should "return error for invalid repo name" in {
    val response = activities.listStargazers(
      accessToken,
      headerUserAgent,
      validRepoOwner,
      invalidRepoName,
      false)
    response.isLeft shouldBe true
  }

  "Activities >> ListStarredRepositories" should "return the expected list of repos" in {
    val response = activities.listStarredRepositories(
      accessToken = accessToken,
      headers = headerUserAgent,
      username = validUsername,
      timeline = false,
      pagination = Option(Pagination(validPage, validPerPage))
    )

    response.right.value.result should not be empty
    response.right.value.statusCode shouldBe okStatusCode
  }
  it should "return an empty list of repos for invalid page parameter" in {
    val response = activities.listStarredRepositories(
      accessToken = accessToken,
      headers = headerUserAgent,
      username = validUsername,
      timeline = false,
      pagination = Option(Pagination(invalidPage, validPerPage))
    )

    response.right.value.result shouldBe empty
    response.right.value.statusCode shouldBe okStatusCode
  }
  it should "return error for invalid username" in {
    val response =
      activities.listStarredRepositories(accessToken, headerUserAgent, invalidUsername, false)
    response.isLeft shouldBe true
  }

  "Organizations >> ListMembers" should "return the expected list of users" in {
    val response = organizations.listMembers(
      accessToken = accessToken,
      headers = headerUserAgent,
      org = validRepoOwner,
      pagination = Option(Pagination(validPage, validPerPage))
    )

    response.right.value.result should not be empty
    response.right.value.statusCode shouldBe okStatusCode
  }
  it should "return an empty list of users for an invalid page parameter" in {
    val response = organizations.listMembers(
      accessToken = accessToken,
      headers = headerUserAgent,
      org = validRepoOwner,
      pagination = Option(Pagination(invalidPage, validPerPage))
    )

    response.right.value.result shouldBe empty
    response.right.value.statusCode shouldBe okStatusCode
  }
  it should "return error for an invalid organization" in {
    val response = organizations.listMembers(accessToken, headerUserAgent, invalidUsername)
    response.isLeft shouldBe true
  }

  "Organizations >> ListOutsideCollaborators" should "return the expected list of users" in {
    val response = organizations.listOutsideCollaborators(
      accessToken = accessToken,
      headers = headerUserAgent,
      org = validOrganizationName,
      pagination = Option(Pagination(validPage, validPerPage))
    )

    response.right.value.result should not be empty
    response.right.value.statusCode shouldBe okStatusCode
  }
  it should "return an empty list of users for an invalid page parameter" in {
    val response = organizations.listOutsideCollaborators(
      accessToken = accessToken,
      headers = headerUserAgent,
      org = validOrganizationName,
      pagination = Option(Pagination(invalidPage, validPerPage))
    )

    response.right.value.result shouldBe empty
    response.right.value.statusCode shouldBe okStatusCode
  }
  it should "return error for an invalid organization" in {
    val response =
      organizations.listOutsideCollaborators(accessToken, headerUserAgent, invalidOrganizationName)
    response.isLeft shouldBe true
  }

}
 */
