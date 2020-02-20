/*
 * Copyright 2016-2020 47 Degrees, LLC. <http://www.47deg.com>
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

import cats.effect.IO
import cats.data.NonEmptyList
import github4s.Github
import github4s.domain._
import github4s.utils.{BaseIntegrationSpec, Integration}

trait GHGitDataSpec extends BaseIntegrationSpec {

  "GitData >> GetReference" should "return a list of references" taggedAs Integration in {
    val response = Github[IO](accessToken).gitData
      .getReference(validRepoOwner, validRepoName, "heads", headerUserAgent)
      .unsafeRunSync()

    testIsRight[NonEmptyList[Ref]](response, { r =>
      r.result.tail.nonEmpty shouldBe true
      r.statusCode shouldBe okStatusCode
    })
  }

  it should "return at least one reference" taggedAs Integration in {
    val response = Github[IO](accessToken).gitData
      .getReference(validRepoOwner, validRepoName, validRefSingle, headerUserAgent)
      .unsafeRunSync()

    testIsRight[NonEmptyList[Ref]](response, { r =>
      r.result.head.ref.contains(validRefSingle) shouldBe true
      r.statusCode shouldBe okStatusCode
    })
  }

  it should "return an error when an invalid repository name is passed" taggedAs Integration in {
    val response = Github[IO](accessToken).gitData
      .getReference(validRepoOwner, invalidRepoName, validRefSingle, headerUserAgent)
      .unsafeRunSync()

    testIsLeft(response)
  }

  "GitData >> GetCommit" should "return one commit" taggedAs Integration in {
    val response = Github[IO](accessToken).gitData
      .getCommit(validRepoOwner, validRepoName, validCommitSha, headerUserAgent)
      .unsafeRunSync()

    testIsRight[RefCommit](response, { r =>
      r.result.message shouldBe validCommitMsg
      r.statusCode shouldBe okStatusCode
    })
  }

  it should "return an error when an invalid repository name is passed" taggedAs Integration in {
    val response = Github[IO](accessToken).gitData
      .getCommit(validRepoOwner, invalidRepoName, validCommitSha, headerUserAgent)
      .unsafeRunSync()

    testIsLeft(response)
  }

  "GitData >> GetTree" should "return the file tree non-recursively" taggedAs Integration in {
    val response =
      Github[IO](accessToken).gitData
        .getTree(validRepoOwner, validRepoName, validCommitSha, recursive = false, headerUserAgent)
        .unsafeRunSync()

    testIsRight[TreeResult](
      response, { r =>
        r.statusCode shouldBe okStatusCode
        r.result.tree.map(_.path) shouldBe List(".gitignore", "build.sbt", "project")
        r.result.truncated shouldBe Some(false)
      }
    )
  }

  it should "return the file tree recursively" taggedAs Integration in {
    val response =
      Github[IO](accessToken).gitData
        .getTree(validRepoOwner, validRepoName, validCommitSha, recursive = true, headerUserAgent)
        .unsafeRunSync()

    testIsRight[TreeResult](
      response, { r =>
        r.statusCode shouldBe okStatusCode
        r.result.tree.map(_.path) shouldBe List(
          ".gitignore",
          "build.sbt",
          "project",
          "project/build.properties",
          "project/plugins.sbt")
      }
    )
  }

  it should "return an error when an invalid repository name is passed" taggedAs Integration in {
    val response = Github[IO](accessToken).gitData
      .getTree(validRepoOwner, invalidRepoName, validCommitSha, recursive = false, headerUserAgent)
      .unsafeRunSync()

    testIsLeft(response)
  }

}
