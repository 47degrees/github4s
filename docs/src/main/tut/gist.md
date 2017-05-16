---
layout: docs
title: Gist API
---

# Gist API

Github4s supports the [Gist API](https://developer.github.com/v3/gists/). As a result,
with Github4s, you can:

- [Create a gist](#create-a-gist)

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

They also make use of `cats.Id` but any type container implementing `MonadError[M, Throwable]` will do.

Support for `cats.Id`, `cats.Eval` and `Future` (the only supported option for scala-js) are
provided out of the box when importing `github4s.{js,jvm}.Implicits._`.

## Create a gist

You can create a gist using `newGist`, it takes as arguments:

- the gist description.
- whether it is public or private.
- an association of file names and file contents where the contents are wrapped in
[GistFile][gist-scala]s.

To create a gist:

```scala
import github4s.free.domain.GistFile
val files = Map(
  "token.scala" -> GistFile("val accessToken = sys.env.get(\"GITHUB4S_ACCESS_TOKEN\")"),
  "gh4s.scala"  -> GistFile("val gh = Github(accessToken)")
)
val newGist = Github(accessToken).gists.newGist("Github4s entry point", public = true, files)

newGist.exec[cats.Id, HttpResponse[String]]() match {
  case Left(e) => println("Something went wrong: s{e.getMessage}")
  case Right(r) => println(r.result)
}
```

The `result` on the right is the created [Gist][gist-scala].

See [the API doc](https://developer.github.com/v3/gists/#create-a-gist) for full reference.

As you can see, a few features of the gist endpoint are missing. As a result, if you'd like to see a
feature supported, feel free to create an issue and/or a pull request!

[gist-scala]: https://github.com/47deg/github4s/blob/master/github4s/shared/src/main/scala/github4s/free/domain/Gist.scala