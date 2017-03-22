/*
 * Copyright (c) 2016 47 Degrees, LLC. <http://www.47deg.com>
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
 * the Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
 * FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
 * IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package github4s.jvm

import cats.instances.FutureInstances
import cats.{Eval, _}
import github4s.free.interpreters.Interpreters
import github4s.{EvalInstances, HttpRequestBuilderExtensionJVM, IdInstances}
import scala.concurrent.Future
import scalaj.http.HttpResponse
import scala.concurrent.ExecutionContext.Implicits.global
import github4s.implicits._

trait ImplicitsJVM
    extends IdInstances
    with EvalInstances
    with FutureInstances
    with HttpRequestBuilderExtensionJVM {

  implicit val intInstanceIdScalaJ = new Interpreters[Id, HttpResponse[String]]
  implicit val intInstanceEvalScalaJ =
    new Interpreters[Eval, HttpResponse[String]]
  implicit val intInstanceFutureScalaJ =
    new Interpreters[Future, HttpResponse[String]]

}
