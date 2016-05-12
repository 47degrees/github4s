package com.fortysevendeg.github4s

import org.scalatest.mock.MockitoSugar

import scalaj.http.{HttpResponse, HttpRequest}

trait TestUtils extends MockitoSugar with ResponsesJSON {

  val accessToken = sys.props.get("token")

  val validUsername = "rafaparadela"
  val invalidUsername = "GHInvalidaUserName"
  val invalidPassword = "invalidPassword"

  val validScopes = List("public_repo")
  val validNote = "New access token"
  val validClientId = "e8e39175648c9db8c280"
  val invalidClientSecret = "1234567890"

  val validRepoOwner = "47deg"
  val validRepoName = "github4s"
  val invalidRepoName = "GHInvalidRepoName"
  val validRedirectUri = "http://localhost:9000/_oauth-callback"

  val validSinceInt = 100
  val invalidSinceInt = -1

  val okStatusCode = 200
  val unauthorizedStatusCode = 401
  val notFoundStatusCode = 404



  val getRepoHttpSuccessResponse  = httpRes(getRepoJson, okStatusCode)
  val listCommitsSuccessResponse  = httpRes(listCommitsJson, okStatusCode)
  val newAuthorizationSuccessResponse  = httpRes(newAuthorizationJson, okStatusCode)

  val badCredentialHttpResponse = httpRes(badCredentialsJson, unauthorizedStatusCode)
  val notFoundHttpResponse  = httpRes(notFoundJson, notFoundStatusCode)


  def httpRes(body: String, code: Int) = HttpResponse[String](body, code, headers = Map.empty)

}
