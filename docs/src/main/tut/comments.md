---
layout: docs
title: Comments API
---

# Comments API

Github4s supports the [Comments API](https://developer.github.com/v3/activity/comments/). As a result,
with github4s, you can:


- [Create a Comment](#create-a-comment)
- [Edit a Comment](#edit-a-comment)
- [Delete a Comment](#delete-a-comment)

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

## Create a Comment

You can create a comment for an issue whit the next parameters:

 - the repository coordinates (owner and name of the repository)
 - `number`: The issue number
 - `body`: The comment description

 To create a comment:

```scala
val createcomment = Github(accessToken).comments.create("47deg", "github4s", 123, "this is the comment")
createcomment.exec[cats.Id, HttpResponse[String]]() match {
  case Left(e) => println("Something went wrong: s{e.getMessage}")
  case Right(r) => println(r.result)
}
```

## Edit a Comment


You can edit a comment from an issue whit the next parameters:

 - the repository coordinates (owner and name of the repository)
 - `id`: The comment id
 - `body`: The new comment description

 To edit a comment:

```scala
val editComment = Github(accessToken).comments.edit("47deg", "github4s", 20, "this is the new comment")
editComment.exec[cats.Id, HttpResponse[String]]() match {
  case Left(e) => println("Something went wrong: s{e.getMessage}")
  case Right(r) => println(r.result)
}
```


## Delete a Comment


You can delete a comment from an issue whit the next parameters:

 - the repository coordinates (owner and name of the repository)
 - `id`: The comment id

 To delete a comment:

```scala
val deleteComment = Github(accessToken).comments.delete("47deg", "github4s", 20)
deleteComment.exec[cats.Id, HttpResponse[String]]() match {
  case Left(e) => println("Something went wrong: s{e.getMessage}")
  case Right(r) => println(r.result)
}
```

See [the API doc](https://developer.github.com/v3/activity/comments/) for full reference.

As you can see, a few features of the pull request endpoint are missing. As a result, if you'd like
to see a feature supported, feel free to create an issue and/or a pull request!

[comment-scala]: https://github.com/47deg/github4s/blob/master/github4s/shared/src/main/scala/github4s/free/domain/Comment.scala
