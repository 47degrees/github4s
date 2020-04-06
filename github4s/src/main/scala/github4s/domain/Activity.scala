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

package github4s.domain

final case class Subscription(
    subscribed: Boolean,
    ignored: Boolean,
    reason: Option[String],
    created_at: String,
    url: String,
    thread_url: String
)

final case class SubscriptionRequest(
    subscribed: Boolean,
    ignored: Boolean
)

final case class Stargazer(
    starred_at: Option[String],
    user: User
)

final case class StarredRepository(
    starred_at: Option[String],
    repo: Repository
)
