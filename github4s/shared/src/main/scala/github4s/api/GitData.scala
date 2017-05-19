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

package github4s.api

import cats.data.NonEmptyList
import github4s.GithubResponses.GHResponse
import github4s.{Decoders, Encoders, GithubApiUrls, HttpClient, HttpRequestBuilderExtension}
import github4s.free.domain._
import github4s.free.interpreters.Capture
import io.circe.syntax._
import io.circe.generic.auto._

import scala.language.higherKinds

/**
 * Factory that encapsulates all the Git Database API calls
 */
class GitData[C, M[_]](
    implicit urls: GithubApiUrls,
    C: Capture[M],
    httpClientImpl: HttpRequestBuilderExtension[C, M]) {

  import Decoders._
  import Encoders._

  val httpClient = new HttpClient[C, M]

  /**
   * Get a Reference by name
   *
   * The ref in the URL must be formatted as `heads/branch`, not just branch.
   * For example, the call to get the data for `master` branch will be `heads/master`.
   *
   * If the `ref` doesn't exist in the repository, but existing `refs` start with `ref` they will be
   * returned as an array. For example, a call to get the data for a branch named `feature`,
   * which doesn't exist, would return head refs including `featureA` and `featureB` which do.
   *
   * @param accessToken to identify the authenticated user
   * @param headers optional user headers to include in the request
   * @param owner of the repo
   * @param repo name of the repo
   * @param ref ref formatted as heads/branch
   * @return a GHResponse with the Ref list
   */
  def reference(
      accessToken: Option[String] = None,
      headers: Map[String, String] = Map(),
      owner: String,
      repo: String,
      ref: String): M[GHResponse[NonEmptyList[Ref]]] =
    httpClient.get[NonEmptyList[Ref]](accessToken, s"repos/$owner/$repo/git/refs/$ref", headers)

  /**
   * Create a Reference
   *
   * The ref in the URL must be formatted as `heads/branch`, not just branch.
   * For example, the call to get the data for `master` branch will be `heads/master`.
   *
   * @param accessToken to identify the authenticated user
   * @param headers optional user headers to include in the request
   * @param owner of the repo
   * @param repo name of the repo
   * @param ref The name of the fully qualified reference (ie: refs/heads/master).
   * If it doesn't start with 'refs' and have at least two slashes, it will be rejected.
   * @param sha the SHA1 value to set this reference to
   * @return a GHResponse with the Ref
   */
  def createReference(
      accessToken: Option[String] = None,
      headers: Map[String, String] = Map(),
      owner: String,
      repo: String,
      ref: String,
      sha: String): M[GHResponse[Ref]] =
    httpClient.post[Ref](
      accessToken,
      s"repos/$owner/$repo/git/refs",
      headers,
      dropNullPrint(CreateReferenceRequest(ref, sha).asJson))

  /**
   * Update a Reference
   *
   * @param accessToken to identify the authenticated user
   * @param headers optional user headers to include in the request
   * @param owner of the repo
   * @param repo name of the repo
   * @param ref ref formatted as heads/branch
   * @param sha the SHA1 value to set this reference to
   * @param force Indicates whether to force the update or to make sure the update is a fast-forward update.
   * Leaving this out or setting it to `false` will make sure you're not overwriting work. Default: `false`
   * @return a GHResponse with the Ref
   */
  def updateReference(
      accessToken: Option[String] = None,
      headers: Map[String, String] = Map(),
      owner: String,
      repo: String,
      ref: String,
      sha: String,
      force: Boolean = false): M[GHResponse[Ref]] =
    httpClient.patch[Ref](
      accessToken,
      s"repos/$owner/$repo/git/refs/$ref",
      headers,
      dropNullPrint(UpdateReferenceRequest(sha, force).asJson))

  /**
   * Get a Commit by sha
   *
   * @param accessToken to identify the authenticated user
   * @param headers optional user headers to include in the request
   * @param owner of the repo
   * @param repo name of the repo
   * @param sha the sha of the commit
   * @return a GHResponse with the Commit
   */
  def commit(
      accessToken: Option[String] = None,
      headers: Map[String, String] = Map(),
      owner: String,
      repo: String,
      sha: String): M[GHResponse[RefCommit]] =
    httpClient.get[RefCommit](accessToken, s"repos/$owner/$repo/git/commits/$sha", headers)

  /**
   * Create a new Commit
   *
   * @param accessToken to identify the authenticated user
   * @param headers optional user headers to include in the request
   * @param owner of the repo
   * @param repo name of the repo
   * @param message the new commit's message.
   * @param tree the SHA of the tree object this commit points to
   * @param parents the SHAs of the commits that were the parents of this commit. If omitted or empty,
   * the commit will be written as a root commit. For a single parent, an array of one SHA should be provided;
   * for a merge commit, an array of more than one should be provided.
   * @param author object containing information about the author.
   * @return a GHResponse with RefCommit
   */
  def createCommit(
      accessToken: Option[String] = None,
      headers: Map[String, String] = Map(),
      owner: String,
      repo: String,
      message: String,
      tree: String,
      parents: List[String] = Nil,
      author: Option[RefAuthor] = None): M[GHResponse[RefCommit]] =
    httpClient.post[RefCommit](
      accessToken,
      s"repos/$owner/$repo/git/commits",
      headers,
      dropNullPrint(NewCommitRequest(message, tree, parents, author).asJson))

  /**
   * Create a new Blob
   *
   * @param accessToken to identify the authenticated user
   * @param headers optional user headers to include in the request
   * @param owner of the repo
   * @param repo name of the repo
   * @param content the new blob's content.
   * @param encoding the encoding used for content. Currently, "utf-8" and "base64" are supported. Default: "utf-8".
   * @return a GHResponse with RefObject
   */
  def createBlob(
      accessToken: Option[String] = None,
      headers: Map[String, String] = Map(),
      owner: String,
      repo: String,
      content: String,
      encoding: Option[String] = None): M[GHResponse[RefInfo]] =
    httpClient.post[RefInfo](
      accessToken,
      s"repos/$owner/$repo/git/blobs",
      headers,
      dropNullPrint(NewBlobRequest(content, encoding).asJson))

  /**
   * Create a new Tree
   *
   * The tree creation API will take nested entries as well. If both a tree and a nested path modifying
   * that tree are specified, it will overwrite the contents of that tree with the new path contents
   * and write a new tree out.
   *
   * IMPORTANT: If you don't set the baseTree, the commit will be created on top of everything;
   * however, it will only contain your change, the rest of your files will show up as deleted.
   *
   * @param accessToken to identify the authenticated user
   * @param headers optional user headers to include in the request
   * @param owner of the repo
   * @param repo name of the repo
   * @param baseTree the SHA1 of the tree you want to update with new data.
   * @param treeDataList list (of path, mode, type, and sha/blob) specifying a tree structure:
   *  - path: The file referenced in the tree
   *  - mode: The file mode; one of 100644 for file (blob), 100755 for executable (blob),
   *  040000 for subdirectory (tree), 160000 for submodule (commit),
   *  or 120000 for a blob that specifies the path of a symlink
   *  - type	string	Either blob, tree, or commit
   *  - sha	string	The SHA1 checksum ID of the object in the tree
   *  - content	string	The content you want this file to have.
   *  GitHub will write this blob out and use that SHA for this entry. Use either this, or tree.sha.
   * @return a GHResponse with TreeResult
   */
  def createTree(
      accessToken: Option[String] = None,
      headers: Map[String, String] = Map(),
      owner: String,
      repo: String,
      baseTree: Option[String],
      treeDataList: List[TreeData]): M[GHResponse[TreeResult]] =
    httpClient.post[TreeResult](
      accessToken,
      s"repos/$owner/$repo/git/trees",
      headers,
      dropNullPrint(NewTreeRequest(baseTree, treeDataList).asJson))

  /**
   * Create a Tag
   *
   * @param accessToken to identify the authenticated user
   * @param headers optional user headers to include in the request
   * @param owner of the repo
   * @param repo name of the repo
   * @param tag the tag.
   * @param message the new tag message.
   * @param objectSha the SHA of the git object this is tagging
   * @param objectType the type of the object we're tagging.
   * Normally this is a `commit` but it can also be a `tree` or a `blob`.
   * @param tagger object containing information about the individual creating the tag.
   * @return a GHResponse with Tag
   */
  def createTag(
      accessToken: Option[String] = None,
      headers: Map[String, String] = Map(),
      owner: String,
      repo: String,
      tag: String,
      message: String,
      objectSha: String,
      objectType: String,
      tagger: Option[RefAuthor] = None): M[GHResponse[Tag]] =
    httpClient.post[Tag](
      accessToken,
      s"repos/$owner/$repo/git/tags",
      headers,
      dropNullPrint(NewTagRequest(tag, message, objectSha, objectType, tagger).asJson))

}
