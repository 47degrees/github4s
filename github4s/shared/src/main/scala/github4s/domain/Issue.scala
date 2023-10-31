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

package github4s.domain

import java.time.ZonedDateTime

final case class Issue(
    id: Long,
    title: String,
    url: String,
    repository_url: String,
    labels_url: String,
    comments_url: String,
    events_url: String,
    html_url: String,
    number: Int,
    state: String,
    labels: List[Label] = List.empty,
    comments: Int,
    created_at: String,
    updated_at: String,
    body: Option[String] = None,
    user: Option[User] = None,
    assignee: Option[User] = None,
    locked: Option[Boolean] = None,
    pull_request: Option[IssuePullRequest] = None,
    closed_at: Option[String] = None
)

final case class Label(
    name: String,
    color: String,
    id: Option[Long] = None,
    description: Option[String] = None,
    url: Option[String] = None,
    default: Option[Boolean] = None
)

final case class IssuePullRequest(
    url: Option[String] = None,
    html_url: Option[String] = None,
    diff_url: Option[String] = None,
    patch_url: Option[String] = None
)

final case class SearchIssuesResult(
    total_count: Int,
    incomplete_results: Boolean,
    items: List[Issue]
)

final case class NewIssueRequest(
    title: String,
    body: String,
    labels: List[String],
    assignees: List[String],
    milestone: Option[Int] = None
)

final case class EditIssueRequest(
    state: String,
    title: String,
    body: String,
    labels: List[String],
    assignees: List[String],
    milestone: Option[Int] = None
)

final case class Comment(
    id: Long,
    url: String,
    html_url: String,
    body: String,
    created_at: String,
    updated_at: String,
    user: Option[User] = None
)

final case class CommentData(body: String)

final case class Milestone(
    url: String,
    html_url: String,
    labels_url: String,
    id: Long,
    node_id: String,
    number: Int,
    state: String,
    title: String,
    description: String,
    creator: Creator,
    open_issues: Int,
    closed_issues: Int,
    created_at: String,
    updated_at: String,
    closed_at: Option[String] = None,
    due_on: Option[String] = None
)

final case class MilestoneData(
    title: String,
    state: Option[String] = None,
    description: Option[String] = None,
    due_on: Option[ZonedDateTime] = None
)
