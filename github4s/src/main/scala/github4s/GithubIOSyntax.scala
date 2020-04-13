/*
 * Copyright 2016-2020 47 Degrees Open Source <https://www.47deg.com>
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

import cats.Id
import cats.effect.{IO, LiftIO}

import scala.concurrent.Future

object GithubIOSyntax {

  implicit val futureLiftIO: LiftIO[Future] = new LiftIO[Future] {

    override def liftIO[A](ioa: IO[A]): Future[A] = ioa.unsafeToFuture()
  }

  implicit val idLiftIO: LiftIO[Id] = new LiftIO[Id] {

    override def liftIO[A](ioa: IO[A]): Id[A] = ioa.unsafeRunSync()
  }

  implicit class IOOps[A](val self: IO[A]) extends AnyVal {

    def toFuture(implicit liftIO: LiftIO[Future]): Future[A] = liftIO.liftIO(self)

    def toId(implicit liftIO: LiftIO[Id]): Id[A] = liftIO.liftIO(self)
  }
}
