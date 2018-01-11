/*
 * Copyright 2016-2018 47 Degrees, LLC. <http://www.47deg.com>
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
import github4s.{GHGists, HttpClient}
import github4s.app.GitHub4s
import github4s.free.domain._
import github4s.utils.BaseSpec

class GHGistSpec extends BaseSpec {

  "Gist.newGist" should "call to GistOps with the right parameters" in {

    val response: Free[GitHub4s, GHResponse[Gist]] =
      Free.pure(Right(GHResult(gist, okStatusCode, Map.empty)))

    val gistOps = mock[GistOpsTest]
    (gistOps.newGist _)
      .expects(
        validGistDescription,
        validGistPublic,
        Map(validGistFilename → GistFile(validGistFileContent)),
        sampleToken)
      .returns(response)

    val ghGists = new GHGists(sampleToken)(gistOps)
    ghGists.newGist(
      validGistDescription,
      validGistPublic,
      Map(validGistFilename → GistFile(validGistFileContent)))
  }

}
