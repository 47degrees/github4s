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
import github4s.api.GitData
import github4s.free.domain._
import github4s.utils.BaseSpec

class GitDataSpec extends BaseSpec {

  "GitData.reference" should "call to httpClient.get with the right parameters" in {

    val response: GHResponse[NonEmptyList[Ref]] =
      Right(GHResult(NonEmptyList(ref, Nil), okStatusCode, Map.empty))

    val httpClientMock = httpClientMockGet[NonEmptyList[Ref]](
      url = s"repos/$validRepoOwner/$validRepoName/git/refs/$validRefSingle",
      response = response
    )

    val gitData = new GitData[String, Id] {
      override val httpClient: HttpClient[String, Id] = httpClientMock
    }
    gitData.reference(sampleToken, headerUserAgent, validRepoOwner, validRepoName, validRefSingle)
  }

  "GitData.createReference" should "call to httpClient.post with the right parameters" in {

    val response: GHResponse[Ref] =
      Right(GHResult(ref, okStatusCode, Map.empty))
    val request =
      s"""
         |{
         |  "ref": "refs/$validRefSingle",
         |  "sha": "$validCommitSha"
         |}
       """.stripMargin
    val httpClientMock = httpClientMockPost[Ref](
      url = s"repos/$validRepoOwner/$validRepoName/git/refs",
      json = request,
      response = response
    )

    val gitData = new GitData[String, Id] {
      override val httpClient: HttpClient[String, Id] = httpClientMock
    }
    gitData.createReference(
      sampleToken,
      headerUserAgent,
      validRepoOwner,
      validRepoName,
      s"refs/$validRefSingle",
      validCommitSha)
  }

  "GitData.updateReference" should "call to httpClient.patch with the right parameters" in {

    val response: GHResponse[Ref] =
      Right(GHResult(ref, okStatusCode, Map.empty))
    val force = false
    val request =
      s"""
         |{
         |  "sha": "$validCommitSha",
         |  "force": $force
         |}
       """.stripMargin

    val httpClientMock = httpClientMockPatch[Ref](
      url = s"repos/$validRepoOwner/$validRepoName/git/refs/$validRefSingle",
      json = request,
      response = response
    )

    val gitData = new GitData[String, Id] {
      override val httpClient: HttpClient[String, Id] = httpClientMock
    }
    gitData.updateReference(
      sampleToken,
      headerUserAgent,
      validRepoOwner,
      validRepoName,
      validRefSingle,
      validCommitSha,
      force)
  }

  "GitData.getCommit" should "call to httpClient.get with the right parameters" in {

    val response: GHResponse[RefCommit] =
      Right(GHResult(refCommit, okStatusCode, Map.empty))
    val httpClientMock = httpClientMockGet[RefCommit](
      url = s"repos/$validRepoOwner/$validRepoName/git/commits/$validCommitSha",
      response = response
    )
    val gitData = new GitData[String, Id] {
      override val httpClient: HttpClient[String, Id] = httpClientMock
    }

    gitData.commit(sampleToken, headerUserAgent, validRepoOwner, validRepoName, validCommitSha)
  }

  "GitData.createCommit" should "call to httpClient.post with the right parameters" in {

    val response: GHResponse[RefCommit] =
      Right(GHResult(refCommit, okStatusCode, Map.empty))
    val request =
      s"""
         |{
         |  "message": "$validNote",
         |  "tree": "$validTreeSha",
         |  "parents": ["$validCommitSha"],
         |  "author": {
         |    "name": "${refCommitAuthor.name}",
         |    "email": "${refCommitAuthor.email}",
         |    "date": "${refCommitAuthor.date}"
         |  }
         |}
       """.stripMargin
    val httpClientMock = httpClientMockPost[RefCommit](
      url = s"repos/$validRepoOwner/$validRepoName/git/commits",
      json = request,
      response = response
    )
    val gitData = new GitData[String, Id] {
      override val httpClient: HttpClient[String, Id] = httpClientMock
    }
    gitData.createCommit(
      sampleToken,
      headerUserAgent,
      validRepoOwner,
      validRepoName,
      validNote,
      validTreeSha,
      List(validCommitSha),
      Some(refCommitAuthor))
  }

  "GitData.createBlob" should "call to httpClient.post with the right parameters" in {

    val response: GHResponse[RefInfo] =
      Right(GHResult(new RefInfo(validCommitSha, githubApiUrl), okStatusCode, Map.empty))
    val request =
      s"""
         |{
         |  "content": "$validNote"
         |  ${encoding.map(e => ",\"encoding\": \"" + e + "\"").getOrElse(" ")}
         |}
       """.stripMargin
    val httpClientMock = httpClientMockPost[RefInfo](
      url = s"repos/$validRepoOwner/$validRepoName/git/blobs",
      json = request,
      response = response
    )
    val gitData = new GitData[String, Id] {
      override val httpClient: HttpClient[String, Id] = httpClientMock
    }
    gitData.createBlob(
      sampleToken,
      headerUserAgent,
      validRepoOwner,
      validRepoName,
      validNote,
      encoding)
  }

  "GitData.createTree" should "call to httpClient.post with the right parameters" in {

    val response: GHResponse[TreeResult] =
      Right(
        GHResult(
          TreeResult(validCommitSha, githubApiUrl, treeDataResult),
          okStatusCode,
          Map.empty))
    def treeDataJson(treeData: TreeData): String = {

      def attr: String = treeData match {
        case d: TreeDataSha  => "\"sha\": \"" + d.sha + "\""
        case d: TreeDataBlob => "\"content\": \"" + d.content + "\""
      }

      s"""
         |{
         |  "path": "${treeData.path}",
         |  "mode": "${treeData.mode}",
         |  "type": "${treeData.`type`}",
         |  $attr
         |}
       """.stripMargin
    }

    val request =
      s"""
         |{
         |  "base_tree": "$validTreeSha",
         |  "tree": [ ${treeDataList.map(treeDataJson).mkString(",")} ]
         |}
       """.stripMargin
    val httpClientMock = httpClientMockPost[TreeResult](
      url = s"repos/$validRepoOwner/$validRepoName/git/trees",
      json = request,
      response = response
    )
    val gitData = new GitData[String, Id] {
      override val httpClient: HttpClient[String, Id] = httpClientMock
    }
    gitData.createTree(
      sampleToken,
      headerUserAgent,
      validRepoOwner,
      validRepoName,
      Some(validTreeSha),
      treeDataList)
  }

  "GitData.createTag" should "call to httpClient.post with the right parameters" in {

    val response: GHResponse[Tag] = Right(GHResult(tag, okStatusCode, Map.empty))
    val request =
      s"""
         |{
         |  "tag": "$validTagTitle",
         |  "message": "$validNote",
         |  "object": "$validCommitSha",
         |  "type": "$commitType",
         |  "tagger": {
         |    "name": "${refCommitAuthor.name}",
         |    "email": "${refCommitAuthor.email}",
         |    "date": "${refCommitAuthor.date}"
         |  }
         |}
       """.stripMargin
    val httpClientMock = httpClientMockPost[Tag](
      url = s"repos/$validRepoOwner/$validRepoName/git/tags",
      json = request,
      response = response
    )
    val gitData = new GitData[String, Id] {
      override val httpClient: HttpClient[String, Id] = httpClientMock
    }
    gitData.createTag(
      sampleToken,
      headerUserAgent,
      validRepoOwner,
      validRepoName,
      validTagTitle,
      validNote,
      validCommitSha,
      commitType,
      Some(refCommitAuthor))
  }

}
