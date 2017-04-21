/*
 * Copyright 2016-2017 47 Degrees, LLC. <http://www.47deg.com>
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

import github4s.GithubResponses.{GHResult, JsonParsingException, UnexpectedException}
import io.circe.Decoder
import io.circe.parser._
import github4s.GithubResponses.GHResponse

import scalaj.http._
import cats.implicits._
import github4s.free.interpreters.Capture

trait HttpRequestBuilderExtensionJVM {

  implicit def extensionJVM[M[_]](
      implicit C: Capture[M]): HttpRequestBuilderExtension[HttpResponse[String], M] =
    new HttpRequestBuilderExtension[HttpResponse[String], M] {

      def run[A](rb: HttpRequestBuilder[HttpResponse[String], M])(
          implicit D: Decoder[A]): M[GHResponse[A]] = {

        val connTimeoutMs: Int = 1000
        val readTimeoutMs: Int = 5000

        val params = rb.params.map {
          case (key, value) => s"$key=$value"
        } mkString ("?", "&", "")

        val request = Http(url = rb.url)
          .method(rb.httpVerb.verb)
          .option(HttpOptions.connTimeout(connTimeoutMs))
          .option(HttpOptions.readTimeout(readTimeoutMs))
          .params(rb.params)
          .headers(rb.authHeader)
          .headers(rb.headers)
          .copy(urlBuilder = (req: HttpRequest) => s"${req.url}$params")

        rb.data match {
          case Some(d) ⇒
            C.capture(
              toEntity[A](
                request
                  .postData(d)
                  .header("content-type", "application/json")
                  .asString,
                request.url))
          case _ ⇒ C.capture(toEntity[A](request.asString, request.url))
        }
      }

    }

  def toEntity[A](response: HttpResponse[String], url: String)(
      implicit D: Decoder[A]): GHResponse[A] = response match {
    case r if r.isSuccess ⇒
      decode[A](r.body).fold(
        e ⇒ Either.left(JsonParsingException(e.getMessage, r.body)),
        result ⇒ Either.right(GHResult(result, r.code, toLowerCase(r.headers)))
      )
    case r ⇒
      Either.left(
        UnexpectedException(s"Failed invoking get with status : ${r.code}, body : \n ${r.body}"))
  }

  private def toLowerCase(
      headers: Map[String, IndexedSeq[String]]): Map[String, IndexedSeq[String]] =
    headers.map(e ⇒ (e._1.toLowerCase, e._2))
}
