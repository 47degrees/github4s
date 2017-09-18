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

import cats.data.EitherK
import github4s.free.algebra._

object app {
  type COGH01[A]   = EitherK[RepositoryOp, UserOp, A]
  type COGH02[A]   = EitherK[GistOp, COGH01, A]
  type COGH03[A]   = EitherK[IssueOp, COGH02, A]
  type COGH04[A]   = EitherK[AuthOp, COGH03, A]
  type COGH05[A]   = EitherK[GitDataOp, COGH04, A]
  type COGH06[A]   = EitherK[PullRequestOp, COGH05, A]
  type COGH07[A]   = EitherK[ActivityOp, COGH06, A]
  type GitHub4s[A] = EitherK[OrganizationOp, COGH07, A]
}
