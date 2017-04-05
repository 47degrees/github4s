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
import github4s.free.algebra.RepositoryOps
import github4s.free.domain._
import github4s.utils.TestUtils
import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito._
import org.scalatest.mockito.MockitoSugar.mock
import org.scalatest.{FlatSpec, Matchers}

class GHReposSpec extends FlatSpec with Matchers with TestUtils {

  "GHRepos.contents" should "call to RepositoryOps with the right parameters" in {

    val response: Free[GitHub4s, GHResponse[NonEmptyList[Content]]] =
      Free.pure(Right(GHResult(NonEmptyList(content, Nil), okStatusCode, Map.empty)))

    val repoOps = mock[RepositoryOps[GitHub4s]]
    when(
      repoOps.getContents(
        any[String],
        any[String],
        any[String],
        any[Option[String]],
        any[Option[String]]))
      .thenReturn(response)

    val token       = Some("token")
    val ghReposData = new GHRepos(token)(repoOps)
    ghReposData.getContents(validRepoOwner, validRepoName, validFilePath, Some("master"))

    verify(repoOps).getContents(
      validRepoOwner,
      validRepoName,
      validFilePath,
      Some("master"),
      token)
  }

  "GHRepos.createRelease" should "call to RepositoryOps with the right parameters" in {

    val response: Free[GitHub4s, GHResponse[Release]] =
      Free.pure(Right(GHResult(release, createdStatusCode, Map.empty)))

    val repoOps = mock[RepositoryOps[GitHub4s]]
    when(
      repoOps.createRelease(
        any[String],
        any[String],
        any[String],
        any[String],
        any[String],
        any[Option[String]],
        any[Option[Boolean]],
        any[Option[Boolean]],
        any[Option[String]]))
      .thenReturn(response)

    val token       = Some("token")
    val ghReposData = new GHRepos(token)(repoOps)
    ghReposData.createRelease(
      validRepoOwner,
      validRepoName,
      validTagTitle,
      validTagTitle,
      validNote,
      Some("master"),
      Some(false),
      Some(false))

    verify(repoOps).createRelease(
      validRepoOwner,
      validRepoName,
      validTagTitle,
      validTagTitle,
      validNote,
      Some("master"),
      Some(false),
      Some(false),
      token)
  }

}
