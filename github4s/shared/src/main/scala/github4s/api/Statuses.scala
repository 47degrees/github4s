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

package github4s.api

import github4s._
import github4s.GithubResponses.GHResponse
import github4s.{GithubApiUrls, HttpClient, HttpRequestBuilderExtension}
import github4s.free.domain.{CombinedStatus, NewStatusRequest, Status}
import github4s.free.interpreters.Capture
import io.circe.generic.auto._
import io.circe.syntax._

/** Factory to encapsulate calls related to Statuses operations */
class Statuses[C, M[_]](implicit urls: GithubApiUrls,
                        C: Capture[M],
                        httpClientImpl: HttpRequestBuilderExtension[C, M]) {

  import Decoders._

  val httpClient = new HttpClient[C, M]

  /**
    * Get the combined status for a specific ref
    *
    * @param accessToken to identify the authenticated user
    * @param headers optional user headers to include in the request
    * @param owner of the repo
    * @param repo name of the commit
    * @param ref commit SHA, branch name or tag name
    * @return a GHResponse with the combined status
    */
  def get(accessToken: Option[String] = None,
          headers: Map[String, String] = Map(),
          owner: String,
          repo: String,
          ref: String): M[GHResponse[CombinedStatus]] =
    httpClient.get[CombinedStatus](accessToken, s"repos/$owner/$repo/commits/$ref/status", headers)

  /**
    * List statuses for a commit
    *
    * @param accessToken to identify the authenticated user
    * @param headers optional user headers to include in the request
    * @param owner of the repo
    * @param repo name of the repo
    * @param ref commit SHA, branch name or tag name
    * @return a GHResponse with the status list
    */
  def list(accessToken: Option[String] = None,
           headers: Map[String, String] = Map(),
           owner: String,
           repo: String,
           ref: String): M[GHResponse[List[Status]]] =
    httpClient.get[List[Status]](accessToken, s"repos/$owner/$repo/commits/$ref/statuses", headers)

  /**
    * Create a status
    *
    * @param accessToken to identify the authenticated user
    * @param headers optional user headers to include in the request
    * @param owner of the repo
    * @param repo name of the repo
    * @param sha commit sha to create the status on
    * @param target_url url to associate with the status, will appear in the GitHub UI
    * @param state of the status: pending, success, error, or failure
    * @param description of the status
    * @param context identifier of the status maker
    * @return a GHResopnse with the created Status
    */
  def create(accessToken: Option[String] = None,
             headers: Map[String, String] = Map(),
             owner: String,
             repo: String,
             sha: String,
             state: String,
             target_url: Option[String],
             description: Option[String],
             context: Option[String]): M[GHResponse[Status]] =
    httpClient.post[Status](
      accessToken,
      s"repos/$owner/$repo/statuses/$sha",
      headers,
      dropNullPrint(NewStatusRequest(state, target_url, description, context).asJson))
}