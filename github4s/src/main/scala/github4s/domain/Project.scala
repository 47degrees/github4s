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

final case class Project(
    owner_url: String,
    url: String,
    html_url: String,
    columns_url: String,
    id: Int,
    node_id: String,
    name: String,
    body: Option[String],
    number: Int,
    creator: Creator,
    created_at: String,
    updated_at: String,
    organization_permission: Option[String],
    `private`: Option[Boolean]
)

final case class Creator(
    login: String,
    id: Int,
    node_id: String,
    avatar_url: String,
    gravatar_id: Option[String],
    url: String,
    html_url: String,
    followers_url: String,
    following_url: String,
    gists_url: String,
    starred_url: String,
    subscriptions_url: String,
    organizations_url: String,
    repos_url: String,
    events_url: String,
    received_events_url: String,
    `type`: String,
    site_admin: Boolean
)

final case class Column(
    url: String,
    project_url: String,
    cards_url: String,
    id: Int,
    node_id: String,
    name: String,
    created_at: String,
    updated_at: String
)
