---
layout: docs
title: Pull Request API
---

# Pull Request API

Github4s supports the [Pull Request API](https://developer.github.com/v3/pulls/). As a result,
with Github4s, you can:

- [List pull requests](#list-pull-requests)
- [List the files in a pull request](#list-the-files-in-a-pull-request)
- [Create a pull request](#create-a-pull-request)

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

They also make use of `cats.Id`, but any type container implementing `MonadError[M, Throwable]` will do.

Support for `cats.Id`, `cats.Eval`, and `Future` (the only supported option for scala-js) are
provided out of the box when importing `github4s.{js,jvm}.Implicits._`.

## List pull requests

You can list the pull requests for a repository using `list`; it takes as arguments:

- the repository coordinates (`owner` and `name` of the repository).
- a list of [PRFilter](https://github.com/47deg/github4s/blob/master/github4s/shared/src/main/scala/github4s/free/domain/PullRequest.scala).

As an example, let's say we want the open pull requests in <https://github.com/scala/scala> sorted
by popularity:

```tut:silent
import github4s.free.domain._
val prFilters = List(PRFilterOpen, PRFilterSortPopularity)
val listPullRequests = Github(accessToken).pullRequests.list("scala", "scala", prFilters)

listPullRequests.exec[cats.Id, HttpResponse[String]]() match {
  case Left(e) => println("Something went wrong: s{e.getMessage}")
  case Right(r) => println(r.result)
}
```

The `result` on the right is the matching [List[PullRequest]][pr-scala].

See [the API doc](https://developer.github.com/v3/pulls/#list-pull-requests) for full reference.

## List the files in a pull request

You can also list the files for a pull request using `listFiles`; it takes as arguments:

- the repository coordinates (`owner` and `name` of the repository).
- the pull request number.

To list the files for a pull request:

```scala
val listPullRequestFiles = Github(accessToken).pullRequests.listFiles("47deg", "github4s", 102)

listPullRequestFiles.exec[cats.Id, HttpResponse[String]]() match {
  case Left(e) => println("Something went wrong: s{e.getMessage}")
  case Right(r) => println(r.result)
}
```

the `result` on the right is the [List[PullRequestFile]][pr-scala].

See [the API doc](https://developer.github.com/v3/pulls/#list-pull-requests-files) for full
reference.

## Create a pull request
If you want to create a pull request, you can follow two different methods.

On the one hand, we can pass the following parameters:

 - the repository coordinates (`owner` and `name` of the repository).
 - `title` (as part of the `NewPullRequestData` object): Title for the pull request.
 - `body` (as part of the `NewPullRequestData` object): Description for the pull request.
 - `head`: The name of the branch where your changes are implemented.
 - `base`: The name of the branch you want the changes pulled into.
 - `maintainerCanModify`: Optional. Indicates whether maintainers can modify the pull request. `true` by default.

```scala
val createPullRequestData = Github(accessToken).pullRequests.create(
  "47deg",
  "github4s",
  NewPullRequestData("title","body"),
  "my-branch",
  "base-branch",
  Some(true))

createPullRequestData.exec[cats.Id, HttpResponse[String]]() match {
  case Left(e) => println("Something went wrong: s{e.getMessage}")
  case Right(r) => println(r.result)
}
```

On the other hand, we can pass an `issue` id (through `NewPullRequestIssue` object)
instead of the title and body.

**NOTE**: This option deletes the issue.

```scala
val createPullRequestIssue = Github(accessToken).pullRequests.create(
  "47deg",
  "github4s",
  NewPullRequestIssue("105"),
  "my-branch",
  "base-branch",
  Some(true))

createPullRequestIssue.exec[cats.Id, HttpResponse[String]]() match {
  case Left(e) => println("Something went wrong: s{e.getMessage}")
  case Right(r) => println(r.result)
}
```

See [the API doc](https://developer.github.com/v3/pulls/#create-a-pull-request) for full reference.

As you can see, a few features of the pull request endpoint are missing. As a result, if you'd like
to see a feature supported, feel free to create an issue and/or a pull request!

[pr-scala]: https://github.com/47deg/github4s/blob/master/github4s/shared/src/main/scala/github4s/free/domain/PullRequest.scala