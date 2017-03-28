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
    author: RefCommitAuthor,
    committer: RefCommitAuthor,
    message: String,
    tree: RefInfo,
    parents: List[RefInfo])

case class RefCommitAuthor(date: String, name: String, email: String)

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
    tree: List[TreeDataResult])
    extends RefInfo(sha, url)

case class TreeDataResult(
    path: String,
    mode: String,
    `type`: String,
    size: Option[Int],
    sha: String,
    url: String)

case class NewCommitRequest(message: String, tree: String, parents: List[String], author: Option[RefCommitAuthor])

case class NewBlobRequest(content: String, encoding: Option[String])

case class NewTreeRequest(base_tree: Option[String], tree: List[TreeData])

case class UpdateReferenceRequest(sha: String, force: Option[Boolean])
