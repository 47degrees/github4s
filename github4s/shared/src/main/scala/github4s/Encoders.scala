/*
 * Copyright 2016-2022 47 Degrees Open Source <https://www.47deg.com>
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

package github4s

import github4s.domain.RepoUrlKeys.{CommitComparisonResponse, FileComparison}
import github4s.domain._
import io.circe._
import io.circe.generic.semiauto.deriveEncoder
import io.circe.syntax._

object Encoders {

  implicit val encodeTreeData: Encoder[TreeData] = {
    val sha  = deriveEncoder[TreeDataSha]
    val blob = deriveEncoder[TreeDataBlob]
    Encoder.instance {
      case d: TreeDataSha  => sha(d)
      case d: TreeDataBlob => blob(d)
    }
  }

  implicit val encodeNewPullRequest: Encoder[CreatePullRequest] = {
    val data  = deriveEncoder[CreatePullRequestData]
    val issue = deriveEncoder[CreatePullRequestIssue]
    Encoder.instance {
      case d: CreatePullRequestData  => data(d)
      case d: CreatePullRequestIssue => issue(d)
    }
  }

  implicit val encodePrrStatus: Encoder[PullRequestReviewState] =
    Encoder.encodeString.contramap(_.value)

  implicit val encodePrrEvent: Encoder[PullRequestReviewEvent] =
    Encoder.encodeString.contramap(_.value)

  implicit val encodeEditGistFile: Encoder[EditGistFile] = {
    deriveEncoder[EditGistFile].mapJsonObject(
      _.filter(e => !(e._1.equals("filename") && e._2.isNull))
    )
  }

  implicit val encoderCommit: Encoder[Commit] = Encoder.instance { c =>
    Json.obj(
      "sha"      -> c.sha.asJson,
      "html_url" -> c.url.asJson,
      "author" -> Json.obj(
        "avatar_url" -> c.avatar_url.asJson,
        "html_url"   -> c.author_url.asJson,
        "login"      -> c.login.asJson
      ),
      "commit" -> Json.obj(
        "message" -> c.message.asJson,
        "author"  -> Json.obj("date" -> c.date.asJson)
      )
    )
  }

  implicit val encoderReviewersResponse: Encoder[ReviewersResponse] =
    deriveEncoder[ReviewersResponse]
  implicit val encoderSearchIssuesResult: Encoder[SearchIssuesResult] =
    deriveEncoder[SearchIssuesResult]
  implicit val encoderSearchReposResult: Encoder[SearchReposResult] =
    deriveEncoder[SearchReposResult]
  implicit val encoderStatusRepository: Encoder[StatusRepository] = {
    val base = deriveEncoder[StatusRepository]
    base.mapJsonObject { j =>
      val urls    = j("urls").flatMap(_.asObject)
      val updated = j.remove("urls")
      urls.map(updated.deepMerge).getOrElse(updated)
    }

  }
  implicit val encoderStatus: Encoder[Status]         = deriveEncoder[Status]
  implicit val encoderTreeResult: Encoder[TreeResult] = deriveEncoder[TreeResult]
  implicit val encoderUserRepoPermission: Encoder[UserRepoPermission] =
    deriveEncoder[UserRepoPermission]
  implicit val encoderWriteFileResponse: Encoder[WriteFileResponse] =
    deriveEncoder[WriteFileResponse]
  implicit val encoderWriteResponseCommit: Encoder[WriteResponseCommit] =
    deriveEncoder[WriteResponseCommit]
  implicit val encoderSubscription: Encoder[Subscription]       = deriveEncoder[Subscription]
  implicit val encoderTag: Encoder[Tag]                         = deriveEncoder[Tag]
  implicit val encoderTeam: Encoder[Team]                       = deriveEncoder[Team]
  implicit val encoderTreeDataResult: Encoder[TreeDataResult]   = deriveEncoder[TreeDataResult]
  implicit val encoderOAuthToken: Encoder[OAuthToken]           = deriveEncoder[OAuthToken]
  implicit val encoderProject: Encoder[Project]                 = deriveEncoder[Project]
  implicit val encoderPullRequestBase: Encoder[PullRequestBase] = deriveEncoder[PullRequestBase]
  implicit val encoderPullRequestFile: Encoder[PullRequestFile] = deriveEncoder[PullRequestFile]
  implicit val encoderBlobContent: Encoder[BlobContent]         = deriveEncoder[BlobContent]
  implicit val encoderBranchCommit: Encoder[BranchCommit]       = deriveEncoder[BranchCommit]
  implicit val encoderBranch: Encoder[Branch]                   = deriveEncoder[Branch]
  implicit val encoderCard: Encoder[Card]                       = deriveEncoder[Card]
  implicit val encoderColumn: Encoder[Column]                   = deriveEncoder[Column]
  implicit val encoderCombinedStatus: Encoder[CombinedStatus]   = deriveEncoder[CombinedStatus]
  implicit val encoderCommiter: Encoder[Committer]              = deriveEncoder[Committer]
  implicit val encoderContent: Encoder[Content]                 = deriveEncoder[Content]
  implicit val encoderCreator: Encoder[Creator]                 = deriveEncoder[Creator]
  implicit val encoderPullRequestReview: Encoder[PullRequestReview] =
    deriveEncoder[PullRequestReview]
  implicit val encoderRefAuthor: Encoder[RefAuthor]             = deriveEncoder[RefAuthor]
  implicit val encoderRefCommit: Encoder[RefCommit]             = deriveEncoder[RefCommit]
  implicit val encoderRefInfo: Encoder[RefInfo]                 = deriveEncoder[RefInfo]
  implicit val encoderRefObject: Encoder[RefObject]             = deriveEncoder[RefObject]
  implicit val encoderRef: Encoder[Ref]                         = deriveEncoder[Ref]
  implicit val encoderRelease: Encoder[Release]                 = deriveEncoder[Release]
  implicit val encoderRepoPermissions: Encoder[RepoPermissions] = deriveEncoder[RepoPermissions]
  implicit val encoderRepositoryBase: Encoder[RepositoryBase] = Encoder.instance[RepositoryBase] {
    rb =>
      Json.obj(
        "id"                -> rb.id.asJson,
        "name"              -> rb.name.asJson,
        "full_name"         -> rb.full_name.asJson,
        "owner"             -> rb.owner.asJson,
        "private"           -> rb.`private`.asJson,
        "description"       -> rb.description.asJson,
        "fork"              -> rb.fork.asJson,
        "archived"          -> rb.archived.asJson,
        "created_at"        -> rb.created_at.asJson,
        "updated_at"        -> rb.updated_at.asJson,
        "pushed_at"         -> rb.pushed_at.asJson,
        "homepage"          -> rb.homepage.asJson,
        "language"          -> rb.language.asJson,
        "organization"      -> rb.organization.asJson,
        "size"              -> rb.status.size.asJson,
        "stargazers_count"  -> rb.status.stargazers_count.asJson,
        "watchers_count"    -> rb.status.watchers_count.asJson,
        "forks_count"       -> rb.status.forks_count.asJson,
        "open_issues_count" -> rb.status.open_issues_count.asJson,
        "open_issues"       -> rb.status.open_issues.asJson,
        "watchers"          -> rb.status.watchers.asJson,
        "network_count"     -> rb.status.network_count.asJson,
        "subscribers_count" -> rb.status.subscribers_count.asJson,
        "has_issues"        -> rb.status.has_issues.asJson,
        "has_downloads"     -> rb.status.has_downloads.asJson,
        "has_wiki"          -> rb.status.has_wiki.asJson,
        "has_pages"         -> rb.status.has_pages.asJson,
        "url"               -> rb.urls.url.asJson,
        "html_url"          -> rb.urls.html_url.asJson,
        "git_url"           -> rb.urls.git_url.asJson,
        "ssh_url"           -> rb.urls.ssh_url.asJson,
        "clone_url"         -> rb.urls.clone_url.asJson,
        "svn_url"           -> rb.urls.svn_url.asJson,
        "permissions"       -> rb.permissions.asJson,
        "default_branch"    -> rb.default_branch.asJson,
        "topics"            -> rb.topics.asJson
      )
  }
  implicit val encoderPullRequest: Encoder[PullRequest] = deriveEncoder[PullRequest]
  implicit val encoderDeleteFileRequest: Encoder[DeleteFileRequest] =
    deriveEncoder[DeleteFileRequest]
  implicit val encoderWriteFileContentRequest: Encoder[WriteFileRequest] =
    deriveEncoder[WriteFileRequest]
  implicit val encoderCreateReferenceRequest: Encoder[CreateReferenceRequest] =
    deriveEncoder[CreateReferenceRequest]
  implicit val encoderNewCommitRequest: Encoder[NewCommitRequest] = deriveEncoder[NewCommitRequest]
  implicit val encoderNewBlobRequest: Encoder[NewBlobRequest]     = deriveEncoder[NewBlobRequest]
  implicit val encoderNewTreeRequest: Encoder[NewTreeRequest]     = deriveEncoder[NewTreeRequest]
  implicit val encoderNewTagRequest: Encoder[NewTagRequest]       = deriveEncoder[NewTagRequest]
  implicit val encoderUpdateReferenceRequest: Encoder[UpdateReferenceRequest] =
    deriveEncoder[UpdateReferenceRequest]
  implicit val encoderSubscriptionRequest: Encoder[SubscriptionRequest] =
    deriveEncoder[SubscriptionRequest]
  implicit val encoderNewGistRequest: Encoder[NewGistRequest]     = deriveEncoder[NewGistRequest]
  implicit val encoderEditGistRequest: Encoder[EditGistRequest]   = deriveEncoder[EditGistRequest]
  implicit val encoderNewIssueRequest: Encoder[NewIssueRequest]   = deriveEncoder[NewIssueRequest]
  implicit val encoderEditIssueRequest: Encoder[EditIssueRequest] = deriveEncoder[EditIssueRequest]
  implicit val encoderLabel: Encoder[Label]                       = deriveEncoder[Label]
  implicit val encoderCommentData: Encoder[CommentData]           = deriveEncoder[CommentData]
  implicit val encoderNewReleaseRequest: Encoder[NewReleaseRequest] =
    deriveEncoder[NewReleaseRequest]
  implicit val encoderNewStatusRequest: Encoder[NewStatusRequest] = deriveEncoder[NewStatusRequest]
  implicit val encoderMilestoneData: Encoder[MilestoneData]       = deriveEncoder[MilestoneData]
  implicit val encodeBranchUpdateRequest: Encoder[BranchUpdateRequest] =
    deriveEncoder[BranchUpdateRequest]

  implicit val encoderCreateReviewComment: Encoder[CreateReviewComment] =
    deriveEncoder[CreateReviewComment]
  implicit val encodeNewPullRequestReview: Encoder[CreatePRReviewRequest] =
    deriveEncoder[CreatePRReviewRequest]
  implicit val encodeRequiestedReviewers: Encoder[ReviewersRequest] =
    deriveEncoder[ReviewersRequest]
  implicit val encodeStargazer: Encoder[Stargazer] =
    Encoder.instance[Stargazer] { s =>
      val user = Encoder[User].apply(s.user)
      s.starred_at match {
        case Some(value) =>
          Json.obj("starred_at" -> value.asJson, "user" -> user)
        case None => user
      }
    }

  implicit val encodeRepository: Encoder[Repository] = Encoder.instance[Repository] { r =>
    val base = RepositoryBase(
      id = r.id,
      name = r.name,
      full_name = r.full_name,
      owner = r.owner,
      `private` = r.`private`,
      fork = r.fork,
      archived = r.archived,
      urls = r.urls,
      created_at = r.created_at,
      updated_at = r.updated_at,
      pushed_at = r.pushed_at,
      status = r.status,
      default_branch = r.default_branch,
      description = r.description,
      homepage = r.homepage,
      language = r.language,
      organization = r.organization,
      permissions = r.permissions,
      topics = r.topics
    )
    base.asJson deepMerge
      Json.obj("parent" -> r.parent.asJson, "source" -> r.source.asJson)
  }
  implicit val encodeStarredRepository: Encoder[StarredRepository] =
    Encoder.instance[StarredRepository] { sr =>
      val repo = Encoder[Repository].apply(sr.repo)
      sr.starred_at match {
        case Some(value) =>
          Json.obj("starred_at" -> value.asJson, "repo" -> repo)
        case None => repo
      }
    }

  implicit val encoderPublicOrganizationEvents: Encoder[PublicOrganizationEvent] =
    Encoder.instance { e =>
      Json.obj(
        "id"         -> e.id.asJson,
        "type"       -> e.`type`.asJson,
        "public"     -> e.public.asJson,
        "created_at" -> e.created_at.asJson,
        "actor" -> Json.obj(
          "login" -> e.actor_login.asJson
        ),
        "repo" -> Json.obj(
          "full_name" -> e.repo_full_name.asJson
        )
      )
    }

  implicit val encoderIssue: Encoder[Issue]                       = deriveEncoder[Issue]
  implicit val encoderIssuePullRequest: Encoder[IssuePullRequest] = deriveEncoder[IssuePullRequest]
  implicit val encoderGistFile: Encoder[GistFile]                 = deriveEncoder[GistFile]
  implicit val encodeGist: Encoder[Gist]                          = deriveEncoder[Gist]
  implicit val encoderUser: Encoder[User]                         = deriveEncoder[User]
  implicit val encoderComment: Encoder[Comment]                   = deriveEncoder[Comment]
  implicit val encoderMilestone: Encoder[Milestone]               = deriveEncoder[Milestone]
  implicit val encodeBranchUpdateResponse: Encoder[BranchUpdateResponse] =
    deriveEncoder[BranchUpdateResponse]
  implicit val encodeFileComparisonNotRenamed: Encoder[FileComparison.FileComparisonNotRenamed] =
    deriveEncoder[FileComparison.FileComparisonNotRenamed]

  // Ensures that the `status` field is populated when encoded as it is not part of the model.
  implicit val encodeFileComparisonRenamed: Encoder[FileComparison.FileComparisonRenamed] =
    deriveEncoder[FileComparison.FileComparisonRenamed].mapJson { json =>
      json.deepMerge(
        Json.obj(
          "status" -> Json.fromString("renamed")
        )
      )
    }
  implicit val encodeFileComparison: Encoder[FileComparison] = Encoder.instance {
    case a: FileComparison.FileComparisonNotRenamed => encodeFileComparisonNotRenamed(a)
    case b: FileComparison.FileComparisonRenamed    => encodeFileComparisonRenamed(b)
  }
  implicit val encodeCommitComparisonResponse: Encoder[CommitComparisonResponse] =
    deriveEncoder[CommitComparisonResponse]
}
