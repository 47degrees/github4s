---
layout: docs
title: Authorization API
permalink: auth
---

# Authorization API

Github4s supports the [Authorization API](https://developer.github.com/v3/oauth_authorizations/). As a result,
with Github4s, you can:

- [Create a new authorization token](#create-a-new-authorization-token)
- [Authorize a url](#authorize-a-url)
- [Get an access token](#get-an-access-token)

The following examples assume the following imports:

```scala mdoc:silent
import github4s.Github
import github4s.GithubIOSyntax._
import cats.effect.IO
import scala.concurrent.ExecutionContext.Implicits.global

implicit val IOContextShift = IO.contextShift(global)
```
They also make use of `cats.Id`, but any type container `F` implementing `ConcurrentEffect` will do.

LiftIO syntax for `cats.Id` and `Future` are provided in `GithubIOSyntax`.

**NOTE**: In the examples you will see `Github(None)`
because if you are authenticating for the first time you don't have any access token yet.

### Create a new authorization token

Used to request a new auth token given basic authentication.

You can create a new authorization token using `newAuth`; it takes as arguments:

- basic authentication for the token holder (`username` and `password`).
- `scopes`: attached to the token, for more information see [the scopes doc](https://developer.github.com/v3/oauth/#scopes).
- `note`: to remind you what the OAuth token is for.
- `client_id`: the 20 character OAuth app client key for which to create the token.
- `client_secret`: the 40 character OAuth app client secret for which to create the token.

```scala mdoc:compile-only
val newAuth = Github[IO](None).auth.newAuth(
  "rafaparadela",
  "invalidPassword",
  List("public_repo"),
  "New access token",
  "e8e39175648c9db8c280",
  "1234567890")
val response = newAuth.unsafeRunSync()
response.result match {
  case Left(e) => println(s"Something went wrong: ${e.getMessage}")
  case Right(r) => println(r)
}
```

The `result` on the right is the created [Authorization][auth-scala] including the created token.

See [the API doc](https://developer.github.com/v3/oauth_authorizations/#create-a-new-authorization) for full reference.


### Authorize a url

Generates an authorize url with a random state, both are returned within an [Authorize][auth-scala].

You can authorize a url using `authorizeUrl`; it takes as arguments:

- `client_id`: the 20 character OAuth app client key for which to create the token.
- `redirect_uri`: the URL in your app where users will be sent to after authorization.
- `scopes`: attached to the token, for more information see [the scopes doc](https://developer.github.com/v3/oauth/#scopes).

```scala mdoc:compile-only
val authorizeUrl = Github[IO](None).auth.authorizeUrl(
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
val getAccessToken = Github[IO](None).auth.getAccessToken(
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
