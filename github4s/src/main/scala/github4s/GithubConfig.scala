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

import org.http4s.Header

/**
 * Configuration for github4s
 * @param baseUrl of the GitHub API. If you use GitHub enterprise, you'll need to modify this.
 * @param authorizeUrl for the first step of the oAuth process. If you use GitHub enterprise, you'll need to modify this.
 * @param accessTokenUrl for the second step of the oAuth process. If you use GitHub enterprise, you'll need to modify this.
 * @param headers to add to all requests sent to the GitHub API. Defaults to "github4s" as user agent.
 */
final case class GithubConfig(
    baseUrl: String,
    authorizeUrl: String,
    accessTokenUrl: String,
    headers: Map[String, String]
) {
  def toHeaderList: List[Header] = headers.map { case (k, v) => Header(k, v) }.toList
}

object GithubConfig {
  implicit val default: GithubConfig =
    GithubConfig(
      baseUrl = "https://api.github.com/",
      authorizeUrl =
        "https://github.com/login/oauth/authorize?client_id=%s&redirect_uri=%s&scope=%s&state=%s",
      accessTokenUrl = "https://github.com/login/oauth/access_token",
      headers = Map("User-Agent" -> "github4s")
    )
}
