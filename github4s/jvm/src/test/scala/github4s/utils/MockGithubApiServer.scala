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

package github4s.utils

import org.mockserver.model.HttpRequest._
import org.mockserver.model.HttpResponse._
import org.mockserver.model.JsonBody._
import org.mockserver.model.NottableString._
import org.mockserver.model.Parameter

trait MockGithubApiServer extends MockServerService with FakeResponses with TestUtilsJVM {

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
        .withBody(unauthorizedResponse))

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

  //Repos >> list org repos
  mockServer
    .when(
      request
        .withMethod("GET")
        .withPath(s"/orgs/$validRepoOwner/repos")
        .withQueryStringParameter("page", validPage.toString))
    .respond(response.withStatusCode(okStatusCode).withBody(listOrgReposValidResponse))

  mockServer
    .when(
      request
        .withMethod("GET")
        .withPath(s"/orgs/$validRepoOwner/repos")
        .withQueryStringParameter("page", invalidPage.toString))
    .respond(response.withStatusCode(okStatusCode).withBody(emptyListResponse))

  mockServer
    .when(
      request
        .withMethod("GET")
        .withPath(s"/orgs/$validRepoOwner/repos"))
    .respond(response.withStatusCode(notFoundStatusCode).withBody(notFoundResponse))

  //Repos >> get contents
  mockServer
    .when(
      request
        .withMethod("GET")
        .withPath(s"/repos/$validRepoOwner/$validRepoName/contents/$validFilePath"))
    .respond(response.withStatusCode(okStatusCode).withBody(validRepoFileContents))

  mockServer
    .when(
      request
        .withMethod("GET")
        .withPath(s"/repos/$validRepoOwner/$validRepoName/contents/$validDirPath"))
    .respond(response.withStatusCode(okStatusCode).withBody(validRepoDirContents))

  mockServer
    .when(
      request
        .withMethod("GET")
        .withPath(s"/repos/$validRepoOwner/$validRepoName/contents/$validSymlinkPath"))
    .respond(response.withStatusCode(okStatusCode).withBody(validRepoSymlinkContents))

  mockServer
    .when(
      request
        .withMethod("GET")
        .withPath(s"/repos/$validRepoOwner/$validRepoName/contents/$validSubmodulePath"))
    .respond(response.withStatusCode(okStatusCode).withBody(validRepoSubmoduleContents))

  mockServer
    .when(
      request
        .withMethod("GET")
        .withPath(s"/repos/$validRepoOwner/$invalidRepoName/contents/$validFilePath"))
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

  //Repos >> create release
  mockServer
    .when(
      request
        .withMethod("POST")
        .withPath(s"/repos/$validRepoOwner/$validRepoName/releases")
        .withHeader("Authorization", tokenHeader))
    .respond(
      response
        .withStatusCode(createdStatusCode)
        .withBody(validNewReleaseResponse))

  mockServer
    .when(
      request
        .withMethod("POST")
        .withPath(s"/repos/$validRepoOwner/$validRepoName/releases")
        .withHeader(not("Authorization")))
    .respond(
      response
        .withStatusCode(unauthorizedStatusCode)
        .withBody(unauthorizedResponse))

  //Repos >> getStatus
  mockServer
    .when(
      request
        .withMethod("GET")
        .withPath(s"/repos/$validRepoOwner/$validRepoName/commits/$validRefSingle/status")
        .withHeader("Authorization", tokenHeader))
    .respond(response.withStatusCode(okStatusCode).withBody(getCombinedStatusValidResponse))

  mockServer
    .when(
      request
        .withMethod("GET")
        .withPath(s"/repos/$validRepoOwner/$validRepoName/commits/$validRefSingle/status")
        .withHeader(not("Authorization")))
    .respond(response.withStatusCode(unauthorizedStatusCode).withBody(unauthorizedResponse))

  mockServer
    .when(
      request
        .withMethod("GET")
        .withPath(s"/repos/$validRepoOwner/$validRepoName/commits/$invalidRef/status")
        .withHeader(not("Authorization")))
    .respond(response.withStatusCode(notFoundStatusCode).withBody(notFoundResponse))

  //Repos >> listStatus
  mockServer
    .when(
      request
        .withMethod("GET")
        .withPath(s"/repos/$validRepoOwner/$validRepoName/commits/$validRefSingle/statuses")
        .withHeader("Authorization", tokenHeader))
    .respond(response.withStatusCode(okStatusCode).withBody(listStatusValidResponse))

  mockServer
    .when(
      request
        .withMethod("GET")
        .withPath(s"/repos/$validRepoOwner/$validRepoName/commits/$validRefSingle/statuses")
        .withHeader(not("Authorization")))
    .respond(response.withStatusCode(unauthorizedStatusCode).withBody(unauthorizedResponse))

  mockServer
    .when(
      request
        .withMethod("GET")
        .withPath(s"/repos/$validRepoOwner/$validRepoName/commits/$invalidRef/statuses")
        .withHeader(not("Authorization")))
    .respond(response.withStatusCode(okStatusCode).withBody(emptyListResponse))

  //Repos >> createStatus
  mockServer
    .when(
      request
        .withMethod("POST")
        .withPath(s"/repos/$validRepoOwner/$validRepoName/statuses/$validCommitSha")
        .withHeader("Authorization", tokenHeader))
    .respond(response.withStatusCode(createdStatusCode).withBody(createStatusValidResponse))

  mockServer
    .when(
      request
        .withMethod("POST")
        .withPath(s"/repos/$validRepoOwner/$validRepoName/statuses/$validCommitSha")
        .withHeader(not("Authorization")))
    .respond(response.withStatusCode(unauthorizedStatusCode).withBody(unauthorizedResponse))

  mockServer
    .when(
      request
        .withMethod("POST")
        .withPath(s"/repos/$validRepoOwner/$validRepoName/statuses/$invalidCommitSha")
        .withHeader("Authorization", tokenHeader))
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
        .withBody(unauthorizedResponse))

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
        .withBody(unauthorizedResponse))

  mockServer
    .when(
      request
        .withMethod("PATCH")
        .withPath(s"/repos/$validRepoOwner/$validRepoName/git/refs/$validRefSingle")
        .withHeader("Authorization", tokenHeader))
    .respond(
      response
        .withStatusCode(okStatusCode)
        .withBody(singleReference))

  mockServer
    .when(
      request
        .withMethod("PATCH")
        .withPath(s"/repos/$validRepoOwner/$validRepoName/git/refs/$validRefSingle")
        .withHeader(not("Authorization")))
    .respond(
      response
        .withStatusCode(unauthorizedStatusCode)
        .withBody(unauthorizedResponse))

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
        .withBody(unauthorizedResponse))

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
        .withBody(unauthorizedResponse))

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
        .withBody(unauthorizedResponse))

  //PullRequests >> get
  mockServer
    .when(
      request
        .withMethod("GET")
        .withPath(s"/repos/$validRepoOwner/$validRepoName/pulls/$validPullRequestNumber"))
    .respond(response.withStatusCode(okStatusCode).withBody(getPullRequestValidResponse))

  mockServer
    .when(
      request
        .withMethod("GET")
        .withPath(s"/repos/$validRepoOwner/$invalidRepoName/pulls/$validPullRequestNumber"))
    .respond(response.withStatusCode(notFoundStatusCode).withBody(notFoundResponse))

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

  //PullRequests >> listFiles
  mockServer
    .when(
      request
        .withMethod("GET")
        .withPath(s"/repos/$validRepoOwner/$validRepoName/pulls/$validPullRequestNumber/files"))
    .respond(response.withStatusCode(okStatusCode).withBody(validListPullRequestFilesReponse))

  mockServer
    .when(
      request
        .withMethod("GET")
        .withPath(s"/repos/$validRepoOwner/$invalidRepoName/pulls/$validPullRequestNumber/files"))
    .respond(response.withStatusCode(notFoundStatusCode).withBody(notFoundResponse))

  //PullRequests >> create
  mockServer
    .when(
      request
        .withMethod("POST")
        .withPath(s"/repos/$validRepoOwner/$validRepoName/pulls")
        .withBody(json(s"""
            |{
            |  "title": "${validNewPullRequestData.title}",
            |  "head": "$validHead",
            |  "base": "$validBase",
            |  "body": "${validNewPullRequestData.body}"
            |}
          """.stripMargin)))
    .respond(response.withStatusCode(createdStatusCode).withBody(validCreatePullRequest))

  mockServer
    .when(
      request
        .withMethod("POST")
        .withPath(s"/repos/$validRepoOwner/$invalidRepoName/pulls")
        .withBody(json(s"""
             |{
             |  "title": "${validNewPullRequestData.title}",
             |  "body": "${validNewPullRequestData.body}",
             |  "head": "$validHead",
             |  "base": "$validBase"
             |}
           """.stripMargin)))
    .respond(response.withStatusCode(notFoundStatusCode).withBody(notFoundResponse))

  mockServer
    .when(
      request
        .withMethod("POST")
        .withPath(s"/repos/$validRepoOwner/$validRepoName/pulls")
        .withBody(json(s"""
            |{
            |  "issue": ${validNewPullRequestIssue.issue},
            |  "head": "$validHead",
            |  "base": "$validBase"
            |}
          """.stripMargin)))
    .respond(response.withStatusCode(createdStatusCode).withBody(validCreatePullRequest))

  mockServer
    .when(
      request
        .withMethod("POST")
        .withPath(s"/repos/$validRepoOwner/$invalidRepoName/pulls")
        .withBody(json(s"""
           |{
           |  "issue": ${invalidNewPullRequestIssue.issue},
           |  "head": "$invalidHead",
           |  "base": "$invalidBase"
           |}
         """.stripMargin)))
    .respond(response.withStatusCode(createdStatusCode).withBody(validCreatePullRequest))

  //PullRequests >> listReviews
  mockServer
    .when(
      request
        .withMethod("GET")
        .withPath(s"/repos/$validRepoOwner/$validRepoName/pulls/$validPullRequestNumber/reviews")
        .withHeader("Authorization", tokenHeader))
    .respond(response.withStatusCode(okStatusCode).withBody(listReviewsValidResponse))

  mockServer
    .when(
      request
        .withMethod("GET")
        .withPath(s"/repos/$validRepoOwner/$invalidRepoName/pulls/$validPullRequestNumber/reviews")
        .withHeader("Authorization", tokenHeader))
    .respond(response.withStatusCode(notFoundStatusCode).withBody(notFoundResponse))

  //PullRequests >> getReview
  mockServer
    .when(
      request
        .withMethod("GET")
        .withPath(
          s"/repos/$validRepoOwner/$validRepoName/pulls/$validPullRequestNumber/reviews/$validPullRequestReviewNumber")
        .withHeader("Authorization", tokenHeader))
    .respond(response.withStatusCode(okStatusCode).withBody(getReviewValidResponse))

  mockServer
    .when(
      request
        .withMethod("GET")
        .withPath(
          s"/repos/$validRepoOwner/$invalidRepoName/pulls/$validPullRequestNumber/reviews/$validPullRequestReviewNumber")
        .withHeader("Authorization", tokenHeader))
    .respond(response.withStatusCode(notFoundStatusCode).withBody(notFoundResponse))

  //Issues >> list
  mockServer
    .when(
      request
        .withMethod("GET")
        .withPath(s"/repos/$validRepoOwner/$validRepoName/issues")
        .withHeader("Authorization", tokenHeader))
    .respond(response.withStatusCode(okStatusCode).withBody(listIssuesValidResponse))

  mockServer
    .when(
      request
        .withMethod("GET")
        .withPath(s"/repos/$validRepoOwner/$invalidRepoName/issues")
        .withHeader("Authorization", tokenHeader))
    .respond(response.withStatusCode(notFoundStatusCode).withBody(notFoundResponse))

  //Issues >> get
  mockServer
    .when(
      request
        .withMethod("GET")
        .withPath(s"/repos/$validRepoOwner/$validRepoName/issues/$validIssueNumber")
        .withHeader("Authorization", tokenHeader))
    .respond(response.withStatusCode(okStatusCode).withBody(getIssueValidResponse))

  mockServer
    .when(
      request
        .withMethod("GET")
        .withPath(s"/repos/$validRepoOwner/$validRepoName/issues/$invalidIssueNumber")
        .withHeader("Authorization", tokenHeader))
    .respond(response.withStatusCode(notFoundStatusCode).withBody(notFoundResponse))

  //Issues >> create
  mockServer
    .when(
      request
        .withMethod("POST")
        .withPath(s"/repos/$validRepoOwner/$validRepoName/issues")
        .withHeader("Authorization", tokenHeader))
    .respond(response.withStatusCode(createdStatusCode).withBody(createIssueValidResponse))

  mockServer
    .when(
      request
        .withMethod("POST")
        .withPath(s"/repos/$validRepoOwner/$validRepoName/issues")
        .withHeader(not("Authorization")))
    .respond(response.withStatusCode(unauthorizedStatusCode).withBody(unauthorizedResponse))

  mockServer
    .when(
      request
        .withMethod("POST")
        .withPath(s"/repos/$validRepoOwner/$invalidRepoName/issues")
        .withHeader("Authorization", tokenHeader))
    .respond(response.withStatusCode(notFoundStatusCode).withBody(notFoundResponse))

  //Issues >> edit
  mockServer
    .when(
      request
        .withMethod("PATCH")
        .withPath(s"/repos/$validRepoOwner/$validRepoName/issues/$validIssueNumber")
        .withHeader("Authorization", tokenHeader))
    .respond(response.withStatusCode(okStatusCode).withBody(createIssueValidResponse))

  mockServer
    .when(
      request
        .withMethod("PATCH")
        .withPath(s"/repos/$validRepoOwner/$validRepoName/issues/$validIssueNumber")
        .withHeader(not("Authorization")))
    .respond(response.withStatusCode(unauthorizedStatusCode).withBody(unauthorizedResponse))

  mockServer
    .when(
      request
        .withMethod("PATCH")
        .withPath(s"/repos/$validRepoOwner/$invalidRepoName/issues/$validIssueNumber")
        .withHeader("Authorization", tokenHeader))
    .respond(response.withStatusCode(notFoundStatusCode).withBody(notFoundResponse))

  //Issues >> search
  mockServer
    .when(
      request
        .withMethod("GET")
        .withPath(s"/search/issues")
        .withQueryStringParameters(new Parameter("q", s".*$validSearchQuery.*"))
        .withHeader("Authorization", tokenHeader))
    .respond(response.withStatusCode(okStatusCode).withBody(searchIssuesValidResponse))

  mockServer
    .when(
      request
        .withMethod("GET")
        .withPath(s"/search/issues")
        .withQueryStringParameters(new Parameter("q", s".*$nonExistentSearchQuery.*"))
        .withHeader("Authorization", tokenHeader))
    .respond(response.withStatusCode(okStatusCode).withBody(searchIssuesEmptyResponse))

  //Issues >> List comments of an Issue
  mockServer
    .when(
      request
        .withMethod("GET")
        .withPath(s"/repos/$validRepoOwner/$validRepoName/issues/$validIssueNumber/comments")
        .withHeader("Authorization", tokenHeader))
    .respond(response.withStatusCode(okStatusCode).withBody(listCommentsValidResponse))

  mockServer
    .when(
      request
        .withMethod("GET")
        .withPath(s"/repos/$validRepoOwner/$validRepoName/issues/$invalidIssueNumber/comments")
        .withHeader("Authorization", tokenHeader))
    .respond(response.withStatusCode(notFoundStatusCode).withBody(notFoundResponse))

  mockServer
    .when(
      request
        .withMethod("GET")
        .withPath(s"/repos/$validRepoOwner/$validRepoName/issues/$validIssueNumber/comments")
        .withHeader(not("Authorization")))
    .respond(response.withStatusCode(notFoundStatusCode).withBody(notFoundResponse))

  //Issues >> Create a comment
  mockServer
    .when(
      request
        .withMethod("POST")
        .withPath(s"/repos/$validRepoOwner/$validRepoName/issues/$validIssueNumber/comments")
        .withHeader("Authorization", tokenHeader))
    .respond(response.withStatusCode(createdStatusCode).withBody(commentResponse))

  mockServer
    .when(
      request
        .withMethod("POST")
        .withPath(s"/repos/$validRepoOwner/$validRepoName/issues/$validIssueNumber/comments")
        .withHeader(not("Authorization")))
    .respond(response.withStatusCode(notFoundStatusCode).withBody(notFoundResponse))

  mockServer
    .when(
      request
        .withMethod("POST")
        .withPath(s"/repos/$validRepoOwner/$validRepoName/issues/$invalidIssueNumber/comments")
        .withHeader("Authorization", tokenHeader))
    .respond(response.withStatusCode(notFoundStatusCode).withBody(notFoundResponse))

  //Issues >> Edit a comment
  mockServer
    .when(
      request
        .withMethod("PATCH")
        .withPath(s"/repos/$validRepoOwner/$validRepoName/issues/comments/$validCommentId")
        .withHeader("Authorization", tokenHeader))
    .respond(response.withStatusCode(okStatusCode).withBody(commentResponse))

  mockServer
    .when(
      request
        .withMethod("PATCH")
        .withPath(s"/repos/$validRepoOwner/$validRepoName/issues/comments/$validCommentId")
        .withHeader(not("Authorization")))
    .respond(response.withStatusCode(notFoundStatusCode).withBody(notFoundResponse))

  mockServer
    .when(
      request
        .withMethod("PATCH")
        .withPath(s"/repos/$validRepoOwner/$validRepoName/issues/comments/$invalidCommentId")
        .withHeader("Authorization", tokenHeader))
    .respond(response.withStatusCode(notFoundStatusCode).withBody(notFoundResponse))

  //Issues >> Delete a comment
  mockServer
    .when(
      request
        .withMethod("DELETE")
        .withPath(s"/repos/$validRepoOwner/$validRepoName/issues/comments/$validCommentId")
        .withHeader("Authorization", tokenHeader))
    .respond(response.withStatusCode(deletedStatusCode))

  mockServer
    .when(
      request
        .withMethod("DELETE")
        .withPath(s"/repos/$validRepoOwner/$validRepoName/issues/comments/$validCommentId")
        .withHeader(not("Authorization")))
    .respond(response.withStatusCode(notFoundStatusCode).withBody(notFoundResponse))

  mockServer
    .when(
      request
        .withMethod("DELETE")
        .withPath(s"/repos/$validRepoOwner/$validRepoName/issues/comments/$invalidCommentId")
        .withHeader("Authorization", tokenHeader))
    .respond(response.withStatusCode(notFoundStatusCode).withBody(notFoundResponse))

  //Issues >> Add labels for an Issue
  mockServer
    .when(
      request
        .withMethod("POST")
        .withPath(s"/repos/$validRepoOwner/$validRepoName/issues/$validIssueNumber/labels")
        .withHeader("Authorization", tokenHeader))
    .respond(response.withStatusCode(okStatusCode).withBody(listLabelsValidResponse))

  mockServer
    .when(
      request
        .withMethod("POST")
        .withPath(s"/repos/$validRepoOwner/$validRepoName/issues/$invalidIssueNumber/lablels")
        .withHeader("Authorization", tokenHeader))
    .respond(response.withStatusCode(notFoundStatusCode).withBody(notFoundResponse))

  mockServer
    .when(
      request
        .withMethod("POST")
        .withPath(s"/repos/$validRepoOwner/$validRepoName/issues/$validIssueNumber/labels")
        .withHeader(not("Authorization")))
    .respond(response.withStatusCode(notFoundStatusCode).withBody(notFoundResponse))

  //Issues >> Remove label from an Issue
  mockServer
    .when(request
      .withMethod("DELETE")
      .withPath(
        s"/repos/$validRepoOwner/$validRepoName/issues/$validIssueNumber/labels/${validIssueLabel.head}")
      .withHeader("Authorization", tokenHeader))
    .respond(response.withStatusCode(okStatusCode).withBody(listLabelsValidResponse))

  mockServer
    .when(request
      .withMethod("DELETE")
      .withPath(
        s"/repos/$validRepoOwner/$validRepoName/issues/$invalidIssueNumber/labels/${validIssueLabel.head}")
      .withHeader("Authorization", tokenHeader))
    .respond(response.withStatusCode(notFoundStatusCode).withBody(notFoundResponse))

  mockServer
    .when(request
      .withMethod("DELETE")
      .withPath(
        s"/repos/$validRepoOwner/$validRepoName/issues/$validIssueNumber/labels/${validIssueLabel.head}")
      .withHeader(not("Authorization")))
    .respond(response.withStatusCode(notFoundStatusCode).withBody(notFoundResponse))

  //Issues >> List labels for an Issue
  mockServer
    .when(
      request
        .withMethod("GET")
        .withPath(s"/repos/$validRepoOwner/$validRepoName/issues/$validIssueNumber/labels")
        .withHeader("Authorization", tokenHeader))
    .respond(response.withStatusCode(okStatusCode).withBody(listLabelsValidResponse))

  mockServer
    .when(
      request
        .withMethod("GET")
        .withPath(s"/repos/$validRepoOwner/$validRepoName/issues/$invalidIssueNumber/lablels")
        .withHeader("Authorization", tokenHeader))
    .respond(response.withStatusCode(notFoundStatusCode).withBody(notFoundResponse))

  mockServer
    .when(
      request
        .withMethod("GET")
        .withPath(s"/repos/$validRepoOwner/$validRepoName/issues/$validIssueNumber/labels")
        .withHeader(not("Authorization")))
    .respond(response.withStatusCode(notFoundStatusCode).withBody(notFoundResponse))

  //Activities >> Set a thread subscription
  mockServer
    .when(
      request
        .withMethod("PUT")
        .withPath(s"/notifications/threads/$validThreadId/subscription")
        .withHeader("Authorization", tokenHeader))
    .respond(response.withStatusCode(okStatusCode).withBody(setThreadSubscription))

  mockServer
    .when(
      request
        .withMethod("PUT")
        .withPath(s"/notifications/threads/$validThreadId/subscription")
        .withHeader(not("Authorization")))
    .respond(response.withStatusCode(notFoundStatusCode).withBody(notFoundResponse))

  mockServer
    .when(
      request
        .withMethod("PUT")
        .withPath(s"/notifications/threads/$invalidThreadId/subscription")
        .withHeader("Authorization", tokenHeader))
    .respond(response.withStatusCode(notFoundStatusCode).withBody(notFoundResponse))

  //Activities >> List stargazers
  mockServer
    .when(
      request
        .withMethod("GET")
        .withPath(s"/repos/$validRepoOwner/$validRepoName/stargazers")
        .withQueryStringParameter("page", validPage.toString))
    .respond(response.withStatusCode(okStatusCode).withBody(getUsersValidResponse))

  mockServer
    .when(
      request
        .withMethod("GET")
        .withPath(s"/repos/$validRepoOwner/$validRepoName/stargazers")
        .withQueryStringParameter("page", invalidPage.toString))
    .respond(response.withStatusCode(okStatusCode).withBody(emptyListResponse))

  mockServer
    .when(
      request
        .withMethod("GET")
        .withPath(s"/repos/$validRepoOwner/$invalidRepoName/stargazers"))
    .respond(response.withStatusCode(notFoundStatusCode).withBody(notFoundResponse))

  //Activities >> List starred repositories
  mockServer
    .when(
      request
        .withMethod("GET")
        .withPath(s"/users/$validUsername/starred")
        .withQueryStringParameter("page", validPage.toString))
    .respond(response.withStatusCode(okStatusCode).withBody(getStarredReposValidResponse))

  mockServer
    .when(
      request
        .withMethod("GET")
        .withPath(s"/users/$validUsername/starred")
        .withQueryStringParameter("page", invalidPage.toString))
    .respond(response.withStatusCode(okStatusCode).withBody(emptyListResponse))

  mockServer
    .when(
      request
        .withMethod("GET")
        .withPath(s"/users/$invalidUsername/starred"))
    .respond(response.withStatusCode(notFoundStatusCode).withBody(notFoundResponse))

  //Organizations >> List members
  mockServer
    .when(
      request
        .withMethod("GET")
        .withPath(s"/orgs/$validRepoOwner/members")
        .withQueryStringParameter("page", validPage.toString))
    .respond(response.withStatusCode(okStatusCode).withBody(getUsersValidResponse))

  mockServer
    .when(
      request
        .withMethod("GET")
        .withPath(s"/orgs/$validRepoOwner/members")
        .withQueryStringParameter("page", invalidPage.toString))
    .respond(response.withStatusCode(okStatusCode).withBody(emptyListResponse))

  mockServer
    .when(
      request
        .withMethod("GET")
        .withPath(s"/orgs/$invalidUsername/members"))
    .respond(response.withStatusCode(notFoundStatusCode).withBody(notFoundResponse))
}
