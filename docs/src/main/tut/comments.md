---
layout: docs
title: Comments API
---

# Comments API

Github4s supports the [Comments API](https://developer.github.com/v3/activity/comments/). As a result,
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

#Create a Comment

#Edit a Comment

#Delete a Comment

See [the API doc](https://developer.github.com/v3/activity/comments/#set-a-thread-subscription) for full reference.

As a result, if you'd like to see a feature supported, feel free to create an issue and/or a pull request!