/*
 * Copyright (c) 2016 47 Degrees, LLC. <http://www.47deg.com>
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
 * the Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
 * FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
 * IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package github4s

import github4s.GithubResponses.{GHResult, JsonParsingException, UnexpectedException}
import io.circe.Decoder
import io.circe.parser._
import io.circe.generic.auto._
import github4s.GithubResponses.GHResponse

import scalaj.http._
import cats.implicits._
import github4s.GithubDefaultUrls._
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
