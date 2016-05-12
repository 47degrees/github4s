package com.fortysevendeg.github4s.integration

import cats.Id
import cats.scalatest.{XorMatchers, XorValues}
import com.fortysevendeg.github4s.Github._
import com.fortysevendeg.github4s.GithubResponses._
import com.fortysevendeg.github4s.free.interpreters.IdInterpreters._
import com.fortysevendeg.github4s.{Github, TestUtils}
import org.scalatest.{Matchers, FlatSpec}


class GHReposSpec extends FlatSpec with Matchers with XorMatchers with XorValues with TestUtils {

  "Repos >> Get" should "return the expected name when valid repo is provided" in {

    val response = Github(accessToken).repos.get(validRepoOwner, validRepoName).exec[Id]
    response shouldBe right
    response.value.entity.name shouldBe validRepoName
    response.value.statusCode shouldBe okStatusCode
  }

  it should "return error when an invalid repo name is passed" in {
    val response = Github(accessToken).repos.get(validRepoOwner, invalidRepoName).exec[Id]
    response shouldBe left
  }

  "Repos >> ListCommits" should "return the expected list of commits for valid data" in {
    val response = Github(accessToken).repos.listCommits(validRepoOwner, validRepoName).exec[Id]
    response shouldBe right
    response.value.entity.nonEmpty shouldBe true
    response.value.statusCode shouldBe okStatusCode
  }

  it should "return error for invalid repo name" in {
    val response = Github(accessToken).repos.listCommits(invalidRepoName, validRepoName).exec[Id]
    response shouldBe left
  }

}