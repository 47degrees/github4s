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

import cats.free.Free
import github4s.GithubResponses.{GHResponse, GHResult}
import github4s.{GHOrganizations, HttpClient}
import github4s.app.GitHub4s
import github4s.free.domain.User
import github4s.utils.BaseSpec

class GHOrganizationsSpec extends BaseSpec {

  "Organizations.listMembers" should "call to OrganizationsOps with the right parameters" in {

    val response: Free[GitHub4s, GHResponse[List[User]]] =
      Free.pure(Right(GHResult(List(user), okStatusCode, Map.empty)))

    val organizationOps = mock[OrganizationOpsTest]
    (organizationOps.listMembers _)
      .expects(validRepoOwner, None, None, None, sampleToken)
      .returns(response)

    val ghOrganizations = new GHOrganizations(sampleToken)(organizationOps)
    ghOrganizations.listMembers(validRepoOwner)
  }

}
