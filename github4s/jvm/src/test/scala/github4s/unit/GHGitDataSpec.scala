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

import cats.data.NonEmptyList
import cats.free.Free
import github4s.GHGitData
import github4s.GithubResponses.{GHResponse, GHResult}
import github4s.app.GitHub4s
import github4s.free.algebra.GitDataOps
import github4s.free.domain._
import github4s.utils.TestUtils
import org.scalatest.{FlatSpec, Matchers}
import org.scalatest.mockito.MockitoSugar.mock
import org.mockito.Mockito._
import org.mockito.ArgumentMatchers.any

class GHGitDataSpec extends FlatSpec with Matchers with TestUtils {

  "GHGitData.getReference" should "call to GitDataOps with the right parameters" in {

    val response: Free[GitHub4s, GHResponse[NonEmptyList[Ref]]] =
      Free.pure(Right(GHResult(NonEmptyList(ref, Nil), okStatusCode, Map.empty)))

    val gitDataOps = mock[GitDataOps[GitHub4s]]
    when(gitDataOps.getReference(any[String], any[String], any[String], any[Option[String]]))
      .thenReturn(response)

    val token     = Some("token")
    val ghGitData = new GHGitData(token)(gitDataOps)
    ghGitData.getReference(validRepoOwner, validRepoName, validRefSingle)

    verify(gitDataOps).getReference(validRepoOwner, validRepoName, validRefSingle, token)
  }

  "GHGitData.createReference" should "call to GitDataOps with the right parameters" in {

    val response: Free[GitHub4s, GHResponse[Ref]] =
      Free.pure(Right(GHResult(ref, okStatusCode, Map.empty)))

    val gitDataOps = mock[GitDataOps[GitHub4s]]
    when(
      gitDataOps
        .createReference(any[String], any[String], any[String], any[String], any[Option[String]]))
      .thenReturn(response)

    val token     = Some("token")
    val ghGitData = new GHGitData(token)(gitDataOps)
    ghGitData.createReference(validRepoOwner, validRepoName, validRefSingle, validCommitSha)

    verify(gitDataOps).createReference(
      validRepoOwner,
      validRepoName,
      validRefSingle,
      validCommitSha,
      token)
  }

  "GHGitData.updateReference" should "call to GitDataOps with the right parameters" in {

    val response: Free[GitHub4s, GHResponse[Ref]] =
      Free.pure(Right(GHResult(ref, okStatusCode, Map.empty)))

    val gitDataOps = mock[GitDataOps[GitHub4s]]
    when(
      gitDataOps.updateReference(
        any[String],
        any[String],
        any[String],
        any[String],
        any[Option[Boolean]],
        any[Option[String]]))
      .thenReturn(response)

    val token     = Some("token")
    val ghGitData = new GHGitData(token)(gitDataOps)
    ghGitData.updateReference(
      validRepoOwner,
      validRepoName,
      validRefSingle,
      validCommitSha,
      Some(false))

    verify(gitDataOps).updateReference(
      validRepoOwner,
      validRepoName,
      validRefSingle,
      validCommitSha,
      Some(false),
      token)
  }

  "GHGitData.getCommit" should "call to GitDataOps with the right parameters" in {

    val response: Free[GitHub4s, GHResponse[RefCommit]] =
      Free.pure(Right(GHResult(refCommit, okStatusCode, Map.empty)))

    val gitDataOps = mock[GitDataOps[GitHub4s]]
    when(gitDataOps.getCommit(any[String], any[String], any[String], any[Option[String]]))
      .thenReturn(response)

    val token     = Some("token")
    val ghGitData = new GHGitData(token)(gitDataOps)
    ghGitData.getCommit(validRepoOwner, validRepoName, validCommitSha)

    verify(gitDataOps).getCommit(validRepoOwner, validRepoName, validCommitSha, token)
  }

  "GHGitData.createCommit" should "call to GitDataOps with the right parameters" in {

    val response: Free[GitHub4s, GHResponse[RefCommit]] =
      Free.pure(Right(GHResult(refCommit, okStatusCode, Map.empty)))

    val gitDataOps = mock[GitDataOps[GitHub4s]]
    when(
      gitDataOps.createCommit(
        any[String],
        any[String],
        any[String],
        any[String],
        any[List[String]],
        any[Option[RefAuthor]],
        any[Option[String]]))
      .thenReturn(response)

    val token     = Some("token")
    val ghGitData = new GHGitData(token)(gitDataOps)
    ghGitData.createCommit(
      validRepoOwner,
      validRepoName,
      validNote,
      validTreeSha,
      List(validCommitSha),
      Some(refCommitAuthor))

    verify(gitDataOps).createCommit(
      validRepoOwner,
      validRepoName,
      validNote,
      validTreeSha,
      List(validCommitSha),
      Some(refCommitAuthor),
      token)
  }

  "GHGitData.createBlob" should "call to GitDataOps with the right parameters" in {

    val response: Free[GitHub4s, GHResponse[RefInfo]] =
      Free.pure(
        Right(GHResult(new RefInfo(validCommitSha, githubApiUrl), okStatusCode, Map.empty)))

    val gitDataOps = mock[GitDataOps[GitHub4s]]
    when(gitDataOps
      .createBlob(any[String], any[String], any[String], any[Option[String]], any[Option[String]]))
      .thenReturn(response)

    val token     = Some("token")
    val ghGitData = new GHGitData(token)(gitDataOps)
    ghGitData.createBlob(validRepoOwner, validRepoName, validNote, encoding)

    verify(gitDataOps).createBlob(validRepoOwner, validRepoName, validNote, encoding, token)
  }

  "GHGitData.createTree" should "call to GitDataOps with the right parameters" in {

    val response: Free[GitHub4s, GHResponse[TreeResult]] =
      Free.pure(
        Right(
          GHResult(
            TreeResult(validCommitSha, githubApiUrl, treeDataResult),
            okStatusCode,
            Map.empty)))

    val gitDataOps = mock[GitDataOps[GitHub4s]]
    when(
      gitDataOps.createTree(
        any[String],
        any[String],
        any[Option[String]],
        any[List[TreeData]],
        any[Option[String]]))
      .thenReturn(response)

    val token     = Some("token")
    val ghGitData = new GHGitData(token)(gitDataOps)
    ghGitData.createTree(validRepoOwner, validRepoName, Some(validTreeSha), treeDataList)

    verify(gitDataOps).createTree(
      validRepoOwner,
      validRepoName,
      Some(validTreeSha),
      treeDataList,
      token)
  }

  "GHGitData.createTag" should "call to GitDataOps with the right parameters" in {

    val response: Free[GitHub4s, GHResponse[Tag]] =
      Free.pure(Right(GHResult(tag, okStatusCode, Map.empty)))

    val gitDataOps = mock[GitDataOps[GitHub4s]]
    when(
      gitDataOps.createTag(
        any[String],
        any[String],
        any[String],
        any[String],
        any[String],
        any[String],
        any[Option[RefAuthor]],
        any[Option[String]]))
      .thenReturn(response)

    val token     = Some("token")
    val ghGitData = new GHGitData(token)(gitDataOps)
    ghGitData.createTag(
      validRepoOwner,
      validRepoName,
      validTagTitle,
      validNote,
      validCommitSha,
      commitType,
      Some(refCommitAuthor))

    verify(gitDataOps).createTag(
      validRepoOwner,
      validRepoName,
      validTagTitle,
      validNote,
      validCommitSha,
      commitType,
      Some(refCommitAuthor),
      token)
  }

}
