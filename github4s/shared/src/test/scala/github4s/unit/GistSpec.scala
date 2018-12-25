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

import cats.Id
import github4s.GithubResponses.{GHResponse, GHResult}
import github4s.HttpClient
import github4s.api.Gists
import github4s.free.domain._
import github4s.utils.BaseSpec

class GistSpec extends BaseSpec {

  "Gist.newGist" should "call to httpClient.post with the right parameters" in {

    val response: GHResponse[Gist] =
      Right(GHResult(gist, okStatusCode, Map.empty))

    val request =
      """
        |{
        |  "description": "A Gist",
        |  "public": true,
        |  "files": {
        |    "test.scala": {
        |      "content": "val meaningOfLife = 42"
        |    }
        |  }
        |}""".stripMargin

    val httpClientMock = httpClientMockPost[Gist](
      url = "gists",
      json = request,
      response = response
    )

    val gists = new Gists[String, Id] {
      override val httpClient: HttpClient[String, Id] = httpClientMock
    }

    gists.newGist(
      validGistDescription,
      validGistPublic,
      Map(validGistFilename → GistFile(validGistFileContent)),
      headerUserAgent,
      sampleToken
    )
  }

  "Gist.getGist" should "call to httpClient.get with the right parameters without sha" in {

    val response: GHResponse[Gist] =
      Right(GHResult(gist, okStatusCode, Map.empty))

    val httpClientMock = httpClientMockGet[Gist](
      url = s"gists/$validGistId",
      response = response
    )

    val gists = new Gists[String, Id] {
      override val httpClient: HttpClient[String, Id] = httpClientMock
    }

    gists.getGist(
      validGistId,
      sha = None,
      headerUserAgent,
      sampleToken
    )
  }

  it should "call to httpClient.get with the right parameters with sha" in {

    val response: GHResponse[Gist] =
      Right(GHResult(gist, okStatusCode, Map.empty))

    val httpClientMock = httpClientMockGet[Gist](
      url = s"gists/$validGistId/$validGistSha",
      response = response
    )

    val gists = new Gists[String, Id] {
      override val httpClient: HttpClient[String, Id] = httpClientMock
    }

    gists.getGist(
      validGistId,
      sha = Some(validGistSha),
      headerUserAgent,
      sampleToken
    )
  }

  "Gist.editGist" should "call to httpClient.patch with the right parameters" in {

    val response: GHResponse[Gist] =
      Right(GHResult(gist, okStatusCode, Map.empty))

    val request =
      """
        |{
        |  "description": "A Gist",
        |  "files": {
        |    "test.scala": {
        |      "content": "val meaningOfLife = 42"
        |    },
        |    "fest.scala": {
        |      "content": "val meaningOfLife = 42",
        |      "filename": "best.scala"
        |    },
        |    "rest.scala": null
        |  }
        |}""".stripMargin

    val httpClientMock = httpClientMockPatch[Gist](
      url = s"gists/$validGistId",
      json = request,
      response = response
    )

    val gists = new Gists[String, Id] {
      override val httpClient: HttpClient[String, Id] = httpClientMock
    }

    gists.editGist(
      validGistId,
      validGistDescription,
      Map(
        validGistFilename        → Some(EditGistFile(validGistFileContent)),
        validGistOldFilename     → Some(EditGistFile(validGistFileContent, Some(validGistNewFilename))),
        validGistDeletedFilename → None
      ),
      headerUserAgent,
      sampleToken
    )
  }

}
