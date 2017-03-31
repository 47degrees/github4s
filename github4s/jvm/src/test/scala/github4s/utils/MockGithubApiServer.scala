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

package github4s.utils

import org.mockserver.model.HttpRequest._
import org.mockserver.model.HttpResponse._
import org.mockserver.model.JsonBody._
import org.mockserver.model.NottableString._

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
    .when(
      request
        .withMethod("GET")
        .withPath(s"/user")
        .withHeader("Authorization", tokenHeader))
    .respond(response.withStatusCode(okStatusCode).withBody(getUserValidResponse))

  mockServer
    .when(
      request
        .withMethod("GET")
        .withPath(s"/user")
        .withHeader(not("Authorization")))
    .respond(
      response
        .withStatusCode(unauthorizedStatusCode)
        .withBody(unauthorizedReponse))

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
    .respond(
      response
        .withStatusCode(unauthorizedStatusCode)
        .withBody(badCredentialsResponse))

  //Auth >> get access token
  mockServer
    .when(
      request
        .withMethod("POST")
        .withPath(s"/login/oauth/access_token")
        .withBody(
          json(s"{client_id:'',client_secret:'',code:'$validCode',redirect_uri:'',state:''}")))
    .respond(
      response
        .withStatusCode(okStatusCode)
        .withBody(getAccessTokenValidResponse))

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
    .when(
      request
        .withMethod("GET")
        .withPath(s"/repos/$validRepoOwner/$validRepoName"))
    .respond(response.withStatusCode(okStatusCode).withBody(getRepoResponse))

  mockServer
    .when(
      request
        .withMethod("GET")
        .withPath(s"/repos/$validRepoOwner/$invalidRepoName"))
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
    .when(
      request
        .withMethod("GET")
        .withPath(s"/repos/$validRepoOwner/$invalidRepoName/commits"))
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
    .respond(
      response
        .withStatusCode(okStatusCode)
        .withBody(listContributorsValidResponse))

  mockServer
    .when(
      request
        .withMethod("GET")
        .withPath(s"/repos/$validRepoOwner/$validRepoName/contributors"))
    .respond(
      response
        .withStatusCode(okStatusCode)
        .withBody(listContributorsValidResponse))

  mockServer
    .when(
      request
        .withMethod("GET")
        .withPath(s"/repos/$validRepoOwner/$invalidRepoName/contributors"))
    .respond(response.withStatusCode(notFoundStatusCode).withBody(notFoundResponse))

  //Gists >> post new gist

  mockServer
    .when(
      request
        .withMethod("POST")
        .withPath(s"/gists")
        .withHeader("Authorization", tokenHeader))
    .respond(
      response
        .withStatusCode(createdStatusCode)
        .withBody(newGistValidResponse))

  mockServer
    .when(
      request
        .withMethod("POST")
        .withPath(s"/gists")
        .withHeader(not("Authorization")))
    .respond(
      response
        .withStatusCode(unauthorizedStatusCode)
        .withBody(unauthorizedReponse))

  // Git >> References

  mockServer
    .when(
      request
        .withMethod("GET")
        .withPath(s"/repos/$validRepoOwner/$validRepoName/git/refs/$validRefSingle"))
    .respond(
      response
        .withStatusCode(okStatusCode)
        .withBody(singleReference))

  mockServer
    .when(
      request
        .withMethod("GET")
        .withPath(s"/repos/$validRepoOwner/$validRepoName/git/refs/$validRefMultiple"))
    .respond(
      response
        .withStatusCode(okStatusCode)
        .withBody(multipleReference))

  mockServer
    .when(
      request
        .withMethod("GET")
        .withPath(s"/repos/$validRepoOwner/$validRepoName/git/refs/$invalidRef"))
    .respond(
      response
        .withStatusCode(notFoundStatusCode)
        .withBody(notFoundResponse))

  mockServer
    .when(
      request
        .withMethod("POST")
        .withPath(s"/repos/$validRepoOwner/$validRepoName/git/refs")
        .withHeader("Authorization", tokenHeader))
    .respond(
      response
        .withStatusCode(createdStatusCode)
        .withBody(singleReference))

  mockServer
    .when(
      request
        .withMethod("POST")
        .withPath(s"/repos/$validRepoOwner/$validRepoName/git/refs")
        .withHeader(not("Authorization")))
    .respond(
      response
        .withStatusCode(unauthorizedStatusCode)
        .withBody(unauthorizedReponse))

  mockServer
    .when(
      request
        .withMethod("POST")
        .withPath(s"/repos/$validRepoOwner/$validRepoName/git/refs/$validRefSingle")
        .withHeader("Authorization", tokenHeader))
    .respond(
      response
        .withStatusCode(okStatusCode)
        .withBody(singleReference))

  mockServer
    .when(
      request
        .withMethod("POST")
        .withPath(s"/repos/$validRepoOwner/$validRepoName/git/refs/$validRefSingle")
        .withHeader(not("Authorization")))
    .respond(
      response
        .withStatusCode(unauthorizedStatusCode)
        .withBody(unauthorizedReponse))

  mockServer
    .when(
      request
        .withMethod("GET")
        .withPath(s"/repos/$validRepoOwner/$validRepoName/git/commits/$validCommitSha"))
    .respond(
      response
        .withStatusCode(okStatusCode)
        .withBody(commitResult))

  mockServer
    .when(
      request
        .withMethod("GET")
        .withPath(s"/repos/$validRepoOwner/$validRepoName/git/commits/$invalidCommitSha"))
    .respond(
      response
        .withStatusCode(notFoundStatusCode)
        .withBody(notFoundResponse))

  mockServer
    .when(
      request
        .withMethod("POST")
        .withPath(s"/repos/$validRepoOwner/$validRepoName/git/commits")
        .withHeader("Authorization", tokenHeader))
    .respond(
      response
        .withStatusCode(createdStatusCode)
        .withBody(commitResult))

  mockServer
    .when(
      request
        .withMethod("POST")
        .withPath(s"/repos/$validRepoOwner/$validRepoName/git/commits")
        .withHeader(not("Authorization")))
    .respond(
      response
        .withStatusCode(unauthorizedStatusCode)
        .withBody(unauthorizedReponse))

  mockServer
    .when(
      request
        .withMethod("POST")
        .withPath(s"/repos/$validRepoOwner/$validRepoName/git/blobs")
        .withHeader("Authorization", tokenHeader))
    .respond(
      response
        .withStatusCode(createdStatusCode)
        .withBody(blobResult))

  mockServer
    .when(
      request
        .withMethod("POST")
        .withPath(s"/repos/$validRepoOwner/$validRepoName/git/blobs")
        .withHeader(not("Authorization")))
    .respond(
      response
        .withStatusCode(unauthorizedStatusCode)
        .withBody(unauthorizedReponse))

  mockServer
    .when(
      request
        .withMethod("POST")
        .withPath(s"/repos/$validRepoOwner/$validRepoName/git/trees")
        .withHeader("Authorization", tokenHeader))
    .respond(
      response
        .withStatusCode(createdStatusCode)
        .withBody(createTreeResult))

  mockServer
    .when(
      request
        .withMethod("POST")
        .withPath(s"/repos/$validRepoOwner/$validRepoName/git/trees")
        .withHeader(not("Authorization")))
    .respond(
      response
        .withStatusCode(unauthorizedStatusCode)
        .withBody(unauthorizedReponse))

  //PullRequests >> list
  mockServer
    .when(
      request
        .withMethod("GET")
        .withPath(s"/repos/$validRepoOwner/$validRepoName/pulls"))
    .respond(response.withStatusCode(okStatusCode).withBody(listPullRequestsValidResponse))

  mockServer
    .when(
      request
        .withMethod("GET")
        .withPath(s"/repos/$validRepoOwner/$invalidRepoName/pulls"))
    .respond(response.withStatusCode(notFoundStatusCode).withBody(notFoundResponse))

}
