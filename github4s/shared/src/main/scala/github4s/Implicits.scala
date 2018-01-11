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

package github4s

import cats.instances.FutureInstances
import cats.{Eval, FlatMap, Id, Monad, MonadError}
import github4s.free.interpreters._
import scala.concurrent.Future

object implicits
    extends FutureCaptureInstance
    with EvalInstances
    with IdInstances
    with FutureInstances

trait FutureCaptureInstance {
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

  implicit def idMonad(implicit I: Monad[Id]): MonadError[Id, Throwable] =
    new MonadError[Id, Throwable] {

      override def pure[A](x: A): Id[A] = I.pure(x)

      override def ap[A, B](ff: Id[A ⇒ B])(fa: Id[A]): Id[B] = I.ap(ff)(fa)

      override def map[A, B](fa: Id[A])(f: Id[A ⇒ B]): Id[B] = I.map(fa)(f)

      override def flatMap[A, B](fa: Id[A])(f: A ⇒ Id[B]): Id[B] = I.flatMap(fa)(f)

      override def tailRecM[A, B](a: A)(f: A ⇒ Id[Either[A, B]]): Id[B] = I.tailRecM(a)(f)

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
