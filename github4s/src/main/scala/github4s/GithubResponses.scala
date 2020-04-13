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

object GithubResponses {

  final case class GHResponse[A](
      result: Either[GHException, A],
      statusCode: Int,
      headers: Map[String, String]
  )

  sealed abstract class GHException extends Exception {
    final override def fillInStackTrace(): Throwable = this
  }

  final case class JsonParsingException(
      msg: String,
      json: String
  ) extends GHException {
    final override def getMessage: String = msg
  }

}
