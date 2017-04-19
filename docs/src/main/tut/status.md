---
layout: docs
title: Status API
---

# Status API

Github4s supports the [Status API](https://developer.github.com/v3/repos/statuses/). As a result,
with github4s, you can:

- [create a status](#create-a-status)
- [list statuses for a specific git ref](#list-statuses)
- [get the combined status of a specific git ref](#get-combined-status)

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
do such as `cats.Eval` or `Future` (the only supported option for scala-js).

## Create a status

You can create a status using `createStatus`, it takes as arguments:

- the repository coordinates (owner and name of the repository)
- the SHA of the commit for which we want to create a status
- the state of the status we want to create (can be pending, success, failure or error)
- other optional parameters: target url, description and context

See [the API doc](https://developer.github.com/v3/repos/statuses/#parameters) for full reference.

To create a status:

```tut:silent
val createdStatus =
  Github(accessToken).statuses.createStatus("47deg", "github4s", "aaaaaa", "pending")

createdStatus.exec[cats.Id, HttpResponse[String]]() match {
  case Left(e) => println("Something went wrong: s{e.getMessage}")
  case Right(r) => println(r.result)
}
```

The `result` on the right is the created [`Status`][status-scala].

## List statuses

You can also list statuses through `listStatuses`, it take as arguments:

- the repository coordinates (owner and name of the repository)
- a git ref (a SHA, a branch name or a tag name)

To list the statuses for a specific ref:

```tut:silent
val listStatuses =
  Github(accessToken).statuses.listStatuses("47deg", "github4s", "heads/master")

listStatuses.exec[cats.Id, HttpResponse[String]]() match {
  case Left(e) => println("Something went wrong: s{e.getMessage}")
  case Right(r) => println(r.result)
}
```

The `result` on the right is the corresponding [`List[Status]`][status-scala].

## Get combined status

Lastly, you can also get the combined status thanks to `getCombinedStatus`, it takes the same
arguments as the operation listing statuses:

```tut:silent
val combinedStatus =
  Github(accessToken).statuses.getCombinedStatus("47deg", "github4s", "heads/master")

combinedStatus.exec[cats.Id, HttpResponse[String]]() match {
  case Left(e) => println("Something went wrong: s{e.getMessage}")
  case Right(r) => println(r.result)
}
```

The `result` on the right is a [`CombinedStatus`][status-scala].

Note that the state of the combined status is the product of a heuristic detailed in
[the API documentation](https://developer.github.com/v3/repos/statuses/#get-the-combined-status-for-a-specific-ref).

[status-scala]: https://github.com/47deg/github4s/blob/master/github4s/shared/src/main/scala/github4s/free/domain/Status.scala