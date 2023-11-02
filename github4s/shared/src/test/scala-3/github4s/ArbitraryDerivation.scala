package github4s

import org.scalacheck.{Arbitrary, Gen}
import shapeless3.deriving.*
import scala.compiletime.*
import scala.collection.immutable.ArraySeq

object ArbitraryDerivation { self =>
  object semiauto:
    extension (A: Arbitrary.type)
      inline def derived[A]: Arbitrary[A] =
        summonInline[Arbitrary[A]]

  inline def deriveArb[A](using g: K0.Generic[A]): Arbitrary[A] =
    import semiauto._
    Arbitrary.derived[A]

  object auto {
    given deriveArb[A](using gen: DerivedGen[A]): Arbitrary[A] = gen.arb
  }

}

// Credit to Georgi Krastev on the typelevel discord;
// https://scastie.scala-lang.org/b8CXWfmXQUClqXplyA6TJg
opaque type DerivedGen[A] = Gen[A]
object DerivedGen extends DerivedGenInstances:
  given [A]: Conversion[Gen[A], DerivedGen[A]]            = identity
  extension [A](gen: DerivedGen[A]) def arb: Arbitrary[A] = Arbitrary(gen)

  private final class ArrayProduct(elems: Array[Any]) extends Product:
    def canEqual(that: Any): Boolean            = true
    def productElement(n: Int): Any             = elems(n)
    def productArity: Int                       = elems.length
    override def productIterator: Iterator[Any] = elems.iterator

  def product[A](arbs: Seq[Arbitrary[_]])(using gen: K0.ProductGeneric[A]): DerivedGen[A] =
    Gen
      .sequence[Array[Any], Any](arbs.map(_.arbitrary))
      .map(arr => gen.fromProduct(ArrayProduct(arr)).asInstanceOf[A])

  def coproduct[A](arbs: => IndexedSeq[Arbitrary[_ <: A]]): DerivedGen[A] =
    Gen.lzy(
      if arbs.isEmpty then Gen.fail else Gen.choose(0, arbs.length - 1).flatMap(arbs(_).arbitrary)
    )

sealed abstract class DerivedGenInstances:
  inline def summonAllArb[A, U](using gen: K0.Generic[A]): IndexedSeq[Arbitrary[_ <: U]] =
    ArraySeq.unsafeWrapArray(summonAsArray[K0.LiftP[Arbitrary, gen.MirroredElemTypes]]).asInstanceOf

  inline given derived[A](using gen: K0.Generic[A]): DerivedGen[A] =
    gen.derive(DerivedGen.product(summonAllArb[A, Any]), DerivedGen.coproduct(summonAllArb[A, A]))
