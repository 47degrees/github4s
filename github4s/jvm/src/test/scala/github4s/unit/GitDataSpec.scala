/*
 * Copyright (c) 2016 47 Degrees, LLC. <http://www.47deg.com>
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
 * the Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
 * FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
 * IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package github4s.unit

import cats.Id
import cats.data.NonEmptyList
import github4s.{HttpClient, HttpRequestBuilderExtensionJVM, IdInstances}
import github4s.GithubResponses.{GHResponse, GHResult}
import github4s.api.GitData
import github4s.free.domain._
import github4s.utils.{DummyGithubUrls, TestUtils}
import org.mockito.ArgumentMatchers.any
import org.mockito.ArgumentMatchers.argThat
import org.mockito.ArgumentMatchers.{eq => argEq}
import org.mockito.Mockito._
import org.scalatest.mockito.MockitoSugar.mock
import org.scalatest.{FlatSpec, Matchers}
import io.circe.Decoder

import scalaj.http.HttpResponse

class GitDataSpec
    extends FlatSpec
    with Matchers
    with TestUtils
    with DummyGithubUrls
    with IdInstances
    with HttpRequestBuilderExtensionJVM {

  "GitData.reference" should "call to httpClient.get with the right parameters" in {

    val response: GHResponse[NonEmptyList[Ref]] =
      Right(GHResult(NonEmptyList(ref, Nil), okStatusCode, Map.empty))

    val httpClientMock = mock[HttpClient[HttpResponse[String], Id]]
    when(
      httpClientMock.get[NonEmptyList[Ref]](
        any[Option[String]],
        any[String],
        any[Map[String, String]],
        any[Map[String, String]],
        any[Option[Pagination]])(any[Decoder[NonEmptyList[Ref]]]))
      .thenReturn(response)

    val token = Some("token")
    val gitData = new GitData[HttpResponse[String], Id] {
      override val httpClient: HttpClient[HttpResponse[String], Id] = httpClientMock
    }
    gitData.reference(token, headerUserAgent, validRepoOwner, validRepoName, validRefSingle)

    verify(httpClientMock).get[NonEmptyList[Ref]](
      argEq(token),
      argEq(s"repos/$validRepoOwner/$validRepoName/git/refs/$validRefSingle"),
      argEq(headerUserAgent),
      any[Map[String, String]],
      any[Option[Pagination]]
    )(any[Decoder[NonEmptyList[Ref]]])
  }

  "GHGitData.createReference" should "call to httpClient.post with the right parameters" in {

    val response: GHResponse[Ref] =
      Right(GHResult(ref, okStatusCode, Map.empty))

    val httpClientMock = mock[HttpClient[HttpResponse[String], Id]]
    when(
      httpClientMock
        .post[Ref](any[Option[String]], any[String], any[Map[String, String]], any[String])(
          any[Decoder[Ref]]))
      .thenReturn(response)

    val token = Some("token")
    val gitData = new GitData[HttpResponse[String], Id] {
      override val httpClient: HttpClient[HttpResponse[String], Id] = httpClientMock
    }
    gitData.createReference(
      token,
      headerUserAgent,
      validRepoOwner,
      validRepoName,
      s"refs/$validRefSingle",
      validCommitSha)

    val request =
      s"""
         |{
         |  "ref": "refs/$validRefSingle",
         |  "sha": "$validCommitSha"
         |}
       """.stripMargin

    verify(httpClientMock).post[Ref](
      argEq(token),
      argEq(s"repos/$validRepoOwner/$validRepoName/git/refs"),
      argEq(headerUserAgent),
      argThat(JsonArgMatcher(request))
    )(any[Decoder[Ref]])
  }

  "GHGitData.updateReference" should "call to httpClient.patch with the right parameters" in {

    val response: GHResponse[Ref] =
      Right(GHResult(ref, okStatusCode, Map.empty))

    val httpClientMock = mock[HttpClient[HttpResponse[String], Id]]
    when(
      httpClientMock
        .patch[Ref](any[Option[String]], any[String], any[Map[String, String]], any[String])(
          any[Decoder[Ref]]))
      .thenReturn(response)

    val token = Some("token")
    val force = Some(false)
    val gitData = new GitData[HttpResponse[String], Id] {
      override val httpClient: HttpClient[HttpResponse[String], Id] = httpClientMock
    }
    gitData.updateReference(
      token,
      headerUserAgent,
      validRepoOwner,
      validRepoName,
      validRefSingle,
      validCommitSha,
      force)

    val request =
      s"""
         |{
         |  "sha": "$validCommitSha"
         |  ${force.map(f => ",\"force\": " + f).getOrElse(" ")}
         |}
       """.stripMargin

    verify(httpClientMock).patch[Ref](
      argEq(token),
      argEq(s"repos/$validRepoOwner/$validRepoName/git/refs/$validRefSingle"),
      argEq(headerUserAgent),
      argThat(JsonArgMatcher(request))
    )(any[Decoder[Ref]])
  }

  "GHGitData.getCommit" should "call to httpClient.get with the right parameters" in {

    val response: GHResponse[RefCommit] =
      Right(GHResult(refCommit, okStatusCode, Map.empty))

    val httpClientMock = mock[HttpClient[HttpResponse[String], Id]]
    when(
      httpClientMock.get[RefCommit](
        any[Option[String]],
        any[String],
        any[Map[String, String]],
        any[Map[String, String]],
        any[Option[Pagination]])(any[Decoder[RefCommit]]))
      .thenReturn(response)

    val token = Some("token")
    val gitData = new GitData[HttpResponse[String], Id] {
      override val httpClient: HttpClient[HttpResponse[String], Id] = httpClientMock
    }

    gitData.commit(token, headerUserAgent, validRepoOwner, validRepoName, validCommitSha)

    verify(httpClientMock).get[RefCommit](
      argEq(token),
      argEq(s"repos/$validRepoOwner/$validRepoName/git/commits/$validCommitSha"),
      argEq(headerUserAgent),
      any[Map[String, String]],
      any[Option[Pagination]]
    )(any[Decoder[RefCommit]])
  }

  "GHGitData.createCommit" should "call to httpClient.post with the right parameters" in {

    val response: GHResponse[RefCommit] =
      Right(GHResult(refCommit, okStatusCode, Map.empty))

    val httpClientMock = mock[HttpClient[HttpResponse[String], Id]]
    when(
      httpClientMock
        .post[RefCommit](any[Option[String]], any[String], any[Map[String, String]], any[String])(
          any[Decoder[RefCommit]]))
      .thenReturn(response)

    val token = Some("token")
    val gitData = new GitData[HttpResponse[String], Id] {
      override val httpClient: HttpClient[HttpResponse[String], Id] = httpClientMock
    }
    gitData.createCommit(
      token,
      headerUserAgent,
      validRepoOwner,
      validRepoName,
      validNote,
      validTreeSha,
      List(validCommitSha),
      Some(refCommitAuthor))

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

    verify(httpClientMock).post[RefCommit](
      argEq(token),
      argEq(s"repos/$validRepoOwner/$validRepoName/git/commits"),
      argEq(headerUserAgent),
      argThat(JsonArgMatcher(request))
    )(any[Decoder[RefCommit]])
  }

  "GHGitData.createBlob" should "call to httpClient.post with the right parameters" in {

    val response: GHResponse[RefInfo] =
      Right(GHResult(new RefInfo(validCommitSha, githubApiUrl), okStatusCode, Map.empty))

    val httpClientMock = mock[HttpClient[HttpResponse[String], Id]]
    when(
      httpClientMock
        .post[RefInfo](any[Option[String]], any[String], any[Map[String, String]], any[String])(
          any[Decoder[RefInfo]]))
      .thenReturn(response)

    val token = Some("token")
    val gitData = new GitData[HttpResponse[String], Id] {
      override val httpClient: HttpClient[HttpResponse[String], Id] = httpClientMock
    }
    gitData.createBlob(token, headerUserAgent, validRepoOwner, validRepoName, validNote, encoding)

    val request =
      s"""
         |{
         |  "content": "$validNote"
         |  ${encoding.map(e => ",\"encoding\": \"" + e + "\"").getOrElse(" ")}
         |}
       """.stripMargin

    verify(httpClientMock).post[RefInfo](
      argEq(token),
      argEq(s"repos/$validRepoOwner/$validRepoName/git/blobs"),
      argEq(headerUserAgent),
      argThat(JsonArgMatcher(request))
    )(any[Decoder[RefInfo]])
  }

  "GHGitData.createTree" should "call to httpClient.post with the right parameters" in {

    val response: GHResponse[TreeResult] =
      Right(
        GHResult(
          TreeResult(validCommitSha, githubApiUrl, treeDataResult),
          okStatusCode,
          Map.empty))

    val httpClientMock = mock[HttpClient[HttpResponse[String], Id]]
    when(
      httpClientMock
        .post[TreeResult](any[Option[String]], any[String], any[Map[String, String]], any[String])(
          any[Decoder[TreeResult]]))
      .thenReturn(response)

    val token = Some("token")
    val gitData = new GitData[HttpResponse[String], Id] {
      override val httpClient: HttpClient[HttpResponse[String], Id] = httpClientMock
    }
    gitData.createTree(
      token,
      headerUserAgent,
      validRepoOwner,
      validRepoName,
      Some(validTreeSha),
      treeDataList)

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

    verify(httpClientMock).post[TreeResult](
      argEq(token),
      argEq(s"repos/$validRepoOwner/$validRepoName/git/trees"),
      argEq(headerUserAgent),
      argThat(JsonArgMatcher(request))
    )(any[Decoder[TreeResult]])
  }

  "GHGitData.createTag" should "call to httpClient.post with the right parameters" in {

    val response: GHResponse[Tag] = Right(GHResult(tag, okStatusCode, Map.empty))

    val httpClientMock = mock[HttpClient[HttpResponse[String], Id]]
    when(
      httpClientMock
        .post[Tag](any[Option[String]], any[String], any[Map[String, String]], any[String])(
          any[Decoder[Tag]]))
      .thenReturn(response)

    val token = Some("token")
    val gitData = new GitData[HttpResponse[String], Id] {
      override val httpClient: HttpClient[HttpResponse[String], Id] = httpClientMock
    }
    gitData.createTag(
      token,
      headerUserAgent,
      validRepoOwner,
      validRepoName,
      validTagTitle,
      validNote,
      validCommitSha,
      commitType,
      Some(refCommitAuthor))

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

    verify(httpClientMock).post[Tag](
      argEq(token),
      argEq(s"repos/$validRepoOwner/$validRepoName/git/tags"),
      argEq(headerUserAgent),
      argThat(JsonArgMatcher(request))
    )(any[Decoder[Tag]])
  }

}
