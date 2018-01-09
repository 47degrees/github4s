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

import cats.effect.IO
import fr.hmil.roshttp.response.SimpleHttpResponse
import github4s.{HttpRequestBuilder, HttpRequestBuilderExtension, HttpRequestBuilderExtensionJS}
import github4s.GithubResponses.GHResponse
import io.circe.Decoder

import scala.concurrent.Future

trait IOHttpRequestBuilderExtensionJS extends HttpRequestBuilderExtensionJS {

  import monix.execution.Scheduler.Implicits.global

  implicit def extensionIOJS: HttpRequestBuilderExtension[SimpleHttpResponse, IO] =
    new HttpRequestBuilderExtension[SimpleHttpResponse, IO] {

      def run[A](rb: HttpRequestBuilder[SimpleHttpResponse, IO])(
          implicit D: Decoder[A]): IO[GHResponse[A]] =
        runMapWrapper[A](rb, decodeEntity[A])

      def runEmpty(rb: HttpRequestBuilder[SimpleHttpResponse, IO]): IO[GHResponse[Unit]] =
        runMapWrapper[Unit](rb, emptyResponse)

      private[this] def runMapWrapper[A](
          rb: HttpRequestBuilder[SimpleHttpResponse, IO],
          mapResponse: SimpleHttpResponse => GHResponse[A]): IO[GHResponse[A]] =
        IO.fromFuture(IO(runMap[A, IO](rb, mapResponse)))
    }
}
