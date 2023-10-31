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

package org.scalacheck.fortyseven
// Note: Needs to be in org.scalacheck to access package-private methods

import cats._
import cats.syntax.all._
import org.scalacheck.Gen

object GenInstances extends GenInstances

trait GenInstances extends GenInstances1

/*
Copyright Chris Davenport, 2020

MIT License: https://github.com/davenverse/cats-scalacheck/blob/main/LICENSE

Original source at: https://github.com/davenverse/cats-scalacheck/blob/a17cfeadb7caf07374676994d9066eca4319a8fc/core/shared/src/main/scala/org/scalacheck/cats/instances/GenInstances.scala

Copied here since cats-scalacheck isn't yet published for scala3. This can be deleted once it is
 */

sealed private[fortyseven] trait GenInstances1 extends GenInstances0 {
  implicit val genInstances: Monad[Gen] with Alternative[Gen] with FunctorFilter[Gen] =
    new Monad[Gen] with Alternative[Gen] with FunctorFilter[Gen] {
      // Members declared in cats.Applicative
      override def pure[A](x: A): Gen[A] =
        Gen.const(x)

      // Members declared in cats.FlatMap
      override def flatMap[A, B](fa: Gen[A])(f: A => Gen[B]): Gen[B] =
        fa.flatMap(f)
      override def tailRecM[A, B](a: A)(f: A => Gen[Either[A, B]]): Gen[B] =
        Gen.tailRecM(a)(f)

      override def combineK[A](x: Gen[A], y: Gen[A]): Gen[A] = Gen.gen { (params, seed) =>
        val xGen = x.doApply(params, seed)
        if (xGen.retrieve.isDefined) xGen
        else y.doApply(params, seed)
      }

      override def empty[A]: Gen[A] = Gen.fail

      override def map2Eval[A, B, Z](fa: Gen[A], fb: Eval[Gen[B]])(f: (A, B) => Z): Eval[Gen[Z]] =
        Eval.later(map2(fa, Gen.lzy(fb.value))(f))

      override def product[A, B](fa: Gen[A], fb: Gen[B]): Gen[(A, B)] = Gen.zip(fa, fb)

      override def functor: Functor[Gen] = this

      override def mapFilter[A, B](fa: Gen[A])(f: A => Option[B]): Gen[B] =
        fa.flatMap { a =>
          f(a) match {
            case Some(b) => pure(b)
            case _       => Gen.fail
          }
        }
    }

  implicit def genMonoid[A: Monoid]: Monoid[Gen[A]] = new Monoid[Gen[A]] {
    override def empty: Gen[A] = Gen.const(Monoid[A].empty)
    override def combine(x: Gen[A], y: Gen[A]) = for {
      xa <- x
      ya <- y
    } yield xa |+| ya
  }

}

sealed private[fortyseven] trait GenInstances0 {
  implicit def genSemigroup[A: Semigroup]: Semigroup[Gen[A]] = new Semigroup[Gen[A]] {
    override def combine(x: Gen[A], y: Gen[A]) = for {
      xa <- x
      ya <- y
    } yield xa |+| ya
  }
}
