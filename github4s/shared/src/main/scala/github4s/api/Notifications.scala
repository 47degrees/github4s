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

import github4s.GithubResponses.GHResponse
import github4s.{GithubApiUrls, HttpClient, HttpRequestBuilderExtension}
import github4s.free.domain._
import github4s.free.interpreters.Capture
import github4s.util.URLEncoder
import io.circe.syntax._
import io.circe.generic.auto._

/** Factory to encapsulate calls related to Notifications operations  */
class Notifications[C, M[_]](
    implicit urls: GithubApiUrls,
    C: Capture[M],
    httpClientImpl: HttpRequestBuilderExtension[C, M]) {

  val httpClient = new HttpClient[C, M]

  /**
   * Set a thread subscription
   *
   * @param accessToken Token to identify the authenticated user
   * @param headers Optional user headers to include in the request
   * @param id Conversation id for subscribe or unsubscribe
   * @param subscribed Determines if notifications should be received from this thread
   * @param ignored Determines if all notifications should be blocked from this thread
   */
  def setThreadSub(
      accessToken: Option[String] = None,
      headers: Map[String, String] = Map(),
      id: Int,
      subscribed: Boolean,
      ignored: Boolean): M[GHResponse[Subscription]] =
    httpClient.put[Subscription](
      accessToken,
      s"notifications/threads/$id/subscription",
      headers,
      data = SubscriptionRequest(subscribed, ignored).asJson.noSpaces)

}
