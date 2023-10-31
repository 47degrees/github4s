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
import github4s.Encoders._
import github4s.Decoders._
import github4s.domain._
import github4s.http.HttpClient
import github4s.interpreters.GistsInterpreter
import github4s.utils.BaseSpec

class GistSpec extends BaseSpec {

  "Gist.newGist" should "call to httpClient.post with the right parameters" in {

    val request = NewGistRequest(
      validGistDescription,
      validGistPublic,
      Map(validGistFilename -> GistFile(validGistFileContent))
    )

    implicit val httpClientMock: HttpClient[IO] = httpClientMockPost[NewGistRequest, Gist](
      url = "gists",
      req = request,
      response = IO.pure(gist)
    )

    val gists = new GistsInterpreter[IO]

    gists
      .newGist(
        validGistDescription,
        validGistPublic,
        Map(validGistFilename -> GistFile(validGistFileContent)),
        headerUserAgent
      )
      .shouldNotFail
  }

  "Gist.getGist" should "call to httpClient.get with the right parameters without sha" in {

    implicit val httpClientMock: HttpClient[IO] = httpClientMockGet[Gist](
      url = s"gists/$validGistId",
      response = IO.pure(gist)
    )

    val gists = new GistsInterpreter[IO]

    gists
      .getGist(
        validGistId,
        sha = None,
        headerUserAgent
      )
      .shouldNotFail
  }

  it should "call to httpClient.get with the right parameters with sha" in {

    implicit val httpClientMock: HttpClient[IO] = httpClientMockGet[Gist](
      url = s"gists/$validGistId/$validGistSha",
      response = IO.pure(gist)
    )

    val gists = new GistsInterpreter[IO]

    gists
      .getGist(
        validGistId,
        sha = Some(validGistSha),
        headerUserAgent
      )
      .shouldNotFail
  }

  "Gist.editGist" should "call to httpClient.patch with the right parameters" in {

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

    implicit val httpClientMock: HttpClient[IO] = httpClientMockPatch[EditGistRequest, Gist](
      url = s"gists/$validGistId",
      req = request,
      response = IO.pure(gist)
    )

    val gists = new GistsInterpreter[IO]

    gists
      .editGist(
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
      .shouldNotFail
  }

}
