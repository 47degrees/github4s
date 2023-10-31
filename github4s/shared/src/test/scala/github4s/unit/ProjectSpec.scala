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
import github4s.domain._
import github4s.http.HttpClient
import github4s.interpreters.ProjectsInterpreter
import github4s.utils.BaseSpec

class ProjectSpec extends BaseSpec {

  "Project.listProjects" should "call to httpClient.get with the right parameters" in {

    implicit val httpClientMock: HttpClient[IO] = httpClientMockGet[List[Project]](
      url = s"orgs/$validRepoOwner/projects",
      headers = headerAccept,
      response = IO.pure(List(project))
    )

    val projects = new ProjectsInterpreter[IO]

    projects
      .listProjects(validRepoOwner, None, None, headers = headerUserAgent ++ headerAccept)
      .shouldNotFail
  }

  "Project.listProjectsRepository" should "call to httpClient.get with the right parameters" in {

    implicit val httpClientMock: HttpClient[IO] = httpClientMockGet[List[Project]](
      url = s"repos/$validRepoOwner/$validRepoName/projects",
      headers = headerAccept,
      response = IO.pure(List(project))
    )

    val projects = new ProjectsInterpreter[IO]

    projects
      .listProjectsRepository(
        validRepoOwner,
        validRepoName,
        None,
        None,
        headers = headerUserAgent ++ headerAccept
      )
      .shouldNotFail

  }

  "Project.listColumns" should "call to httpClient.get with the right parameters" in {

    implicit val httpClientMock: HttpClient[IO] = httpClientMockGet[List[Column]](
      url = s"projects/$validProjectId/columns",
      headers = headerAccept,
      response = IO.pure(List(column))
    )

    val projects = new ProjectsInterpreter[IO]

    projects
      .listColumns(validProjectId, None, headers = headerUserAgent ++ headerAccept)
      .shouldNotFail
  }

  "Project.listCards" should "call to httpClient.get with the right parameters" in {

    implicit val httpClientMock: HttpClient[IO] = httpClientMockGet[List[Card]](
      url = s"projects/columns/$validColumnId/cards",
      headers = headerAccept,
      response = IO.pure(List(card))
    )

    val project = new ProjectsInterpreter[IO]

    project.listCards(validColumnId, None, None, headerUserAgent ++ headerAccept).shouldNotFail
  }

}
