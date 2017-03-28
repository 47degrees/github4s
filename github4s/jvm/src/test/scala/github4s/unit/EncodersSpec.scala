/*
 * Copyright (c) 2016 47 Degrees, LLC. <http://www.47deg.com>
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
 * the Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
 * FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
 * IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
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

  }

}
