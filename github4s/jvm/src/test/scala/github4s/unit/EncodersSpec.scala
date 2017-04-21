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
import github4s.free.domain.{TreeData, TreeDataBlob, TreeDataSha}
import io.circe.parser._
import io.circe.syntax._
import org.scalatest._

class EncodersSpec extends FlatSpec with Matchers {

  "TreeData encoder" should "encode the TreeDataSha" in {
    val treeData: TreeData = TreeDataSha("README.md", "100644", "blob", "XXXXXX")

    val expectedJsonString =
      s"""
        | {
        |   "path": "${treeData.path}",
        |   "mode": "${treeData.mode}",
        |   "type": "${treeData.`type`}",
        |   "sha": "XXXXXX"
        | }
      """.stripMargin

    val expectedJson = parse(expectedJsonString).right.get
    val actualJson   = treeData.asJson

    actualJson shouldBe expectedJson
  }

  it should "encode the TreeDataBlob" in {
    val treeData: TreeData = TreeDataBlob("README.md", "100644", "blob", "Blob Data")

    val expectedJsonString =
      s"""
         | {
         |   "path": "${treeData.path}",
         |   "mode": "${treeData.mode}",
         |   "type": "${treeData.`type`}",
         |   "content": "Blob Data"
         | }
      """.stripMargin

    val expectedJson = parse(expectedJsonString).right.get
    val actualJson   = treeData.asJson

    actualJson shouldBe expectedJson
  }

}
