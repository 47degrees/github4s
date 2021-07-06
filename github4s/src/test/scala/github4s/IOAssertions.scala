package github4s

import cats.Eq
import cats.effect.{ContextShift, IO, Timer}
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
      pos: Position
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
    )(implicit timer: Timer[IO], CS: ContextShift[IO]): IO[Assertion] =
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
