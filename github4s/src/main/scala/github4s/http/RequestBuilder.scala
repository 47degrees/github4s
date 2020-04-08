/*
 * Copyright 2016-2020 47 Degrees <https://www.47deg.com>
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

import org.http4s.Method

case class RequestBuilder[Res](
    url: String,
    httpVerb: Method = Method.GET,
    authHeader: Map[String, String] = Map.empty[String, String],
    data: Option[Res] = None,
    params: Map[String, String] = Map.empty[String, String],
    headers: Map[String, String] = Map.empty[String, String]
) {

  def postMethod: RequestBuilder[Res] = this.copy(httpVerb = Method.POST)

  def patchMethod: RequestBuilder[Res] = this.copy(httpVerb = Method.PATCH)

  def putMethod: RequestBuilder[Res] = this.copy(httpVerb = Method.PUT)

  def deleteMethod: RequestBuilder[Res] = this.copy(httpVerb = Method.DELETE)

  def withHeaders(headers: Map[String, String]): RequestBuilder[Res] = this.copy(headers = headers)

  def withParams(params: Map[String, String]): RequestBuilder[Res] = this.copy(params = params)

  def withData(data: Res): RequestBuilder[Res] = this.copy(data = Some(data))

  def withAuth(accessToken: Option[String] = None): RequestBuilder[Res] =
    this.copy(authHeader = accessToken match {
      case Some(token) => Map("Authorization" -> s"token $token")
      case _           => Map.empty[String, String]
    })
}
