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

import cats.Id
import cats.data.NonEmptyList
import github4s.GithubResponses.{GHResponse, GHResult}
import github4s.HttpClient
import github4s.api.Repos
import github4s.free.domain._
import github4s.utils.BaseSpec

class ReposSpec extends BaseSpec {

  "Repos.getContents" should "call to httpClient.get with the right parameters" in {

    val response: GHResponse[NonEmptyList[Content]] =
      Right(GHResult(NonEmptyList(content, Nil), okStatusCode, Map.empty))

    val httpClientMock = httpClientMockGet[NonEmptyList[Content]](
      url = s"repos/$validRepoOwner/$validRepoName/contents/$validFilePath",
      params = Map("ref" -> "master"),
      response = response
    )

    val repos = new Repos[String, Id] {
      override val httpClient: HttpClient[String, Id] = httpClientMock
    }
    repos.getContents(
      accessToken = sampleToken,
      headers = headerUserAgent,
      owner = validRepoOwner,
      repo = validRepoName,
      path = validFilePath,
      ref = Some("master")
    )
  }

  "Repos.createRelease" should "call to httpClient.post with the right parameters" in {

    val response: GHResponse[Release] = Right(GHResult(release, okStatusCode, Map.empty))
    val request =
      s"""
         |{
         |  "tag_name": "$validTagTitle",
         |  "target_commitish": "master",
         |  "name": "$validTagTitle",
         |  "body": "$validNote",
         |  "draft": false,
         |  "prerelease": true
         |}
       """.stripMargin
    val httpClientMock = httpClientMockPost[Release](
      url = s"repos/$validRepoOwner/$validRepoName/releases",
      json = request,
      response = response
    )

    val repos = new Repos[String, Id] {
      override val httpClient: HttpClient[String, Id] = httpClientMock
    }
    repos.createRelease(
      accessToken = sampleToken,
      headers = headerUserAgent,
      owner = validRepoOwner,
      repo = validRepoName,
      tagName = validTagTitle,
      name = validTagTitle,
      body = validNote,
      targetCommitish = Some("master"),
      draft = Some(false),
      prerelease = Some(true)
    )
  }

}
