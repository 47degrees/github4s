/*
 * Copyright 2016-2020 47 Degrees Open Source <https://www.47deg.com>
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
import cats.syntax.either._
import github4s.GHResponse
import github4s.domain._
import github4s.interpreters.GistsInterpreter
import github4s.utils.BaseSpec

class GistSpec extends BaseSpec {

  implicit val token = sampleToken

  "Gist.newGist" should "call to httpClient.post with the right parameters" in {

    val response: IO[GHResponse[Gist]] =
      IO(GHResponse(gist.asRight, okStatusCode, Map.empty))

    val request = NewGistRequest(
      validGistDescription,
      validGistPublic,
      Map(validGistFilename -> GistFile(validGistFileContent))
    )

    implicit val httpClientMock = httpClientMockPost[NewGistRequest, Gist](
      url = "gists",
      req = request,
      response = response
    )

    val gists = new GistsInterpreter[IO]

    gists.newGist(
      validGistDescription,
      validGistPublic,
      Map(validGistFilename -> GistFile(validGistFileContent)),
      headerUserAgent
    )
  }

  "Gist.getGist" should "call to httpClient.get with the right parameters without sha" in {

    val response: IO[GHResponse[Gist]] =
      IO(GHResponse(gist.asRight, okStatusCode, Map.empty))

    implicit val httpClientMock = httpClientMockGet[Gist](
      url = s"gists/$validGistId",
      response = response
    )

    val gists = new GistsInterpreter[IO]

    gists.getGist(
      validGistId,
      sha = None,
      headerUserAgent
    )
  }

  it should "call to httpClient.get with the right parameters with sha" in {

    val response: IO[GHResponse[Gist]] =
      IO(GHResponse(gist.asRight, okStatusCode, Map.empty))

    implicit val httpClientMock = httpClientMockGet[Gist](
      url = s"gists/$validGistId/$validGistSha",
      response = response
    )

    val gists = new GistsInterpreter[IO]

    gists.getGist(
      validGistId,
      sha = Some(validGistSha),
      headerUserAgent
    )
  }

  "Gist.editGist" should "call to httpClient.patch with the right parameters" in {

    val response: IO[GHResponse[Gist]] =
      IO(GHResponse(gist.asRight, okStatusCode, Map.empty))

    val request = EditGistRequest(
      validGistDescription,
      Map(
        validGistFilename -> Some(EditGistFile(validGistFileContent)),
        validGistOldFilename -> Some(
          EditGistFile(validGistFileContent, Some(validGistNewFilename))
        ),
        validGistDeletedFilename -> None
      )
    )

    implicit val httpClientMock = httpClientMockPatch[EditGistRequest, Gist](
      url = s"gists/$validGistId",
      req = request,
      response = response
    )

    val gists = new GistsInterpreter[IO]

    gists.editGist(
      validGistId,
      validGistDescription,
      Map(
        validGistFilename -> Some(EditGistFile(validGistFileContent)),
        validGistOldFilename -> Some(
          EditGistFile(validGistFileContent, Some(validGistNewFilename))
        ),
        validGistDeletedFilename -> None
      ),
      headerUserAgent
    )
  }

}
