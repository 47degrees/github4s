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

package github4s

import cats.data.NonEmptyList
import github4s.GithubResponses.{GHIO, GHResponse}
import github4s.app._
import github4s.free.algebra._
import github4s.free.domain._

class GHUsers(accessToken: Option[String] = None)(implicit O: UserOps[GitHub4s]) {

  def get(username: String): GHIO[GHResponse[User]] = O.getUser(username, accessToken)

  def getAuth: GHIO[GHResponse[User]] = O.getAuthUser(accessToken)

  def getUsers(since: Int, pagination: Option[Pagination] = None): GHIO[GHResponse[List[User]]] =
    O.getUsers(since, pagination, accessToken)

}

class GHRepos(accessToken: Option[String] = None)(implicit O: RepositoryOps[GitHub4s]) {

  def get(owner: String, repo: String): GHIO[GHResponse[Repository]] =
    O.getRepo(owner, repo, accessToken)

  def getContents(
      owner: String,
      repo: String,
      path: String,
      ref: Option[String] = None): GHIO[GHResponse[NonEmptyList[Content]]] =
    O.getContents(owner, repo, path, ref, accessToken)

  def listCommits(
      owner: String,
      repo: String,
      sha: Option[String] = None,
      path: Option[String] = None,
      author: Option[String] = None,
      since: Option[String] = None,
      until: Option[String] = None,
      pagination: Option[Pagination] = None
  ): GHIO[GHResponse[List[Commit]]] =
    O.listCommits(owner, repo, sha, path, author, since, until, pagination, accessToken)

  def listContributors(
      owner: String,
      repo: String,
      anon: Option[String] = None
  ): GHIO[GHResponse[List[User]]] =
    O.listContributors(owner, repo, anon, accessToken)

  def createRelease(
      owner: String,
      repo: String,
      tagName: String,
      name: String,
      body: String,
      targetCommitish: Option[String] = None,
      draft: Option[Boolean] = None,
      prerelease: Option[Boolean] = None
  ): GHIO[GHResponse[Release]] =
    O.createRelease(
      owner,
      repo,
      tagName,
      name,
      body,
      targetCommitish,
      draft,
      prerelease,
      accessToken)

}

class GHAuth(accessToken: Option[String] = None)(implicit O: AuthOps[GitHub4s]) {

  def newAuth(
      username: String,
      password: String,
      scopes: List[String],
      note: String,
      client_id: String,
      client_secret: String
  ): GHIO[GHResponse[Authorization]] =
    O.newAuth(username, password, scopes, note, client_id, client_secret)

  def authorizeUrl(
      client_id: String,
      redirect_uri: String,
      scopes: List[String]
  ): GHIO[GHResponse[Authorize]] =
    O.authorizeUrl(client_id, redirect_uri, scopes)

  def getAccessToken(
      client_id: String,
      client_secret: String,
      code: String,
      redirect_uri: String,
      state: String
  ): GHIO[GHResponse[OAuthToken]] =
    O.getAccessToken(client_id, client_secret, code, redirect_uri, state)
}

class GHGists(accessToken: Option[String] = None)(implicit O: GistOps[GitHub4s]) {
  def newGist(
      description: String,
      public: Boolean,
      files: Map[String, GistFile]
  ): GHIO[GHResponse[Gist]] =
    O.newGist(description, public, files, accessToken)
}

class GHIssues(accessToken: Option[String] = None)(implicit O: IssueOps[GitHub4s]) {

  def listIssues(
      owner: String,
      repo: String
  ): GHIO[GHResponse[List[Issue]]] =
    O.listIssues(owner, repo, accessToken)

  def searchIssues(
      query: String,
      searchParams: List[SearchParam]
  ): GHIO[GHResponse[SearchIssuesResult]] =
    O.searchIssues(query, searchParams, accessToken)

  def createIssue(
      owner: String,
      repo: String,
      title: String,
      body: String,
      milestone: Option[Int] = None,
      labels: List[String] = List.empty,
      assignees: List[String] = List.empty
  ): GHIO[GHResponse[Issue]] =
    O.createIssue(owner, repo, title, body, milestone, labels, assignees, accessToken)

  def editIssue(
      owner: String,
      repo: String,
      issue: Int,
      state: String,
      title: String,
      body: String,
      milestone: Option[Int] = None,
      labels: List[String] = List.empty,
      assignees: List[String] = List.empty
  ): GHIO[GHResponse[Issue]] =
    O.editIssue(owner, repo, issue, state, title, body, milestone, labels, assignees, accessToken)
}

class GHGitData(accessToken: Option[String] = None)(implicit O: GitDataOps[GitHub4s]) {

  def getReference(
      owner: String,
      repo: String,
      ref: String
  ): GHIO[GHResponse[NonEmptyList[Ref]]] =
    O.getReference(owner, repo, ref, accessToken)

  def createReference(
      owner: String,
      repo: String,
      ref: String,
      sha: String
  ): GHIO[GHResponse[Ref]] =
    O.createReference(owner, repo, ref, sha, accessToken)

  def updateReference(
      owner: String,
      repo: String,
      ref: String,
      sha: String,
      force: Option[Boolean] = None
  ): GHIO[GHResponse[Ref]] =
    O.updateReference(owner, repo, ref, sha, force, accessToken)

  def getCommit(
      owner: String,
      repo: String,
      sha: String
  ): GHIO[GHResponse[RefCommit]] =
    O.getCommit(owner, repo, sha, accessToken)

  def createCommit(
      owner: String,
      repo: String,
      message: String,
      tree: String,
      parents: List[String] = Nil,
      author: Option[RefAuthor] = None
  ): GHIO[GHResponse[RefCommit]] =
    O.createCommit(owner, repo, message, tree, parents, author, accessToken)

  def createBlob(
      owner: String,
      repo: String,
      content: String,
      encoding: Option[String]
  ): GHIO[GHResponse[RefInfo]] =
    O.createBlob(owner, repo, content, encoding, accessToken)

  def createTree(
      owner: String,
      repo: String,
      baseTree: Option[String],
      treeDataList: List[TreeData]
  ): GHIO[GHResponse[TreeResult]] =
    O.createTree(owner, repo, baseTree, treeDataList, accessToken)

  def createTag(
      owner: String,
      repo: String,
      tag: String,
      message: String,
      objectSha: String,
      objectType: String,
      author: Option[RefAuthor] = None
  ): GHIO[GHResponse[Tag]] =
    O.createTag(owner, repo, tag, message, objectSha, objectType, author, accessToken)
}

class GHPullRequests(accessToken: Option[String] = None)(implicit O: PullRequestOps[GitHub4s]) {
  def list(
      owner: String,
      repo: String,
      filters: List[PRFilter] = Nil
  ): GHIO[GHResponse[List[PullRequest]]] =
    O.listPullRequests(owner, repo, filters, accessToken)
}
