package com.fortysevendeg.github4s

import cats.data.Xor
import cats.free.Free
import cats.syntax.xor._
import com.fortysevendeg.github4s.app.GitHub4s
import io.circe.Decoder
import io.circe.parser._
import io.circe.generic.auto._
import scalaj.http.HttpResponse


object GithubResponses {

  type GHIO[A] = Free[GitHub4s, A]

  type GHResponse[A] = GHException Xor GHResult[A]


  sealed trait GHResult[A]

  final case class GHItemResult[A](value: A, statusCode: Int, headers: Map[String, IndexedSeq[String]]) extends GHResult[A]

  final case class GHListResult[A](value: A, statusCode: Int, headers: Map[String, IndexedSeq[String]], decoder: Decoder[A]) extends GHResult[A]


  sealed abstract class GHException(msg : String, cause : Option[Throwable] = None) extends Throwable(msg) {
    cause foreach initCause
  }

  case class JsonParsingException(msg : String) extends GHException(msg)

  case class UnexpectedException(msg : String) extends GHException(msg)


  def toEntity[A](response: HttpResponse[String], d: Decoder[A]): GHResponse[A] = {

    response match {
      case r if r.isSuccess => {
        implicit val D: Decoder[A] = d
        decode[A](r.body).fold(e => JsonParsingException(e.getMessage).left[GHResult[A]], (result) => {
          result match {
            case Nil => Xor.Right(GHListResult(result, r.code, toLowerCase(r.headers), d))
            case _ :: _ => Xor.Right(GHListResult(result, r.code, toLowerCase(r.headers), d))
            case _ => Xor.Right(GHItemResult(result, r.code, toLowerCase(r.headers)))
          }
        })
      }
      case r => UnexpectedException(s"Failed invoking get with status : ${r.code}, body : \n ${r.body}").left[GHResult[A]]
    }
  }

  def toEmpty(response: HttpResponse[String]): GHResponse[Unit] = response match {
    case r if r.isSuccess => Xor.Right(GHItemResult(Unit, r.code, toLowerCase(r.headers)))
    case r => UnexpectedException(s"Failed invoking get with status : ${r.code}, body : \n ${r.body}").left[GHResult[Unit]]
  }

  private def toLowerCase(headers: Map[String, IndexedSeq[String]]): Map[String, IndexedSeq[String]] = headers.map(e => (e._1.toLowerCase, e._2))

  implicit class GHEntity[A](result: GHResult[A]) {
    def entity[A] = result match {
      case GHListResult(r, _, _, _) => r
      case GHItemResult(r, _, _) => r
    }

    def statusCode[A] = result match {
      case GHListResult(_, sc, _, _) => sc
      case GHItemResult(_, sc, _) => sc
    }

  }

}