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

final case class Gist(
    url: String,
    id: String,
    description: String,
    public: Boolean,
    files: Map[String, GistFile]
)

final case class GistFile(
    content: String
)

final case class NewGistRequest(
    description: String,
    public: Boolean,
    files: Map[String, GistFile]
)

final case class EditGistFile(
    content: String,
    filename: Option[String] = None
)

final case class EditGistRequest(
    description: String,
    files: Map[String, Option[EditGistFile]]
)
