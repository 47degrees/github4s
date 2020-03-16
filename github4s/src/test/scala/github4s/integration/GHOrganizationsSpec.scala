/*
 * Copyright 2016-2020 47 Degrees, LLC. <http://www.47deg.com>
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

trait GHOrganizationsSpec extends BaseIntegrationSpec {

  "Organization >> ListMembers" should "return the expected list of users" taggedAs Integration in {
    val response = clientResource
      .use { client =>
        Github[IO](client, accessToken).organizations
          .listMembers(validRepoOwner, headers = headerUserAgent)
      }
      .unsafeRunSync()

    testIsRight[List[User]](response, r => r.nonEmpty shouldBe true)
    response.statusCode shouldBe okStatusCode
  }

  it should "return error for an invalid org" taggedAs Integration in {
    val response = clientResource
      .use { client =>
        Github[IO](client, accessToken).organizations
          .listMembers(invalidUsername, headers = headerUserAgent)
      }
      .unsafeRunSync()

    testIsLeft(response)
    response.statusCode shouldBe notFoundStatusCode
  }

  "Organization >> ListOutsideCollaborators" should "return expected list of users" ignore {
    val response = clientResource
      .use { client =>
        Github[IO](client, accessToken).organizations
          .listOutsideCollaborators(validOrganizationName, headers = headerUserAgent)
      }
      .unsafeRunSync()

    testIsRight[List[User]](response, r => r.nonEmpty shouldBe true)
    response.statusCode shouldBe okStatusCode
  }

  it should "return error for an invalid org" taggedAs Integration in {
    val response = clientResource
      .use { client =>
        Github[IO](client, accessToken).organizations
          .listOutsideCollaborators(invalidOrganizationName, headers = headerUserAgent)
      }
      .unsafeRunSync()

    testIsLeft(response)
    response.statusCode shouldBe notFoundStatusCode
  }

}
