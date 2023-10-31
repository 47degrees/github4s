/*
 * Copyright 2016-2023 47 Degrees Open Source <https://www.47deg.com>
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
    id: Long,
    node_id: String,
    name: String,
    number: Int,
    creator: Creator,
    created_at: String,
    updated_at: String,
    body: Option[String] = None,
    organization_permission: Option[String] = None,
    `private`: Option[Boolean] = None
)

final case class Creator(
    login: String,
    id: Long,
    node_id: String,
    avatar_url: String,
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
    site_admin: Boolean,
    gravatar_id: Option[String] = None
)

final case class Column(
    url: String,
    project_url: String,
    cards_url: String,
    id: Long,
    node_id: String,
    name: String,
    created_at: String,
    updated_at: String
)

final case class Card(
    url: String,
    project_url: String,
    id: Long,
    node_id: String,
    archived: Boolean,
    creator: Creator,
    created_at: String,
    updated_at: String,
    column_url: String,
    note: Option[String] = None,
    content_url: Option[String] = None
)
