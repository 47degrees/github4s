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

import cats.implicits._
import cats.instances.FutureInstances
import cats.{Monad, Id, Eval, MonadError, FlatMap}
import github4s.free.interpreters._
import scala.concurrent.{ExecutionContext, Future}

object implicits extends Interpreters with EvalInstances with IdInstances with FutureInstances {

  //Future Capture evidence:
  implicit val futureCaptureInstance = new Capture[Future] {
    override def capture[A](a: ⇒ A): Future[A] = Future.successful(a)
  }

}

trait EvalInstances {

  implicit val evalCaptureInstance = new Capture[Eval] {
    override def capture[A](a: ⇒ A): Eval[A] = Eval.later(a)
  }

  implicit def evalMonadError(implicit FM: FlatMap[Eval]): MonadError[Eval, Throwable] =
    new MonadError[Eval, Throwable] {

      override def pure[A](x: A): Eval[A] = Eval.now(x)

      override def map[A, B](fa: Eval[A])(f: A ⇒ B): Eval[B] = fa.map(f)

      override def flatMap[A, B](fa: Eval[A])(ff: A ⇒ Eval[B]): Eval[B] =
        fa.flatMap(ff)

      override def tailRecM[A, B](a: A)(f: A ⇒ Eval[Either[A, B]]): Eval[B] = FM.tailRecM(a)(f)

      override def raiseError[A](e: Throwable): Eval[A] =
        Eval.later({ throw e })

      override def handleErrorWith[A](fa: Eval[A])(f: Throwable ⇒ Eval[A]): Eval[A] =
        Eval.later({
          try {
            fa.value
          } catch {
            case e: Throwable ⇒ f(e).value
          }
        })
    }

}

trait IdInstances {

  implicit val idCaptureInstance = new Capture[Id] {
    override def capture[A](a: ⇒ A): Id[A] = idMonad.pure(a)
  }

  implicit def idMonad(implicit I: Monad[Id], FM: FlatMap[Id]): MonadError[Id, Throwable] =
    new MonadError[Id, Throwable] {

      override def pure[A](x: A): Id[A] = I.pure(x)

      override def ap[A, B](ff: Id[A ⇒ B])(fa: Id[A]): Id[B] = I.ap(ff)(fa)

      override def map[A, B](fa: Id[A])(f: Id[A ⇒ B]): Id[B] = I.map(fa)(f)

      override def flatMap[A, B](fa: Id[A])(f: A ⇒ Id[B]): Id[B] = I.flatMap(fa)(f)

      override def tailRecM[A, B](a: A)(f: A ⇒ Id[Either[A, B]]): Id[B] = FM.tailRecM(a)(f)

      override def raiseError[A](e: Throwable): Id[A] = throw e

      override def handleErrorWith[A](fa: Id[A])(f: Throwable ⇒ Id[A]): Id[A] = {
        try {
          fa
        } catch {
          case e: Exception ⇒ f(e)
        }
      }
    }

}
