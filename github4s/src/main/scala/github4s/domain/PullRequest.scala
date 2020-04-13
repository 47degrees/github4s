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

package github4s.domain

final case class PullRequest(
    id: Int,
    number: Int,
    state: String,
    title: String,
    body: Option[String],
    locked: Boolean,
    html_url: String,
    created_at: String,
    updated_at: Option[String],
    closed_at: Option[String],
    merged_at: Option[String],
    merge_commit_sha: Option[String],
    base: Option[PullRequestBase],
    head: Option[PullRequestBase],
    user: Option[User],
    assignee: Option[User]
)

final case class PullRequestBase(
    label: Option[String],
    ref: String,
    sha: String,
    user: Option[User],
    repo: Option[Repository]
)

final case class PullRequestFile(
    sha: String,
    filename: String,
    status: String,
    additions: Int,
    deletions: Int,
    changes: Int,
    blob_url: String,
    raw_url: String,
    contents_url: String,
    patch: Option[String],
    previous_filename: Option[String]
)
sealed trait CreatePullRequest {
  def head: String
  def base: String
  def maintainer_can_modify: Option[Boolean]
}
final case class CreatePullRequestData(
    title: String,
    head: String,
    base: String,
    body: String,
    maintainer_can_modify: Option[Boolean] = Some(true)
) extends CreatePullRequest

final case class CreatePullRequestIssue(
    issue: Int,
    head: String,
    base: String,
    maintainer_can_modify: Option[Boolean] = Some(true)
) extends CreatePullRequest

sealed abstract class PRFilter(val name: String, val value: String)
    extends Product
    with Serializable {
  def tupled: (String, String) = name -> value
}

sealed abstract class PRFilterState(override val value: String) extends PRFilter("state", value)
final case object PRFilterOpen                                  extends PRFilterState("open")
final case object PRFilterClosed                                extends PRFilterState("closed")
final case object PRFilterAll                                   extends PRFilterState("all")

final case class PRFilterHead(override val value: String) extends PRFilter("head", value)

final case class PRFilterBase(override val value: String) extends PRFilter("base", value)

sealed abstract class PRFilterSort(override val value: String) extends PRFilter("sort", value)
final case object PRFilterSortCreated                          extends PRFilterSort("created")
final case object PRFilterSortUpdated                          extends PRFilterSort("updated")
final case object PRFilterSortPopularity                       extends PRFilterSort("popularity")
final case object PRFilterSortLongRunning                      extends PRFilterSort("long-running")

sealed abstract class PRFilterDirection(override val value: String)
    extends PRFilter("direction", value)
final case object PRFilterOrderAsc  extends PRFilterDirection("asc")
final case object PRFilterOrderDesc extends PRFilterDirection("desc")

sealed trait NewPullRequest
final case class NewPullRequestData(title: String, body: String) extends NewPullRequest
final case class NewPullRequestIssue(issue: Int)                 extends NewPullRequest

final case class PullRequestReview(
    id: Int,
    user: Option[User],
    body: String,
    commit_id: String,
    state: PullRequestReviewState,
    html_url: String,
    pull_request_url: String
)

sealed abstract class PullRequestReviewState(val value: String)
final case object PRRStateApproved         extends PullRequestReviewState("APPROVED")
final case object PRRStateChangesRequested extends PullRequestReviewState("CHANGES_REQUESTED")
final case object PRRStateCommented        extends PullRequestReviewState("COMMENTED")
final case object PRRStatePending          extends PullRequestReviewState("PENDING")
final case object PRRStateDismissed        extends PullRequestReviewState("DISMISSED")
