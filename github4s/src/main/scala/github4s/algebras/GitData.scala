/*
 * Copyright 2016-2020 47 Degrees Open Source <https://www.47deg.com>
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

package github4s.algebras

import cats.data.NonEmptyList
import github4s.GHResponse
import github4s.domain._

trait GitData[F[_]] {

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
   * @param owner of the repo
   * @param repo name of the repo
   * @param ref ref formatted as heads/branch
   * @param headers optional user headers to include in the request
   * @return a GHResponse with the Ref list
   */
  def getReference(
      owner: String,
      repo: String,
      ref: String,
      headers: Map[String, String] = Map()
  ): F[GHResponse[NonEmptyList[Ref]]]

  /**
   * Create a Reference
   *
   * The ref in the URL must be formatted as `heads/branch`, not just branch.
   * For example, the call to get the data for `master` branch will be `heads/master`.
   *
   * @param owner of the repo
   * @param repo name of the repo
   * @param ref The name of the fully qualified reference (ie: refs/heads/master).
   * If it doesn't start with 'refs' and have at least two slashes, it will be rejected.
   * @param sha the SHA1 value to set this reference to
   * @param headers optional user headers to include in the request
   * @return a GHResponse with the Ref
   */
  def createReference(
      owner: String,
      repo: String,
      ref: String,
      sha: String,
      headers: Map[String, String] = Map()
  ): F[GHResponse[Ref]]

  /**
   * Update a Reference
   *
   * @param owner of the repo
   * @param repo name of the repo
   * @param ref ref formatted as heads/branch
   * @param sha the SHA1 value to set this reference to
   * @param force Indicates whether to force the update or to make sure the update is a fast-forward update.
   * Leaving this out or setting it to `false` will make sure you're not overwriting work. Default: `false`
   * @param headers optional user headers to include in the request
   * @return a GHResponse with the Ref
   */
  def updateReference(
      owner: String,
      repo: String,
      ref: String,
      sha: String,
      force: Boolean,
      headers: Map[String, String] = Map()
  ): F[GHResponse[Ref]]

  /**
   * Get a Commit by sha
   *
   * @param owner of the repo
   * @param repo name of the repo
   * @param sha the sha of the commit
   * @param headers optional user headers to include in the request
   * @return a GHResponse with the Commit
   */
  def getCommit(
      owner: String,
      repo: String,
      sha: String,
      headers: Map[String, String] = Map()
  ): F[GHResponse[RefCommit]]

  /**
   * Create a new Commit
   *
   * @param owner of the repo
   * @param repo name of the repo
   * @param message the new commit's message.
   * @param tree the SHA of the tree object this commit points to
   * @param parents the SHAs of the commits that were the parents of this commit. If omitted or empty,
   * the commit will be written as a root commit. For a single parent, an array of one SHA should be provided;
   * for a merge commit, an array of more than one should be provided.
   * @param author object containing information about the author.
   * @param headers optional user headers to include in the request
   * @return a GHResponse with RefCommit
   */
  def createCommit(
      owner: String,
      repo: String,
      message: String,
      tree: String,
      parents: List[String],
      author: Option[RefAuthor],
      headers: Map[String, String] = Map()
  ): F[GHResponse[RefCommit]]

  /**
   * Create a new Blob
   *
   * @param owner of the repo
   * @param repo name of the repo
   * @param content the new blob's content.
   * @param encoding the encoding used for content. Currently, "utf-8" and "base64" are supported. Default: "utf-8".
   * @param headers optional user headers to include in the request
   * @return a GHResponse with RefObject
   */
  def createBlob(
      owner: String,
      repo: String,
      content: String,
      encoding: Option[String],
      headers: Map[String, String] = Map()
  ): F[GHResponse[RefInfo]]

  /**
   * Get a Tree by sha
   *
   * @param owner of the repo
   * @param repo name of the repo
   * @param sha the sha of the tree
   * @param recursive flag whether to get the tree recursively
   * @param headers optional user headers to include in the request
   * @return a GHResponse with the Tree
   */
  def getTree(
      owner: String,
      repo: String,
      sha: String,
      recursive: Boolean,
      headers: Map[String, String] = Map()
  ): F[GHResponse[TreeResult]]

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
   * @param headers optional user headers to include in the request
   * @return a GHResponse with TreeResult
   */
  def createTree(
      owner: String,
      repo: String,
      baseTree: Option[String],
      treeDataList: List[TreeData],
      headers: Map[String, String] = Map()
  ): F[GHResponse[TreeResult]]

  /**
   * Create a Tag
   *
   * @param owner of the repo
   * @param repo name of the repo
   * @param tag the tag.
   * @param message the new tag message.
   * @param objectSha the SHA of the git object this is tagging
   * @param objectType the type of the object we're tagging.
   * Normally this is a `commit` but it can also be a `tree` or a `blob`.
   * @param author object containing information about the individual creating the tag.
   * @param headers optional user headers to include in the request
   * @return a GHResponse with Tag
   */
  def createTag(
      owner: String,
      repo: String,
      tag: String,
      message: String,
      objectSha: String,
      objectType: String,
      author: Option[RefAuthor] = None,
      headers: Map[String, String] = Map()
  ): F[GHResponse[Tag]]
}
