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

case class Repository(
    id: Int,
    name: String,
    full_name: String,
    owner: User,
    `private`: Boolean,
    description: String,
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

  val allFields = List(forks_url,
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
                       deployments_url)

}
