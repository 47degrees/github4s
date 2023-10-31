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
import github4s.Encoders._
import github4s.Decoders._
import github4s.domain._
import github4s.http.HttpClient
import github4s.interpreters.GitDataInterpreter
import github4s.utils.BaseSpec

class GitDataSpec extends BaseSpec {

  "GitData.getReference" should "call to httpClient.get with the right parameters" in {

    implicit val httpClientMock: HttpClient[IO] = httpClientMockGet[NonEmptyList[Ref]](
      url = s"repos/$validRepoOwner/$validRepoName/git/refs/$validRefSingle",
      response = IO.pure(NonEmptyList.one(ref))
    )

    val gitData = new GitDataInterpreter[IO]
    gitData
      .getReference(validRepoOwner, validRepoName, validRefSingle, None, headerUserAgent)
      .shouldNotFail
  }

  "GitData.createReference" should "call to httpClient.post with the right parameters" in {

    val request = CreateReferenceRequest(s"refs/$validRefSingle", validCommitSha)

    implicit val httpClientMock: HttpClient[IO] = httpClientMockPost[CreateReferenceRequest, Ref](
      url = s"repos/$validRepoOwner/$validRepoName/git/refs",
      req = request,
      response = IO.pure(ref)
    )

    val gitData = new GitDataInterpreter[IO]
    gitData
      .createReference(
        validRepoOwner,
        validRepoName,
        s"refs/$validRefSingle",
        validCommitSha,
        headerUserAgent
      )
      .shouldNotFail
  }

  "GitData.updateReference" should "call to httpClient.patch with the right parameters" in {

    val force   = false
    val request = UpdateReferenceRequest(validCommitSha, force)

    implicit val httpClientMock: HttpClient[IO] = httpClientMockPatch[UpdateReferenceRequest, Ref](
      url = s"repos/$validRepoOwner/$validRepoName/git/refs/$validRefSingle",
      req = request,
      response = IO.pure(ref)
    )

    val gitData = new GitDataInterpreter[IO]
    gitData
      .updateReference(
        validRepoOwner,
        validRepoName,
        validRefSingle,
        validCommitSha,
        force,
        headerUserAgent
      )
      .shouldNotFail

  }

  "GitData.getCommit" should "call to httpClient.get with the right parameters" in {

    implicit val httpClientMock: HttpClient[IO] = httpClientMockGet[RefCommit](
      url = s"repos/$validRepoOwner/$validRepoName/git/commits/$validCommitSha",
      response = IO.pure(refCommit)
    )
    val gitData = new GitDataInterpreter[IO]

    gitData.getCommit(validRepoOwner, validRepoName, validCommitSha, headerUserAgent).shouldNotFail
  }

  "GitData.createCommit" should "call to httpClient.post with the right parameters" in {

    val request =
      NewCommitRequest(validNote, validTreeSha, List(validCommitSha), Some(refCommitAuthor))

    implicit val httpClientMock: HttpClient[IO] = httpClientMockPost[NewCommitRequest, RefCommit](
      url = s"repos/$validRepoOwner/$validRepoName/git/commits",
      req = request,
      response = IO.pure(refCommit)
    )
    val gitData = new GitDataInterpreter[IO]
    gitData
      .createCommit(
        validRepoOwner,
        validRepoName,
        validNote,
        validTreeSha,
        List(validCommitSha),
        Some(refCommitAuthor),
        headerUserAgent
      )
      .shouldNotFail

  }

  "GitData.createBlob" should "call to httpClient.post with the right parameters" in {

    val request = NewBlobRequest(validNote, encoding)

    implicit val httpClientMock: HttpClient[IO] = httpClientMockPost[NewBlobRequest, RefInfo](
      url = s"repos/$validRepoOwner/$validRepoName/git/blobs",
      req = request,
      response = IO.pure(RefInfo(validCommitSha, githubApiUrl))
    )
    val gitData = new GitDataInterpreter[IO]
    gitData
      .createBlob(validRepoOwner, validRepoName, validNote, encoding, headerUserAgent)
      .shouldNotFail
  }

  "GitData.getBlob" should "call to httpClient.post with the right parameters" in {

    implicit val httpClientMock: HttpClient[IO] = httpClientMockGet[BlobContent](
      url = s"repos/$validRepoOwner/$validRepoName/git/blobs/$invalidFileSha",
      response = IO.pure(BlobContent("", invalidFileSha, 0))
    )
    val gitData = new GitDataInterpreter[IO]
    gitData.getBlob(validRepoOwner, validRepoName, invalidFileSha, headerUserAgent).shouldNotFail
  }

  "GitData.getTree" should "call to httpClient.get with the right parameters" in {

    implicit val httpClientMock: HttpClient[IO] = httpClientMockGet[TreeResult](
      url = s"repos/$validRepoOwner/$validRepoName/git/trees/$validCommitSha",
      response =
        IO.pure(TreeResult(validCommitSha, githubApiUrl, treeDataResult, truncated = Some(false)))
    )
    val gitData = new GitDataInterpreter[IO]

    gitData
      .getTree(
        validRepoOwner,
        validRepoName,
        validCommitSha,
        recursive = false,
        headerUserAgent
      )
      .shouldNotFail

  }

  "GitData.createTree" should "call to httpClient.post with the right parameters" in {

    val request = NewTreeRequest(treeDataList, Some(validTreeSha))

    implicit val httpClientMock: HttpClient[IO] = httpClientMockPost[NewTreeRequest, TreeResult](
      url = s"repos/$validRepoOwner/$validRepoName/git/trees",
      req = request,
      response = IO.pure(TreeResult(validCommitSha, githubApiUrl, treeDataResult))
    )
    val gitData = new GitDataInterpreter[IO]
    gitData
      .createTree(
        validRepoOwner,
        validRepoName,
        Some(validTreeSha),
        treeDataList,
        headerUserAgent
      )
      .shouldNotFail

  }

  "GitData.createTag" should "call to httpClient.post with the right parameters" in {

    val request =
      NewTagRequest(validTagTitle, validNote, validCommitSha, commitType, Some(refCommitAuthor))

    implicit val httpClientMock: HttpClient[IO] = httpClientMockPost[NewTagRequest, Tag](
      url = s"repos/$validRepoOwner/$validRepoName/git/tags",
      req = request,
      response = IO.pure(tag)
    )
    val gitData = new GitDataInterpreter[IO]
    gitData
      .createTag(
        validRepoOwner,
        validRepoName,
        validTagTitle,
        validNote,
        validCommitSha,
        commitType,
        Some(refCommitAuthor),
        headerUserAgent
      )
      .shouldNotFail
  }

}
