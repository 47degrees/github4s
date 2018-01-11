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
    user: Option[User],
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

case class Comment(
    id: Int,
    url: String,
    html_url: String,
    body: String,
    user: Option[User],
    created_at: String,
    updated_at: String)

case class CommentData(body: String)
