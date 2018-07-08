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

package github4s.cats.effect

import scala.concurrent.{ExecutionContext, Future}

import cats.Eval
import cats.effect.Async
import fr.hmil.roshttp.response.SimpleHttpResponse
import github4s.{HttpRequestBuilder, HttpRequestBuilderExtension, HttpRequestBuilderExtensionJS}
import github4s.GithubResponses.GHResponse
import io.circe.Decoder

trait AsyncHttpRequestBuilderExtensionJS extends HttpRequestBuilderExtensionJS {

  import monix.execution.Scheduler.Implicits.global

  implicit def extensionAsyncJS[F[_]: Async]: HttpRequestBuilderExtension[SimpleHttpResponse, F] =
    new HttpRequestBuilderExtension[SimpleHttpResponse, F] {

      def run[A](rb: HttpRequestBuilder[SimpleHttpResponse, F])(
          implicit D: Decoder[A]): F[GHResponse[A]] =
        runMapWrapper[A](rb, decodeEntity[A])

      def runEmpty(rb: HttpRequestBuilder[SimpleHttpResponse, F]): F[GHResponse[Unit]] =
        runMapWrapper[Unit](rb, emptyResponse)

      private[this] def runMapWrapper[A](
          rb: HttpRequestBuilder[SimpleHttpResponse, F],
          mapResponse: SimpleHttpResponse => GHResponse[A]): F[GHResponse[A]] =
        fromFuture(runMap[A, F](rb, mapResponse))

      /** Taken from Http4s **/
      private def fromFuture[A](future: Eval[Future[A]])(implicit ec: ExecutionContext): F[A] =
        Async[F].async { cb =>
          import scala.util.{Failure, Success}
          future.value.onComplete {
            case Failure(e) => cb(Left(e))
            case Success(a) => cb(Right(a))
          }
        }
      private def fromFuture[A](future: => Future[A])(implicit ec: ExecutionContext): F[A] =
        fromFuture(Eval.always(future))

    }
}
