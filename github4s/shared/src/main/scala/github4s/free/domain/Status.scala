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

package github4s.free.domain

case class Status(
  id: Int,
  url: String,
  state: String,
  target_url: Option[String],
  description: Option[String],
  context: Option[String],
  creator: Option[User],
  created_at: String,
  updated_at: String
)

case class NewStatusRequest(
  state: String,
  target_url: Option[String],
  description: Option[String],
  context: Option[String]
)

case class StatusRepository(
  id: Int,
  name: String,
  full_name: String,
  owner: User,
  `private`: Boolean,
  description: Option[String],
  fork: Boolean,
  urls: Map[String, String]
)

case class CombinedStatus(
  url: String,
  state: String,
  commit_url: String,
  sha: String,
  total_count: Int,
  statuses: List[Status],
  repository: StatusRepository
)