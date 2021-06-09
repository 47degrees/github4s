/*
 * Copyright 2016-2021 47 Degrees Open Source <https://www.47deg.com>
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

package github4s.http

import github4s.GithubConfig
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import Http4sSyntax._

class Http4sSyntaxSpec extends AnyFlatSpec with Matchers {

  "toUri" should "manually encode query parameters to avoid problematic strings" in {
    //All of the characters normally exempt from Uri.encode
    //that also do not get encoded in query parameters
    val knownBadCharacters = "!$&'()*+,;=:@"
    val dummyConfig        = GithubConfig("", "", "", Map.empty)

    //Pulled from https://github.com/47degrees/github4s/issues/569
    val goodQueryStr = "repos:47degrees/github4s+type:pr+label:enhancement+state:open"

    val baseBuilder = RequestBuilder[Unit]("http://example.com")
    val badUri      = baseBuilder.withParams(Map("q" -> knownBadCharacters)).toUri(dummyConfig)
    val goodUri     = baseBuilder.withParams(Map("q" -> goodQueryStr)).toUri(dummyConfig)

    //Neither query string should be modified after encoding
    goodUri.query.renderString shouldBe s"q=$goodQueryStr"
    badUri.query.renderString shouldBe s"q=$knownBadCharacters"
  }
}
