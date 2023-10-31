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

package github4s.unit

import cats.effect.IO
import github4s.domain._
import github4s.http.HttpClient
import github4s.interpreters.AuthInterpreter
import github4s.utils.BaseSpec
import github4s.Encoders._

class AuthSpec extends BaseSpec {

  "Auth.getAccessToken" should "call to httpClient.postOAuth with the right parameters" in {

    implicit val httpClientMock: HttpClient[IO] = httpClientMockPostOAuth[OAuthToken](
      url = dummyConfig.accessTokenUrl,
      response = IO.pure(oAuthToken)
    )

    val auth = new AuthInterpreter[IO]

    auth
      .getAccessToken(
        validClientId,
        invalidClientSecret,
        validCode,
        validRedirectUri,
        validAuthState,
        headerUserAgent
      )
      .shouldNotFail
  }
}
