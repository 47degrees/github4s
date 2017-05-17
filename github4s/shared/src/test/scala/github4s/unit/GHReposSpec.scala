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
import github4s.GHRepos
import github4s.GithubResponses.{GHResponse, GHResult}
import github4s.app.GitHub4s
import github4s.free.domain._
import github4s.utils.BaseSpec

class GHReposSpec extends BaseSpec {

  "GHRepos.get" should "call to RepositoryOps with the right parameters" in {

    val response: Free[GitHub4s, GHResponse[Repository]] =
      Free.pure(Right(GHResult(repo, okStatusCode, Map.empty)))

    val repoOps = mock[RepositoryOpsTest]
    (repoOps.getRepo _)
      .expects(validRepoOwner, validRepoName, sampleToken)
      .returns(response)

    val ghReposData = new GHRepos(sampleToken)(repoOps)
    ghReposData.get(validRepoOwner, validRepoName)
  }

  "GHRepos.contents" should "call to RepositoryOps with the right parameters" in {

    val response: Free[GitHub4s, GHResponse[NonEmptyList[Content]]] =
      Free.pure(Right(GHResult(NonEmptyList(content, Nil), okStatusCode, Map.empty)))

    val repoOps = mock[RepositoryOpsTest]
    (repoOps.getContents _)
      .expects(validRepoOwner, validRepoName, validFilePath, Some("master"), sampleToken)
      .returns(response)

    val ghReposData = new GHRepos(sampleToken)(repoOps)
    ghReposData.getContents(validRepoOwner, validRepoName, validFilePath, Some("master"))
  }

  "GHRepos.listCommits" should "call to RepositoryOps with the right parameters" in {

    val response: Free[GitHub4s, GHResponse[List[Commit]]] =
      Free.pure(Right(GHResult(List(commit), okStatusCode, Map.empty)))

    val repoOps = mock[RepositoryOpsTest]
    (repoOps.listCommits _)
      .expects(validRepoOwner, validRepoName, None, None, None, None, None, None, sampleToken)
      .returns(response)

    val ghReposData = new GHRepos(sampleToken)(repoOps)
    ghReposData.listCommits(validRepoOwner, validRepoName)
  }

  "GHRepos.listContributors" should "call to RepositoryOps with the right parameters" in {

    val response: Free[GitHub4s, GHResponse[List[User]]] =
      Free.pure(Right(GHResult(List(user), okStatusCode, Map.empty)))

    val repoOps = mock[RepositoryOpsTest]
    (repoOps.listContributors _)
      .expects(validRepoOwner, validRepoName, None, sampleToken)
      .returns(response)

    val ghReposData = new GHRepos(sampleToken)(repoOps)
    ghReposData.listContributors(validRepoOwner, validRepoName)
  }

  "GHRepos.createRelease" should "call to RepositoryOps with the right parameters" in {

    val response: Free[GitHub4s, GHResponse[Release]] =
      Free.pure(Right(GHResult(release, createdStatusCode, Map.empty)))

    val repoOps = mock[RepositoryOpsTest]
    (repoOps.createRelease _)
      .expects(
        validRepoOwner,
        validRepoName,
        validTagTitle,
        validTagTitle,
        validNote,
        Some("master"),
        Some(false),
        Some(false),
        sampleToken)
      .returns(response)

    val ghReposData = new GHRepos(sampleToken)(repoOps)
    ghReposData.createRelease(
      validRepoOwner,
      validRepoName,
      validTagTitle,
      validTagTitle,
      validNote,
      Some("master"),
      Some(false),
      Some(false))
  }

  "GHRepos.getStatus" should "call to RepositoryOps with the right parameters" in {
    val response: Free[GitHub4s, GHResponse[CombinedStatus]] =
      Free.pure(Right(GHResult(combinedStatus, okStatusCode, Map.empty)))

    val repoOps = mock[RepositoryOpsTest]
    (repoOps.getCombinedStatus _)
      .expects(validRepoOwner, validRepoName, validRefSingle, sampleToken)
      .returns(response)
    val ghReposData = new GHRepos(sampleToken)(repoOps)
    ghReposData.getCombinedStatus(validRepoOwner, validRepoName, validRefSingle)
  }

  "GHRepos.listStatus" should "call to RepositoryOps with the right parameters" in {
    val response: Free[GitHub4s, GHResponse[List[Status]]] =
      Free.pure(Right(GHResult(List(status), okStatusCode, Map.empty)))

    val repoOps = mock[RepositoryOpsTest]
    (repoOps.listStatus _)
      .expects(validRepoOwner, validRepoName, validRefSingle, sampleToken)
      .returns(response)
    val ghReposData = new GHRepos(sampleToken)(repoOps)
    ghReposData.listStatus(validRepoOwner, validRepoName, validRefSingle)
  }

  "GHRepos.createStatus" should "call to RepositoryOps with the right parameters" in {
    val response: Free[GitHub4s, GHResponse[Status]] =
      Free.pure(Right(GHResult(status, createdStatusCode, Map.empty)))

    val repoOps = mock[RepositoryOpsTest]
    (repoOps.createStatus _)
      .expects(
        validRepoOwner,
        validRepoName,
        validCommitSha,
        validStatusState,
        None,
        None,
        None,
        sampleToken)
      .returns(response)
    val ghReposData = new GHRepos(sampleToken)(repoOps)
    ghReposData
      .createStatus(
        validRepoOwner,
        validRepoName,
        validCommitSha,
        validStatusState,
        None,
        None,
        None)
  }
}
