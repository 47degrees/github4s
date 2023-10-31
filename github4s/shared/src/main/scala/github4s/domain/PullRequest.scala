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

final case class PullRequest(
    id: Long,
    number: Int,
    state: String,
    title: String,
    locked: Boolean,
    html_url: String,
    created_at: String,
    body: Option[String] = None,
    updated_at: Option[String] = None,
    closed_at: Option[String] = None,
    merged_at: Option[String] = None,
    merge_commit_sha: Option[String] = None,
    base: Option[PullRequestBase] = None,
    head: Option[PullRequestBase] = None,
    user: Option[User] = None,
    assignee: Option[User] = None,
    draft: Boolean
)

final case class PullRequestBase(
    ref: String,
    sha: String,
    label: Option[String] = None,
    user: Option[User] = None,
    repo: Option[Repository] = None
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
    patch: Option[String] = None,
    previous_filename: Option[String] = None
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
    maintainer_can_modify: Option[Boolean] = Some(true),
    draft: Boolean
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
case object PRFilterOpen                                        extends PRFilterState("open")
case object PRFilterClosed                                      extends PRFilterState("closed")
case object PRFilterAll                                         extends PRFilterState("all")

final case class PRFilterHead(override val value: String) extends PRFilter("head", value)

final case class PRFilterBase(override val value: String) extends PRFilter("base", value)

sealed abstract class PRFilterSort(override val value: String) extends PRFilter("sort", value)
case object PRFilterSortCreated                                extends PRFilterSort("created")
case object PRFilterSortUpdated                                extends PRFilterSort("updated")
case object PRFilterSortPopularity                             extends PRFilterSort("popularity")
case object PRFilterSortLongRunning                            extends PRFilterSort("long-running")

sealed abstract class PRFilterDirection(override val value: String)
    extends PRFilter("direction", value)
case object PRFilterOrderAsc  extends PRFilterDirection("asc")
case object PRFilterOrderDesc extends PRFilterDirection("desc")

sealed trait NewPullRequest
final case class NewPullRequestData(title: String, body: String, draft: Boolean)
    extends NewPullRequest
final case class NewPullRequestIssue(issue: Int) extends NewPullRequest

final case class PullRequestReview(
    id: Long,
    body: String,
    commit_id: String,
    state: PullRequestReviewState,
    html_url: String,
    pull_request_url: String,
    user: Option[User] = None
)

sealed abstract class PullRequestReviewState(val value: String)
case object PRRStateApproved         extends PullRequestReviewState("APPROVED")
case object PRRStateChangesRequested extends PullRequestReviewState("CHANGES_REQUESTED")
case object PRRStateCommented        extends PullRequestReviewState("COMMENTED")
case object PRRStatePending          extends PullRequestReviewState("PENDING")
case object PRRStateDismissed        extends PullRequestReviewState("DISMISSED")

final case class CreatePRReviewRequest(
    commit_id: Option[String] = None,
    body: String,
    event: PullRequestReviewEvent = PRREventPending,
    comments: List[CreateReviewComment] = Nil
)

sealed abstract class PullRequestReviewEvent(val value: String)

case object PRREventApprove        extends PullRequestReviewEvent("APPROVE")
case object PRREventRequestChanges extends PullRequestReviewEvent("REQUEST_CHANGES")
case object PRREventComment        extends PullRequestReviewEvent("COMMENT")
case object PRREventPending        extends PullRequestReviewEvent("PENDING")

case class CreateReviewComment(path: String, position: Int, body: String)

final case class ReviewersRequest(
    reviewers: List[String],
    team_reviewers: List[String] = Nil
)

final case class ReviewersResponse(
    users: List[User],
    teams: List[Team]
)

final case class BranchUpdateRequest(expected_head_sha: Option[String])
final case class BranchUpdateResponse(message: String, url: String)
