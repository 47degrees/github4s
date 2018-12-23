---
layout: docs
title: Gist API
---

# Gist API

Github4s supports the [Gist API](https://developer.github.com/v3/gists/). As a result,
with Github4s, you can:

- [Create a gist](#create-a-gist)
- [Get a single gist or specific revision of a gist](#get-a-gist)

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

Support for `cats.Id`, `cats.Eval`, and `Future` (the only supported option for scala-js) are
provided out of the box when importing `github4s.{js,jvm}.Implicits._`.

## Create a gist

You can create a gist using `newGist`; it takes as arguments:

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
  case Left(e) => println(s"Something went wrong: ${e.getMessage}")
  case Right(r) => println(r.result)
}
```

The `result` on the right is the created [Gist][gist-scala].

See [the API doc](https://developer.github.com/v3/gists/#create-a-gist) for full reference.

## Get a single gist or specific revision of a gist

You can create a gist using `getGist`; it takes as arguments:

- the gist id (obtained via [creation of a gist](#create-a-gist), for ex.).
- optional sha of the gist revision.

To get a single gist:

```scala
val singleGist = Github(accessToken).gists.getGist("aa5a315d61ae9438b18d")

singleGist.exec[cats.Id, HttpResponse[String]]() match {
  case Left(e) => println(s"Something went wrong: ${e.getMessage}")
  case Right(r) => println(r.result)
}
```

Similarly, to get a specific revision of a gist:

```scala
val sepcificRevisionGist = Github(accessToken).gists.getGist("aa5a315d61ae9438b18d", Some("4e481528046a016fc11d6e7d8d623b55ea11e372"))

sepcificRevisionGist.exec[cats.Id, HttpResponse[String]]() match {
  case Left(e) => println(s"Something went wrong: ${e.getMessage}")
  case Right(r) => println(r.result)
}
```

The `result` on the right is the requested [Gist][gist-scala].

See [the API doc](https://developer.github.com/v3/gists/#get-a-single-gist) for full reference.

## Edit a gist

You can edit a gist using `editGist`; it takes as arguments:

- the gist id (obtained via [creation of a gist](#create-a-gist), for ex.).
- the gist description.
- an association of file names and optional file contents where the contents are wrapped in
[EditGistFile][gist-scala]s, if a new file name required, then it must be provided.

To edit a gist (change description, update content of _token.scala_, rename _gh4s.scala_ and remove _token.class_ file):

```scala
import github4s.free.domain.EditGistFile
val files = Map(
  "token.scala" -> Some(EditGistFile("lazy val accessToken = sys.env.get(\"GITHUB4S_ACCESS_TOKEN\")")),
  "gh4s.scala"  -> Some(EditGistFile("val gh = Github(accessToken)", Some("GH4s.scala"))),
  "token.class"  -> None
)

val updatedGist = Github(accessToken).gists.editGist("aa5a315d61ae9438b18d", "Updated github4s entry point", files)

updatedGist.exec[cats.Id, HttpResponse[String]]() match {
  case Left(e) => println(s"Something went wrong: ${e.getMessage}")
  case Right(r) => println(r.result)
}
```

The `result` on the right is the updated [Gist][gist-scala].

See [the API doc](https://developer.github.com/v3/gists/#edit-a-gist) for full reference.

As you can see, a few features of the gist endpoint are missing. As a result, if you'd like to see a
feature supported, feel free to create an issue and/or a pull request!

[gist-scala]: https://github.com/47deg/github4s/blob/master/github4s/shared/src/main/scala/github4s/free/domain/Gist.scala