/*
 * Copyright 2016-2018 47 Degrees, LLC. <http://www.47deg.com>
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

package github4s.scalaz

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
