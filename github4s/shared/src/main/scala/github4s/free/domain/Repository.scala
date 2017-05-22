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

package github4s.free.domain

case class Repository(
    id: Int,
    name: String,
    full_name: String,
    owner: User,
    `private`: Boolean,
    description: Option[String],
    fork: Boolean,
    urls: RepoUrls,
    created_at: String,
    updated_at: String,
    pushed_at: String,
    homepage: Option[String] = None,
    language: Option[String] = None,
    status: RepoStatus,
    organization: Option[User] = None
)

case class RepoStatus(
    size: Int,
    stargazers_count: Int,
    watchers_count: Int,
    forks_count: Int,
    open_issues_count: Int,
    open_issues: Option[Int],
    watchers: Option[Int],
    network_count: Option[Int],
    subscribers_count: Option[Int],
    has_issues: Boolean,
    has_downloads: Boolean,
    has_wiki: Boolean,
    has_pages: Boolean
)

case class RepoUrls(
    url: String,
    html_url: String,
    git_url: String,
    ssh_url: String,
    clone_url: String,
    svn_url: String,
    otherUrls: Map[String, String]
)

case class Release(
    id: Int,
    tag_name: String,
    target_commitish: String,
    name: String,
    body: String,
    draft: Boolean,
    prerelease: Boolean,
    created_at: String,
    published_at: String,
    author: Option[User],
    url: String,
    html_url: String,
    assets_url: String,
    upload_url: String,
    tarball_url: String,
    zipball_url: String)

case class Content(
    `type`: String,
    encoding: Option[String],
    target: Option[String],
    submodule_git_url: Option[String],
    size: Int,
    name: String,
    path: String,
    content: Option[String],
    sha: String,
    url: String,
    git_url: String,
    html_url: String,
    download_url: Option[String])

case class Commit(
    sha: String,
    message: String,
    date: String,
    url: String,
    login: Option[String],
    avatar_url: Option[String],
    author_url: Option[String]
)

case class NewReleaseRequest(
    tag_name: String,
    name: String,
    body: String,
    target_commitish: Option[String],
    draft: Option[Boolean],
    prerelease: Option[Boolean])

case class Status(
    id: Int,
    url: String,
    state: String,
    target_url: Option[String],
    description: Option[String],
    context: Option[String],
    creator: Option[User],
    created_at: String,
    updated_at: String
)

case class NewStatusRequest(
    state: String,
    target_url: Option[String],
    description: Option[String],
    context: Option[String]
)

case class StatusRepository(
    id: Int,
    name: String,
    full_name: String,
    owner: Option[User],
    `private`: Boolean,
    description: Option[String],
    fork: Boolean,
    urls: Map[String, String]
)

case class CombinedStatus(
    url: String,
    state: String,
    commit_url: String,
    sha: String,
    total_count: Int,
    statuses: List[Status],
    repository: StatusRepository
)
object RepoUrlKeys {

  val forks_url         = "forks_url"
  val keys_url          = "keys_url"
  val collaborators_url = "collaborators_url"
  val teams_url         = "teams_url"
  val hooks_url         = "hooks_url"
  val issue_events_url  = "issue_events_url"
  val events_url        = "events_url"
  val assignees_url     = "assignees_url"
  val branches_url      = "branches_url"
  val tags_url          = "tags_url"
  val blobs_url         = "blobs_url"
  val git_tags_url      = "git_tags_url"
  val git_refs_url      = "git_refs_url"
  val trees_url         = "trees_url"
  val statuses_url      = "statuses_url"
  val languages_url     = "languages_url"
  val stargazers_url    = "stargazers_url"
  val contributors_url  = "contributors_url"
  val subscribers_url   = "subscribers_url"
  val subscription_url  = "subscription_url"
  val commits_url       = "commits_url"
  val git_commits_url   = "git_commits_url"
  val comments_url      = "comments_url"
  val issue_comment_url = "issue_comment_url"
  val contents_url      = "contents_url"
  val compare_url       = "compare_url"
  val merges_url        = "merges_url"
  val archive_url       = "archive_url"
  val downloads_url     = "downloads_url"
  val issues_url        = "issues_url"
  val pulls_url         = "pulls_url"
  val milestones_url    = "milestones_url"
  val notifications_url = "notifications_url"
  val labels_url        = "labels_url"
  val releases_url      = "releases_url"
  val deployments_url   = "deployments_url"

  val allFields = List(
    forks_url,
    keys_url,
    collaborators_url,
    teams_url,
    hooks_url,
    issue_events_url,
    events_url,
    assignees_url,
    branches_url,
    tags_url,
    blobs_url,
    git_tags_url,
    git_refs_url,
    trees_url,
    statuses_url,
    languages_url,
    stargazers_url,
    contributors_url,
    subscribers_url,
    subscription_url,
    commits_url,
    git_commits_url,
    comments_url,
    issue_comment_url,
    contents_url,
    compare_url,
    merges_url,
    archive_url,
    downloads_url,
    issues_url,
    pulls_url,
    milestones_url,
    notifications_url,
    labels_url,
    releases_url,
    deployments_url
  )

}
