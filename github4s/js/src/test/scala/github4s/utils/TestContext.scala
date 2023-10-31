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
