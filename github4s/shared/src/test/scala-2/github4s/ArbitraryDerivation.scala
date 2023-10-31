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
