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

package github4s.api

import github4s.free.domain._
import github4s.{Decoders, GithubApiUrls, HttpClient}
import io.circe.generic.auto._
import io.circe.syntax._
import cats.implicits._

/** Factory to encapsulate calls related to Repositories operations  */
class Gists(implicit urls: GithubApiUrls) {
  import Decoders._

  val httpClient = new HttpClient

  /**
    * Create a new gist
    *
    * @param description of the gist
    * @param public boolean value that describes if the Gist should be public or not
    * @param files map describing the filenames of the Gist and its contents
    * @param accessToken to identify the authenticated user
    */
  def newGist(description: String,
              public: Boolean,
              files: Map[String, GistFile],
              accessToken: Option[String] = None) =
    httpClient.post[Gist](
      accessToken,
      "gists",
      data = NewGistRequest(description, public, files).asJson.noSpaces
    )

}
