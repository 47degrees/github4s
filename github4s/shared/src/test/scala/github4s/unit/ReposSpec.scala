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

package github4s.unit

import cats.data.NonEmptyList
import cats.effect.IO
import github4s.Decoders._
import github4s.Encoders._
import github4s.domain.RepoUrlKeys.CommitComparisonResponse
import github4s.domain._
import github4s.http.HttpClient
import github4s.internal.Base64._
import github4s.interpreters.RepositoriesInterpreter
import github4s.utils.BaseSpec
import org.http4s

class ReposSpec extends BaseSpec {

  "Repos.get" should "call to httpClient.get with the right parameters" in {

    implicit val httpClientMock: HttpClient[IO] = httpClientMockGet[Repository](
      url = s"repos/$validRepoOwner/$validRepoName",
      response = IO.pure(repo)
    )

    val repos = new RepositoriesInterpreter[IO]

    repos.get(validRepoOwner, validRepoName, headerUserAgent).shouldNotFail
  }

  "Repos.listReleases" should "call to httpClient.get with the right parameters" in {

    implicit val httpClientMock: HttpClient[IO] = httpClientMockGet[List[Release]](
      url = s"repos/$validRepoOwner/$validRepoName/releases",
      response = IO.pure(List(release))
    )

    val repos = new RepositoriesInterpreter[IO]

    repos.listReleases(validRepoOwner, validRepoName, None, headers = headerUserAgent).shouldNotFail
  }

  "Repos.latestRelease" should "call to httpClient.get with the right parameters" in {

    implicit val httpClientMock: HttpClient[IO] = httpClientMockGet[Option[Release]](
      url = s"repos/$validRepoOwner/$validRepoName/releases/latest",
      response = IO.pure(Option(release))
    )

    val repos = new RepositoriesInterpreter[IO]

    repos.latestRelease(validRepoOwner, validRepoName, headers = headerUserAgent).shouldNotFail
  }

  "Repos.getRelease" should "call to httpClient.get with the right parameters" in {

    implicit val httpClientMock: HttpClient[IO] = httpClientMockGet[Option[Release]](
      url = s"repos/$validRepoOwner/$validRepoName/releases/${release.id}",
      response = IO.pure(Option(release))
    )

    val repos = new RepositoriesInterpreter[IO]

    repos
      .getRelease(release.id, validRepoOwner, validRepoName, headers = headerUserAgent)
      .shouldNotFail
  }

  "Repos.listOrgRepos" should "call to httpClient.get with the right parameters" in {

    implicit val httpClientMock: HttpClient[IO] = httpClientMockGet[List[Repository]](
      url = s"orgs/$validRepoOwner/repos",
      response = IO.pure(List(repo))
    )

    val repos = new RepositoriesInterpreter[IO]

    repos.listOrgRepos(validRepoOwner, headers = headerUserAgent).shouldNotFail
  }

  "Repos.listUserRepos" should "call to httpClient.get with the right parameters" in {

    implicit val httpClientMock: HttpClient[IO] = httpClientMockGet[List[Repository]](
      url = s"users/$validRepoOwner/repos",
      response = IO.pure(List(repo))
    )

    val repos = new RepositoriesInterpreter[IO]

    repos.listUserRepos(validRepoOwner, headers = headerUserAgent).shouldNotFail
  }

  "Repos.getContents" should "call to httpClient.get with the right parameters" in {

    implicit val httpClientMock: HttpClient[IO] = httpClientMockGet[NonEmptyList[Content]](
      url = s"repos/$validRepoOwner/$validRepoName/contents/$validFilePath",
      params = Map("ref" -> "main"),
      response = IO.pure(NonEmptyList.one(content))
    )

    val repos = new RepositoriesInterpreter[IO]

    repos
      .getContents(
        validRepoOwner,
        validRepoName,
        validFilePath,
        Some("main"),
        None,
        headerUserAgent
      )
      .shouldNotFail
  }

  "Repos.createFile" should "call to httpClient.put with the right parameters" in {

    val request = WriteFileRequest(
      validNote,
      validFileContent.getBytes.toBase64,
      None,
      Some(validBranchName),
      Some(validCommitter),
      Some(validCommitter)
    )

    implicit val httpClientMock: HttpClient[IO] =
      httpClientMockPut[WriteFileRequest, WriteFileResponse](
        url = s"repos/$validRepoOwner/$validRepoName/contents/$validFilePath",
        req = request,
        response = IO.pure(writeFileResponse)
      )

    val repos = new RepositoriesInterpreter[IO]

    repos
      .createFile(
        validRepoOwner,
        validRepoName,
        validFilePath,
        validNote,
        validFileContent.getBytes,
        Some(validBranchName),
        Some(validCommitter),
        Some(validCommitter),
        headerUserAgent
      )
      .shouldNotFail
  }

  "Repos.updateFile" should "call to httpClient.put with the right parameters" in {

    val request = WriteFileRequest(
      validNote,
      validFileContent.getBytes.toBase64,
      Some(validCommitSha),
      Some(validBranchName),
      Some(validCommitter),
      Some(validCommitter)
    )

    implicit val httpClientMock: HttpClient[IO] =
      httpClientMockPut[WriteFileRequest, WriteFileResponse](
        url = s"repos/$validRepoOwner/$validRepoName/contents/$validFilePath",
        req = request,
        response = IO.pure(writeFileResponse)
      )

    val repos = new RepositoriesInterpreter[IO]

    repos
      .updateFile(
        validRepoOwner,
        validRepoName,
        validFilePath,
        validNote,
        validFileContent.getBytes,
        validCommitSha,
        Some(validBranchName),
        Some(validCommitter),
        Some(validCommitter),
        headerUserAgent
      )
      .shouldNotFail
  }

  "Repos.deleteFile" should "call to httpClient.delete with the right parameters" in {

    val request = DeleteFileRequest(
      validNote,
      validCommitSha,
      Some(validBranchName),
      Some(validCommitter),
      Some(validCommitter)
    )

    implicit val httpClientMock: HttpClient[IO] =
      httpClientMockDeleteWithBody[DeleteFileRequest, WriteFileResponse](
        url = s"repos/$validRepoOwner/$validRepoName/contents/$validFilePath",
        req = request,
        response = IO.pure(writeFileResponse)
      )

    val repos = new RepositoriesInterpreter[IO]

    repos
      .deleteFile(
        validRepoOwner,
        validRepoName,
        validFilePath,
        validNote,
        validCommitSha,
        Some(validBranchName),
        Some(validCommitter),
        Some(validCommitter),
        headerUserAgent
      )
      .shouldNotFail
  }

  "Repos.createRelease" should "call to httpClient.post with the right parameters" in {

    val request = NewReleaseRequest(
      validTagTitle,
      validTagTitle,
      validNote,
      Some("main"),
      Some(false),
      Some(true)
    )

    implicit val httpClientMock: HttpClient[IO] = httpClientMockPost[NewReleaseRequest, Release](
      url = s"repos/$validRepoOwner/$validRepoName/releases",
      req = request,
      response = IO.pure(release)
    )

    val repos = new RepositoriesInterpreter[IO]

    repos
      .createRelease(
        validRepoOwner,
        validRepoName,
        validTagTitle,
        validTagTitle,
        validNote,
        Some("main"),
        Some(false),
        Some(true),
        headerUserAgent
      )
      .shouldNotFail
  }

  "Repos.listCommits" should "call to httpClient.get with the right parameters" in {

    implicit val httpClientMock: HttpClient[IO] = httpClientMockGet[List[Commit]](
      url = s"repos/$validRepoOwner/$validRepoName/commits",
      response = IO.pure(List(commit))
    )

    val repos = new RepositoriesInterpreter[IO]

    repos.listCommits(validRepoOwner, validRepoName, headers = headerUserAgent).shouldNotFail
  }

  "Repos.listBranches" should "call to httpClient.get with the right parameters" in {

    implicit val httpClientMock: HttpClient[IO] = httpClientMockGet[List[Branch]](
      url = s"repos/$validRepoOwner/$validRepoName/branches",
      response = IO.pure(List(branch))
    )

    val repos = new RepositoriesInterpreter[IO]

    repos.listBranches(validRepoOwner, validRepoName, headers = headerUserAgent).shouldNotFail
  }

  "Repos.listBranches" should "list protected branches only" in {

    implicit val httpClientMock: HttpClient[IO] = httpClientMockGet[List[Branch]](
      url = s"repos/$validRepoOwner/$validRepoName/branches",
      params = Map("protected" -> "true"),
      response = IO.pure(List(protectedBranch))
    )

    val repos = new RepositoriesInterpreter[IO]

    repos
      .listBranches(validRepoOwner, validRepoName, Some(true), None, headerUserAgent)
      .shouldNotFail
  }

  "Repos.listContributors" should "call to httpClient.get with the right parameters" in {

    implicit val httpClientMock: HttpClient[IO] = httpClientMockGet[List[User]](
      url = s"repos/$validRepoOwner/$validRepoName/contributors",
      response = IO.pure(List(user))
    )

    val repos = new RepositoriesInterpreter[IO]

    repos.listContributors(validRepoOwner, validRepoName, headers = headerUserAgent).shouldNotFail
  }

  "Repos.listCollaborators" should "call to httpClient.get with the right parameters" in {

    implicit val httpClientMock: HttpClient[IO] = httpClientMockGet[List[User]](
      url = s"repos/$validRepoOwner/$validRepoName/collaborators",
      response = IO.pure(List(user))
    )

    val repos = new RepositoriesInterpreter[IO]

    repos.listCollaborators(validRepoOwner, validRepoName, headers = headerUserAgent).shouldNotFail
  }

  "Repos.userIsCollaborator" should "call to httpClient.getWithoutResponse with the right parameters" in {

    implicit val httpClientMock: HttpClient[IO] = httpClientMockGetWithoutResponse(
      url = s"repos/$validRepoOwner/$validRepoName/collaborators/$validUsername",
      response = IO.unit,
      responseStatus = http4s.Status.NoContent
    )

    val repos = new RepositoriesInterpreter[IO]

    repos
      .userIsCollaborator(
        validRepoOwner,
        validRepoName,
        validUsername,
        headers = headerUserAgent
      )
      .shouldNotFail
  }

  "Repos.getRepoPermissionForUser" should "call to httpClient.get with the right parameters" in {

    implicit val httpClientMock: HttpClient[IO] = httpClientMockGet[UserRepoPermission](
      url = s"repos/$validRepoOwner/$validRepoName/collaborators/$validUsername/permission",
      response = IO.pure(userRepoPermission)
    )

    val repos = new RepositoriesInterpreter[IO]

    repos
      .getRepoPermissionForUser(
        validRepoOwner,
        validRepoName,
        validUsername,
        headers = headerUserAgent
      )
      .shouldNotFail
  }

  "Repos.getCombinedStatus" should "call httpClient.get with the right parameters" in {

    implicit val httpClientMock: HttpClient[IO] = httpClientMockGet[CombinedStatus](
      url = s"repos/$validRepoOwner/$validRepoName/commits/$validRefSingle/status",
      response = IO.pure(combinedStatus)
    )

    val repos = new RepositoriesInterpreter[IO]

    repos
      .getCombinedStatus(validRepoOwner, validRepoName, validRefSingle, headerUserAgent)
      .shouldNotFail
  }

  "Repos.listStatuses" should "call htppClient.get with the right parameters" in {

    implicit val httpClientMock: HttpClient[IO] = httpClientMockGet[List[Status]](
      url = s"repos/$validRepoOwner/$validRepoName/commits/$validRefSingle/statuses",
      response = IO.pure(List(status))
    )

    val repos = new RepositoriesInterpreter[IO]

    repos
      .listStatuses(validRepoOwner, validRepoName, validRefSingle, None, headerUserAgent)
      .shouldNotFail
  }

  "Repos.createStatus" should "call httpClient.post with the right parameters" in {

    val request = NewStatusRequest(validStatusState, None, None, None)

    implicit val httpClientMock: HttpClient[IO] = httpClientMockPost[NewStatusRequest, Status](
      url = s"repos/$validRepoOwner/$validRepoName/statuses/$validCommitSha",
      req = request,
      response = IO.pure(status),
      responseStatus = http4s.Status.Created
    )

    val repos = new RepositoriesInterpreter[IO]

    repos
      .createStatus(
        validRepoOwner,
        validRepoName,
        validCommitSha,
        validStatusState,
        None,
        None,
        None,
        headerUserAgent
      )
      .shouldNotFail

  }

  "Repos.searchRepos" should "call httpClient.get with the right parameters" in {

    implicit val httpClientMock: HttpClient[IO] = httpClientMockGet[SearchReposResult](
      url = githubApiUrl,
      params = Map("q" -> s"+${validSearchParams.map(_.value).mkString("+")}"),
      response = IO.pure(searchReposResult)
    )

    val repos = new RepositoriesInterpreter[IO]

    repos.searchRepos("", validSearchParams, None, headerUserAgent).shouldNotFail
  }

  "Repos.compare" should "call to httpClient.get with the right parameters" in {

    implicit val httpClientMock: HttpClient[IO] = httpClientMockGet[CommitComparisonResponse](
      url = s"repos/$validRepoOwner/$validRepoName/compare/$validCommitSha...$validMergeCommitSha",
      response = IO.pure(validCommitComparisonResponse)
    )

    val repos = new RepositoriesInterpreter[IO]

    repos
      .compareCommits(
        validRepoOwner,
        validRepoName,
        validCommitSha,
        validMergeCommitSha,
        headerUserAgent
      )
      .shouldNotFail
  }

}
