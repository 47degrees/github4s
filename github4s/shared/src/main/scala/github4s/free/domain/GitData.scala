/*
 * Copyright 2016-2018 47 Degrees, LLC. <http://www.47deg.com>
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

package github4s.free.domain

class RefInfo(val sha: String, val url: String) {
  override def toString: String = s"RefInfo($sha, $url)"
}

case class Ref(ref: String, url: String, `object`: RefObject)

case class RefObject(`type`: String, override val sha: String, override val url: String)
    extends RefInfo(sha, url)

case class RefCommit(
    sha: String,
    url: String,
    author: RefAuthor,
    committer: RefAuthor,
    message: String,
    tree: RefInfo,
    parents: List[RefInfo])

case class Tag(
  tag: String,
  sha: String,
  url: String,
  message: String,
  tagger: RefAuthor,
  `object`: RefObject)

case class RefAuthor(date: String, name: String, email: String)

sealed abstract class TreeData extends Product with Serializable {
  def path: String
  def mode: String
  def `type`: String
}

case class TreeDataSha(path: String, mode: String, `type`: String, sha: String) extends TreeData

case class TreeDataBlob(path: String, mode: String, `type`: String, content: String) extends TreeData

case class TreeResult(
    override val sha: String,
    override val url: String,
    tree: List[TreeDataResult],
    truncated: Option[Boolean] = None)
    extends RefInfo(sha, url)

case class TreeDataResult(
    path: String,
    mode: String,
    `type`: String,
    size: Option[Int],
    sha: String,
    url: String)

case class NewCommitRequest(message: String, tree: String, parents: List[String], author: Option[RefAuthor])

case class NewBlobRequest(content: String, encoding: Option[String])

case class NewTreeRequest(base_tree: Option[String], tree: List[TreeData])

case class CreateReferenceRequest(ref: String, sha: String)

case class UpdateReferenceRequest(sha: String, force: Boolean)

case class NewTagRequest(tag: String, message: String, `object`: String, `type`: String, tagger: Option[RefAuthor])
