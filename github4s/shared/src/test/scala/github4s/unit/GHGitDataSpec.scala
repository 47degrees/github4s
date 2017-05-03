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
import github4s.free.domain._
import github4s.utils.BaseSpec

class GHGitDataSpec extends BaseSpec {

  "GHGitData.getReference" should "call to GitDataOps with the right parameters" in {

    val response: Free[GitHub4s, GHResponse[NonEmptyList[Ref]]] =
      Free.pure(Right(GHResult(NonEmptyList(ref, Nil), okStatusCode, Map.empty)))

    val gitDataOps = mock[GitDataOpsTest]
    (gitDataOps.getReference _)
      .expects(validRepoOwner, validRepoName, validRefSingle, sampleToken)
      .returns(response)

    val ghGitData = new GHGitData(sampleToken)(gitDataOps)
    ghGitData.getReference(validRepoOwner, validRepoName, validRefSingle)
  }

  "GHGitData.createReference" should "call to GitDataOps with the right parameters" in {

    val response: Free[GitHub4s, GHResponse[Ref]] =
      Free.pure(Right(GHResult(ref, okStatusCode, Map.empty)))

    val gitDataOps = mock[GitDataOpsTest]
    (gitDataOps.createReference _)
      .expects(validRepoOwner, validRepoName, validRefSingle, validCommitSha, sampleToken)
      .returns(response)

    val ghGitData = new GHGitData(sampleToken)(gitDataOps)
    ghGitData.createReference(validRepoOwner, validRepoName, validRefSingle, validCommitSha)
  }

  "GHGitData.updateReference" should "call to GitDataOps with the right parameters" in {

    val response: Free[GitHub4s, GHResponse[Ref]] =
      Free.pure(Right(GHResult(ref, okStatusCode, Map.empty)))

    val gitDataOps = mock[GitDataOpsTest]
    (gitDataOps.updateReference _)
      .expects(
        validRepoOwner,
        validRepoName,
        validRefSingle,
        validCommitSha,
        Some(false),
        sampleToken)
      .returns(response)

    val ghGitData = new GHGitData(sampleToken)(gitDataOps)
    ghGitData.updateReference(
      validRepoOwner,
      validRepoName,
      validRefSingle,
      validCommitSha,
      Some(false))
  }

  "GHGitData.getCommit" should "call to GitDataOps with the right parameters" in {

    val response: Free[GitHub4s, GHResponse[RefCommit]] =
      Free.pure(Right(GHResult(refCommit, okStatusCode, Map.empty)))

    val gitDataOps = mock[GitDataOpsTest]
    (gitDataOps.getCommit _)
      .expects(validRepoOwner, validRepoName, validCommitSha, sampleToken)
      .returns(response)

    val ghGitData = new GHGitData(sampleToken)(gitDataOps)
    ghGitData.getCommit(validRepoOwner, validRepoName, validCommitSha)
  }

  "GHGitData.createCommit" should "call to GitDataOps with the right parameters" in {

    val response: Free[GitHub4s, GHResponse[RefCommit]] =
      Free.pure(Right(GHResult(refCommit, okStatusCode, Map.empty)))

    val gitDataOps = mock[GitDataOpsTest]
    (gitDataOps.createCommit _)
      .expects(
        validRepoOwner,
        validRepoName,
        validNote,
        validTreeSha,
        List(validCommitSha),
        Some(refCommitAuthor),
        sampleToken)
      .returns(response)

    val ghGitData = new GHGitData(sampleToken)(gitDataOps)
    ghGitData.createCommit(
      validRepoOwner,
      validRepoName,
      validNote,
      validTreeSha,
      List(validCommitSha),
      Some(refCommitAuthor))
  }

  "GHGitData.createBlob" should "call to GitDataOps with the right parameters" in {

    val response: Free[GitHub4s, GHResponse[RefInfo]] =
      Free.pure(
        Right(GHResult(new RefInfo(validCommitSha, githubApiUrl), okStatusCode, Map.empty)))

    val gitDataOps = mock[GitDataOpsTest]
    (gitDataOps.createBlob _)
      .expects(validRepoOwner, validRepoName, validNote, encoding, sampleToken)
      .returns(response)

    val ghGitData = new GHGitData(sampleToken)(gitDataOps)
    ghGitData.createBlob(validRepoOwner, validRepoName, validNote, encoding)
  }

  "GHGitData.createTree" should "call to GitDataOps with the right parameters" in {

    val response: Free[GitHub4s, GHResponse[TreeResult]] =
      Free.pure(
        Right(
          GHResult(
            TreeResult(validCommitSha, githubApiUrl, treeDataResult),
            okStatusCode,
            Map.empty)))

    val gitDataOps = mock[GitDataOpsTest]
    (gitDataOps.createTree _)
      .expects(validRepoOwner, validRepoName, Some(validTreeSha), treeDataList, sampleToken)
      .returns(response)

    val ghGitData = new GHGitData(sampleToken)(gitDataOps)
    ghGitData.createTree(validRepoOwner, validRepoName, Some(validTreeSha), treeDataList)
  }

  "GHGitData.createTag" should "call to GitDataOps with the right parameters" in {

    val response: Free[GitHub4s, GHResponse[Tag]] =
      Free.pure(Right(GHResult(tag, okStatusCode, Map.empty)))

    val gitDataOps = mock[GitDataOpsTest]
    (gitDataOps.createTag _)
      .expects(
        validRepoOwner,
        validRepoName,
        validTagTitle,
        validNote,
        validCommitSha,
        commitType,
        Some(refCommitAuthor),
        sampleToken)
      .returns(response)

    val ghGitData = new GHGitData(sampleToken)(gitDataOps)
    ghGitData.createTag(
      validRepoOwner,
      validRepoName,
      validTagTitle,
      validNote,
      validCommitSha,
      commitType,
      Some(refCommitAuthor))
  }

}
