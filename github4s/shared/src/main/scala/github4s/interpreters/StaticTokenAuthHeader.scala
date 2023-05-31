package github4s.interpreters

import cats.Applicative
import cats.syntax.all._
import github4s.algebras.AuthHeader

case class StaticTokenAuthHeader[F[_]: Applicative](accessToken: Option[String])
    extends AuthHeader[F] {
  override def header: F[Map[String, String]] =
    accessToken
      .fold(Map.empty[String, String])(token => Map("Authorization" -> s"token $token"))
      .pure[F]
}

object StaticTokenAuthHeader {
  def noToken[F[_]: Applicative] = StaticTokenAuthHeader[F](none[String])
}
