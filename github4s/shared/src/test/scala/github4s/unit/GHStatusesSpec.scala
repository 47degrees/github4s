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
import github4s.GHStatuses
import github4s.GithubResponses.{GHResponse, GHResult}
import github4s.app.GitHub4s
import github4s.free.domain.{CombinedStatus, Status}
import github4s.utils.BaseSpec

class GHStatusesSpec extends BaseSpec {

  "GHStatuses.get" should "call to StatusesOps with the right parameters" in {
    val response: Free[GitHub4s, GHResponse[CombinedStatus]] =
      Free.pure(Right(GHResult(combinedStatus, okStatusCode, Map.empty)))

    val statusesOps = mock[StatusOpsTest]
    (statusesOps.getCombinedStatus _)
      .expects(validRepoOwner, validRepoName, validRefSingle, sampleToken)
      .returns(response)
    val ghStatuses = new GHStatuses(sampleToken)(statusesOps)
    ghStatuses.getCombinedStatus(validRepoOwner, validRepoName, validRefSingle)
  }

  "GHStatuses.list" should "call to StatusesOps with the right parameters" in {
    val response: Free[GitHub4s, GHResponse[List[Status]]] =
      Free.pure(Right(GHResult(List(status), okStatusCode, Map.empty)))

    val statusesOps = mock[StatusOpsTest]
    (statusesOps.listStatuses _)
      .expects(validRepoOwner, validRepoName, validRefSingle, sampleToken)
      .returns(response)
    val ghStatuses = new GHStatuses(sampleToken)(statusesOps)
    ghStatuses.listStatuses(validRepoOwner, validRepoName, validRefSingle)
  }

  "GHStatuses.create" should "call to StatusesOps with the right parameters" in {
    val response: Free[GitHub4s, GHResponse[Status]] =
      Free.pure(Right(GHResult(status, createdStatusCode, Map.empty)))

    val statusesOps = mock[StatusOpsTest]
    (statusesOps.createStatus _)
      .expects(
        validRepoOwner,
        validRepoName,
        validCommitSha,
        validStatusState,
        None,
        None,
        None,
        sampleToken)
      .returns(response)
    val ghStatuses = new GHStatuses(sampleToken)(statusesOps)
    ghStatuses
      .createStatus(
        validRepoOwner,
        validRepoName,
        validCommitSha,
        validStatusState,
        None,
        None,
        None)
  }
}
