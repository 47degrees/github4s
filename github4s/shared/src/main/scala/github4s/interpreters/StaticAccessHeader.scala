package github4s.interpreters

import github4s.GHResponse
import github4s.algebras.AccessHeader

case class StaticAccessHeader[F[_]](accessHeader: Map[String, String]) extends AccessHeader[F] {
  override def withAccessHeader[T](f: Map[String, String] => F[GHResponse[T]]): F[GHResponse[T]] =
    f(accessHeader)
}

object StaticAccessHeader {
  def noHeader[F[_]] = new StaticAccessHeader[F](Map.empty)
}
