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

package github4s.integration

import github4s.HttpRequestBuilderExtension
import github4s.free.interpreters.{Capture, Interpreters}
import github4s.jvm.ImplicitsJVM
import github4s.utils.{BaseIntegrationSpec, TestUtilsJVM}

import scala.concurrent.Future
import scalaj.http.HttpResponse

class IntegrationSpec
    extends BaseIntegrationSpec[HttpResponse[String]]
    with GHAuthSpec[HttpResponse[String]]
    with GHGistsSpec[HttpResponse[String]]
    with GHGitDataSpec[HttpResponse[String]]
    with GHIssuesSpec[HttpResponse[String]]
    with GHPullRequestsSpec[HttpResponse[String]]
    with GHReposSpec[HttpResponse[String]]
    with GHUsersSpec[HttpResponse[String]]
    with GHActivitiesSpec[HttpResponse[String]]
    with ImplicitsJVM
    with TestUtilsJVM {

  override implicit def extension(implicit capture: Capture[Future]): HttpRequestBuilderExtension[
    HttpResponse[String],
    Future] =
    extensionJVM[Future]

  override implicit def futureInterpreters: Interpreters[Future, HttpResponse[String]] =
    intInstanceFutureScalaJ
}
