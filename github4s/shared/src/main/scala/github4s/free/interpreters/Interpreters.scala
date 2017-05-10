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

package github4s.free.interpreters

import cats.data.Kleisli
import cats.{~>, ApplicativeError, MonadError}
import github4s.GithubDefaultUrls._
import github4s.HttpRequestBuilderExtension
import github4s.api._
import github4s.app._
import github4s.free.algebra._
import simulacrum.typeclass

import scala.language.higherKinds

@typeclass
trait Capture[M[_]] {
  def capture[A](a: ⇒ A): M[A]
}

class Interpreters[M[_], C](
    implicit A: ApplicativeError[M, Throwable],
    C: Capture[M],
    httpClientImpl: HttpRequestBuilderExtension[C, M]) {

  type K[A] = Kleisli[M, Map[String, String], A]

  implicit def interpreters(
      implicit A: MonadError[M, Throwable]
  ): GitHub4s ~> K = {
    val c01interpreter: COGH01 ~> K = repositoryOpsInterpreter or userOpsInterpreter
    val c02interpreter: COGH02 ~> K = gistOpsInterpreter or c01interpreter
    val c03interpreter: COGH03 ~> K = issueOpsInterpreter or c02interpreter
    val c04interpreter: COGH04 ~> K = authOpsInterpreter or c03interpreter
    val c05interpreter: COGH05 ~> K = gitDataOpsInterpreter or c04interpreter
    val c06interpreter: COGH06 ~> K = pullRequestOpsInterpreter or c05interpreter
    val c07interpreter: COGH07 ~> K = notificationOpsInterpreter or c06interpreter
    val all: GitHub4s ~> K          = statusOpsInterpreter or c07interpreter
    all
  }

  /**
   * Lifts Repository Ops to an effect capturing Monad such as Task via natural transformations
   */
  def repositoryOpsInterpreter: RepositoryOp ~> K = new (RepositoryOp ~> K) {

    val repos = new Repos()

    def apply[A](fa: RepositoryOp[A]): K[A] = Kleisli[M, Map[String, String], A] { headers =>
      fa match {
        case GetRepo(owner, repo, accessToken) ⇒ repos.get(accessToken, headers, owner, repo)
        case GetContents(owner, repo, path, ref, accessToken) ⇒
          repos.getContents(accessToken, headers, owner, repo, path, ref)
        case ListCommits(owner, repo, sha, path, author, since, until, pagination, accessToken) ⇒
          repos.listCommits(
            accessToken,
            headers,
            owner,
            repo,
            sha,
            path,
            author,
            since,
            until,
            pagination)
        case ListContributors(owner, repo, anon, accessToken) ⇒
          repos.listContributors(accessToken, headers, owner, repo, anon)
        case CreateRelease(
            owner,
            repo,
            tagName,
            name,
            body,
            targetCommitish,
            draft,
            prerelease,
            accessToken) =>
          repos.createRelease(
            accessToken,
            headers,
            owner,
            repo,
            tagName,
            name,
            body,
            targetCommitish,
            draft,
            prerelease)
      }
    }
  }

  /**
   * Lifts User Ops to an effect capturing Monad such as Task via natural transformations
   */
  def userOpsInterpreter: UserOp ~> K =
    new (UserOp ~> K) {

      val users = new Users()

      def apply[A](fa: UserOp[A]): K[A] = Kleisli[M, Map[String, String], A] { headers =>
        fa match {
          case GetUser(username, accessToken) ⇒ users.get(accessToken, headers, username)
          case GetAuthUser(accessToken)       ⇒ users.getAuth(accessToken, headers)
          case GetUsers(since, pagination, accessToken) ⇒
            users.getUsers(accessToken, headers, since, pagination)
        }
      }
    }

  /**
   * Lifts Auth Ops to an effect capturing Monad such as Task via natural transformations
   */
  def authOpsInterpreter: AuthOp ~> K =
    new (AuthOp ~> K) {

      val auth = new Auth()

      def apply[A](fa: AuthOp[A]): K[A] = Kleisli[M, Map[String, String], A] { headers =>
        fa match {
          case NewAuth(username, password, scopes, note, client_id, client_secret) ⇒
            auth.newAuth(username, password, scopes, note, client_id, client_secret, headers)
          case AuthorizeUrl(client_id, redirect_uri, scopes) ⇒
            auth.authorizeUrl(client_id, redirect_uri, scopes)
          case GetAccessToken(client_id, client_secret, code, redirect_uri, state) ⇒
            auth.getAccessToken(client_id, client_secret, code, redirect_uri, state, headers)
        }
      }
    }

  /**
   * Lifts Gist Ops to an effect capturing Monad such as Task via natural transformations
   */
  def gistOpsInterpreter: GistOp ~> K =
    new (GistOp ~> K) {

      val gists = new Gists()

      def apply[A](fa: GistOp[A]): K[A] = Kleisli[M, Map[String, String], A] { headers =>
        fa match {
          case NewGist(description, public, files, accessToken) ⇒
            gists.newGist(description, public, files, headers, accessToken)
        }
      }
    }

  /**
   * Lifts Issue Ops to an effect capturing Monad such as Task via natural transformations
   */
  def issueOpsInterpreter: IssueOp ~> K =
    new (IssueOp ~> K) {

      val issues = new Issues()

      def apply[A](fa: IssueOp[A]): K[A] = Kleisli[M, Map[String, String], A] { headers =>
        fa match {
          case ListIssues(owner, repo, accessToken) ⇒
            issues.list(accessToken, headers, owner, repo)
          case SearchIssues(query, searchParams, accessToken) ⇒
            issues.search(accessToken, headers, query, searchParams)
          case CreateIssue(owner, repo, title, body, milestone, labels, assignees, accessToken) ⇒
            issues
              .create(accessToken, headers, owner, repo, title, body, milestone, labels, assignees)
          case EditIssue(
              owner,
              repo,
              issue,
              state,
              title,
              body,
              milestone,
              labels,
              assignees,
              accessToken) ⇒
            issues.edit(
              accessToken,
              headers,
              owner,
              repo,
              issue,
              state,
              title,
              body,
              milestone,
              labels,
              assignees)
          case CreateComment(owner, repo, number, body, accessToken) ⇒
            issues.createComment(accessToken, headers, owner, repo, number, body)
          case EditComment(owner, repo, id, body, accessToken) ⇒
            issues.editComment(accessToken, headers, owner, repo, id, body)
          case DeleteComment(owner, repo, id, accessToken) ⇒
            issues.deleteComment(accessToken, headers, owner, repo, id)
        }
      }
    }

  /**
   * Lifts Notification Ops to an effect capturing Monad such as Task via natural transformations
   */
  def notificationOpsInterpreter: NotificationOp ~> K =
    new (NotificationOp ~> K) {

      val notifications = new Notifications()

      def apply[A](fa: NotificationOp[A]): K[A] = Kleisli[M, Map[String, String], A] { headers =>
        fa match {
          case SetThreadSub(id, subscribed, ignored, accessToken) ⇒
            notifications.setThreadSub(accessToken, headers, id, subscribed, ignored)
        }
      }
    }

  /**
   * Lifts Git Ops to an effect capturing Monad such as Task via natural transformations
   */
  def gitDataOpsInterpreter: GitDataOp ~> K =
    new (GitDataOp ~> K) {

      val gitData = new GitData()

      def apply[A](fa: GitDataOp[A]): K[A] = Kleisli[M, Map[String, String], A] { headers =>
        fa match {
          case GetReference(owner, repo, ref, accessToken) ⇒
            gitData.reference(accessToken, headers, owner, repo, ref)
          case CreateReference(owner, repo, ref, sha, accessToken) ⇒
            gitData.createReference(accessToken, headers, owner, repo, ref, sha)
          case UpdateReference(owner, repo, ref, sha, force, accessToken) ⇒
            gitData.updateReference(accessToken, headers, owner, repo, ref, sha, force)
          case GetCommit(owner, repo, sha, accessToken) ⇒
            gitData.commit(accessToken, headers, owner, repo, sha)
          case CreateCommit(owner, repo, message, tree, parents, author, accessToken) ⇒
            gitData.createCommit(accessToken, headers, owner, repo, message, tree, parents, author)
          case CreateBlob(owner, repo, content, encoding, accessToken) ⇒
            gitData.createBlob(accessToken, headers, owner, repo, content, encoding)
          case CreateTree(owner, repo, baseTree, treeDataList, accessToken) ⇒
            gitData.createTree(accessToken, headers, owner, repo, baseTree, treeDataList)
          case CreateTag(owner, repo, tag, message, objectSha, objectType, author, accessToken) ⇒
            gitData.createTag(
              accessToken,
              headers,
              owner,
              repo,
              tag,
              message,
              objectSha,
              objectType,
              author)
        }
      }
    }

  /**
   * Lifts PullRequest Ops to an effect capturing Monad such as Task via natural transformations
   */
  def pullRequestOpsInterpreter: PullRequestOp ~> K =
    new (PullRequestOp ~> K) {

      val pullRequests = new PullRequests()

      def apply[A](fa: PullRequestOp[A]): K[A] = Kleisli[M, Map[String, String], A] { headers =>
        fa match {
          case ListPullRequests(owner, repo, filters, accessToken) ⇒
            pullRequests.list(accessToken, headers, owner, repo, filters)
          case ListPullRequestFiles(owner, repo, number, accessToken) ⇒
            pullRequests.listFiles(accessToken, headers, owner, repo, number)
          case CreatePullRequest(
              owner,
              repo,
              newPullRequest,
              head,
              base,
              maintainerCanModify,
              accessToken) ⇒
            pullRequests
              .create(
                accessToken,
                headers,
                owner,
                repo,
                newPullRequest,
                head,
                base,
                maintainerCanModify)
        }
      }
    }

  /** Lifts Status Ops to an effect capturing Monad such as Task via natural transformations */
  def statusOpsInterpreter: StatusOp ~> K =
    new (StatusOp ~> K) {

      val statuses = new Statuses()

      def apply[A](fa: StatusOp[A]): K[A] = Kleisli[M, Map[String, String], A] { headers =>
        fa match {
          case GetCombinedStatus(owner, repo, ref, accessToken) ⇒
            statuses.get(accessToken, headers, owner, repo, ref)
          case ListStatuses(owner, repo, ref, accessToken) ⇒
            statuses.list(accessToken, headers, owner, repo, ref)
          case CreateStatus(
              owner,
              repo,
              sha,
              state,
              target_url,
              description,
              context,
              accessToken) ⇒
            statuses.create(
              accessToken,
              headers,
              owner,
              repo,
              sha,
              state,
              target_url,
              description,
              context)
        }
      }
    }

}
