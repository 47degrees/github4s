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

package github4s.unit

import cats.effect.IO
import cats.data.NonEmptyList
import cats.syntax.either._
import github4s.GHResponse
import github4s.domain._
import github4s.interpreters.RepositoriesInterpreter
import github4s.utils.BaseSpec
import com.github.marklister.base64.Base64._

class ReposSpec extends BaseSpec {

  implicit val token = sampleToken

  "Repos.get" should "call to httpClient.get with the right parameters" in {
    val response: IO[GHResponse[Repository]] =
      IO(GHResponse(repo.asRight, okStatusCode, Map.empty))

    implicit val httpClientMock = httpClientMockGet[Repository](
      url = s"repos/$validRepoOwner/$validRepoName",
      response = response
    )

    val repos = new RepositoriesInterpreter[IO]

    repos.get(validRepoOwner, validRepoName, headerUserAgent)
  }

  "Repos.listOrgRepos" should "call to httpClient.get with the right parameters" in {
    val response: IO[GHResponse[List[Repository]]] =
      IO(GHResponse(List(repo).asRight, okStatusCode, Map.empty))

    implicit val httpClientMock = httpClientMockGet[List[Repository]](
      url = s"orgs/$validRepoOwner/repos",
      response = response
    )

    val repos = new RepositoriesInterpreter[IO]

    repos.listOrgRepos(validRepoOwner, headers = headerUserAgent)
  }

  "Repos.listUserRepos" should "call to httpClient.get with the right parameters" in {
    val response: IO[GHResponse[List[Repository]]] =
      IO(GHResponse(List(repo).asRight, okStatusCode, Map.empty))

    implicit val httpClientMock = httpClientMockGet[List[Repository]](
      url = s"users/$validRepoOwner/repos",
      response = response
    )

    val repos = new RepositoriesInterpreter[IO]

    repos.listUserRepos(validRepoOwner, headers = headerUserAgent)
  }

  "Repos.getContents" should "call to httpClient.get with the right parameters" in {
    val response: IO[GHResponse[NonEmptyList[Content]]] =
      IO(GHResponse(NonEmptyList.one(content).asRight, okStatusCode, Map.empty))

    implicit val httpClientMock = httpClientMockGet[NonEmptyList[Content]](
      url = s"repos/$validRepoOwner/$validRepoName/contents/$validFilePath",
      params = Map("ref" -> "master"),
      response = response
    )

    val repos = new RepositoriesInterpreter[IO]

    repos.getContents(
      validRepoOwner,
      validRepoName,
      validFilePath,
      Some("master"),
      None,
      headerUserAgent
    )
  }

  "Repos.createFile" should "call to httpClient.put with the right parameters" in {
    val response: IO[GHResponse[WriteFileResponse]] =
      IO(GHResponse(writeFileResponse.asRight, okStatusCode, Map.empty))

    val request = WriteFileRequest(
      validNote,
      validFileContent.getBytes.toBase64,
      None,
      Some(validBranchName),
      Some(validCommitter),
      Some(validCommitter)
    )

    implicit val httpClientMock = httpClientMockPut[WriteFileRequest, WriteFileResponse](
      url = s"repos/$validRepoOwner/$validRepoName/contents/$validFilePath",
      req = request,
      response = response
    )

    val repos = new RepositoriesInterpreter[IO]

    repos.createFile(
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
  }

  "Repos.updateFile" should "call to httpClient.put with the right parameters" in {
    val response: IO[GHResponse[WriteFileResponse]] =
      IO(GHResponse(writeFileResponse.asRight, okStatusCode, Map.empty))

    val request = WriteFileRequest(
      validNote,
      validFileContent.getBytes.toBase64,
      Some(validCommitSha),
      Some(validBranchName),
      Some(validCommitter),
      Some(validCommitter)
    )

    implicit val httpClientMock = httpClientMockPut[WriteFileRequest, WriteFileResponse](
      url = s"repos/$validRepoOwner/$validRepoName/contents/$validFilePath",
      req = request,
      response = response
    )

    val repos = new RepositoriesInterpreter[IO]

    repos.updateFile(
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
  }

  "Repos.deleteFile" should "call to httpClient.delete with the right parameters" in {
    val response: IO[GHResponse[WriteFileResponse]] =
      IO(GHResponse(writeFileResponse.asRight, okStatusCode, Map.empty))

    val request = DeleteFileRequest(
      validNote,
      validCommitSha,
      Some(validBranchName),
      Some(validCommitter),
      Some(validCommitter)
    )

    implicit val httpClientMock =
      httpClientMockDeleteWithBody[DeleteFileRequest, WriteFileResponse](
        url = s"repos/$validRepoOwner/$validRepoName/contents/$validFilePath",
        req = request,
        response = response
      )

    val repos = new RepositoriesInterpreter[IO]

    repos.deleteFile(
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
  }

  "Repos.createRelease" should "call to httpClient.post with the right parameters" in {
    val response: IO[GHResponse[Release]] = IO(GHResponse(release.asRight, okStatusCode, Map.empty))

    val request = NewReleaseRequest(
      validTagTitle,
      validTagTitle,
      validNote,
      Some("master"),
      Some(false),
      Some(true)
    )

    implicit val httpClientMock = httpClientMockPost[NewReleaseRequest, Release](
      url = s"repos/$validRepoOwner/$validRepoName/releases",
      req = request,
      response = response
    )

    val repos = new RepositoriesInterpreter[IO]

    repos.createRelease(
      validRepoOwner,
      validRepoName,
      validTagTitle,
      validTagTitle,
      validNote,
      Some("master"),
      Some(false),
      Some(true),
      headerUserAgent
    )
  }

  "Repos.listCommits" should "call to httpClient.get with the right parameters" in {
    val response: IO[GHResponse[List[Commit]]] =
      IO(GHResponse(List(commit).asRight, okStatusCode, Map.empty))

    implicit val httpClientMock = httpClientMockGet[List[Commit]](
      url = s"repos/$validRepoOwner/$validRepoName/commits",
      response = response
    )

    val repos = new RepositoriesInterpreter[IO]

    repos.listCommits(validRepoOwner, validRepoName, headers = headerUserAgent)
  }

  "Repos.listBranches" should "call to httpClient.get with the right parameters" in {
    val response: IO[GHResponse[List[Branch]]] =
      IO(GHResponse(List(branch).asRight, okStatusCode, Map.empty))

    implicit val httpClientMock = httpClientMockGet[List[Branch]](
      url = s"repos/$validRepoOwner/$validRepoName/branches",
      response = response
    )

    val repos = new RepositoriesInterpreter[IO]

    repos.listBranches(validRepoOwner, validRepoName, headers = headerUserAgent)
  }

  "Repos.listBranches" should "list protected branches only" in {
    val response: IO[GHResponse[List[Branch]]] =
      IO(GHResponse(List(protectedBranch).asRight, okStatusCode, Map.empty))

    implicit val httpClientMock = httpClientMockGet[List[Branch]](
      url = s"repos/$validRepoOwner/$validRepoName/branches",
      params = Map("protected" -> "true"),
      response = response
    )

    val repos = new RepositoriesInterpreter[IO]

    repos.listBranches(validRepoOwner, validRepoName, Some(true), None, headerUserAgent)
  }

  "Repos.listContributors" should "call to httpClient.get with the right parameters" in {
    val response: IO[GHResponse[List[User]]] =
      IO(GHResponse(List(user).asRight, okStatusCode, Map.empty))

    implicit val httpClientMock = httpClientMockGet[List[User]](
      url = s"repos/$validRepoOwner/$validRepoName/contributors",
      response = response
    )

    val repos = new RepositoriesInterpreter[IO]

    repos.listContributors(validRepoOwner, validRepoName, headers = headerUserAgent)
  }

  "Repos.listCollaborators" should "call to httpClient.get with the right parameters" in {
    val response: IO[GHResponse[List[User]]] =
      IO(GHResponse(List(user).asRight, okStatusCode, Map.empty))

    implicit val httpClientMock = httpClientMockGet[List[User]](
      url = s"repos/$validRepoOwner/$validRepoName/collaborators",
      response = response
    )

    val repos = new RepositoriesInterpreter[IO]

    repos.listCollaborators(validRepoOwner, validRepoName, headers = headerUserAgent)
  }

  "Repos.getCombinedStatus" should "call httpClient.get with the right parameters" in {
    val response: IO[GHResponse[CombinedStatus]] =
      IO(GHResponse(combinedStatus.asRight, okStatusCode, Map.empty))

    implicit val httpClientMock = httpClientMockGet[CombinedStatus](
      url = s"repos/$validRepoOwner/$validRepoName/commits/$validRefSingle/status",
      response = response
    )

    val repos = new RepositoriesInterpreter[IO]

    repos.getCombinedStatus(validRepoOwner, validRepoName, validRefSingle, headerUserAgent)
  }

  "Repos.listStatuses" should "call htppClient.get with the right parameters" in {
    val response: IO[GHResponse[List[Status]]] =
      IO(GHResponse(List(status).asRight, okStatusCode, Map.empty))

    implicit val httpClientMock = httpClientMockGet[List[Status]](
      url = s"repos/$validRepoOwner/$validRepoName/commits/$validRefSingle/statuses",
      response = response
    )

    val repos = new RepositoriesInterpreter[IO]

    repos.listStatuses(validRepoOwner, validRepoName, validRefSingle, None, headerUserAgent)
  }

  "Repos.createStatus" should "call httpClient.post with the right parameters" in {
    val response: IO[GHResponse[Status]] =
      IO(GHResponse(status.asRight, createdStatusCode, Map.empty))

    val request = NewStatusRequest(validStatusState, None, None, None)

    implicit val httpClientMock = httpClientMockPost[NewStatusRequest, Status](
      url = s"repos/$validRepoOwner/$validRepoName/statuses/$validCommitSha",
      req = request,
      response = response
    )

    val repos = new RepositoriesInterpreter[IO]

    repos.createStatus(
      validRepoOwner,
      validRepoName,
      validCommitSha,
      validStatusState,
      None,
      None,
      None,
      headerUserAgent
    )

  }
}
