---
layout: docs
title: Authentication API
---

# Authentication API

Github4s supports the [Authentication API](https://developer.github.com/v3/auth/). As a result,
with github4s, you can:

- [Set a thread subscription](#set-a-thread-subscription)

The following examples assume the following imports and token:

```tut:silent
import github4s.Github
import github4s.Github._
import github4s.jvm.Implicits._
import scalaj.http.HttpResponse
// if you're using ScalaJS, replace occurrences of HttpResponse by SimpleHttpResponse
//import github4s.js.Implicits._
//import fr.hmil.roshttp.response.SimpleHttpResponse

val accessToken = sys.env.get("GITHUB4S_ACCESS_TOKEN")
```

They also make use of `cats.Id` but any type container implementing `MonadError[M, Throwable]` will
do.

Support for `cats.Id`, `cats.Eval` and `Future` (the only supported option for scala-js) are
provided out of the box when importing `github4s.{js,jvm}.Implicits._`.


  def newAuth(
      username: String,
      password: String,
      scopes: List[String],
      note: String,
      client_id: String,
      client_secret: String
  ): GHIO[GHResponse[Authorization]] =
    O.newAuth(username, password, scopes, note, client_id, client_secret)

  def authorizeUrl(
      client_id: String,
      redirect_uri: String,
      scopes: List[String]
  ): GHIO[GHResponse[Authorize]] =
    O.authorizeUrl(client_id, redirect_uri, scopes)

  def getAccessToken(
      client_id: String,
      client_secret: String,
      code: String,
      redirect_uri: String,
      state: String
  ): GHIO[GHResponse[OAuthToken]] =
    O.getAccessToken(client_id, client_secret, code, redirect_uri, state)