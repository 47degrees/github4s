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

case class Issue(
    id: Int,
    title: String,
    body: String,
    url: String,
    repository_url: String,
    labels_url: String,
    comments_url: String,
    events_url: String,
    html_url: String,
    number: Int,
    state: String,
    user: User,
    assignee: Option[User],
    labels: List[Label] = List.empty,
    locked: Option[Boolean],
    comments: Int,
    pull_request: Option[IssuePullRequest],
    closed_at: Option[String],
    created_at: String,
    updated_at: String)

case class Label(
    id: Option[Int],
    name: String,
    url: String,
    color: String,
    default: Option[Boolean])

case class IssuePullRequest(
    url: Option[String],
    html_url: Option[String],
    diff_url: Option[String],
    patch_url: Option[String])

case class SearchIssuesResult(total_count: Int, incomplete_results: Boolean, items: List[Issue])

case class NewIssueRequest(
    title: String,
    body: String,
    milestone: Option[Int],
    labels: List[String],
    assignees: List[String])

case class EditIssueRequest(
    state: String,
    title: String,
    body: String,
    milestone: Option[Int],
    labels: List[String],
    assignees: List[String])
