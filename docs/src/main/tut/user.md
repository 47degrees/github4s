---
layout: docs
title: User API
---

# User API

Github4s supports the [User API](https://developer.github.com/v3/users/). As a result,
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

  def get(username: String): GHIO[GHResponse[User]] = O.getUser(username, accessToken)

  def getAuth: GHIO[GHResponse[User]] = O.getAuthUser(accessToken)

  def getUsers(since: Int, pagination: Option[Pagination] = None): GHIO[GHResponse[List[User]]] =
    O.getUsers(since, pagination, accessToken)