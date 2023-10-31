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

package github4s.interpreters

import cats.Functor
import cats.data.NonEmptyList
import cats.syntax.functor._
import github4s.Decoders._
import github4s.Encoders._
import github4s.GHResponse
import github4s.algebras.Repositories
import github4s.domain.RepoUrlKeys.CommitComparisonResponse
import github4s.domain._
import github4s.http.HttpClient
import github4s.internal.Base64._

class RepositoriesInterpreter[F[_]: Functor](implicit
    client: HttpClient[F]
) extends Repositories[F] {
  override def get(
      owner: String,
      repo: String,
      headers: Map[String, String]
  ): F[GHResponse[Repository]] =
    client.get[Repository](s"repos/$owner/$repo", headers)

  override def listOrgRepos(
      org: String,
      `type`: Option[String],
      pagination: Option[Pagination],
      headers: Map[String, String]
  ): F[GHResponse[List[Repository]]] =
    client.get[List[Repository]](
      s"orgs/$org/repos",
      headers,
      `type`.fold(Map.empty[String, String])(t => Map("type" -> t)),
      pagination
    )

  override def listUserRepos(
      user: String,
      `type`: Option[String],
      pagination: Option[Pagination],
      headers: Map[String, String]
  ): F[GHResponse[List[Repository]]] =
    client.get[List[Repository]](
      s"users/$user/repos",
      headers,
      `type`.fold(Map.empty[String, String])(t => Map("type" -> t)),
      pagination
    )

  override def searchRepos(
      query: String,
      searchParams: List[SearchParam],
      pagination: Option[Pagination],
      headers: Map[String, String] = Map.empty
  ): F[GHResponse[SearchReposResult]] =
    client.get[SearchReposResult](
      "search/repositories",
      headers,
      params = Map("q" -> s"$query+${searchParams.map(_.value).mkString("+")}"),
      pagination
    )

  override def getContents(
      owner: String,
      repo: String,
      path: String,
      ref: Option[String],
      pagination: Option[Pagination],
      headers: Map[String, String]
  ): F[GHResponse[NonEmptyList[Content]]] =
    client.get[NonEmptyList[Content]](
      s"repos/$owner/$repo/contents/$path",
      headers,
      ref.fold(Map.empty[String, String])(r => Map("ref" -> r)),
      pagination
    )

  override def createFile(
      owner: String,
      repo: String,
      path: String,
      message: String,
      content: Array[Byte],
      branch: Option[String],
      committer: Option[Committer],
      author: Option[Committer],
      headers: Map[String, String]
  ): F[GHResponse[WriteFileResponse]] =
    client.put[WriteFileRequest, WriteFileResponse](
      s"repos/$owner/$repo/contents/$path",
      headers,
      WriteFileRequest(message, content.toBase64, None, branch, committer, author)
    )

  override def updateFile(
      owner: String,
      repo: String,
      path: String,
      message: String,
      content: Array[Byte],
      sha: String,
      branch: Option[String],
      committer: Option[Committer],
      author: Option[Committer],
      headers: Map[String, String]
  ): F[GHResponse[WriteFileResponse]] =
    client.put[WriteFileRequest, WriteFileResponse](
      s"repos/$owner/$repo/contents/$path",
      headers,
      WriteFileRequest(message, content.toBase64, Some(sha), branch, committer, author)
    )

  override def deleteFile(
      owner: String,
      repo: String,
      path: String,
      message: String,
      sha: String,
      branch: Option[String],
      committer: Option[Committer],
      author: Option[Committer],
      headers: Map[String, String]
  ): F[GHResponse[WriteFileResponse]] =
    client.deleteWithBody[DeleteFileRequest, WriteFileResponse](
      s"repos/$owner/$repo/contents/$path",
      headers,
      DeleteFileRequest(message, sha, branch, committer, author)
    )

  override def listCommits(
      owner: String,
      repo: String,
      sha: Option[String],
      path: Option[String],
      author: Option[String],
      since: Option[String],
      until: Option[String],
      pagination: Option[Pagination],
      headers: Map[String, String]
  ): F[GHResponse[List[Commit]]] =
    client.get[List[Commit]](
      s"repos/$owner/$repo/commits",
      headers,
      Map(
        "sha"    -> sha,
        "path"   -> path,
        "author" -> author,
        "since"  -> since,
        "until"  -> until
      ).collect { case (key, Some(value)) =>
        key -> value
      },
      pagination
    )

  override def compareCommits(
      owner: String,
      repo: String,
      commitSha: String,
      baseSha: String,
      headers: Map[String, String] = Map()
  ): F[GHResponse[CommitComparisonResponse]] =
    client.get[CommitComparisonResponse](
      s"repos/$owner/$repo/compare/$baseSha...$commitSha",
      headers
    )

  override def listBranches(
      owner: String,
      repo: String,
      onlyProtected: Option[Boolean],
      pagination: Option[Pagination],
      headers: Map[String, String]
  ): F[GHResponse[List[Branch]]] =
    client.get[List[Branch]](
      s"repos/$owner/$repo/branches",
      headers,
      Map(
        "protected" -> onlyProtected.map(_.toString)
      ).collect { case (key, Some(value)) =>
        key -> value
      },
      pagination
    )

  override def listContributors(
      owner: String,
      repo: String,
      anon: Option[String],
      pagination: Option[Pagination],
      headers: Map[String, String]
  ): F[GHResponse[List[User]]] =
    client.get[List[User]](
      s"repos/$owner/$repo/contributors",
      headers,
      Map(
        "anon" -> anon
      ).collect { case (key, Some(value)) =>
        key -> value
      },
      pagination
    )

  override def listCollaborators(
      owner: String,
      repo: String,
      affiliation: Option[String],
      pagination: Option[Pagination],
      headers: Map[String, String]
  ): F[GHResponse[List[User]]] =
    client.get[List[User]](
      s"repos/$owner/$repo/collaborators",
      headers,
      Map(
        "affiliation" -> affiliation
      ).collect { case (key, Some(value)) =>
        key -> value
      },
      pagination
    )

  override def userIsCollaborator(
      owner: String,
      repo: String,
      username: String,
      headers: Map[String, String]
  ): F[GHResponse[Boolean]] =
    client
      .getWithoutResponse(
        s"repos/$owner/$repo/collaborators/$username",
        headers
      )
      .map(handleIsCollaboratorResponse)

  override def getRepoPermissionForUser(
      owner: String,
      repo: String,
      username: String,
      headers: Map[String, String]
  ): F[GHResponse[UserRepoPermission]] =
    client
      .get[UserRepoPermission](
        s"repos/$owner/$repo/collaborators/$username/permission",
        headers,
        Map.empty
      )

  override def latestRelease(
      owner: String,
      repo: String,
      headers: Map[String, String]
  ): F[GHResponse[Option[Release]]] =
    client
      .get[Option[Release]](s"repos/$owner/$repo/releases/latest", headers, Map.empty)

  override def getRelease(
      releaseId: Long,
      owner: String,
      repo: String,
      headers: Map[String, String]
  ): F[GHResponse[Option[Release]]] =
    client
      .get[Option[Release]](
        s"repos/$owner/$repo/releases/$releaseId",
        headers,
        Map.empty
      )

  override def listReleases(
      owner: String,
      repo: String,
      pagination: Option[Pagination],
      headers: Map[String, String]
  ): F[GHResponse[List[Release]]] =
    client.get[List[Release]](
      s"repos/$owner/$repo/releases",
      headers,
      Map.empty,
      pagination
    )

  override def createRelease(
      owner: String,
      repo: String,
      tagName: String,
      name: String,
      body: String,
      targetCommitish: Option[String],
      draft: Option[Boolean],
      prerelease: Option[Boolean],
      headers: Map[String, String]
  ): F[GHResponse[Release]] =
    client.post[NewReleaseRequest, Release](
      s"repos/$owner/$repo/releases",
      headers,
      NewReleaseRequest(tagName, name, body, targetCommitish, draft, prerelease)
    )

  override def getCombinedStatus(
      owner: String,
      repo: String,
      ref: String,
      headers: Map[String, String]
  ): F[GHResponse[CombinedStatus]] =
    client.get[CombinedStatus](s"repos/$owner/$repo/commits/$ref/status", headers)

  override def listStatuses(
      owner: String,
      repo: String,
      ref: String,
      pagination: Option[Pagination],
      headers: Map[String, String]
  ): F[GHResponse[List[Status]]] =
    client.get[List[Status]](
      s"repos/$owner/$repo/commits/$ref/statuses",
      headers,
      pagination = pagination
    )

  override def createStatus(
      owner: String,
      repo: String,
      sha: String,
      state: String,
      target_url: Option[String],
      description: Option[String],
      context: Option[String],
      headers: Map[String, String]
  ): F[GHResponse[Status]] =
    client.post[NewStatusRequest, Status](
      s"repos/$owner/$repo/statuses/$sha",
      headers,
      NewStatusRequest(state, target_url, description, context)
    )

  private def handleIsCollaboratorResponse(response: GHResponse[Unit]): GHResponse[Boolean] =
    response.result match {
      case Right(_) => response.copy(result = Right(true))
      case Left(_) if response.statusCode == 404 =>
        response.copy(result = Right(false))
      case Left(error) => GHResponse(Left(error), response.statusCode, response.headers)
    }
}
