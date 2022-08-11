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

import cats.data.NonEmptyList
import cats.syntax.all._
import github4s.domain.RepoUrlKeys.{CommitComparisonResponse, FileComparison}
import github4s.domain._
import io.circe.Decoder.Result
import io.circe._
import io.circe.generic.semiauto.deriveDecoder

/**
 * Implicit circe decoders of domains objects
 */
object Decoders {
  final case class Author(
      login: Option[String],
      avatar_url: Option[String],
      html_url: Option[String]
  )

  implicit val decodeAuthor: Decoder[Author] = deriveDecoder[Author]

  implicit val decodeCommit: Decoder[Commit] = Decoder.instance { c =>
    for {
      sha     <- c.downField("sha").as[String]
      message <- c.downField("commit").downField("message").as[String]
      date    <- c.downField("commit").downField("author").downField("date").as[String]
      url     <- c.downField("html_url").as[String]
      author  <- c.downField("author").as[Option[Author]]
    } yield Commit(
      sha = sha,
      message = message,
      date = date,
      url = url,
      login = author.flatMap(_.login),
      avatar_url = author.flatMap(_.avatar_url),
      author_url = author.flatMap(_.html_url)
    )
  }

  implicit val decodeBranch: Decoder[Branch] = Decoder.instance { c =>
    for {
      name            <- c.downField("name").as[String]
      commit          <- c.downField("commit").as[BranchCommit]
      branchProtected <- c.downField("protected").as[Option[Boolean]]
      protection_url  <- c.downField("protection_url").as[Option[String]]
    } yield Branch(
      name = name,
      commit = commit,
      `protected` = branchProtected,
      protection_url = protection_url
    )
  }

  implicit val decodeBranchCommit: Decoder[BranchCommit] = Decoder.instance { c =>
    for {
      url <- c.downField("url").as[String]
      sha <- c.downField("sha").as[String]
    } yield BranchCommit(
      url = url,
      sha = sha
    )
  }

  def readRepoUrls(c: HCursor): Either[DecodingFailure, Map[String, String]] =
    RepoUrlKeys.allFields
      .traverse(name => c.downField(name).as[Option[String]].map(_.map(value => name -> value)))
      .map(_.flatten.toMap)

  implicit val decodeStatusRepository: Decoder[StatusRepository] = {
    Decoder.instance { c =>
      for {
        id          <- c.downField("id").as[Long]
        name        <- c.downField("name").as[String]
        full_name   <- c.downField("full_name").as[String]
        owner       <- c.downField("owner").as[Option[User]]
        priv        <- c.downField("private").as[Boolean]
        description <- c.downField("description").as[Option[String]]
        fork        <- c.downField("fork").as[Boolean]
        repoUrls    <- readRepoUrls(c)
      } yield StatusRepository(
        id = id,
        name = name,
        full_name = full_name,
        owner = owner,
        `private` = priv,
        description = description,
        fork = fork,
        urls = repoUrls
      )
    }
  }

  implicit val decodeRepositoryBase: Decoder[RepositoryBase] = {

    Decoder.instance { c =>
      for {
        id                <- c.downField("id").as[Long]
        name              <- c.downField("name").as[String]
        full_name         <- c.downField("full_name").as[String]
        owner             <- c.downField("owner").as[User]
        priv              <- c.downField("private").as[Boolean]
        description       <- c.downField("description").as[Option[String]]
        fork              <- c.downField("fork").as[Boolean]
        archived          <- c.downField("archived").as[Boolean]
        created_at        <- c.downField("created_at").as[String]
        updated_at        <- c.downField("updated_at").as[String]
        pushed_at         <- c.downField("pushed_at").as[String]
        homepage          <- c.downField("homepage").as[Option[String]]
        language          <- c.downField("language").as[Option[String]]
        organization      <- c.downField("organization").as[Option[User]]
        size              <- c.downField("size").as[Int]
        stargazers_count  <- c.downField("stargazers_count").as[Int]
        watchers_count    <- c.downField("watchers_count").as[Int]
        forks_count       <- c.downField("forks_count").as[Int]
        open_issues_count <- c.downField("open_issues_count").as[Int]
        open_issues       <- c.downField("open_issues").as[Option[Int]]
        watchers          <- c.downField("watchers").as[Option[Int]]
        network_count     <- c.downField("network_count").as[Option[Int]]
        subscribers_count <- c.downField("subscribers_count").as[Option[Int]]
        has_issues        <- c.downField("has_issues").as[Boolean]
        has_downloads     <- c.downField("has_downloads").as[Boolean]
        has_wiki          <- c.downField("has_wiki").as[Boolean]
        has_pages         <- c.downField("has_pages").as[Boolean]
        url               <- c.downField("url").as[String]
        html_url          <- c.downField("html_url").as[String]
        git_url           <- c.downField("git_url").as[String]
        ssh_url           <- c.downField("ssh_url").as[String]
        clone_url         <- c.downField("clone_url").as[String]
        svn_url           <- c.downField("svn_url").as[String]
        permissions       <- c.downField("permissions").as[Option[RepoPermissions]]
        default_branch    <- c.downField("default_branch").as[String]
        topics   <- c.downField("topics").as[Option[List[String]]].map(_.getOrElse(List.empty))
        repoUrls <- readRepoUrls(c)
      } yield RepositoryBase(
        id = id,
        name = name,
        full_name = full_name,
        owner = owner,
        `private` = priv,
        description = description,
        fork = fork,
        archived = archived,
        created_at = created_at,
        updated_at = updated_at,
        pushed_at = pushed_at,
        homepage = homepage,
        language = language,
        organization = organization,
        permissions = permissions,
        status = RepoStatus(
          size = size,
          stargazers_count = stargazers_count,
          watchers_count = watchers_count,
          forks_count = forks_count,
          open_issues_count = open_issues_count,
          open_issues = open_issues,
          watchers = watchers,
          network_count = network_count,
          subscribers_count = subscribers_count,
          has_issues = has_issues,
          has_downloads = has_downloads,
          has_wiki = has_wiki,
          has_pages = has_pages
        ),
        urls = RepoUrls(
          url = url,
          html_url = html_url,
          git_url = git_url,
          ssh_url = ssh_url,
          clone_url = clone_url,
          svn_url = svn_url,
          otherUrls = repoUrls
        ),
        default_branch = default_branch,
        topics = topics
      )
    }
  }

  implicit val decodeRepository: Decoder[Repository] = for {
    base   <- decodeRepositoryBase
    parent <- Decoder[Option[RepositoryBase]].at("parent")
    source <- Decoder[Option[RepositoryBase]].at("source")
  } yield Repository.fromBaseRepos(base, parent, source)

  implicit val decodePRStatus: Decoder[PullRequestReviewState] =
    Decoder.decodeString.emap {
      case PRRStateApproved.value         => PRRStateApproved.asRight
      case PRRStateChangesRequested.value => PRRStateChangesRequested.asRight
      case PRRStateCommented.value        => PRRStateCommented.asRight
      case PRRStatePending.value          => PRRStatePending.asRight
      case PRRStateDismissed.value        => PRRStateDismissed.asRight
      case other                          => s"Unknown pull request review state: $other".asLeft
    }

  implicit val decodeGistFile: Decoder[GistFile] = Decoder.instance { c =>
    for {
      content <- c.downField("content").as[String]
    } yield GistFile(
      content = content
    )
  }

  implicit val decodeGist: Decoder[Gist] = Decoder.instance { c =>
    for {
      url         <- c.downField("url").as[String]
      id          <- c.downField("id").as[String]
      description <- c.downField("description").as[String]
      public      <- c.downField("public").as[Boolean]
      files       <- c.downField("files").as[Map[String, GistFile]]
    } yield Gist(
      url = url,
      id = id,
      description = description,
      public = public,
      files = files
    )
  }

  implicit val decodeStarredRepository: Decoder[StarredRepository] =
    Decoder[Repository]
      .map(StarredRepository(_))
      .or(
        Decoder.instance(c =>
          for {
            starred_at <- c.downField("starred_at").as[String]
            repo       <- c.downField("repo").as[Repository]
          } yield StarredRepository(repo, Some(starred_at))
        )
      )

  implicit val decodePublicGitHubEvent: Decoder[PublicGitHubEvent] =
    Decoder.instance(c =>
      for {
        id             <- c.downField("id").as[Long]
        event_type     <- c.downField("type").as[String]
        actor_login    <- c.downField("actor").downField("login").as[String]
        repo_full_name <- c.downField("repo").downField("full_name").as[String]
        public         <- c.downField("public").as[Boolean]
        created_at     <- c.downField("created_at").as[String]
      } yield PublicGitHubEvent(
        id,
        event_type,
        actor_login,
        repo_full_name,
        public,
        created_at
      )
    )

  implicit val decoderFileComparisonNotRenamed: Decoder[FileComparison.FileComparisonNotRenamed] =
    deriveDecoder[FileComparison.FileComparisonNotRenamed]
  implicit val decoderFileComparisonRenamed: Decoder[FileComparison.FileComparisonRenamed] =
    deriveDecoder[FileComparison.FileComparisonRenamed]

  // Disambiguates between renamed and non-renamed cases based on status
  implicit val decoderFileComparison: Decoder[FileComparison] =
    Decoder.instance { c =>
      c.downField("status").as[String].flatMap { status =>
        if (status == "renamed") decoderFileComparisonRenamed(c)
        else decoderFileComparisonNotRenamed(c)
      }
    }

  implicit val decoderCreatePullRequestData: Decoder[CreatePullRequestData] =
    deriveDecoder[CreatePullRequestData]
  implicit val decoderCreatePullRequestIssue: Decoder[CreatePullRequestIssue] =
    deriveDecoder[CreatePullRequestIssue]
  implicit val decoderNewBlobRequest: Decoder[NewBlobRequest]   = deriveDecoder[NewBlobRequest]
  implicit val decoderNewGistRequest: Decoder[NewGistRequest]   = deriveDecoder[NewGistRequest]
  implicit val decoderNewIssueRequest: Decoder[NewIssueRequest] = deriveDecoder[NewIssueRequest]
  implicit val decoderNewReleaseRequest: Decoder[NewReleaseRequest] =
    deriveDecoder[NewReleaseRequest]
  implicit val decoderSubscriptionRequest: Decoder[SubscriptionRequest] =
    deriveDecoder[SubscriptionRequest]
  implicit val decoderTreeData: Decoder[TreeData] = {
    val sha  = deriveDecoder[TreeDataSha]
    val blob = deriveDecoder[TreeDataBlob]
    sha.widen[TreeData] or blob.widen[TreeData]
  }
  implicit val decoderUpdateReferenceRequest: Decoder[UpdateReferenceRequest] =
    deriveDecoder[UpdateReferenceRequest]
  implicit val decoderWriteFileRequest: Decoder[WriteFileRequest] = deriveDecoder[WriteFileRequest]
  implicit val decoderReviewersRequest: Decoder[ReviewersRequest] = deriveDecoder[ReviewersRequest]
  implicit val decoderNewStatusRequest: Decoder[NewStatusRequest] = deriveDecoder[NewStatusRequest]
  implicit val decoderNewTagRequest: Decoder[NewTagRequest]       = deriveDecoder[NewTagRequest]
  implicit val decoderNewTreeRequest: Decoder[NewTreeRequest]     = deriveDecoder[NewTreeRequest]
  implicit val decoderNewCommitRequest: Decoder[NewCommitRequest] = deriveDecoder[NewCommitRequest]
  implicit val decoderBranchUpdateRequest: Decoder[BranchUpdateRequest] =
    deriveDecoder[BranchUpdateRequest]

  implicit def decodeNonEmptyList[T](implicit D: Decoder[T]): Decoder[NonEmptyList[T]] = {

    def decodeCursors(cursors: List[HCursor]): Result[NonEmptyList[T]] =
      cursors.toNel
        .toRight(DecodingFailure("Empty Response", Nil))
        .flatMap(nelCursors => nelCursors.traverse(_.as[T]))

    Decoder.instance { c =>
      c.as[T] match {
        case Right(r) => Right(NonEmptyList(r, Nil))
        case Left(_)  => c.as[List[HCursor]] flatMap decodeCursors
      }
    }
  }

  implicit val decoderCommitter: Decoder[Committer] = deriveDecoder[Committer]
  implicit val decoderWriteResponseCommit: Decoder[WriteResponseCommit] =
    deriveDecoder[WriteResponseCommit]
  implicit val decoderWriteFileResponse: Decoder[WriteFileResponse] =
    deriveDecoder[WriteFileResponse]
  implicit val decoderPullRequestFile: Decoder[PullRequestFile] = deriveDecoder[PullRequestFile]
  implicit val decoderPullRequestReview: Decoder[PullRequestReview] =
    deriveDecoder[PullRequestReview]
  implicit val decoderUser: Decoder[User] = deriveDecoder[User]

  implicit val decoderRepoPermissions: Decoder[RepoPermissions] = deriveDecoder[RepoPermissions]
  implicit val decoderPullRequestBase: Decoder[PullRequestBase] = deriveDecoder[PullRequestBase]

  implicit val decoderPullRequest: Decoder[PullRequest]           = deriveDecoder[PullRequest]
  implicit val decoderRefObject: Decoder[RefObject]               = deriveDecoder[RefObject]
  implicit val decoderRef: Decoder[Ref]                           = deriveDecoder[Ref]
  implicit val decoderRefAuthor: Decoder[RefAuthor]               = deriveDecoder[RefAuthor]
  implicit val decoderRefCommit: Decoder[RefCommit]               = deriveDecoder[RefCommit]
  implicit val decoderRefInfo: Decoder[RefInfo]                   = deriveDecoder[RefInfo]
  implicit val decoderTreeDataResult: Decoder[TreeDataResult]     = deriveDecoder[TreeDataResult]
  implicit val decoderTreeResult: Decoder[TreeResult]             = deriveDecoder[TreeResult]
  implicit val decoderTag: Decoder[Tag]                           = deriveDecoder[Tag]
  implicit val decoderLabel: Decoder[Label]                       = deriveDecoder[Label]
  implicit val decoderIssuePullRequest: Decoder[IssuePullRequest] = deriveDecoder[IssuePullRequest]
  implicit val decoderIssue: Decoder[Issue]                       = deriveDecoder[Issue]
  implicit val decoderSearchIssuesResult: Decoder[SearchIssuesResult] =
    deriveDecoder[SearchIssuesResult]
  implicit val decoderSearchReposResult: Decoder[SearchReposResult] = deriveDecoder
  implicit val decoderComment: Decoder[Comment]                     = deriveDecoder[Comment]
  implicit val decoderStatus: Decoder[Status]                       = deriveDecoder[Status]
  implicit val decoderCombinedStatus: Decoder[CombinedStatus]       = deriveDecoder[CombinedStatus]
  implicit val decoderContent: Decoder[Content]                     = deriveDecoder[Content]
  implicit val decoderBlobContent: Decoder[BlobContent]             = deriveDecoder[BlobContent]
  implicit val decoderSubscription: Decoder[Subscription]           = deriveDecoder[Subscription]
  implicit val decoderOAuthToken: Decoder[OAuthToken]               = deriveDecoder[OAuthToken]
  implicit val decoderRelease: Decoder[Release]                     = deriveDecoder[Release]
  implicit val decoderUserRepoPermission: Decoder[UserRepoPermission] =
    deriveDecoder[UserRepoPermission]

  implicit val decodeStargazer: Decoder[Stargazer] =
    decoderUser
      .map(Stargazer(_))
      .or(
        Decoder.instance(c =>
          for {
            starred_at <- c.downField("starred_at").as[String]
            user       <- c.downField("user").as[User]
          } yield Stargazer(user, Some(starred_at))
        )
      )

  implicit val decodeTeam: Decoder[Team]           = deriveDecoder[Team]
  implicit val decodeCreator: Decoder[Creator]     = deriveDecoder[Creator]
  implicit val decodeMilestone: Decoder[Milestone] = deriveDecoder[Milestone]
  implicit val decodeProject: Decoder[Project]     = deriveDecoder[Project]
  implicit val decodeColumn: Decoder[Column]       = deriveDecoder[Column]
  implicit val decodeCard: Decoder[Card]           = deriveDecoder[Card]

  implicit val decodeReviewers: Decoder[ReviewersResponse] =
    deriveDecoder[ReviewersResponse]
  implicit val decoderCommentData: Decoder[CommentData] = deriveDecoder[CommentData]
  implicit val decoderPullRequestReviewEvent: Decoder[PullRequestReviewEvent] =
    Decoder[String].emap {
      case s if s == PRREventApprove.value        => Right(PRREventApprove)
      case s if s == PRREventRequestChanges.value => Right(PRREventRequestChanges)
      case s if s == PRREventComment.value        => Right(PRREventComment)
      case s if s == PRREventPending.value        => Right(PRREventPending)
      case other                                  => Left(s"Bad event: $other")
    }
  implicit val decoderCreateReviewComment: Decoder[CreateReviewComment] =
    deriveDecoder[CreateReviewComment]
  implicit val decoderCreatePRReviewRequest: Decoder[CreatePRReviewRequest] =
    deriveDecoder[CreatePRReviewRequest]
  implicit val decoderCreatePullRequest: Decoder[CreatePullRequest] = {
    val data  = deriveDecoder[CreatePullRequestData]
    val issue = deriveDecoder[CreatePullRequestIssue]
    data.widen[CreatePullRequest] or issue.widen[CreatePullRequest]
  }
  implicit val decoderCreateReferenceRequest: Decoder[CreateReferenceRequest] =
    deriveDecoder[CreateReferenceRequest]
  implicit val decoderDeleteFileRequest: Decoder[DeleteFileRequest] =
    deriveDecoder[DeleteFileRequest]
  implicit val decoderEditGistFile: Decoder[EditGistFile]         = deriveDecoder[EditGistFile]
  implicit val decoderEditGistRequest: Decoder[EditGistRequest]   = deriveDecoder[EditGistRequest]
  implicit val decoderEditIssueRequest: Decoder[EditIssueRequest] = deriveDecoder[EditIssueRequest]
  implicit val decoderMilestoneData: Decoder[MilestoneData]       = deriveDecoder[MilestoneData]

  implicit val decodeBranchUpdateResponse: Decoder[BranchUpdateResponse] =
    deriveDecoder[BranchUpdateResponse]
  implicit val decodeCommitComparisonResponse: Decoder[CommitComparisonResponse] =
    deriveDecoder[CommitComparisonResponse]
}
