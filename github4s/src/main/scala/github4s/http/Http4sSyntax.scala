/*
 * Copyright 2016-2020 47 Degrees, LLC. <http://www.47deg.com>
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

package github4s.http

import org.http4s._
import org.http4s.MediaType
import org.http4s.Headers
import io.circe.{Encoder, Json, Printer}
import io.circe.syntax._
import org.http4s.headers.`Content-Type`

object Http4sSyntax {

  implicit class RequestOps[F[_]](self: Request[F]) {
    def withJsonBody[T](maybeData: Option[T])(implicit enc: Encoder[T]): Request[F] =
      maybeData.fold(self)(data =>
        self
          .withContentType(`Content-Type`(MediaType.application.json))
          .withEntity(data.asJson.noSpaceNorNull)
      )
  }

  implicit class JsonOps(val self: Json) extends AnyVal {
    def noSpaceNorNull: String = Printer.noSpaces.copy(dropNullValues = true).print(self)
  }

  implicit class HeadersOps(self: Headers) {
    def toMap: Map[String, String] =
      self.toList.map(header => (header.name.value, header.value)).toMap
  }

  implicit class RequestBuilderOps[R](val self: RequestBuilder[R]) extends AnyVal {

    def toHeaderList: List[Header] =
      (self.headers.map(kv => Header(kv._1, kv._2)) ++
        self.authHeader.map(kv => Header(kv._1, kv._2))).toList

    def toUri(urls: GithubAPIv3Config): Uri =
      Uri.fromString(self.url).getOrElse(Uri.unsafeFromString(urls.baseUrl)) =?
        self.params.map(kv => (kv._1, List(kv._2)))

  }

}
