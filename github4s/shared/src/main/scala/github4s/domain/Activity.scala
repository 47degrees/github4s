/*
 * Copyright 2016-2022 47 Degrees Open Source <https://www.47deg.com>
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
    created_at: String,
    url: String,
    thread_url: String,
    reason: Option[String] = None
)

final case class SubscriptionRequest(
    subscribed: Boolean,
    ignored: Boolean
)

final case class Stargazer(
    user: User,
    starred_at: Option[String] = None
)

final case class StarredRepository(
    repo: Repository,
    starred_at: Option[String] = None
)

final case class PublicOrganizationEvent(
    id: Long,
    `type`: String,
    actor_login: String,
    repo_full_name: String,
    public: Boolean,
    created_at: String
)
