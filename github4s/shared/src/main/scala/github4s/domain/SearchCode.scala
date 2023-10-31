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

final case class SearchCodeResult(
    total_count: Int,
    incomplete_results: Boolean,
    items: List[SearchCodeResultItem]
)

final case class SearchCodeResultItem(
    name: String,
    path: String,
    sha: String,
    url: String,
    git_url: String,
    html_url: String,
    repository: RepositoryMinimal,
    score: Double,
    file_size: Option[Long],
    language: Option[String],
    last_modified_at: Option[String],
    line_numbers: Option[List[String]],
    text_matches: Option[List[SearchResultTextMatch]]
)

sealed trait SearchCodeParam {
  protected def paramName: String
  protected def paramValue: String
  def value: String = s"$paramName:$paramValue"
}

object SearchCodeParam {

  final case class In(values: Set[In.Value]) extends SearchCodeParam {
    override def paramName: String  = "in"
    override def paramValue: String = values.map(_.value).mkString(",")
  }
  object In {
    sealed trait Value {
      def value: String
    }
    case object File extends Value {
      override def value: String = "file"
    }
    case object Path extends Value {
      override def value: String = "path"
    }
  }

  final case class User(name: String) extends SearchCodeParam {
    override def paramName: String  = "user"
    override def paramValue: String = name
  }

  final case class Organization(name: String) extends SearchCodeParam {
    override def paramName: String  = "org"
    override def paramValue: String = name
  }

  final case class Repository(owner: String, repo: String) extends SearchCodeParam {
    override def paramName: String  = "repo"
    override def paramValue: String = s"$owner/$repo"
  }

  final case class Path(path: String) extends SearchCodeParam {
    override def paramName: String  = "path"
    override def paramValue: String = path
  }

  final case class Language(language: String) extends SearchCodeParam {
    override def paramName: String  = "language"
    override def paramValue: String = language
  }

  final case class Size(op: Option[ComparisonOperator] = None, size: Long) extends SearchCodeParam {
    override def paramName: String  = "size"
    override def paramValue: String = s"${op.getOrElse("")}$size"
  }

  final case class Filename(filename: String) extends SearchCodeParam {
    override def paramName: String  = "filename"
    override def paramValue: String = filename
  }

  final case class Extension(extension: String) extends SearchCodeParam {
    override def paramName: String  = "extension"
    override def paramValue: String = extension
  }
}
