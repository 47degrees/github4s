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

Github4s uses [Tagless Final encoding](https://typelevel.org/blog/2017/12/27/optimizing-final-tagless.html).

Every Github4s API call returns an `F[GHResponse[A]]` where `F` has an instance of [cats.effect.Sync][cats-sync].

`GHResponse[A]` contains the result `A` given by Github (or an error) as well as the status code and
headers of the response:

```scala
final case class GHResponse[A](
    result: Either[GHException, A],
    statusCode: Int,
    headers: Map[String, String]
)
```

To make HTTP calls, Github4s relies on [an http4s' HTTP client][http4s-client] which needs to be
supplied as we'll see later. Here, we are making use of `JavaNetClientBuilder` because of its ease
of use in a REPL. However, for production use you should prefer `BlazeClientBuilder` over it as
detailed in [the documentation][http4s-client].

```scala mdoc:silent
import java.util.concurrent.Executors

import cats.effect.{Blocker, ContextShift, IO}
import org.http4s.client.{Client, JavaNetClientBuilder}

import scala.concurrent.ExecutionContext.global

val httpClient: Client[IO] = {
  val blockingPool = Executors.newFixedThreadPool(5)
  val blocker = Blocker.liftExecutorService(blockingPool)
  implicit val cs: ContextShift[IO] = IO.contextShift(global)
  JavaNetClientBuilder[IO](blocker).create // use BlazeClientBuilder for production use
}
```

As an introductory example, we can get a user with the following:

```scala mdoc:silent
import github4s.Github
val accessToken = sys.env.get("GITHUB4S_ACCESS_TOKEN")
val user1 = Github[IO](httpClient, accessToken).users.get("rafaparadela")
```

`user1` in this case is a `IO[GHResponse[User]]`.

### Using `F[_]: cats.effect.Sync`

Any type with a `cats.effect.Sync` instance can be used with this example, such as
`monix.eval.Task`.

```scala mdoc:compile-only
object ProgramF {
  import cats.effect.Sync
  import github4s.Github
  import github4s.GithubResponses.GHResponse
  import github4s.domain.User
  import org.http4s.client.Client

  def u1[F[_]: Sync](httpClient: Client[F]): F[GHResponse[User]] =
    Github[F](httpClient, accessToken).users.get("juanpedromoreno")
}
```

### Using `cats.effect.IO`

```scala mdoc:compile-only
object ProgramIO {
  import java.util.concurrent.Executors

  import cats.effect.{Blocker, ContextShift, IO}
  import github4s.Github
  import org.http4s.client.{Client, JavaNetClientBuilder}

  import scala.concurrent.ExecutionContext.global

  val httpClient: Client[IO] = {
    val blockingPool = Executors.newFixedThreadPool(5)
    val blocker = Blocker.liftExecutorService(blockingPool)
    implicit val cs: ContextShift[IO] = IO.contextShift(global)
    JavaNetClientBuilder[IO](blocker).create // use BlazeClientBuilder for production use
  }

  val u2 = Github[IO](httpClient, accessToken).users.get("juanpedromoreno")
  u2.unsafeRunSync()
}
```

### Using `cats.Id`

Support for `cats.Id` is provided with `GithubIOSyntax` which contains syntax to lift from `IO`.

```scala mdoc:compile-only
object ProgramId {
  import java.util.concurrent.Executors

  import cats.effect.{Blocker, ContextShift, IO}
  import github4s.Github
  import github4s.GithubIOSyntax._
  import org.http4s.client.{Client, JavaNetClientBuilder}

  import scala.concurrent.ExecutionContext.global

  val httpClient: Client[IO] = {
    val blockingPool = Executors.newFixedThreadPool(5)
    val blocker = Blocker.liftExecutorService(blockingPool)
    implicit val cs: ContextShift[IO] = IO.contextShift(global)
    JavaNetClientBuilder[IO](blocker).create // use BlazeClientBuilder for production use
  }

  val u4 = Github[IO](httpClient, accessToken).users.get("juanpedromoreno").toId
}
```

### Using `Future`

Support for `Future` is provided with `GithubIOSyntax` which contains syntax to lift from `IO`.

```scala mdoc:compile-only
object ProgramFuture {
  import java.util.concurrent.Executors

  import cats.effect.{Blocker, ContextShift, IO}
  import github4s.Github
  import github4s.GithubIOSyntax._
  import org.http4s.client.{Client, JavaNetClientBuilder}

  import scala.concurrent.Await
  import scala.concurrent.ExecutionContext.Implicits.global
  import scala.concurrent.duration._

  val httpClient: Client[IO] = {
    val blockingPool = Executors.newFixedThreadPool(5)
    val blocker = Blocker.liftExecutorService(blockingPool)
    implicit val cs: ContextShift[IO] = IO.contextShift(global)
    JavaNetClientBuilder[IO](blocker).create // use BlazeClientBuilder for production use
  }

  val u5 = Github[IO](httpClient, accessToken).users.get("juanpedromoreno").toFuture
  Await.result(u5, 2.seconds)
}
```


## Specifying custom headers

Headers are an optional field for any Github API request:

```scala mdoc:silent:fail
object ProgramEvalWithHeaders {
  import java.util.concurrent.Executors

  import cats.effect.Blocker
  import github4s.Github
  import monix.execution.Scheduler.Implicits.global
  import monix.eval.Task
  import org.http4s.client.{Client, JavaNetClientBuilder}

  val httpClient: Client[Task] = {
    val blockingPool = Executors.newFixedThreadPool(5)
    val blocker = Blocker.liftExecutorService(blockingPool)
    JavaNetClientBuilder[IO](blocker).create // use BlazeClientBuilder for production use
  }

  val userHeaders = Map("user-agent" -> "github4s")
  val u6 = Github[Task](accessToken).users.get("rafaparadela", userHeaders)
}
```

[access-token]: https://github.com/settings/tokens
[cats-sync]: https://typelevel.org/cats-effect/typeclasses/sync.html
[monix-task]: https://monix.io/docs/3x/eval/task.html
[http4s-client]: https://http4s.org/v0.21/client/
