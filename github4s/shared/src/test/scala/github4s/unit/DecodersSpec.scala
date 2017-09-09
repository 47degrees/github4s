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

import cats.data.NonEmptyList
import cats.syntax.either._
import github4s.Decoders._
import github4s.free.domain._
import github4s.utils.FakeResponses
import io.circe.generic.auto._
import io.circe.parser._
import org.scalatest._

class DecodersSpec extends FlatSpec with Matchers with FakeResponses {

  "Commit decoder" should "return a list of commits when the JSON is valid" in {
    decode[List[Commit]](listCommitsValidResponse).isRight shouldBe true
  }

  it should "return an empty list for an empty JSON" in {
    decode[List[Commit]](emptyListResponse).toOption map (_.isEmpty shouldBe true)
  }

  "Repository decoder" should "return a valid repo for a valid JSON" in {
    decode[Repository](getRepoResponse).isRight shouldBe true
  }

  it should "return an error for an empty JSON" in {
    decode[Repository](emptyListResponse).isLeft shouldBe true
  }

  "CombinedStatus decoder" should "return a valid repo for a valid JSON" in {
    decode[CombinedStatus](getCombinedStatusValidResponse).isRight shouldBe true
  }

  it should "return an error for an empty JSON" in {
    decode[CombinedStatus](emptyListResponse).isLeft shouldBe true
  }

  "Stargazer decoder" should "return a stargazer if given a user" in {
    decode[Stargazer](getUserValidResponse).isRight shouldBe true
  }

  it should "return a stargazer if given a stargazer" in {
    decode[Stargazer](getStargazerValidResponse).isRight shouldBe true
  }

  "StarredRepository decoder" should "return a starred repository if given a repo" in {
    decode[StarredRepository](getRepoResponse).isRight shouldBe true
  }

  it should "return a starred repository if given a starred repository" in {
    decode[StarredRepository](getStarredRepoValidResponse).isRight shouldBe true
  }

  "NonEmptyList Decoder" should "return a valid NonEmptyList for a valid JSON list" in {
    decode[NonEmptyList[Int]]("[1,2,3]") shouldBe Right(NonEmptyList.of(1, 2, 3))
  }

  case class Foo(a: Int)
  it should "return a valid NonEmtpyList for a valid JSON" in {
    decode[NonEmptyList[Foo]]("""{"a": 1}""") shouldBe Right(NonEmptyList(Foo(1), Nil))
  }

  it should "return an error for an empty list" in {
    decode[NonEmptyList[Int]](emptyListResponse).isLeft shouldBe true
  }

}
