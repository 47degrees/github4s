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

package github4s.scalaz

import cats.implicits._
import github4s.Github._
import cats.MonadError
import github4s.HttpRequestBuilderExtensionJVM

import scalaz.concurrent.Task
import github4s.free.interpreters.{Capture, Interpreters}

import scalaj.http.HttpResponse
import scalaz._

object implicits extends HttpRequestBuilderExtensionJVM {

  implicit val taskCaptureInstance = new Capture[Task] {
    override def capture[A](a: ⇒ A): Task[A] = Task.now(a)
  }

  implicit def g4sTaskMonadError: MonadError[Task, Throwable] =
    new MonadError[Task, Throwable] {

      override def pure[A](x: A): Task[A] = Task.now(x)

      override def map[A, B](fa: Task[A])(f: A ⇒ B): Task[B] = fa.map(f)

      override def flatMap[A, B](fa: Task[A])(ff: A ⇒ Task[B]): Task[B] =
        fa.flatMap(ff)

      override def tailRecM[A, B](a: A)(f: A ⇒ Task[Either[A, B]]): Task[B] =
        Task.tailrecM((a: A) ⇒ f(a) map (t ⇒ toScalazDisjunction(t)))(a)

      override def raiseError[A](e: Throwable): Task[A] = Task.fail(e)

      override def handleErrorWith[A](fa: Task[A])(f: Throwable ⇒ Task[A]): Task[A] =
        fa.handleWith({ case x ⇒ f(x) })

    }

  implicit val intInstanceTaskScalaJ =
    new Interpreters[Task, HttpResponse[String]]

  private[this] def toScalazDisjunction[A, B](disj: Either[A, B]): A \/ B =
    disj.fold(l ⇒ -\/(l), r ⇒ \/-(r))
}
