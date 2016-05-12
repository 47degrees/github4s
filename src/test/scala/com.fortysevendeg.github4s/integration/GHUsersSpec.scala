package com.fortysevendeg.github4s.integration

import cats.Id
import cats.scalatest.{XorMatchers, XorValues}
import com.fortysevendeg.github4s.Github._
import com.fortysevendeg.github4s.GithubResponses._
import com.fortysevendeg.github4s.free.interpreters.IdInterpreters._
import com.fortysevendeg.github4s.{Github, TestUtils}
import org.scalatest._


class GHUsersSpec extends FlatSpec with Matchers with XorMatchers with XorValues with TestUtils {

  "Users >> Get" should "return the expected login for a valid username" in {
    val response = Github(accessToken).users.get(validUsername).exec[Id]
    response shouldBe right
    response.value.entity.login shouldBe validUsername
    response.value.statusCode shouldBe okStatusCode
  }

  it should "return error on Left for invalid username" in {
    val response = Github(accessToken).users.get(invalidUsername).exec[Id]
    response shouldBe left
  }

  "Users >> GetAuth" should "return error on Left when no accessToken is provided" in {
    val response = Github().users.getAuth.exec[Id]
    response shouldBe left
  }

  "Users >> GetUsers" should "return users for a valid since value" in {
    val response = Github(accessToken).users.getUsers(validSinceInt).exec[Id]
    response shouldBe right
    response.value.entity.nonEmpty shouldBe true
    response.value.statusCode shouldBe okStatusCode
  }

  it should "return error on Left when a invalid since value is provided" in {
    val response = Github(accessToken).users.getUsers(invalidSinceInt).exec[Id]
    response shouldBe right
    response.value.entity.nonEmpty shouldBe true
    response.value.statusCode shouldBe okStatusCode
  }

}
