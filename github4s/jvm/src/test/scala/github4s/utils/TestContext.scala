package github4s.utils

import scala.concurrent.ExecutionContext
import scala.concurrent.ExecutionContext.Implicits.global

object TestContext {
    // See TestContext in JS test code for an explanation of why this value
    // is living off in an object
    val context: ExecutionContext = global
}