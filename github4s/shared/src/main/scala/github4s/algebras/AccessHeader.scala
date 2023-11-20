package github4s.algebras

import github4s.GHResponse

trait AccessHeader[F[_]] {
  def withAccessHeader[T](f: Map[String, String] => F[GHResponse[T]]): F[GHResponse[T]]
}

object AccessHeader {
  def from[F[_]](accessToken: AccessToken[F]): AccessHeader[F] = new AccessHeader[F] {
    override def withAccessHeader[T](f: Map[String, String] => F[GHResponse[T]]): F[GHResponse[T]] =
      accessToken.withAccessToken { token =>
        f(token.fold(Map.empty[String, String])(t => Map("Authorization" -> s"token $t")))
      }
  }
}
