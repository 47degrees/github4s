package github4s.utils

import org.scalajs.macrotaskexecutor.MacrotaskExecutor.Implicits

import scala.concurrent.ExecutionContext

object TestContext {
    // Why is this context distinct from the global execution context that Scala provides
    // out of the box?
    // In short, because browsers / JS are _very different_ from the JVM. Copying / pasting
    // from the macrotask executor's explanation of why that library exists in its README:
    // (https://github.com/scala-js/scala-js-macrotask-executor/tree/v1.0.0)
    // 
    // "Unless you have some very, very specific and unusual requirements, this is the optimal
    // ExecutionContext implementation for use in any Scala.js project.
    // If you're using ExecutionContext and not using this project, you likely have some serious
    // bugs and/or performance issues waiting to be discovered."
    // 
    // This library goes through the trouble of providing this special execution context and
    // reorganizing just to provide that context in tests to make it more obvious for downstream
    // JS consumers what to fix / how to make a pretty menacing warning go away.

    val context: ExecutionContext = Implicits.global
}