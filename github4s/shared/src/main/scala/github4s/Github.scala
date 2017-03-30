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

package github4s

import cats.data.{EitherT, Kleisli}
import cats.MonadError
import cats.implicits._
import github4s.GithubResponses._
import github4s.app._
import github4s.free.interpreters.Interpreters

import scala.concurrent.Future

/**
 * Represent the Github API wrapper
 * @param accessToken to identify the authenticated user
 */
class Github(accessToken: Option[String] = None) {

  lazy val users        = new GHUsers(accessToken)
  lazy val repos        = new GHRepos(accessToken)
  lazy val auth         = new GHAuth(accessToken)
  lazy val gists        = new GHGists(accessToken)
  lazy val issues       = new GHIssues(accessToken)
  lazy val gitData      = new GHGitData(accessToken)
  lazy val pullRequests = new GHPullRequests(accessToken)

}

/** Companion object for [[github4s.Github]] */
object Github {
  def apply(accessToken: Option[String] = None) = new Github(accessToken)

  implicit class GithubIOSyntaxEither[A](gio: GHIO[GHResponse[A]]) {

    def execK[M[_], C](
        implicit I: Interpreters[M, C],
        A: MonadError[M, Throwable],
        H: HttpRequestBuilderExtension[C, M]): Kleisli[M, Map[String, String], GHResponse[A]] =
      gio foldMap I.interpreters

    def exec[M[_], C](headers: Map[String, String] = Map())(
        implicit I: Interpreters[M, C],
        A: MonadError[M, Throwable],
        H: HttpRequestBuilderExtension[C, M]): M[GHResponse[A]] =
      execK.run(headers)

    def execFuture[C](headers: Map[String, String] = Map())(
        implicit I: Interpreters[Future, C],
        A: MonadError[Future, Throwable],
        H: HttpRequestBuilderExtension[C, Future]): Future[GHResponse[A]] =
      exec[Future, C](headers)

    def liftGH: EitherT[GHIO, GHException, GHResult[A]] =
      EitherT[GHIO, GHException, GHResult[A]](gio)

  }
}
