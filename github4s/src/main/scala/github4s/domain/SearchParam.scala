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

sealed trait SearchParam {
  protected def paramName: String
  protected def paramValue: String
  def value: String = s"$paramName:$paramValue"
}

sealed trait IssueType extends SearchParam {
  override def paramName: String = "type"
}

final case object IssueTypeIssue extends IssueType {
  override def paramValue: String = "issue"
}

final case object IssueTypePullRequest extends IssueType {
  override def paramValue: String = "pr"
}

final case class SearchIn(values: Set[SearchInValue]) extends SearchParam {
  override def paramName: String  = "in"
  override def paramValue: String = values.map(_.value).mkString(",")
}

sealed trait SearchInValue {
  def value: String
}

final case object SearchInTitle extends SearchInValue {
  override def value: String = "title"
}

final case object SearchInBody extends SearchInValue {
  override def value: String = "body"
}

final case object SearchInComments extends SearchInValue {
  override def value: String = "comments"
}

sealed trait IssueState extends SearchParam {
  override def paramName: String = "state"
}

final case object IssueStateOpen extends IssueState {
  override def paramValue: String = "open"
}

final case object IssueStateClosed extends IssueState {
  override def paramValue: String = "closed"
}

final case class LabelParam(label: String, exclude: Boolean = false) extends SearchParam {
  override def paramName: String = s"${if (exclude) "-" else ""}label"

  override def paramValue: String = label
}

sealed trait OwnerParam extends SearchParam

final case class OwnerParamOwnedByUser(user: String) extends OwnerParam {
  override def paramName: String = "user"

  override def paramValue: String = user
}

final case class OwnerParamInRepository(repo: String) extends OwnerParam {
  override def paramName: String = "repo"

  override def paramValue: String = repo
}
