---
layout: docs
title: Getting Started
permalink: docs
---

# Getting started

## API token

In order to access the Github API, you will need to have [an access token][access-token] with the
appropriate scopes (i.e. if you want to create gists, your token will need to have the gist scope).

A personal token can be valid forever. However, the ones issued for [Github Apps][github-app] have to be re-issued every hour. 
To solve that issue, github4s provides [AccessToken][access-token-scala] which you can hook into to provide authentication for your app using third-party libraries such as JWT implementations, for example.
You can find the default implementation for static tokens in [StaticAccessToken][static-access-token-scala].

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
import cats.effect.IO
import org.http4s.client.{Client, JavaNetClientBuilder}

val httpClient: Client[IO] = {
  JavaNetClientBuilder[IO].create // You can use any http4s backend
}
```

As an introductory example, we can get a user with the following:

```scala mdoc:silent
import github4s.Github
val accessToken = sys.env.get("GITHUB_TOKEN")
val user1 = Github[IO](httpClient, accessToken).users.get("rafaparadela")
```

`user1` in this case is a `IO[GHResponse[User]]`.

## Error handling

Depending on the response issued by GitHub, you might find yourself in the unhappy path of
`GHResponse`. In this case you will have a `Left` as `GHResponse#result` which contains a `GHError`.

The `GHError` ADT defines different cases based on the response's status code:
- 400 maps to `BadRequestError`
- 401   ->    `UnauthorizedError`
- 403   ->    `ForbiddenError`
- 404   ->    `NotFoundError`
- 422   ->    `UnprocessableEntityError`
- 423   ->    `RateLimitExceededError`

Thanks to these, you can fine-grain your logic according to your needs. E.g. if you're hitting a
`RateLimitExceededError`, it might be worth it to inspect the headers and wait accordingly before
retrying your request.

We support an extensive set of errors. However, since GitHub's documentation regarding
errors is sparse, it's definitely possible, or rather extremly likely, that this set of supported
errors is not exhaustive.

If you find an unsupported error, which translates into either:
- `UnhandledResponseError` which corresponds to a status code which was not handled, or
- `JsonParsingError` which indicates that the JSON sent back by GitHub couldn't be decoded into
the case classes defined by github4s,
please create an issue at https://github.com/47degrees/github4s/issues.

## Using different effect types

Github4s supports different effect types which we will go through next.

### Using `F[_]: cats.effect.Concurrent`

Any type with a `cats.effect.Concurrent` instance can be used with this example

```scala mdoc:compile-only
object ProgramF {
  import cats.effect.Concurrent
  import github4s.Github
  import github4s.GHResponse
  import github4s.domain.User
  import org.http4s.client.Client

  def u1[F[_]: Concurrent](httpClient: Client[F]): F[GHResponse[User]] =
    Github[F](httpClient, accessToken).users.get("juanpedromoreno")
}
```

### Using `cats.effect.IO`

```scala mdoc:compile-only
import cats.effect.IO
import cats.effect.unsafe.implicits.global  
import github4s.Github
import org.http4s.client.{Client, JavaNetClientBuilder}

object ProgramIO {

  val httpClient: Client[IO] = JavaNetClientBuilder[IO].create // You can use any http4s backend

  val u2 = Github[IO](httpClient, accessToken).users.get("juanpedromoreno")
  u2.unsafeRunSync()
}
```

### Using `Future`

```scala mdoc:compile-only
object ProgramFuture {
  import cats.effect.IO
  import cats.effect.unsafe.implicits.global
  import github4s.Github
  import org.http4s.client.{Client, JavaNetClientBuilder}

  import scala.concurrent.Await
  import scala.concurrent.duration._

  val httpClient: Client[IO] = JavaNetClientBuilder[IO].create // You can use any http4s backend

  val u5 = Github[IO](httpClient, accessToken).users.get("juanpedromoreno").unsafeToFuture()
  Await.result(u5, 2.seconds)
}
```

When using capability traits (aka "tagless final") style, you can convert any `Async[F]` to a Future using [`Dispatcher`][dispatcher]

## Using github4s with GitHub Enterprise

By default `Github` instances are configured for the [public GitHub][public-github] endpoints via a fallback
`GithubConfig` instance which is picked up by the `Github` constructor if there's no other `GithubConfig` in the scope.

It is also possible to pass a custom GitHub configuration (e.g. for a particular [GitHub Enterprise][github-enterprise]
server). To override the default configuration values declare a custom `GithubConfig` instance in an appropriate
scope:

```scala mdoc:nest:silent
import github4s.{Github, GithubConfig}

implicit val config: GithubConfig = GithubConfig(
  baseUrl = "",        // default: "https://api.github.com/"
  authorizeUrl = "",   // default: "https://github.com/login/oauth/authorize?client_id=%s&redirect_uri=%s&scope=%s&state=%s"
  accessTokenUrl = "", // default: "https://github.com/login/oauth/access_token"
  headers = Map.empty  // default: Map("User-Agent" -> "github4s")
)

val github = Github[IO](httpClient, None)
```
Please refer your GitHub Enterprise server docs for exact URL values for `baseUrl`, `authorizeUrl` and `accessTokenUrl`.

## Specifying custom headers

Headers are an optional field for any Github API request:

```scala mdoc:silent:fail
object ProgramEvalWithHeaders {

  import cats.effect.IO
  import github4s.Github
  import org.http4s.client.{Client, JavaNetClientBuilder}

  val httpClient: Client[IO] = JavaNetClientBuilder[IO].create // You can use any http4s backend

  val userHeaders = Map("user-agent" -> "github4s")
  val u6 = Github[IO](accessToken).users.get("rafaparadela", userHeaders)
}
```

Additionally, thanks to the aforementioned `GithubConfig`, it is also possible to specify custom
headers which will be added to every request sent to the GitHub API. The user agent `github4s` is
added by default.

[access-token]: https://github.com/settings/tokens
[github-app]: https://github.com/settings/apps
[access-token-scala]: https://github.com/47degrees/github4s/blob/main/github4s/src/main/scala/github4s/algebras/AccessToken.scala
[access-token-scala]: https://github.com/47degrees/github4s/blob/main/github4s/src/main/scala/github4s/interpreters/StaticAccessToken.scala
[cats-sync]: https://typelevel.org/cats-effect/typeclasses/sync.html
[dispatcher]: https://typelevel.org/cats-effect/docs/std/dispatcher
[http4s-client]: https://http4s.org/v0.23/client/
[public-github]: https://github.com
[github-enterprise]: https://github.com/enterprise
