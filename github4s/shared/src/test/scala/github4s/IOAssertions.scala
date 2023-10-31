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

package github4s

import cats.Eq
import cats.effect.{unsafe, IO}
import org.scalactic.Prettifier
import org.scalactic.source.Position
import org.scalatest.exceptions.TestFailedException
import org.scalatest.{Assertion, AsyncTestSuite}

import scala.concurrent.Future
import scala.concurrent.duration._
import scala.reflect.ClassTag

/* Copied from: https://gist.github.com/Daenyth/67575575b5c1acc1d6ea100aae05b3a9
   Under MIT license
 */
trait IOAssertions { self: AsyncTestSuite with org.scalatest.Assertions =>

  // It's "extremely unsafe" because it's an implicit conversion that takes a pure value
  // and silently converts it to a side effecting value that begins running on a thread immediately.
  implicit def extremelyUnsafeIOAssertionToFuture(ioa: IO[Assertion])(implicit
      pos: Position,
      ioRuntime: unsafe.IORuntime
  ): Future[Assertion] = {
    val _ = pos // unused here; exists for override to use
    ioa.unsafeToFuture()
  }

  implicit protected class IOAssertionOps[A](val io: IO[A])(implicit
      pos: Position
  ) {

    /**
     * Same as shouldNotFail, but preferable in cases where explicit type signatures are not used,
     * as `shouldNotFail` will discard any result, and this method will only compile where the
     * author intends `io` to be made from assertions.
     *
     * @example
     *   {{{ List(1,2,3,4,5,6).traverse { n => databaseCheck(n).map { result => result shouldEqual
     *   GoodValue } }.flattenAssertion }}}
     */
    def flattenAssertion(implicit ev: A <:< Seq[Assertion]): IO[Assertion] = {
      val _ = ev // "unused implicit" warning
      io.shouldNotFail
    }

    def shouldResultIn(
        expected: A
    )(implicit eq: Eq[A], prettifier: Prettifier): IO[Assertion] =
      io.flatMap { actual =>
        IO(assert(eq.eqv(expected, actual)))
      }

    def shouldNotFail: IO[Assertion] = io.attempt.flatMap {
      case Left(failed: TestFailedException) =>
        IO.raiseError(failed)
      case Left(err) =>
        IO(fail(s"IO Failed with ${err.getMessage}", err))
      case Right(_) =>
        IO.pure(succeed)

    }

    def shouldTerminate(
        within: FiniteDuration = 5.seconds
    ): IO[Assertion] =
      io.shouldNotFail.timeoutTo(within, IO(fail(s"IO didn't terminate within $within")))

    /**
     * Equivalent to [[org.scalatest.Assertions.assertThrows]] for [[cats.effect.IO]]
     */
    def shouldFailWith[T <: AnyRef](implicit
        classTag: ClassTag[T]
    ): IO[Assertion] =
      io.attempt.flatMap { attempt =>
        IO(assertThrows[T](attempt.toTry.get))
      }
  }
}
