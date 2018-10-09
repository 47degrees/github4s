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

import cats.Id
import cats.data.NonEmptyList
import github4s.GithubResponses.{GHResponse, GHResult}
import github4s.HttpClient
import github4s.api.Repos
import github4s.free.domain._
import github4s.utils.BaseSpec

class ReposSpec extends BaseSpec {

  "Repos.get" should "call to httpClient.get with the right parameters" in {

    val response: GHResponse[Repository] =
      Right(GHResult(repo, okStatusCode, Map.empty))

    val httpClientMock = httpClientMockGet[Repository](
      url = s"repos/$validRepoOwner/$validRepoName",
      response = response
    )

    val repos = new Repos[String, Id] {
      override val httpClient: HttpClient[String, Id] = httpClientMock
    }
    repos.get(
      sampleToken,
      headerUserAgent,
      validRepoOwner,
      validRepoName
    )
  }

  "Repos.listOrgRepos" should "call to httpClient.get with the right parameters" in {

    val response: GHResponse[List[Repository]] =
      Right(GHResult(List(repo), okStatusCode, Map.empty))

    val httpClientMock = httpClientMockGet[List[Repository]](
      url = s"orgs/$validRepoOwner/repos",
      response = response
    )

    val repos = new Repos[String, Id] {
      override val httpClient: HttpClient[String, Id] = httpClientMock
    }
    repos.listOrgRepos(
      sampleToken,
      headerUserAgent,
      validRepoOwner
    )
  }

  "Repos.listUserRepos" should "call to httpClient.get with the right parameters" in {

    val response: GHResponse[List[Repository]] =
      Right(GHResult(List(repo), okStatusCode, Map.empty))

    val httpClientMock = httpClientMockGet[List[Repository]](
      url = s"users/$validRepoOwner/repos",
      response = response
    )

    val repos = new Repos[String, Id] {
      override val httpClient: HttpClient[String, Id] = httpClientMock
    }
    repos.listUserRepos(
      sampleToken,
      headerUserAgent,
      validRepoOwner
    )
  }

  "Repos.getContents" should "call to httpClient.get with the right parameters" in {

    val response: GHResponse[NonEmptyList[Content]] =
      Right(GHResult(NonEmptyList(content, Nil), okStatusCode, Map.empty))

    val httpClientMock = httpClientMockGet[NonEmptyList[Content]](
      url = s"repos/$validRepoOwner/$validRepoName/contents/$validFilePath",
      params = Map("ref" -> "master"),
      response = response
    )

    val repos = new Repos[String, Id] {
      override val httpClient: HttpClient[String, Id] = httpClientMock
    }
    repos.getContents(
      accessToken = sampleToken,
      headers = headerUserAgent,
      owner = validRepoOwner,
      repo = validRepoName,
      path = validFilePath,
      ref = Some("master")
    )
  }

  "Repos.createRelease" should "call to httpClient.post with the right parameters" in {

    val response: GHResponse[Release] = Right(GHResult(release, okStatusCode, Map.empty))
    val request =
      s"""
         |{
         |  "tag_name": "$validTagTitle",
         |  "target_commitish": "master",
         |  "name": "$validTagTitle",
         |  "body": "$validNote",
         |  "draft": false,
         |  "prerelease": true
         |}
       """.stripMargin
    val httpClientMock = httpClientMockPost[Release](
      url = s"repos/$validRepoOwner/$validRepoName/releases",
      json = request,
      response = response
    )

    val repos = new Repos[String, Id] {
      override val httpClient: HttpClient[String, Id] = httpClientMock
    }
    repos.createRelease(
      accessToken = sampleToken,
      headers = headerUserAgent,
      owner = validRepoOwner,
      repo = validRepoName,
      tagName = validTagTitle,
      name = validTagTitle,
      body = validNote,
      targetCommitish = Some("master"),
      draft = Some(false),
      prerelease = Some(true)
    )
  }

  "Repos.listCommits" should "call to httpClient.get with the right parameters" in {

    val response: GHResponse[List[Commit]] =
      Right(GHResult(List(commit), okStatusCode, Map.empty))

    val httpClientMock = httpClientMockGet[List[Commit]](
      url = s"repos/$validRepoOwner/$validRepoName/commits",
      response = response
    )

    val repos = new Repos[String, Id] {
      override val httpClient: HttpClient[String, Id] = httpClientMock
    }

    repos.listCommits(
      accessToken = sampleToken,
      headers = headerUserAgent,
      owner = validRepoOwner,
      repo = validRepoName
    )
  }

  "Repos.listContributors" should "call to httpClient.get with the right parameters" in {

    val response: GHResponse[List[User]] =
      Right(GHResult(List(user), okStatusCode, Map.empty))

    val httpClientMock = httpClientMockGet[List[User]](
      url = s"repos/$validRepoOwner/$validRepoName/contributors",
      response = response
    )

    val repos = new Repos[String, Id] {
      override val httpClient: HttpClient[String, Id] = httpClientMock
    }

    repos.listContributors(
      accessToken = sampleToken,
      headers = headerUserAgent,
      owner = validRepoOwner,
      repo = validRepoName
    )
  }

  "Repos.listCollaborators" should "call to httpClient.get with the right parameters" in {

    val response: GHResponse[List[User]] =
      Right(GHResult(List(user), okStatusCode, Map.empty))

    val httpClientMock = httpClientMockGet[List[User]](
      url = s"repos/$validRepoOwner/$validRepoName/collaborators",
      response = response
    )

    val repos = new Repos[String, Id] {
      override val httpClient: HttpClient[String, Id] = httpClientMock
    }

    repos.listCollaborators(
      accessToken = sampleToken,
      headers = headerUserAgent,
      owner = validRepoOwner,
      repo = validRepoName
    )
  }

  "Repos.getStatus" should "call httpClient.get with the right parameters" in {
    val response: GHResponse[CombinedStatus] =
      Right(GHResult(combinedStatus, okStatusCode, Map.empty))

    val httpClientMock = httpClientMockGet[CombinedStatus](
      url = s"repos/$validRepoOwner/$validRepoName/commits/$validRefSingle/status",
      response = response
    )

    val repos = new Repos[String, Id] {
      override val httpClient: HttpClient[String, Id] = httpClientMock
    }
    repos.getStatus(sampleToken, headerUserAgent, validRepoOwner, validRepoName, validRefSingle)
  }

  "Repos.listStatuses" should "call htppClient.get with the right parameters" in {
    val response: GHResponse[List[Status]] = Right(GHResult(List(status), okStatusCode, Map.empty))

    val httpClientMock = httpClientMockGet[List[Status]](
      url = s"repos/$validRepoOwner/$validRepoName/commits/$validRefSingle/statuses",
      response = response
    )

    val repos = new Repos[String, Id] {
      override val httpClient: HttpClient[String, Id] = httpClientMock
    }
    repos.listStatuses(sampleToken, headerUserAgent, validRepoOwner, validRepoName, validRefSingle)
  }

  "Repos.createStatus" should "call httpClient.post with the right parameters" in {
    val response: GHResponse[Status] = Right(GHResult(status, createdStatusCode, Map.empty))

    val httpClientMock = httpClientMockPost[Status](
      url = s"repos/$validRepoOwner/$validRepoName/statuses/$validCommitSha",
      json = s"""{"state":"$validStatusState"}""",
      response = response
    )

    val repos = new Repos[String, Id] {
      override val httpClient: HttpClient[String, Id] = httpClientMock
    }
    repos.createStatus(
      sampleToken,
      headerUserAgent,
      validRepoOwner,
      validRepoName,
      validCommitSha,
      validStatusState,
      None,
      None,
      None)
  }
}
