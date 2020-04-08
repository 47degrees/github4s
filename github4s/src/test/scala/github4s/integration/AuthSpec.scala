/*
 * Copyright 2016-2020 47 Degrees <https://www.47deg.com>
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

import cats.effect.IO
import github4s.Github
import github4s.domain._
import github4s.utils.{BaseIntegrationSpec, Integration}

trait AuthSpec extends BaseIntegrationSpec {

  "Auth >> NewAuth" should "return error on Left when invalid credential is provided" taggedAs Integration in {

    val response = clientResource
      .use { client =>
        Github[IO](client).auth
          .newAuth(
            validUsername,
            invalidPassword,
            validScopes,
            validNote,
            validClientId,
            invalidClientSecret,
            headerUserAgent
          )
      }
      .unsafeRunSync()

    testIsLeft(response)
  }

  "Auth >> AuthorizeUrl" should "return the expected URL for valid username" taggedAs Integration in {
    val response = clientResource
      .use { client =>
        Github[IO](client).auth
          .authorizeUrl(validClientId, validRedirectUri, validScopes)
      }
      .unsafeRunSync()

    testIsRight[Authorize](response, r => r.url.contains(validRedirectUri) shouldBe true)
    response.statusCode shouldBe okStatusCode
  }

  "Auth >> GetAccessToken" should "return error on Left for invalid code value" taggedAs Integration in {
    val response = clientResource
      .use { client =>
        Github[IO](client).auth
          .getAccessToken(
            validClientId,
            invalidClientSecret,
            "",
            validRedirectUri,
            "",
            headerUserAgent
          )
      }
      .unsafeRunSync()

    testIsLeft(response)
    response.statusCode shouldBe notFoundStatusCode
  }

}
