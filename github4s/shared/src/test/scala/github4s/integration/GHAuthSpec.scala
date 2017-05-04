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

package github4s.integration

import github4s.Github
import github4s.Github._
import github4s.free.domain.Authorize
import github4s.implicits._
import github4s.utils.BaseIntegrationSpec

trait GHAuthSpec[T] extends BaseIntegrationSpec[T] {

  "Auth >> NewAuth" should "return error on Left when invalid credential is provided" in {

    val response = Github().auth
      .newAuth(
        validUsername,
        invalidPassword,
        validScopes,
        validNote,
        validClientId,
        invalidClientSecret)
      .execFuture[T](headerUserAgent)

    testFutureIsLeft(response)
  }

  "Auth >> AuthorizeUrl" should "return the expected URL for valid username" in {
    val response =
      Github().auth
        .authorizeUrl(validClientId, validRedirectUri, validScopes)
        .execFuture[T](headerUserAgent)

    testFutureIsRight[Authorize](response, { r =>
      r.result.url.contains(validRedirectUri) shouldBe true
      r.statusCode shouldBe okStatusCode
    })
  }

  "Auth >> GetAccessToken" should "return error on Left for invalid code value" in {
    val response = Github().auth
      .getAccessToken(validClientId, invalidClientSecret, "", validRedirectUri, "")
      .execFuture[T](headerUserAgent)

    testFutureIsLeft(response)
  }

}
