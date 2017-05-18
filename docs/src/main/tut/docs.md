---
layout: docs
title: Getting Started
---

# Getting started

## API token

In order to access the Github API you will need to have [an access token][access-token] with the
appropriate scopes (i.e. if you want to create gists, your token will need to have the gist scope).

## Github4s

First things first, we'll need to import `github4s.Github` which is the entry point to the Github
API in Github4s.

```tut:silent
import github4s.Github
```

In order for Github4s to work in both JVM and scala-js environments, you'll need to place different
implicits in your scope, depending on your needs:

```tut:silent
import github4s.jvm.Implicits._
// import github4s.js.Implicits._
```

```tut:invisible
val accessToken = sys.env.get("GITHUB4S_ACCESS_TOKEN")
```

Every Github4s API call returns a `GHIO[GHResponse[A]]` which is an alias for
`Free[Github4s, GHResponse[A]]`.

`GHResponse[A]` is, in turn, a type alias for `Either[GHException, GHResult[A]]`.

`GHResult` contains the result `A` given by Github as well as the status code and headers of the
response:

```scala
case class GHResult[A](result: A, statusCode: Int, headers: Map[String, IndexedSeq[String]])
```

As an introductory example, we can get a user with the following:

```tut:silent
val user1 = Github(accessToken).users.get("rafaparadela")
```

`user1` in this case is a `GHIO[GHResponse[User]]` which we can run (`foldMap`) with
`exec[M[_], C]` where `M[_]` represents any type container that implements
`MonadError[M, Throwable]` (for instance `cats.Eval`) and `C` represents a valid implementation of
an [HttpClient][http-client].

The previously mentioned implicit classes carry out of the box
instances for working with [scalaj][scalaj] (for JVM-compatible apps) and [roshttp][roshttp] (for
scala-js-compatible apps). Take into account that in the latter case, you can only use `Future` in
the place of `M[_]`.

A few examples follow with different `MonadError[M, Throwable]`.

### Using `cats.Eval`

```tut:silent
import cats.Eval
import github4s.Github._
import scalaj.http.HttpResponse

object ProgramEval {
  val u1 = user1.exec[Eval, HttpResponse[String]]().value
}
```

As mentioned above, `u1` should have an `GHResult[User]` in the right.

```tut:silent
import cats.implicits._
import github4s.GithubResponses.GHResult

ProgramEval.u1 match {
  case Right(GHResult(result, status, headers)) => result.login
  case Left(e) => e.getMessage
}
```

### Using `cats.Id`

```tut:silent
import cats.Id

object ProgramId {
  val u2 = Github(accessToken).users.get("raulraja").exec[Id, HttpResponse[String]]()
}
```

### Using `Future`

```tut:silent
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._
import scala.concurrent.Await

object ProgramFuture {
  // execFuture[C] is a shortcut for exec[Future, C]
  val u3 = Github(accessToken).users.get("dialelo").execFuture[HttpResponse[String]]()
  Await.result(u3, 2.seconds)
}
```

### Using `scalaz.Task`

```tut:silent
import scalaz.concurrent.Task
import github4s.scalaz.implicits._
import scalaj.http.HttpResponse

object ProgramTask {
  val u4 = Github(accessToken).users.get("franciscodr").exec[Task, HttpResponse[String]]()
  u4.unsafePerformSyncAttempt
}
```

Note that you'll need a dependency to the `github4s-scalaz` pacakge to leverage `scalaz.Task`.

## Specifying custom headers

The different `exec` methods let users include custom headers for any Github API request:

```tut:silent
object ProgramEval {
  val userHeaders = Map("user-agent" -> "github4s")
  val user1 = Github(accessToken).users.get("rafaparadela").exec[Eval, HttpResponse[String]](userHeaders).value
}
```

[http-client]: https://github.com/47deg/github4s/blob/master/github4s/shared/src/main/scala/github4s/HttpClient.scala
[scalaj]: https://github.com/scalaj/scalaj-http
[roshttp]: https://github.com/hmil/RosHTTP
[access-token]: https://github.com/settings/tokens
