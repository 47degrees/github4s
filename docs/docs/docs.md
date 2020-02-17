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

```scala mdoc:silent
import github4s.Github
```
```scala mdoc:invisible
val accessToken = sys.env.get("GITHUB4S_ACCESS_TOKEN")
```

In order for Github4s to work, you'll need an appropriate implicit `ExecutionContext` in Scope.

Github4s is a Tagless Final API.

Every Github4s API call returns an `F[GHResponse[A]]` where `F` has an instance of [cats.effect.ConcurrentEffect][cats-concurrent-effect]

`GHResponse[A]` is, in turn, a type alias for `Either[GHException, GHResult[A]]`.

`GHResult` contains the result `A` given by Github as well as the status code and headers of the
response:

```scala
case class GHResult[A](result: A, statusCode: Int, headers: Map[String, String])
```

As an introductory example, we can get a user with the following:

```scala mdoc:silent:fail
val user1 = Github[IO](accessToken).users.get("rafaparadela")
```

`user1` in this case is a `IO[GHResponse[User]]`.


### Using `cats.effect.IO`

```scala mdoc:compile-only
object ProgramIO {
  import cats.effect.IO
  import cats.effect.IO.contextShift
  import scala.concurrent.ExecutionContext.Implicits.global
  import github4s.Github
  
  implicit val IOContextShift = IO.contextShift(global)

  val u1 = Github[IO](accessToken).users.get("juanpedromoreno")
  u1.unsafeRunSync
}
```

### Using `monix.eval.Task`

```scala mdoc:silent:fail
object ProgramTask {
    import monix.execution.Scheduler.Implicits.global
    import monix.eval.Task
    import github4s.Github
    
    val u2 = Github[Task](accessToken).users.get("rafaparadela")
}
```

As mentioned above, `u2` should have an `Task[GHResponse[User]]`

```scala mdoc:silent:fail
import github4s.GithubResponses.GHResult

ProgramTask.u2.runAsync { result =>
  result match {
    case Right(GHResult(result, status@_, headers@_)) => doSomething
    case Left(e) => doSomething
  }
}

```

Support for `cats.Id` and `Future` are provided with `GithubIOSyntax` which contains syntax to lift from `IO`.

### Using `cats.Id`

```scala mdoc:compile-only
object ProgramId {
  import cats.effect.IO
  import cats.effect.IO.contextShift
  import scala.concurrent.ExecutionContext.Implicits.global
  import github4s.Github
  import github4s.GithubIOSyntax._
  
  implicit val IOContextShift = IO.contextShift(global)

  val u3 = Github[IO](accessToken).users.get("rafaparadela").toId
}
```

### Using `Future`

```scala mdoc:compile-only
object ProgramFuture {
  import cats.effect.IO
  import cats.effect.IO.contextShift
  import scala.concurrent.ExecutionContext.Implicits.global
  import scala.concurrent.duration._
  import scala.concurrent.Await
  import github4s.Github
  import github4s.GithubIOSyntax._

  implicit val IOContextShift = IO.contextShift(global)

  val u4 = Github[IO](accessToken).users.get("dialelo").toFuture
  Await.result(u4, 2.seconds)
}
```


## Specifying custom headers

Headers are an optional field for any Github API request:

```scala mdoc:silent:fail
object ProgramEvalWithHeaders {
  import monix.execution.Scheduler.Implicits.global
  import monix.eval.Task
  import github4s.Github
    
  val userHeaders = Map("user-agent" -> "github4s")
  val u5 = Github[Task](accessToken).users.get("rafaparadela", userHeaders)  
}
```

[access-token]: https://github.com/settings/tokens
[cats-concurrent-effect]: https://typelevel.org/cats-effect/typeclasses/concurrent-effect.html
[monix-task]: https://monix.io/docs/3x/eval/task.html