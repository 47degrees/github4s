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

package github4s.http

import github4s.GithubConfig
import io.circe.syntax._
import io.circe.{Encoder, Json, Printer}
import org.http4s._
import org.http4s.headers.`Content-Type`
import org.typelevel.ci.CIString

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
      self.headers.map(header => (header.name.toString, header.value)).toMap
  }

  implicit class RequestBuilderOps[R](val self: RequestBuilder[R]) extends AnyVal {

    def toHeaderList: List[Header.Raw] =
      (self.headers.map(kv => Header.Raw(CIString(kv._1), kv._2)) ++
        self.authHeader.map(kv => Header.Raw(CIString(kv._1), kv._2))).toList

    def toUri(config: GithubConfig): Uri = {
      val queryString = self.params.toList
        .map { case (k, v) =>
          s"$k=$v"
        }
        .mkString("&")

      // Adding query parameters normally has different encoding than normal Uris.
      // To work around this, we create one verbatim from a manually encoded String.
      // See: https://github.com/http4s/http4s/issues/4203
      val verbatimQuery = Query.unsafeFromString(Uri.encode(queryString))

      Uri
        .fromString(self.url)
        .getOrElse(Uri.unsafeFromString(config.baseUrl))
        .copy(query = verbatimQuery)
    }

  }

}
