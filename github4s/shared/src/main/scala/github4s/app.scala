/*
 * Copyright 2016-2017 47 Degrees, LLC. <http://www.47deg.com>
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

import cats.data.Coproduct
import github4s.free.algebra._

object app {
  type COGH01[A]   = Coproduct[RepositoryOp, UserOp, A]
  type COGH02[A]   = Coproduct[GistOp, COGH01, A]
  type COGH03[A]   = Coproduct[IssueOp, COGH02, A]
  type COGH04[A]   = Coproduct[AuthOp, COGH03, A]
  type COGH05[A]   = Coproduct[GitDataOp, COGH04, A]
  type COGH06[A]   = Coproduct[PullRequestOp, COGH05, A]
  type GitHub4s[A] = Coproduct[StatusOp, COGH06, A]
}
