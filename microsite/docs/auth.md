---
layout: docs
title: Authorization API
permalink: auth
---

# Authorization API

Github4s supports the [Authorization API](https://developer.github.com/v3/oauth_authorizations/). As a result,
with Github4s, you can:

- [Authorize a url](#authorize-a-url)
- [Get an access token](#get-an-access-token)

Previously, you were able to use the authorizations API with a username and password.
This has been removed from the GitHub API as of November 13, 2020, and has also been removed from Github4s.
For more information, [see this documentation notice](https://docs.github.com/en/free-pro-team@latest/rest/overview/other-authentication-methods#via-username-and-password).

The following examples assume the following code:

```scala mdoc:silent
import java.util.concurrent._

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

val gh = Github[IO](httpClient, None)
```

**NOTE**: Above you can see `Github(httpClient, None)`. This is due to the fact that if you are
authenticating for the first time you don't have any access token yet.

### Authorize a url

Generates an authorize url with a random state, both are returned within an [Authorize][auth-scala].

You can authorize a url using `authorizeUrl`; it takes as arguments:

- `client_id`: the 20 character OAuth app client key for which to create the token.
- `redirect_uri`: the URL in your app where users will be sent to after authorization.
- `scopes`: attached to the token, for more information see [the scopes doc](https://developer.github.com/v3/oauth/#scopes).

```scala mdoc:compile-only
val authorizeUrl = gh.auth.authorizeUrl(
  "e8e39175648c9db8c280",
  "http://localhost:9000/_oauth-callback",
  List("public_repo"))
val response = authorizeUrl.unsafeRunSync()
response.result match {
  case Left(e) => println(s"Something went wrong: ${e.getMessage}")
  case Right(r) => println(r)
}
```

The `result` on the right is the created [Authorize][auth-scala].

See [the API doc](https://developer.github.com/v3/oauth/#web-application-flow) for full reference.


### Get an access token

Requests an access token based on the code retrieved in the [Create a new authorization token](#create-a-new-authorization-token) step of the OAuth process.

You can get an access token using `getAccessToken`; it takes as arguments:

- `client_id`: the 20 character OAuth app client key for which to create the token.
- `client_secret`: the 40 character OAuth app client secret for which to create the token.
- `code`: the code you received as a response to [Create a new authorization token](#create-a-new-authorization-token).
- `redirect_uri`: the URL in your app where users will be sent after authorization.
- `state`: the unguessable random string you optionally provided in [Create a new authorization token](#create-a-new-authorization-token).

```scala mdoc:compile-only
val getAccessToken = gh.auth.getAccessToken(
  "e8e39175648c9db8c280",
  "1234567890",
  "code",
  "http://localhost:9000/_oauth-callback",
  "status")
val response = getAccessToken.unsafeRunSync()
response.result match {
  case Left(e) => println(s"Something went wrong: ${e.getMessage}")
  case Right(r) => println(r)
}
```

The `result` on the right is the corresponding [OAuthToken][auth-scala].

See [the API doc](https://developer.github.com/v3/oauth/#web-application-flow) for full reference.

As you can see, a few features of the authorization endpoint are missing.

As a result, if you'd like to see a feature supported, feel free to create an issue and/or a pull request!

[auth-scala]: https://github.com/47degrees/github4s/blob/master/github4s/src/main/scala/github4s/domain/Authorization.scala
