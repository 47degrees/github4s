---
layout: docs
title: Getting Started
---

# Getting started

WIP: Import

```tut:silent
import github4s.Github
```

In order for github4s to work in both JVM and scala-js environments, you'll need to place different implicits in your scope, depending on your needs:

```tut:silent
import github4s.jvm.Implicits._
```

```tut:silent
// import github4s.js.Implicits._
```

```tut:invisible
val accessToken = sys.env.get("GITHUB4S_ACCESS_TOKEN")
```

WIP: Every Github4s api returns a `Free[GHResponse[A], A]` where `GHResponse[A]` is a type alias for `Either[GHException, GHResult[A]]`. GHResult contains the result `[A]` given by GitHub, but also the status code of the response and headers:

```scala
case class GHResult[A](result: A, statusCode: Int, headers: Map[String, IndexedSeq[String]])
```

To get an user

```tut:silent
val user1 = Github(accessToken).users.get("rafaparadela")
```

user1 in this case `Free[GHException Xor GHResult[User], User]` and we can run (`foldMap`) with `exec[M[_], C]` where `M[_]` represent any type container that implements `MonadError[M, Throwable]`, for instance `cats.Eval`; and C represents a valid implementation of an HttpClient. The previously mentioned implicit classes carry already set up instances for working with `scalaj` (for JVM-compatible apps) and `roshttp` (for scala-js-compatible apps). Take into account that in the latter case, you can only use `Future` in the place of `M[_]`:

```tut:silent
import cats.Eval
import github4s.Github._
import scalaj.http._

object ProgramEval {
    val u1 = user1.exec[Eval, HttpResponse[String]]().value
}

```

WIP: As mentioned above `u1` should have an `GHResult[User]` in the right.

```tut:invisible
import cats.implicits._
import github4s.GithubResponses.GHResult
```

```tut:book
ProgramEval.u1 match {
  case Right(GHResult(result, status, headers)) => result.login
  case Left(e) => e.getMessage
}
```

WIP:  With `Id`

```tut:silent
import cats.Id
import scalaj.http._

object ProgramId {
    val u2 = Github(accessToken).users.get("raulraja").exec[Id, HttpResponse[String]]()
}
```

WIP: With `Future`

```tut:silent
import cats.Id
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._
import scala.concurrent.Await
import scalaj.http._

object ProgramFuture {
    // execFuture[C] is a convenience access to exec[Future, C]
    val u3 = Github(accessToken).users.get("dialelo").execFuture[HttpResponse[String]]()
    Await.result(u3, 2.seconds)
}
```

WIP: With `scalaz.Task`

```tut:silent
import scalaz.concurrent.Task
import github4s.scalaz.implicits._
import scalaj.http._
import github4s.jvm.Implicits._

object ProgramTask {
    val u4 = Github(accessToken).users.get("franciscodr").exec[Task, HttpResponse[String]]()
    u4.unsafePerformSyncAttempt
}
```

```tut:invisible
import org.scalatest._
import Matchers._
import cats.Eval
import cats.implicits._
import github4s.Github
import github4s.Github._
import github4s.jvm.Implicits._
import scalaj.http._

val accessToken = sys.env.get("GITHUB4S_ACCESS_TOKEN")
```

```tut:book
object ProgramEval {
    // exec methods let users to include optional headers for any GitHub API request:
    val userHeaders = Map("user-agent" -> "github4s")
    val user1 = Github(accessToken).users.get("rafaparadela").exec[Eval, HttpResponse[String]](userHeaders).value
}

ProgramEval.user1 should matchPattern { case Right(GHResult(_, 200, _)) => }
ProgramEval.user1.toOption map (_.result.login shouldBe "rafaparadela")
```

# Test credentials

Note that for github4s to have access to the GitHub API during the test phases, you need to provide a valid access token with the right credentials (i.e.: users + gists scopes), through the sbt configuration variable "token":

```
sbt -Dtoken=ACCESS_TOKEN_STRING
```
