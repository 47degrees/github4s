package github4s.algebras

trait AuthHeader[F[_]] {
  def header: F[Map[String, String]]
}
