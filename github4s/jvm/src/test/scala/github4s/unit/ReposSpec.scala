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
import github4s.api.Repos
import github4s.free.domain._
import github4s.utils.{DummyGithubUrls, TestUtils}
import github4s.{HttpClient, HttpRequestBuilderExtensionJVM, IdInstances}
import io.circe.Decoder
import org.mockito.ArgumentMatchers.{any, argThat, eq => argEq}
import org.mockito.Mockito._
import org.scalatest.mockito.MockitoSugar.mock
import org.scalatest.{FlatSpec, Matchers}

import scalaj.http.HttpResponse

class ReposSpec
    extends FlatSpec
    with Matchers
    with TestUtils
    with DummyGithubUrls
    with IdInstances
    with HttpRequestBuilderExtensionJVM {

  "Repos.getContents" should "call to httpClient.get with the right parameters" in {

    val response: GHResponse[NonEmptyList[Content]] =
      Right(GHResult(NonEmptyList(content, Nil), okStatusCode, Map.empty))

    val httpClientMock = mock[HttpClient[HttpResponse[String], Id]]
    when(
      httpClientMock
        .get[NonEmptyList[Content]](
          any[Option[String]],
          any[String],
          any[Map[String, String]],
          any[Map[String, String]],
          any[Option[Pagination]])(any[Decoder[NonEmptyList[Content]]]))
      .thenReturn(response)

    val token = Some("token")
    val repos = new Repos[HttpResponse[String], Id] {
      override val httpClient: HttpClient[HttpResponse[String], Id] = httpClientMock
    }
    repos.getContents(
      accessToken = token,
      headers = headerUserAgent,
      owner = validRepoOwner,
      repo = validRepoName,
      path = validFilePath,
      ref = Some("master")
    )

    verify(httpClientMock).get[NonEmptyList[Content]](
      argEq(token),
      argEq(s"repos/$validRepoOwner/$validRepoName/contents/$validFilePath"),
      argEq(headerUserAgent),
      argEq(Map("ref" -> "master")),
      any[Option[Pagination]]
    )(any[Decoder[NonEmptyList[Content]]])
  }

  "Repos.createRelease" should "call to httpClient.post with the right parameters" in {

    val response: GHResponse[Release] = Right(GHResult(release, okStatusCode, Map.empty))

    val httpClientMock = mock[HttpClient[HttpResponse[String], Id]]
    when(
      httpClientMock
        .post[Release](any[Option[String]], any[String], any[Map[String, String]], any[String])(
          any[Decoder[Release]]))
      .thenReturn(response)

    val token = Some("token")
    val repos = new Repos[HttpResponse[String], Id] {
      override val httpClient: HttpClient[HttpResponse[String], Id] = httpClientMock
    }
    repos.createRelease(
      accessToken = token,
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

    verify(httpClientMock).post[Release](
      argEq(token),
      argEq(s"repos/$validRepoOwner/$validRepoName/releases"),
      argEq(headerUserAgent),
      argThat(JsonArgMatcher(request))
    )(any[Decoder[Release]])
  }

}
