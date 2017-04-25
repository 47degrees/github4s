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

import github4s.free.domain.{CombinedStatus, Commit, Repository}
import github4s.utils.FakeResponses
import github4s.Decoders._
import org.scalatest._
import io.circe.generic.auto._
import io.circe.parser._
import cats.implicits._

class DecodersSpec extends FlatSpec with Matchers with FakeResponses {

  "Commit decoder" should "return a list of commits when the JSON is valid" in {
    decode[List[Commit]](listCommitsValidResponse) should be('right)
  }

  it should "return an empty list for an empty JSON" in {
    decode[List[Commit]](emptyListResponse).toOption map (_.isEmpty shouldBe true)
  }

  "Repository decoder" should "return a valid repo for a valid JSON" in {
    decode[Repository](getRepoResponse) should be('right)
  }

  it should "return an error for an empty JSON" in {
    decode[Repository](emptyListResponse) should be('left)
  }

  "StatusRepository decoder" should "return a valid repo for a valid JSON" in {
    decode[CombinedStatus](getCombinedStatusValidResponse) should be('right)
  }

  it should "return an error for an empty JSON" in {
    decode[CombinedStatus](emptyListResponse) should be('left)
  }

}
