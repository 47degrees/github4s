/*
 * Copyright 2016-2020 47 Degrees Open Source <https://www.47deg.com>
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

import cats.data.NonEmptyList
import cats.effect.{IO, Resource}
import cats.implicits._
import github4s.GHError.NotFoundError
import github4s.domain._
import github4s.utils.{BaseIntegrationSpec, Integration}
import github4s.{GHResponse, Github}

trait ReposSpec extends BaseIntegrationSpec {

  "Repos >> Get" should "return the expected name when a valid repo is provided" taggedAs Integration in {
    val response = clientResource
      .use { client =>
        Github[IO](client, accessToken).repos
          .get(validRepoOwner, validRepoName, headers = headerUserAgent)
      }
      .unsafeRunSync()

    testIsRight[Repository](response, r => r.name shouldBe validRepoName)
    response.statusCode shouldBe okStatusCode
  }

  it should "return error when an invalid repo name is passed" taggedAs Integration in {
    val response = clientResource
      .use { client =>
        Github[IO](client, accessToken).repos
          .get(validRepoOwner, invalidRepoName, headers = headerUserAgent)
      }
      .unsafeRunSync()

    testIsLeft[NotFoundError, Repository](response)
    response.statusCode shouldBe notFoundStatusCode
  }

  "Repos >> ListReleases" should "return the expected repos when a valid org is provided" taggedAs Integration in {
    val response = clientResource
      .use { client =>
        Github[IO](client, accessToken).repos
          .listReleases(validRepoOwner, validRepoName, None, headers = headerUserAgent)
      }
      .unsafeRunSync()

    testIsRight[List[Release]](response, r => r.nonEmpty shouldBe true)
    response.statusCode shouldBe okStatusCode
  }

  "Repos >> getRelease" should "return the expected repos when a valid org is provided" taggedAs Integration in {

    val responseResource = for {
      client <- clientResource

      gh: Github[IO] = Github[IO](client, accessToken)
      releasesIO =
        gh.repos.listReleases(validRepoOwner, validRepoName, None, headers = headerUserAgent)

      releasesResponse <- Resource.liftF(releasesIO)

      releases <- Resource.liftF(IO.fromEither(releasesResponse.result))

      releasesAreFoundCheck: IO[List[(Release, GHResponse[Option[Release]])]] = releases.map {
        release =>
          val releaseIO = gh.repos
            .getRelease(release.id, validRepoOwner, validRepoName, headers = headerUserAgent)
          releaseIO.map(r => release -> r)
      }.sequence

    } yield releasesAreFoundCheck

    val responseList: List[(Release, GHResponse[Option[Release]])] = responseResource
      .use(identity)
      .unsafeRunSync()

    forAll(responseList) {
      case (release, response) =>
        testIsRight[Option[Release]](response, r => r should contain(release))
        response.statusCode shouldBe okStatusCode
    }
  }

  "Repos >> LatestRelease" should "return the expected repos when a valid org is provided" taggedAs Integration in {
    val response = clientResource
      .use { client =>
        Github[IO](client, accessToken).repos
          .latestRelease(validRepoOwner, validRepoName, headers = headerUserAgent)
      }
      .unsafeRunSync()

    testIsRight[Option[Release]](response, r => r.nonEmpty shouldBe true)
    response.statusCode shouldBe okStatusCode
  }

  "Repos >> ListOrgRepos" should "return the expected repos when a valid org is provided" taggedAs Integration in {
    val response = clientResource
      .use { client =>
        Github[IO](client, accessToken).repos
          .listOrgRepos(validRepoOwner, headers = headerUserAgent)
      }
      .unsafeRunSync()

    testIsRight[List[Repository]](response, r => r.nonEmpty shouldBe true)
    response.statusCode shouldBe okStatusCode
  }

  it should "return error when an invalid org is passed" taggedAs Integration in {
    val response = clientResource
      .use { client =>
        Github[IO](client, accessToken).repos
          .listOrgRepos(invalidRepoName, headers = headerUserAgent)
      }
      .unsafeRunSync()

    testIsLeft[NotFoundError, List[Repository]](response)
    response.statusCode shouldBe notFoundStatusCode
  }

  "Repos >> ListUserRepos" should "return the expected repos when a valid user is provided" taggedAs Integration in {
    val response = clientResource
      .use { client =>
        Github[IO](client, accessToken).repos
          .listUserRepos(validUsername, headers = headerUserAgent)
      }
      .unsafeRunSync()

    testIsRight[List[Repository]](response, r => r.nonEmpty shouldBe true)
    response.statusCode shouldBe okStatusCode
  }

  it should "return error when an invalid user is passed" taggedAs Integration in {
    val response = clientResource
      .use { client =>
        Github[IO](client, accessToken).repos
          .listUserRepos(invalidUsername, headers = headerUserAgent)
      }
      .unsafeRunSync()

    testIsLeft[NotFoundError, List[Repository]](response)
    response.statusCode shouldBe notFoundStatusCode
  }

  "Repos >> GetContents" should "return the expected contents when valid path is provided" taggedAs Integration in {
    val response = clientResource
      .use { client =>
        Github[IO](client, accessToken).repos
          .getContents(validRepoOwner, validRepoName, validFilePath, headers = headerUserAgent)
      }
      .unsafeRunSync()

    testIsRight[NonEmptyList[Content]](response, r => r.head.path shouldBe validFilePath)
    response.statusCode shouldBe okStatusCode
  }

  "Repos >> GetContents" should "have the same contents with getBlob using fileSha" taggedAs Integration in {

    val blobResponseFileContent = for {
      client <- clientResource
      res = Github[IO](client, accessToken)

      fileContentsIO = res.repos.getContents(
        owner = validRepoOwner,
        repo = validRepoName,
        path = validFilePath,
        headers = headerUserAgent
      )

      fileContentsResponse <- Resource.liftF(fileContentsIO)

      fileContentsEither = fileContentsResponse.result

      fileContents <- Resource.liftF(IO.fromEither(fileContentsEither))

      blobContentIO = res.gitData.getBlob(
        owner = validRepoOwner,
        repo = validRepoName,
        fileSha = fileContents.head.sha,
        headers = headerUserAgent
      )

      blobContentResponse <- Resource.liftF(blobContentIO)

    } yield (blobContentResponse, fileContents.head)

    val (blobContentResponse, fileContent) = blobResponseFileContent
      .use(a => IO.apply(a))
      .unsafeRunSync()

    testIsRight[BlobContent](blobContentResponse, _.content.shouldBe(fileContent.content))
    blobContentResponse.statusCode shouldBe okStatusCode
  }

  it should "return error when an invalid path is passed" taggedAs Integration in {
    val response = clientResource
      .use { client =>
        Github[IO](client, accessToken).repos
          .getContents(validRepoOwner, validRepoName, invalidFilePath, headers = headerUserAgent)
      }
      .unsafeRunSync()

    testIsLeft[NotFoundError, NonEmptyList[Content]](response)
    response.statusCode shouldBe notFoundStatusCode
  }

  "Repos >> ListCommits" should "return the expected list of commits for valid data" taggedAs Integration in {
    val response = clientResource
      .use { client =>
        Github[IO](client, accessToken).repos
          .listCommits(validRepoOwner, validRepoName, headers = headerUserAgent)
      }
      .unsafeRunSync()

    testIsRight[List[Commit]](response, r => r.nonEmpty shouldBe true)
    response.statusCode shouldBe okStatusCode
  }

  it should "return error for invalid repo name" taggedAs Integration in {
    val response = clientResource
      .use { client =>
        Github[IO](client, accessToken).repos
          .listCommits(invalidRepoName, validRepoName, headers = headerUserAgent)
      }
      .unsafeRunSync()

    testIsLeft[NotFoundError, List[Commit]](response)
    response.statusCode shouldBe notFoundStatusCode
  }

  "Repos >> ListBranches" should "return the expected list of branches for valid data" taggedAs Integration in {
    val response = clientResource
      .use { client =>
        Github[IO](client, accessToken).repos
          .listBranches(validRepoOwner, validRepoName, headers = headerUserAgent)
      }
      .unsafeRunSync()

    testIsRight[List[Branch]](response, r => r.nonEmpty shouldBe true)
    response.statusCode shouldBe okStatusCode
  }

  it should "return error for invalid repo name" taggedAs Integration in {
    val response = clientResource
      .use { client =>
        Github[IO](client, accessToken).repos
          .listBranches(invalidRepoName, validRepoName, headers = headerUserAgent)
      }
      .unsafeRunSync()

    testIsLeft[NotFoundError, List[Branch]](response)
    response.statusCode shouldBe notFoundStatusCode
  }

  "Repos >> ListContributors" should "return the expected list of contributors for valid data" taggedAs Integration in {
    val response = clientResource
      .use { client =>
        Github[IO](client, accessToken).repos
          .listContributors(validRepoOwner, validRepoName, headers = headerUserAgent)
      }
      .unsafeRunSync()

    testIsRight[List[User]](response, r => r shouldNot be(empty))
    response.statusCode shouldBe okStatusCode
  }

  it should "return error for invalid repo name" taggedAs Integration in {
    val response = clientResource
      .use { client =>
        Github[IO](client, accessToken).repos
          .listContributors(invalidRepoName, validRepoName, headers = headerUserAgent)
      }
      .unsafeRunSync()

    testIsLeft[NotFoundError, List[User]](response)
    response.statusCode shouldBe notFoundStatusCode
  }

  "Repos >> ListCollaborators" should "return the expected list of collaborators for valid data" taggedAs Integration in {
    val response = clientResource
      .use { client =>
        Github[IO](client, accessToken).repos
          .listCollaborators(validRepoOwner, validRepoName, headers = headerUserAgent)
      }
      .unsafeRunSync()

    testIsRight[List[User]](response, r => r shouldNot be(empty))
    response.statusCode shouldBe okStatusCode
  }

  it should "return error for invalid repo name" taggedAs Integration in {
    val response = clientResource
      .use { client =>
        Github[IO](client, accessToken).repos
          .listCollaborators(invalidRepoName, validRepoName, headers = headerUserAgent)
      }
      .unsafeRunSync()

    testIsLeft[NotFoundError, List[User]](response)
    response.statusCode shouldBe notFoundStatusCode
  }

  "Repos >> GetStatus" should "return a combined status" taggedAs Integration in {
    val response = clientResource
      .use { client =>
        Github[IO](client, accessToken).repos
          .getCombinedStatus(
            validRepoOwner,
            validRepoName,
            validRefSingle,
            headers = headerUserAgent
          )
      }
      .unsafeRunSync()

    testIsRight[CombinedStatus](
      response,
      r => r.repository.full_name shouldBe s"$validRepoOwner/$validRepoName"
    )
    response.statusCode shouldBe okStatusCode
  }

  it should "return an error when an invalid ref is passed" taggedAs Integration in {
    val response = clientResource
      .use { client =>
        Github[IO](client, accessToken).repos
          .getCombinedStatus(validRepoOwner, validRepoName, invalidRef, headers = headerUserAgent)
      }
      .unsafeRunSync()

    testIsLeft[NotFoundError, CombinedStatus](response)
    response.statusCode shouldBe notFoundStatusCode
  }

  "Repos >> ListStatus" should "return a non empty list when a valid ref is provided" taggedAs Integration in {
    val response = clientResource
      .use { client =>
        Github[IO](client, accessToken).repos
          .listStatuses(validRepoOwner, validRepoName, validCommitSha, headers = headerUserAgent)
      }
      .unsafeRunSync()

    testIsRight[List[Status]](response, r => r.nonEmpty shouldBe true)
    response.statusCode shouldBe okStatusCode
  }

  it should "return an error when an invalid ref is provided" taggedAs Integration in {
    val response = clientResource
      .use { client =>
        Github[IO](client, accessToken).repos
          .listStatuses(validRepoOwner, validRepoName, invalidRef, headers = headerUserAgent)
      }
      .unsafeRunSync()

    testIsLeft[NotFoundError, List[Status]](response)
    response.statusCode shouldBe notFoundStatusCode
  }
}
