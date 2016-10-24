/*
 * Copyright (c) 2016 47 Degrees, LLC. <http://www.47deg.com>
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
 * the Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
 * FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
 * IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package github4s.utils

import org.mockserver.model.HttpRequest._
import org.mockserver.model.HttpResponse._
import org.mockserver.model.NottableString._
import org.mockserver.model.{Parameter, ParameterBody}
import org.mockserver.model.JsonBody._

trait MockGithubApiServer extends MockServerService with FakeResponses with TestUtils {

  //Users >> get
  mockServer
    .when(request.withMethod("GET").withPath(s"/users/$validUsername"))
    .respond(response.withStatusCode(okStatusCode).withBody(getUserValidResponse))

  mockServer
    .when(request.withMethod("GET").withPath(s"/users/$invalidUsername"))
    .respond(response.withStatusCode(notFoundStatusCode).withBody(notFoundResponse))

  //Users >> auth
  mockServer
    .when(request.withMethod("GET").withPath(s"/user").withHeader("Authorization", tokenHeader))
    .respond(response.withStatusCode(okStatusCode).withBody(getUserValidResponse))

  mockServer
    .when(request.withMethod("GET").withPath(s"/user").withHeader(not("Authorization")))
    .respond(response.withStatusCode(unauthorizedStatusCode).withBody(unauthorizedReponse))

  //Users >> get users
  mockServer
    .when(
      request
        .withMethod("GET")
        .withPath(s"/users")
        .withQueryStringParameter("since", validSinceInt.toString))
    .respond(response.withStatusCode(okStatusCode).withBody(getUsersValidResponse))

  mockServer
    .when(
      request
        .withMethod("GET")
        .withPath(s"/users")
        .withQueryStringParameter("since", invalidSinceInt.toString))
    .respond(response.withStatusCode(okStatusCode).withBody(emptyListResponse))

  //Auth >> new auth
  mockServer
    .when(
      request
        .withMethod("POST")
        .withPath(s"/authorizations")
        .withHeader("Authorization", validBasicAuth))
    .respond(response.withStatusCode(okStatusCode).withBody(newAuthValidResponse))

  mockServer
    .when(
      request
        .withMethod("POST")
        .withPath(s"/authorizations")
        .withHeader("Authorization", invalidBasicAuth))
    .respond(response.withStatusCode(unauthorizedStatusCode).withBody(badCredentialsResponse))

  //Auth >> get access token
  mockServer
    .when(
      request
        .withMethod("POST")
        .withPath(s"/login/oauth/access_token")
        .withBody(
          json(s"{client_id:'',client_secret:'',code:'$validCode',redirect_uri:'',state:''}")))
    .respond(response.withStatusCode(okStatusCode).withBody(getAccessTokenValidResponse))

  mockServer
    .when(
      request
        .withMethod("POST")
        .withPath(s"/login/oauth/access_token")
        .withBody(
          json(s"{client_id:'',client_secret:'',code:'$invalidCode',redirect_uri:'',state:''}")))
    .respond(response.withStatusCode(okStatusCode).withBody(badVerificationResponse))

  //Repos >> get repo
  mockServer
    .when(request.withMethod("GET").withPath(s"/repos/$validRepoOwner/$validRepoName"))
    .respond(response.withStatusCode(okStatusCode).withBody(getRepoResponse))

  mockServer
    .when(request.withMethod("GET").withPath(s"/repos/$validRepoOwner/$invalidRepoName"))
    .respond(response.withStatusCode(notFoundStatusCode).withBody(notFoundResponse))

  //Repos >> list commits
  mockServer
    .when(
      request
        .withMethod("GET")
        .withPath(s"/repos/$validRepoOwner/$validRepoName/commits")
        .withQueryStringParameter("page", validPage.toString))
    .respond(response.withStatusCode(okStatusCode).withBody(listCommitsValidResponse))

  mockServer
    .when(
      request
        .withMethod("GET")
        .withPath(s"/repos/$validRepoOwner/$validRepoName/commits")
        .withQueryStringParameter("page", invalidPage.toString))
    .respond(response.withStatusCode(okStatusCode).withBody(emptyListResponse))

  mockServer
    .when(request.withMethod("GET").withPath(s"/repos/$validRepoOwner/$invalidRepoName/commits"))
    .respond(response.withStatusCode(notFoundStatusCode).withBody(notFoundResponse))

  //Repos >> list contributors

  mockServer
    .when(
      request
        .withMethod("GET")
        .withPath(s"/repos/$validRepoOwner/$validRepoName/contributors")
        .withQueryStringParameter("anon", invalidAnonParameter.toString))
    .respond(response.withStatusCode(okStatusCode).withBody(emptyListResponse))

  mockServer
    .when(
      request
        .withMethod("GET")
        .withPath(s"/repos/$validRepoOwner/$validRepoName/contributors")
        .withQueryStringParameter("anon", validAnonParameter.toString))
    .respond(response.withStatusCode(okStatusCode).withBody(listContributorsValidResponse))

  mockServer
    .when(
      request.withMethod("GET").withPath(s"/repos/$validRepoOwner/$validRepoName/contributors"))
    .respond(response.withStatusCode(okStatusCode).withBody(listContributorsValidResponse))

  mockServer
    .when(
      request.withMethod("GET").withPath(s"/repos/$validRepoOwner/$invalidRepoName/contributors"))
    .respond(response.withStatusCode(notFoundStatusCode).withBody(notFoundResponse))

  //Gists >> post new gist

  mockServer
    .when(request.withMethod("POST").withPath(s"/gists").withHeader("Authorization", tokenHeader))
    .respond(response.withStatusCode(createdStatusCode).withBody(newGistValidResponse))

  mockServer
    .when(request.withMethod("POST").withPath(s"/gists").withHeader(not("Authorization")))
    .respond(response.withStatusCode(unauthorizedStatusCode).withBody(unauthorizedReponse))

}
