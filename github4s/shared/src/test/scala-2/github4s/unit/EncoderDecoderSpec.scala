/*
 * Copyright 2016-2023 47 Degrees Open Source <https://www.47deg.com>
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

package github4s.unit

import cats.syntax.all._
import com.eed3si9n.expecty.Expecty
import github4s.ArbitraryDerivation
import github4s.Decoders._
import github4s.Encoders._
import github4s.domain.RepoUrlKeys.CommitComparisonResponse
import github4s.domain._
import io.circe.{Decoder, Encoder, Printer}
import org.scalacheck.fortyseven.GenInstances._
import org.scalacheck.{Arbitrary, Gen}
import org.scalactic.source.Position
import org.scalatest.Assertion
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatestplus.scalacheck.ScalaCheckPropertyChecks

import scala.reflect.ClassTag

class EncoderDecoderSpec extends AnyFlatSpec with ScalaCheckPropertyChecks {
  import github4s.ArbitraryDerivation.auto._

  private def checkRoundtrip[A](implicit
      enc: Encoder[A],
      dec: Decoder[A],
      arb: Arbitrary[A],
      pos: Position
  ): Assertion = {
    forAll { (value: A) =>
      // Use Expecty asserts here because manually figuring out the diff between two large, possibly nested, case classes is not a fun time
      val encoded   = enc(value)
      val attempted = dec.decodeJson(encoded)
      val decoded   = attempted.fold(e => fail(s"decode failure: $e\nEncoded:\n$encoded"), identity)
      withClue(encoded.printWith(Printer.spaces2SortKeys)) {
        try Expecty.assert(decoded == value)
        catch {
          // withClue doesn't work with AssertionError - It shows up as a test in error rather than a failure
          case e: AssertionError => fail(e)
        }
      }
      succeed // expecty returns Unit, forAll wants an Assertion
    }
  }

  private def test[A: Encoder: Decoder: Arbitrary](implicit
      tag: ClassTag[A],
      pos: Position
  ): Unit = {
    val name = tag.runtimeClass.getSimpleName
    registerTest(s"Encode/Decode: $name")(checkRoundtrip[A])
  }

  /** Custom instance since `Team` is recursive - it can't be derived */
  implicit def arbTeam: Arbitrary[Team] = Arbitrary {
    import Arbitrary.{arbitrary => arb}
    (
      arb[Long],
      arb[String],
      arb[String],
      arb[String],
      arb[String],
      arb[String],
      arb[String],
      arb[String],
      arb[String],
      arb[String],
      arb[Option[String]],
      arb[Option[Team]]
    ).mapN(Team.apply)
  }

  implicit val arbStatusRepository: Arbitrary[StatusRepository] = {
    val base = ArbitraryDerivation.deriveArb[StatusRepository].arbitrary
    val genUrls = Gen.mapOf(
      (Gen.oneOf(RepoUrlKeys.allFields), Gen.asciiPrintableStr).tupled
    )
    Arbitrary {
      (base, genUrls).mapN { (sr, urls) =>
        sr.copy(urls = urls)
      }
    }
  }

  test[BlobContent]
  test[BranchCommit]
  test[Branch]
  test[Card]
  test[Column]
  test[CombinedStatus]
  test[CommentData]
  test[Comment]
  test[Commit]
  test[Committer]
  test[Content]
  test[CreatePRReviewRequest]
  test[CreatePullRequest]
  test[CreateReferenceRequest]
  test[Creator]
  test[DeleteFileRequest]
  test[EditGistFile]
  test[EditGistRequest]
  test[EditIssueRequest]
  test[GistFile]
  test[Gist]
  test[IssuePullRequest]
  test[Issue]
  test[Label]
  test[MilestoneData]
  test[Milestone]
  test[NewBlobRequest]
  test[NewCommitRequest]
  test[NewGistRequest]
  test[NewIssueRequest]
  test[NewReleaseRequest]
  test[NewStatusRequest]
  test[NewTagRequest]
  test[NewTreeRequest]
  test[OAuthToken]
  test[Project]
  test[PullRequestBase]
  test[PullRequestFile]
  test[PullRequestReviewEvent]
  test[PullRequestReviewState]
  test[PullRequestReview]
  test[PullRequest]
  test[RefAuthor]
  test[RefCommit]
  test[RefInfo]
  test[RefObject]
  test[Ref]
  test[Release]
  test[RepoPermissions]
  test[RepositoryBase]
  test[Repository]
  test[RepositoryMinimal]
  test[ReviewersRequest]
  test[ReviewersResponse]
  test[BranchUpdateRequest]
  test[BranchUpdateResponse]
  test[CommitComparisonResponse]
  test[SearchIssuesResult]
  test[SearchReposResult]
  test[Stargazer]
  test[StarredRepository]
  test[StatusRepository]
  test[Status]
  test[SubscriptionRequest]
  test[Subscription]
  test[Tag]
  test[Team]
  test[TreeDataResult]
  test[TreeData]
  test[TreeResult]
  test[UpdateReferenceRequest]
  test[UserRepoPermission]
  test[User]
  test[WriteFileRequest]
  test[WriteFileResponse]
  test[WriteResponseCommit]
  test[SearchResultTextMatch]
  test[SearchResultTextMatchLocation]
  test[SearchCodeResult]
  test[SearchCodeResultItem]
}
