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

package github4s.free.algebra

import cats.free.{Free, Inject}
import github4s.GithubResponses._
import github4s.free.domain._

/**
 * Notifications ops ADT
 */
sealed trait NotificationOp[A]

final case class SetThreadSub(
    id: Int,
    subscribed: Boolean,
    ignored: Boolean,
    accessToken: Option[String] = None)
    extends NotificationOp[GHResponse[Subscription]]

/**
 * Exposes Notification operations as a Free monadic algebra that may be combined with other Algebras via
 * Coproduct
 */
class NotificationOps[F[_]](implicit I: Inject[NotificationOp, F]) {

  def setThreadSub(
      id: Int,
      subscribed: Boolean,
      ignored: Boolean,
      accessToken: Option[String] = None): Free[F, GHResponse[Subscription]] =
    Free.inject[NotificationOp, F](SetThreadSub(id, subscribed, ignored, accessToken))
}

/**
 * Default implicit based DI factory from which instances of the NotificationOps may be obtained
 */
object NotificationOps {

  implicit def instance[F[_]](implicit I: Inject[NotificationOp, F]): NotificationOps[F] =
    new NotificationOps[F]

}
