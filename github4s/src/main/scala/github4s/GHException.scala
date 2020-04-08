/*
 * Copyright 2016-2020 47 Degrees, LLC. <http://www.47deg.com>
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

sealed abstract class GHException(
    message: String
) extends Exception {
  final override def fillInStackTrace(): Throwable = this
  final override def getMessage: String            = message
}

object GHException {
  final case class UnknownException(
      message: String,
      json: String
  ) extends GHException(message)

  final case class RateLimitExceededException(
      message: String,
      documentation_url: String
  ) extends GHException(message)

  final case class UnauthorizedException(
      message: String,
      documentation_url: String
  ) extends GHException(message)

  final case class NotFoundException(
      message: String,
      documentation_url: String
  ) extends GHException(message)

  final case class ForbiddenException(
      message: String,
      documentation_url: String
  )

  final case class BadRequestException(
      message: String
  ) extends GHException(message)

  sealed trait ErrorCode
  object ErrorCode {
    final case object Missing       extends ErrorCode
    final case object MissingField  extends ErrorCode
    final case object Invalid       extends ErrorCode
    final case object AlreadyExists extends ErrorCode
  }
  final case class UnprocessableEntityError(
      resource: String,
      field: String,
      code: ErrorCode
  )
  final case class UnprocessableEntityException(
      message: String,
      errors: List[UnprocessableEntityError]
  ) extends GHException(message)
}
