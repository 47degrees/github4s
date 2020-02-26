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

package github4s.unit

import cats.effect.IO
import cats.data.NonEmptyList
import github4s.GithubResponses.{GHResponse, GHResult}
import github4s.domain._
import github4s.interpreters.GitDataInterpreter
import github4s.utils.BaseSpec

class GitDataSpec extends BaseSpec {

  implicit val token = sampleToken

  "GitData.getReference" should "call to httpClient.get with the right parameters" in {

    val response: IO[GHResponse[NonEmptyList[Ref]]] =
      IO(Right(GHResult(NonEmptyList(ref, Nil), okStatusCode, Map.empty)))

    implicit val httpClientMock = httpClientMockGet[NonEmptyList[Ref]](
      url = s"repos/$validRepoOwner/$validRepoName/git/refs/$validRefSingle",
      response = response
    )

    val gitData = new GitDataInterpreter[IO]
    gitData.getReference(validRepoOwner, validRepoName, validRefSingle, headerUserAgent)
  }

  "GitData.createReference" should "call to httpClient.post with the right parameters" in {

    val response: IO[GHResponse[Ref]] =
      IO(Right(GHResult(ref, okStatusCode, Map.empty)))
    val request = CreateReferenceRequest(s"refs/$validRefSingle", validCommitSha)

    implicit val httpClientMock = httpClientMockPost[CreateReferenceRequest, Ref](
      url = s"repos/$validRepoOwner/$validRepoName/git/refs",
      req = request,
      response = response
    )

    val gitData = new GitDataInterpreter[IO]
    gitData.createReference(
      validRepoOwner,
      validRepoName,
      s"refs/$validRefSingle",
      validCommitSha,
      headerUserAgent
    )

  }

  "GitData.updateReference" should "call to httpClient.patch with the right parameters" in {

    val response: IO[GHResponse[Ref]] =
      IO(Right(GHResult(ref, okStatusCode, Map.empty)))
    val force   = false
    val request = UpdateReferenceRequest(validCommitSha, force)

    implicit val httpClientMock = httpClientMockPatch[UpdateReferenceRequest, Ref](
      url = s"repos/$validRepoOwner/$validRepoName/git/refs/$validRefSingle",
      req = request,
      response = response
    )

    val gitData = new GitDataInterpreter[IO]
    gitData.updateReference(
      validRepoOwner,
      validRepoName,
      validRefSingle,
      validCommitSha,
      force,
      headerUserAgent
    )

  }

  "GitData.getCommit" should "call to httpClient.get with the right parameters" in {

    val response: IO[GHResponse[RefCommit]] =
      IO(Right(GHResult(refCommit, okStatusCode, Map.empty)))
    implicit val httpClientMock = httpClientMockGet[RefCommit](
      url = s"repos/$validRepoOwner/$validRepoName/git/commits/$validCommitSha",
      response = response
    )
    val gitData = new GitDataInterpreter[IO]

    gitData.getCommit(validRepoOwner, validRepoName, validCommitSha, headerUserAgent)
  }

  "GitData.createCommit" should "call to httpClient.post with the right parameters" in {

    val response: IO[GHResponse[RefCommit]] =
      IO(Right(GHResult(refCommit, okStatusCode, Map.empty)))
    val request =
      NewCommitRequest(validNote, validTreeSha, List(validCommitSha), Some(refCommitAuthor))

    implicit val httpClientMock = httpClientMockPost[NewCommitRequest, RefCommit](
      url = s"repos/$validRepoOwner/$validRepoName/git/commits",
      req = request,
      response = response
    )
    val gitData = new GitDataInterpreter[IO]
    gitData.createCommit(
      validRepoOwner,
      validRepoName,
      validNote,
      validTreeSha,
      List(validCommitSha),
      Some(refCommitAuthor),
      headerUserAgent
    )

  }

  "GitData.createBlob" should "call to httpClient.post with the right parameters" in {

    val response: IO[GHResponse[RefInfo]] =
      IO(Right(GHResult(new RefInfo(validCommitSha, githubApiUrl), okStatusCode, Map.empty)))
    val request = NewBlobRequest(validNote, encoding)

    implicit val httpClientMock = httpClientMockPost[NewBlobRequest, RefInfo](
      url = s"repos/$validRepoOwner/$validRepoName/git/blobs",
      req = request,
      response = response
    )
    val gitData = new GitDataInterpreter[IO]
    gitData.createBlob(validRepoOwner, validRepoName, validNote, encoding, headerUserAgent)

  }

  "GitData.getTree" should "call to httpClient.get with the right parameters" in {

    val response: IO[GHResponse[TreeResult]] =
      IO(
        Right(
          GHResult(
            TreeResult(validCommitSha, githubApiUrl, treeDataResult, truncated = Some(false)),
            okStatusCode,
            Map.empty
          )
        )
      )

    implicit val httpClientMock = httpClientMockGet[TreeResult](
      url = s"repos/$validRepoOwner/$validRepoName/git/trees/$validCommitSha",
      response = response
    )
    val gitData = new GitDataInterpreter[IO]

    gitData.getTree(
      validRepoOwner,
      validRepoName,
      validCommitSha,
      recursive = false,
      headerUserAgent
    )

  }

  "GitData.createTree" should "call to httpClient.post with the right parameters" in {

    val response: IO[GHResponse[TreeResult]] =
      IO(
        Right(
          GHResult(
            TreeResult(validCommitSha, githubApiUrl, treeDataResult),
            okStatusCode,
            Map.empty
          )
        )
      )

    val request = NewTreeRequest(Some(validTreeSha), treeDataList)

    implicit val httpClientMock = httpClientMockPost[NewTreeRequest, TreeResult](
      url = s"repos/$validRepoOwner/$validRepoName/git/trees",
      req = request,
      response = response
    )
    val gitData = new GitDataInterpreter[IO]
    gitData.createTree(
      validRepoOwner,
      validRepoName,
      Some(validTreeSha),
      treeDataList,
      headerUserAgent
    )

  }

  "GitData.createTag" should "call to httpClient.post with the right parameters" in {

    val response: IO[GHResponse[Tag]] = IO(Right(GHResult(tag, okStatusCode, Map.empty)))
    val request =
      NewTagRequest(validTagTitle, validNote, validCommitSha, commitType, Some(refCommitAuthor))

    implicit val httpClientMock = httpClientMockPost[NewTagRequest, Tag](
      url = s"repos/$validRepoOwner/$validRepoName/git/tags",
      req = request,
      response = response
    )
    val gitData = new GitDataInterpreter[IO]
    gitData.createTag(
      validRepoOwner,
      validRepoName,
      validTagTitle,
      validNote,
      validCommitSha,
      commitType,
      Some(refCommitAuthor),
      headerUserAgent
    )

  }

}
