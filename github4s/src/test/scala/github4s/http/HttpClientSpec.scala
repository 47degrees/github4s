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

package github4s.http

import cats.effect.IO
import fs2.Stream
import github4s.GHError._
import io.circe.{DecodingFailure, Encoder, Json}
import io.circe.CursorOp.DownField
import io.circe.syntax._
import io.circe.generic.auto._
import org.http4s.{EntityEncoder, Response, Status}
import org.http4s.circe._
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class HttpClientSpec extends AnyFlatSpec with Matchers {
  case class Foo(a: Int)

  "buildResponse" should "build a right if the response is successful and can be decoded" in {
    val response = Response[IO](body = createBody(Foo(3).asJson))
    val res      = HttpClient.buildResponse[IO, Foo](response)
    res.unsafeRunSync() shouldBe Right(Foo(3))
  }

  it should "build a left if 400" in {
    val bre      = BadRequestError("msg")
    val response = Response[IO](status = Status(400), body = createBody(bre.asJson))
    val res      = HttpClient.buildResponse[IO, Foo](response)
    res.unsafeRunSync() shouldBe Left(bre)
  }

  it should "build a left if 401" in {
    val ue       = UnauthorizedError("msg", "")
    val response = Response[IO](status = Status(401), body = createBody(ue.asJson))
    val res      = HttpClient.buildResponse[IO, Foo](response)
    res.unsafeRunSync() shouldBe Left(ue)
  }

  it should "build a left if 403" in {
    val fe       = ForbiddenError("msg", "")
    val response = Response[IO](status = Status(403), body = createBody(fe.asJson))
    val res      = HttpClient.buildResponse[IO, Foo](response)
    res.unsafeRunSync() shouldBe Left(fe)
  }

  it should "build a left if 404" in {
    val nfe       = NotFoundError("msg", "")
    val response1 = Response[IO](status = Status(404), body = createBody(nfe.asJson))
    val res1      = HttpClient.buildResponse[IO, Foo](response1)
    res1.unsafeRunSync() shouldBe Left(nfe)

    implicit val basicErrorEncoder: Encoder[BasicError] = new Encoder[BasicError] {
      final override def apply(b: BasicError): Json = Json.obj(("error", Json.fromString(b.message)))
    }
    val be        = BasicError("not found")
    val response2 = Response[IO](status = Status(404), body = createBody(be.asJson))
    val res2      = HttpClient.buildResponse[IO, Foo](response2)
    res2.unsafeRunSync() shouldBe Left(be)
  }

  it should "build a left if 422" in {
    val uee =
      UnprocessableEntityError("msg", List(UnprocessableEntity("res", "field", ErrorCode.Custom)))
    val response = Response[IO](status = Status(422), body = createBody(uee.asJson))
    val res      = HttpClient.buildResponse[IO, Foo](response)
    res.unsafeRunSync() shouldBe Left(uee)
  }

  it should "build a left if 423" in {
    val rlee     = RateLimitExceededError("msg", "")
    val response = Response[IO](status = Status(423), body = createBody(rlee.asJson))
    val res      = HttpClient.buildResponse[IO, Foo](response)
    res.unsafeRunSync() shouldBe Left(rlee)
  }

  it should "build a left if unhandled status code" in {
    val rlee     = RateLimitExceededError("msg", "")
    val response = Response[IO](status = Status(402), body = createBody(rlee.asJson))
    val res      = HttpClient.buildResponse[IO, Foo](response)
    res.unsafeRunSync() shouldBe Left(
      UnhandledResponseError("Unhandled status code 402", rlee.asJson.noSpaces)
    )
  }

  it should "build a left if it couldn't decode an A" in {
    val rlee     = RateLimitExceededError("msg", "")
    val response = Response[IO](body = createBody(rlee.asJson))
    val res      = HttpClient.buildResponse[IO, Foo](response)
    res.unsafeRunSync() shouldBe Left(
      JsonParsingError(
        s"Invalid message body: Could not decode JSON: ${rlee.asJson.spaces2}",
        Some(DecodingFailure("Attempt to decode value on failed cursor", List(DownField("a"))))
      )
    )
  }

  it should "build a left if it couldn't decode a GHError" in {
    val foo      = Foo(3)
    val response = Response[IO](status = Status(401), body = createBody(foo.asJson))
    val res      = HttpClient.buildResponse[IO, Foo](response)
    res.unsafeRunSync() shouldBe Left(
      JsonParsingError(
        s"Invalid message body: Could not decode JSON: ${foo.asJson.spaces2}",
        Some(
          DecodingFailure("Attempt to decode value on failed cursor", List(DownField("message")))
        )
      )
    )
  }

  def createBody[F[_]](js: Json): Stream[F, Byte] =
    implicitly[EntityEncoder[F, Json]].toEntity(js).body
}
