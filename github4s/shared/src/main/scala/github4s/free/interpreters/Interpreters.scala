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

package github4s.free.interpreters

import cats.data.Kleisli
import cats.implicits._
import cats.{ApplicativeError, Eval, MonadError, ~>}
import github4s.GithubDefaultUrls._
import github4s.HttpRequestBuilderExtension
import github4s.api._
import github4s.app._
import github4s.free.algebra._
import io.circe.Decoder
import simulacrum.typeclass

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
    val all: GitHub4s ~> K          = gitDataOpsInterpreter or c04interpreter
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
        }
      }
    }

}
