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

package github4s.unit

import github4s.api.{Auth, Gists, Repos, Users}
import github4s.free.domain.Pagination
import github4s.utils.{DummyGithubUrls, MockGithubApiServer, TestUtils}
import org.scalatest._
import cats.implicits._

class ApiSpec
    extends FlatSpec
    with Matchers
    with TestUtils
    with MockGithubApiServer
    with DummyGithubUrls {

  val auth  = new Auth
  val repos = new Repos
  val users = new Users
  val gists = new Gists

  "Auth >> NewAuth" should "return a valid token when valid credential is provided" in {
    val response = auth.newAuth(validUsername, "", validScopes, validNote, validClientId, "")
    response should be('right)

    response.toOption map { r ⇒
      r.result.token.nonEmpty shouldBe true
      r.statusCode shouldBe okStatusCode
    }
  }

  it should "return error on Left when invalid credential is provided" in {
    val response =
      auth.newAuth(validUsername, invalidPassword, validScopes, validNote, validClientId, "")
    response should be('left)
  }

  "Auth >> AuthorizeUrl" should "return the expected URL for valid username" in {
    val response = auth.authorizeUrl(validClientId, validRedirectUri, validScopes)
    response should be('right)

    response.toOption map { r ⇒
      r.result.url.contains(validRedirectUri) shouldBe true
      r.statusCode shouldBe okStatusCode
    }
  }

  "Auth >> GetAccessToken" should "return a valid access_token when a valid code is provided" in {
    val response = auth.getAccessToken("", "", validCode, "", "")
    response should be('right)

    response.toOption map { r ⇒
      r.result.access_token.nonEmpty shouldBe true
      r.statusCode shouldBe okStatusCode
    }

  }

  it should "return error on Left when invalid code is provided" in {
    val response = auth.getAccessToken("", "", invalidCode, "", "")
    response should be('left)
  }

  "Repos >> Get" should "return the expected name when valid repo is provided" in {

    val response = repos.get(accessToken, validRepoOwner, validRepoName)
    response should be('right)

    response.toOption map { r ⇒
      r.result.name shouldBe validRepoName
      r.statusCode shouldBe okStatusCode
    }

  }

  it should "return error when an invalid repo name is passed" in {
    val response = repos.get(accessToken, validRepoOwner, invalidRepoName)
    response should be('left)
  }

  "Repos >> ListCommits" should "return the expected list of commits for valid data" in {
    val response = repos.listCommits(
      accessToken = accessToken,
      owner = validRepoOwner,
      repo = validRepoName,
      pagination = Option(Pagination(validPage, validPerPage))
    )
    response should be('right)

    response.toOption map { r ⇒
      r.result.nonEmpty shouldBe true
      r.statusCode shouldBe okStatusCode
    }
  }

  it should "return an empty list of commits for invalid page parameter" in {
    val response = repos.listCommits(
      accessToken = accessToken,
      owner = validRepoOwner,
      repo = validRepoName,
      pagination = Option(Pagination(invalidPage, validPerPage))
    )

    response should be('right)

    response.toOption map { r ⇒
      r.result.isEmpty shouldBe true
      r.statusCode shouldBe okStatusCode
    }

  }

  it should "return error for invalid repo name" in {
    val response = repos.listCommits(accessToken, validRepoOwner, invalidRepoName)
    response should be('left)
  }

  "Repos >> ListContributors" should "return the expected list of contributors for valid data" in {
    val response = repos.listContributors(
      accessToken = accessToken,
      owner = validRepoOwner,
      repo = validRepoName
    )

    response should be('right)

    response.toOption map { r ⇒
      r.result shouldNot be(empty)
      r.statusCode shouldBe okStatusCode
    }

  }

  it should "return the expected list of contributors for valid data, including a valid anon parameter" in {
    val response = repos.listContributors(
      accessToken = accessToken,
      owner = validRepoOwner,
      repo = validRepoName,
      anon = Option(validAnonParameter)
    )

    response should be('right)
    response.toOption map { r ⇒
      r.result shouldNot be(empty)
      r.statusCode shouldBe okStatusCode
    }

  }

  it should "return an empty list of contributors for invalid anon parameter" in {
    val response = repos.listContributors(
      accessToken = accessToken,
      owner = validRepoOwner,
      repo = validRepoName,
      anon = Some(invalidAnonParameter)
    )

    response should be('right)

    response.toOption map { r ⇒
      r.result shouldBe empty
      r.statusCode shouldBe okStatusCode
    }
  }

  it should "return error for invalid repo name" in {
    val response = repos.listContributors(accessToken, validRepoOwner, invalidRepoName)
    response should be('left)
  }

  "Users >> Get" should "return the expected login for a valid username" in {

    val response = users.get(accessToken, validUsername)

    response should be('right)
    response.toOption map { r ⇒
      r.result.login shouldBe validUsername
      r.statusCode shouldBe okStatusCode
    }

  }

  it should "return error on Left for invalid username" in {
    val response = users.get(accessToken, invalidUsername)
    response should be('left)
  }

  "Users >> GetAuth" should "return the expected login for a valid accessToken" in {
    val response = users.getAuth(accessToken)
    response should be('right)

    response.toOption map { r ⇒
      r.result.login shouldBe validUsername
      r.statusCode shouldBe okStatusCode
    }

  }

  it should "return error on Left when no accessToken is provided" in {
    val response = users.getAuth(None)
    response should be('left)
  }

  "Users >> GetUsers" should "return users for a valid since value" in {
    val response = users.getUsers(accessToken, validSinceInt)
    response should be('right)

    response.toOption map { r ⇒
      r.result.nonEmpty shouldBe true
      r.statusCode shouldBe okStatusCode
    }

  }

  it should "return an empty list when a invalid since value is provided" in {
    val response = users.getUsers(accessToken, invalidSinceInt)
    response should be('right)

    response.toOption map { r ⇒
      r.result.isEmpty shouldBe true
      r.statusCode shouldBe okStatusCode
    }

  }

  "Gists >> PostGist" should "return the provided gist for a valid request" in {
    val response =
      gists.newGist(validGistDescription, validGistPublic, validGistFiles, accessToken)
    response should be('right)

    response.toOption map { r ⇒
      r.statusCode shouldBe createdStatusCode
    }

  }

}
