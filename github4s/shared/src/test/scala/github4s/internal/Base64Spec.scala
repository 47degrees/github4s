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

package github4s.internal

import Base64._
import org.scalatest.funspec.AnyFunSpec

class Base64Spec extends AnyFunSpec {

  describe("encoding") {
    it("encode1") {
      assert("ABCDEFG".getBytes.toBase64 == "QUJDREVGRw==")
    }

    it("encode2") {
      assert("homuho".getBytes.toBase64 == "aG9tdWhv")
    }

    it("encode3") {
      assert("hogepiyofoobar".getBytes.toBase64 == "aG9nZXBpeW9mb29iYXI=")
    }
  }

  describe("decoding") {

    it("decode4") {
      assert("aG9nZXBpeW9mb29iYXI=".toByteArray sameElements "hogepiyofoobar".getBytes)
    }

    it("decode5") {
      assert("+/+/+/+/".toByteArray.sameElements("-_-_-_-_".toByteArray(base64Url)))
    }

  }

  describe("RFC 4648 Test vectors") {

    it("testBigInt") {
      assert(BigInt("14fb9c03d97e", 16).toByteArray.toBase64 == "FPucA9l+")
    }

    it("test7") {
      val testVectors = Seq(
        ""       -> "",
        "f"      -> "Zg==",
        "fo"     -> "Zm8=",
        "foo"    -> "Zm9v",
        "foob"   -> "Zm9vYg==",
        "fooba"  -> "Zm9vYmE=",
        "foobar" -> "Zm9vYmFy"
      )

      for (t <- testVectors)
        // test encoding
        assert(t._1.getBytes.toBase64 == t._2)

      for (t <- testVectors)
        // test decoding
        assert(t._2.toByteArray sameElements t._1.getBytes)
    }

    it("testunpaddedDecode") {
      val testVectors = Seq(
        ""       -> "",
        "f"      -> "Zg==",
        "fo"     -> "Zm8=",
        "foo"    -> "Zm9v",
        "foob"   -> "Zm9vYg==",
        "fooba"  -> "Zm9vYmE=",
        "foobar" -> "Zm9vYmFy"
      )

      for (t <- testVectors)
        // test decoding
        assert(
          t._2.reverse.dropWhile(_ == '=').reverse.toByteArray(base64Url) sameElements t._1.getBytes
        )
    }

    it("testBase64UrlPadding") {
      val testVectors = Seq(
        ""       -> "",
        "f"      -> "Zg%3D%3D",
        "fo"     -> "Zm8%3D",
        "foo"    -> "Zm9v",
        "foob"   -> "Zm9vYg%3D%3D",
        "fooba"  -> "Zm9vYmE%3D",
        "foobar" -> "Zm9vYmFy"
      )

      for (t <- testVectors)
        // test encoding
        assert(t._1.getBytes.toBase64(base64Url) == t._2)
    }

    it("testBase64UrlDecoding") {
      val testVectors = Seq(
        ""       -> "",
        "f"      -> "Zg%3D%3D",
        "fo"     -> "Zm8%3D",
        "foo"    -> "Zm9v",
        "foob"   -> "Zm9vYg%3D%3D",
        "fooba"  -> "Zm9vYmE%3D",
        "foobar" -> "Zm9vYmFy"
      )

      for (t <- testVectors)
        // test encoding
        assert(t._2.toByteArray(base64Url) sameElements t._1.getBytes)
    }
  }
}
