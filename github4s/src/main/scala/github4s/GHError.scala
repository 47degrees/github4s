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

import io.circe.Decoder
import io.circe.generic.semiauto._

/**
  * Top-level exception returned by github4s when an error occurred.
  * @param message that is common to all exceptions
  */
sealed abstract class GHError(
    message: String
) extends Exception {
  final override def fillInStackTrace(): Throwable = this
  final override def getMessage: String            = message
}

object GHError {
  /**
    * Fallback exception, used when a more specific error couldn't be found.
    * @param message placholder message
    * @param json body of the response
    */
  final case class UnknownError(
      message: String,
      json: String
  ) extends GHError(message)

  /**
    * Corresponds to a 400 status code.
    * @param message that was given in the response body
    */
  final case class BadRequestError(
      message: String
  ) extends GHError(message)
  object BadRequestError {
    private[github4s] implicit val badRequestErrorDecoder: Decoder[BadRequestError] =
      deriveDecoder[BadRequestError]
  }

  /**
    * Corresponds to a 401 status code
    * @param message that was given in the response body
    * @param documentation_url associated documentation URL for this endpoint
    */
  final case class UnauthorizedError(
      message: String,
      documentation_url: String
  ) extends GHError(message)
  object UnauthorizedError {
    private[github4s] implicit val unauthorizedErrorDecoder: Decoder[UnauthorizedError] =
      deriveDecoder[UnauthorizedError]
  }

  /**
    * Corresponds to a 403 status code
    * @param message that was given in the response body
    * @param documentation_url associated documentation URL for this endpoint
    */
  final case class ForbiddenError(
      message: String,
      documentation_url: String
  ) extends GHError(message)
  object ForbiddenError {
    private[github4s] implicit val forbiddenErrorDecoder: Decoder[ForbiddenError] =
      deriveDecoder[ForbiddenError]
  }

  /**
    * Corresponds to a 404 status code
    * @param message that was given in the response body
    * @param documentation_url associated documentation URL for this endpoint
    */
  final case class NotFoundError(
      message: String,
      documentation_url: String
  ) extends GHError(message)
  object NotFoundError {
    private[github4s] implicit val notFoundErrorDecoder: Decoder[NotFoundError] =
      deriveDecoder[NotFoundError]
  }

  sealed trait ErrorCode
  object ErrorCode {
    final case object MissingResource       extends ErrorCode
    final case object MissingField          extends ErrorCode
    final case object InvalidFormatting     extends ErrorCode
    final case object ResourceAlreadyExists extends ErrorCode
    final case object Custom                extends ErrorCode
    private[github4s] implicit val errorCodeDecoder: Decoder[ErrorCode] =
      Decoder.decodeString.map {
        case "missing" => MissingResource
        case "missing_field" => MissingField
        case "invalid" => InvalidFormatting
        case "already_exists" => ResourceAlreadyExists
        case _ => Custom
      }
  }
  /**
    * Error given when a 422 status code is returned
    * @param resource github resource on which the error occurred (issue, pull request, etc)
    * @param field for which the error occurred
    * @param code error code to debug the problem
    */
  final case class UnprocessableEntity(
      resource: String,
      field: String,
      code: ErrorCode
  )
  object UnprocessableEntity {
    private[github4s] implicit val unprocessableEntityDecoder: Decoder[UnprocessableEntity] =
      deriveDecoder[UnprocessableEntity]
  }
  /**
    * Corresponds to a 422 status code
    * @param message that was given in the response body
    * @param errors list of validation errors
    */
  final case class UnprocessableEntityError(
      message: String,
      errors: List[UnprocessableEntityError]
  ) extends GHError(message)
  object UnprocessableEntityError {
    private[github4s] implicit val uEntityErrorDecoder: Decoder[UnprocessableEntityError] =
      deriveDecoder[UnprocessableEntityError]
  }

  /**
    * Corresponds to a 423 status code
    * @param message that was given in the response body
    * @param documentation_url associated documentation URL for this endpoint
    */
  final case class RateLimitExceededError(
      message: String,
      documentation_url: String
  ) extends GHError(message)
  object RateLimitExceededError {
    private[github4s] implicit val rleErrorDecoder: Decoder[RateLimitExceededError] =
      deriveDecoder[RateLimitExceededError]
  }
}
