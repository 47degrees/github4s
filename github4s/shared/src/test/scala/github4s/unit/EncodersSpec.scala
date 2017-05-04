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

package github4s.unit

import github4s.Encoders._
import github4s.free.domain._
import github4s.utils.TestData
import io.circe.parser._
import io.circe.syntax._
import org.scalatest.{FlatSpec, Matchers}

class EncodersSpec extends FlatSpec with Matchers with TestData {

  "TreeData encoder" should "encode the TreeDataSha" in {
    val treeData: TreeData = TreeDataSha(validFilePath, validMode, validBlobType, validCommitSha)

    val expectedJsonString =
      s"""
         | {
         |   "path": "$validFilePath",
         |   "mode": "$validMode",
         |   "type": "$validBlobType",
         |   "sha": "$validCommitSha"
         | }
      """.stripMargin

    val expectedJson = parse(expectedJsonString).right.get
    val actualJson   = treeData.asJson

    actualJson shouldBe expectedJson
  }

  it should "encode the TreeDataBlob" in {
    val treeData: TreeData = TreeDataBlob(validFilePath, validMode, validBlobType, validCommitMsg)

    val expectedJsonString =
      s"""
         | {
         |   "path": "$validFilePath",
         |   "mode": "$validMode",
         |   "type": "$validBlobType",
         |   "content": "$validCommitMsg"
         | }
      """.stripMargin

    val expectedJson = parse(expectedJsonString).right.get
    val actualJson   = treeData.asJson

    actualJson shouldBe expectedJson
  }

  "CreatePullRequest encoder" should "encode the CreatePullRequestData" in {
    val createPullRequest: CreatePullRequest =
      CreatePullRequestData(validIssueTitle, validHead, validBase, validCommitMsg, Some(false))

    val expectedJsonString =
      s"""
         | {
         |   "title": "$validIssueTitle",
         |   "head": "$validHead",
         |   "base": "$validBase",
         |   "body": "$validCommitMsg",
         |   "maintainer_can_modify": false
         | }
      """.stripMargin

    val expectedJson = parse(expectedJsonString).right.get
    val actualJson   = createPullRequest.asJson

    actualJson shouldBe expectedJson
  }

  it should "encode the CreatePullRequestIssue" in {
    val createPullRequest: CreatePullRequest =
      CreatePullRequestIssue(validIssueNumber, validHead, validBase, Some(false))

    val expectedJsonString =
      s"""
         | {
         |   "issue": $validIssueNumber,
         |   "head": "$validHead",
         |   "base": "$validBase",
         |   "maintainer_can_modify": false
         | }
      """.stripMargin

    val expectedJson = parse(expectedJsonString).right.get
    val actualJson   = createPullRequest.asJson

    actualJson shouldBe expectedJson
  }

}
