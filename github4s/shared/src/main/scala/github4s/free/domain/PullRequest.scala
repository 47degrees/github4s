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

case class PullRequest(
    id: Int,
    number: Int,
    state: String,
    title: String,
    body: String,
    locked: Boolean,
    html_url: String,
    created_at: String,
    updated_at: Option[String],
    closed_at: Option[String],
    merged_at: Option[String],
    base: Option[PullRequestBase],
    user: Option[User],
    assignee: Option[User])

case class PullRequestBase(
    label: Option[String],
    ref: String,
    sha: String,
    user: User,
    repo: Repository)

sealed abstract class PRFilter(val name: String, val value: String)
    extends Product
    with Serializable {
  def tupled: (String, String) = name -> value
}

sealed abstract class PRFilterState(override val value: String) extends PRFilter("state", value)
case object PRFilterOpen                                        extends PRFilterState("open")
case object PRFilterClosed                                      extends PRFilterState("closed")
case object PRFilterAll                                         extends PRFilterState("all")

case class PRFilterHead(override val value: String) extends PRFilter("head", value)

case class PRFilterBase(override val value: String) extends PRFilter("base", value)

sealed abstract class PRFilterSort(override val value: String) extends PRFilter("sort", value)
case object PRFilterSortCreated                                    extends PRFilterState("created")
case object PRFilterSortUpdated                                    extends PRFilterState("updated")
case object PRFilterSortPopularity                                 extends PRFilterState("popularity")
case object PRFilterSortLongRunning                                extends PRFilterState("long-running")

sealed abstract class PRFilterDirection(override val value: String)
    extends PRFilter("direction", value)
case object PRFilterOrderAsc  extends PRFilterState("asc")
case object PRFilterOrderDesc extends PRFilterState("desc")
