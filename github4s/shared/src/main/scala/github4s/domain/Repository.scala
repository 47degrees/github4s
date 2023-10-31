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

/** A `Repository` but without any recursive definitions (`parent` and `source`) */
final case class RepositoryBase(
    id: Long,
    name: String,
    full_name: String,
    owner: User,
    `private`: Boolean,
    fork: Boolean,
    archived: Boolean,
    urls: RepoUrls,
    created_at: String,
    updated_at: String,
    pushed_at: String,
    status: RepoStatus,
    default_branch: String,
    description: Option[String] = None,
    homepage: Option[String] = None,
    language: Option[String] = None,
    organization: Option[User] = None,
    permissions: Option[RepoPermissions] = None,
    topics: List[String] = Nil
)

/** A `Repository` but with some information omitted (this is used by the search API) */
final case class RepositoryMinimal(
    id: Long,
    node_id: String,
    name: String,
    full_name: String,
    `private`: Boolean,
    owner: User,
    html_url: String,
    description: Option[String] = None,
    fork: Boolean,
    url: String,
    forks_url: String,
    keys_url: String,
    collaborators_url: String,
    teams_url: String,
    hooks_url: String,
    issue_events_url: String,
    events_url: String,
    assignees_url: String,
    branches_url: String,
    blobs_url: String,
    git_tags_url: String,
    git_refs_url: String,
    trees_url: String,
    statuses_url: String,
    languages_url: String,
    stargazers_url: String,
    contributors_url: String,
    subscribers_url: String,
    subscription_url: String,
    commits_url: String,
    git_commits_url: String,
    comments_url: String,
    issue_comment_url: String,
    contents_url: String,
    compare_url: String,
    merges_url: String,
    archive_url: String,
    downloads_url: String,
    issues_url: String,
    pulls_url: String,
    milestones_url: String,
    notifications_url: String,
    releases_url: String,
    deployments_url: String
)

final case class Repository(
    id: Long,
    name: String,
    full_name: String,
    owner: User,
    `private`: Boolean,
    fork: Boolean,
    archived: Boolean,
    urls: RepoUrls,
    created_at: String,
    updated_at: String,
    pushed_at: String,
    status: RepoStatus,
    default_branch: String,
    description: Option[String] = None,
    homepage: Option[String] = None,
    language: Option[String] = None,
    organization: Option[User] = None,
    parent: Option[RepositoryBase] = None,
    permissions: Option[RepoPermissions] = None,
    source: Option[RepositoryBase] = None,
    topics: List[String] = Nil
)

object Repository {
  def fromBaseRepos(
      b: RepositoryBase,
      parent: Option[RepositoryBase],
      source: Option[RepositoryBase]
  ) =
    Repository(
      b.id,
      b.name,
      b.full_name,
      b.owner,
      b.`private`,
      b.fork,
      b.archived,
      b.urls,
      b.created_at,
      b.updated_at,
      b.pushed_at,
      b.status,
      b.default_branch,
      b.description,
      b.homepage,
      b.language,
      b.organization,
      parent,
      b.permissions,
      source,
      b.topics
    )
}

final case class RepoPermissions(
    admin: Boolean,
    push: Boolean,
    pull: Boolean
)

final case class RepoStatus(
    size: Int,
    stargazers_count: Int,
    watchers_count: Int,
    forks_count: Int,
    open_issues_count: Int,
    has_issues: Boolean,
    has_downloads: Boolean,
    has_wiki: Boolean,
    has_pages: Boolean,
    open_issues: Option[Int] = None,
    watchers: Option[Int] = None,
    network_count: Option[Int] = None,
    subscribers_count: Option[Int] = None
)

final case class RepoUrls(
    url: String,
    html_url: String,
    git_url: String,
    ssh_url: String,
    clone_url: String,
    svn_url: String,
    otherUrls: Map[String, String]
)

final case class Release(
    id: Long,
    tag_name: String,
    target_commitish: String,
    name: String,
    body: String,
    draft: Boolean,
    prerelease: Boolean,
    created_at: String,
    url: String,
    html_url: String,
    assets_url: String,
    upload_url: String,
    published_at: Option[String] = None,
    author: Option[User] = None,
    tarball_url: Option[String] = None,
    zipball_url: Option[String] = None
)

final case class Content(
    `type`: String,
    size: Int,
    name: String,
    path: String,
    sha: String,
    url: String,
    git_url: String,
    html_url: String,
    encoding: Option[String] = None,
    target: Option[String] = None,
    submodule_git_url: Option[String] = None,
    content: Option[String] = None,
    download_url: Option[String] = None
)

final case class Commit(
    sha: String,
    message: String,
    date: String,
    url: String,
    login: Option[String] = None,
    avatar_url: Option[String] = None,
    author_url: Option[String] = None
)

final case class Branch(
    name: String,
    commit: BranchCommit,
    `protected`: Option[Boolean] = None,
    protection_url: Option[String] = None
)

final case class BranchCommit(
    sha: String,
    url: String
)

final case class NewReleaseRequest(
    tag_name: String,
    name: String,
    body: String,
    target_commitish: Option[String] = None,
    draft: Option[Boolean] = None,
    prerelease: Option[Boolean] = None
)

final case class Status(
    url: String,
    avatar_url: String,
    id: Long,
    node_id: String,
    state: String,
    created_at: String,
    updated_at: String,
    description: Option[String] = None,
    target_url: Option[String] = None,
    context: Option[String] = None
)

final case class NewStatusRequest(
    state: String,
    target_url: Option[String] = None,
    description: Option[String] = None,
    context: Option[String] = None
)

final case class SearchReposResult(
    total_count: Int,
    incomplete_results: Boolean,
    items: List[Repository]
)

final case class StatusRepository(
    id: Long,
    name: String,
    full_name: String,
    `private`: Boolean,
    fork: Boolean,
    urls: Map[String, String],
    owner: Option[User] = None,
    description: Option[String] = None
)

final case class CombinedStatus(
    url: String,
    state: String,
    commit_url: String,
    sha: String,
    total_count: Int,
    statuses: List[Status],
    repository: StatusRepository
)

final case class WriteFileRequest(
    message: String,
    content: String,
    sha: Option[String] = None,
    branch: Option[String] = None,
    committer: Option[Committer] = None,
    author: Option[Committer] = None
)

final case class DeleteFileRequest(
    message: String,
    sha: String,
    branch: Option[String] = None,
    committer: Option[Committer] = None,
    author: Option[Committer] = None
)

final case class WriteResponseCommit(
    sha: String,
    url: String,
    html_url: String,
    message: String,
    author: Option[Committer] = None,
    committer: Option[Committer] = None
)

final case class WriteFileResponse(
    content: Option[Content],
    commit: WriteResponseCommit
)

final case class Committer(
    name: String,
    email: String
)

final case class UserRepoPermission(permission: String, user: User)

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

  final case class CommitComparisonResponse(
      status: String,
      ahead_by: Int,
      behind_by: Int,
      total_commits: Int,
      url: Option[String] = None,
      html_url: Option[String] = None,
      permalink_url: Option[String] = None,
      diff_url: Option[String] = None,
      patch_url: Option[String] = None,
      base_commit: Option[Commit] = None,
      merge_base_commit: Option[Commit] = None,
      commits: Seq[Commit] = Seq.empty,
      files: Seq[FileComparison] = Seq.empty
  )

  /**
   * A file comparison that contains information on the changes to a file.
   * There are two subtypes: `FileComparisonNotRenamed` and `FileComparisonRenamed` that have different guarantees.
   *
   * * `FileComparisonNotRenamed` guarantees that the `patch` field exists, does not have a `previous_filename` field.
   * * `FileComparisonRenamed` guarantees that the `previous_filename` field exists and sometimes contains a `patch` field.
   *
   * To get values from these fields, there are helper methods `getPatch` and `getPreviousFilename`, though
   * it is recomended to match on your `FileComparison` value to determine which type it is, to remove ambiguity.
   */
  sealed trait FileComparison {
    def sha: String
    def filename: String
    def status: String
    def additions: Int
    def deletions: Int
    def changes: Int
    def blob_url: String
    def raw_url: String
    def contents_url: String

    /**
     * Gets the contents of the `patch` field if it exists, in the case that the file was modified.
     * To guarantee that the `patch` field is available, match this `FileComparison` value as a
     * `FileComparison.FileComparisonNotRenamed` type which always has this field.
     */
    def getPatch: Option[String]

    /**
     * Gets the contents of the `previous_filename` field if it exists.
     * This field is guaranteed to appear in the event of any rename.
     * To guarantee that this field is available, match this `FileComparison` value as a
     * `FileComparison.FileComparisonRenamde` type which always has this field.
     */
    def getPreviousFilename: Option[String]
  }

  object FileComparison {

    /**
     * Represents a file comparison where the file was renamed.
     * The `patch` field will exist if there were also small internal file changes,
     * and the `previous_filename` field is guaranteed to exist, containing the file's previous filename.
     */
    final case class FileComparisonRenamed(
        sha: String,
        filename: String,
        additions: Int,
        deletions: Int,
        changes: Int,
        blob_url: String,
        raw_url: String,
        contents_url: String,
        patch: Option[String],
        previous_filename: String
    ) extends FileComparison {
      val status: String                      = "renamed"
      def getPatch: Option[String]            = patch
      def getPreviousFilename: Option[String] = Some(previous_filename)
    }

    /** Represents a file comparison where a file was not renamed. */
    final case class FileComparisonNotRenamed(
        sha: String,
        filename: String,
        status: String,
        additions: Int,
        deletions: Int,
        changes: Int,
        blob_url: String,
        raw_url: String,
        contents_url: String,
        patch: String
    ) extends FileComparison {
      def getPatch: Option[String]            = Some(patch)
      def getPreviousFilename: Option[String] = None
    }
  }

}
