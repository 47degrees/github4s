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
    open_issues: Int,
    watchers: Int,
    network_count: Int,
    subscribers_count: Int,
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
    forks_url: Option[String] = None,
    keys_url: Option[String] = None,
    collaborators_url: Option[String] = None,
    teams_url: Option[String] = None,
    hooks_url: Option[String] = None,
    issue_events_url: Option[String] = None,
    events_url: Option[String] = None,
    assignees_url: Option[String] = None,
    branches_url: Option[String] = None,
    tags_url: Option[String] = None,
    blobs_url: Option[String] = None,
    git_tags_url: Option[String] = None,
    git_refs_url: Option[String] = None,
    trees_url: Option[String] = None,
    statuses_url: Option[String] = None,
    languages_url: Option[String] = None,
    stargazers_url: Option[String] = None,
    contributors_url: Option[String] = None,
    subscribers_url: Option[String] = None,
    subscription_url: Option[String] = None,
    commits_url: Option[String] = None,
    git_commits_url: Option[String] = None,
    comments_url: Option[String] = None,
    issue_comment_url: Option[String] = None,
    contents_url: Option[String] = None,
    compare_url: Option[String] = None,
    merges_url: Option[String] = None,
    archive_url: Option[String] = None,
    downloads_url: Option[String] = None,
    issues_url: Option[String] = None,
    pulls_url: Option[String] = None,
    milestones_url: Option[String] = None,
    notifications_url: Option[String] = None,
    labels_url: Option[String] = None,
    releases_url: Option[String] = None,
    deployments_url: Option[String] = None
)
