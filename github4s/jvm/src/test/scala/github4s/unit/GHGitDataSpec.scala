/*
 * Copyright 2016-2017 47 Degrees, LLC. <http://www.47deg.com>
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
