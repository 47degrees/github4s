---
layout: docs
title: Getting Started
permalink: docs
---

# Getting started

## API token

In order to access the Github API, you will need to have [an access token][access-token] with the
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
`exec[M[_], C]` where `M[_]` that represents any type container that implements
`MonadError[M, Throwable]` (for instance `cats.Eval`) and `C` represents a valid implementation of
an [HttpClient][http-client].

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

### Using `cats.effect.{Async, Sync}`

On the JVM, you can use any `cats.effect.Sync`, here we're using `cats.effect.IO`:
```tut:silent
import cats.effect.IO
import github4s.cats.effect.jvm.Implicits._

object ProgramTask {
  val u5 = Github(accessToken).users.get("juanpedromoreno").exec[IO, HttpResponse[String]]()
  u5.unsafeRunSync
}
```

Note that you'll need a dependency to the `github4s-cats-effect` package to leverage
cats-effect integration.

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
[access-token]: https://github.com/settings/tokens
