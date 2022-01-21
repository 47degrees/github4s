package github4s

import org.scalacheck.Arbitrary
import org.scalacheck.derive.MkArbitrary

// Note: This is in scala-2 source dir because shapeless isn't available for scala3. The scala3 derivation method would use another mechanism
object ArbitraryDerivation { self =>

  def deriveArb[A](implicit mkArbitrary: MkArbitrary[A]): Arbitrary[A] = mkArbitrary.arbitrary

  object auto {
    implicit def deriveArb[A: MkArbitrary]: Arbitrary[A] = self.deriveArb[A]
  }
}
