---
layout: docs
title: Pull Request API
permalink: pull_request
---

# Pull Request API

Github4s supports the [Pull Request API](https://developer.github.com/v3/pulls/). As a result,
with Github4s, you can interact with:

- [Pull requests](#pull-requests)
  - [Get a pull request](#get-a-pull-request)
  - [List pull requests](#list-pull-requests)
  - [List the files in a pull request](#list-the-files-in-a-pull-request)
  - [Create a pull request](#create-a-pull-request)
- [Reviews](#reviews)
  - [List reviews](#list-pull-request-reviews)
  - [Get a review](#get-an-individual-review)

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

val accessToken = sys.env.get("GITHUB4S_ACCESS_TOKEN")
val gh = Github[IO](httpClient, accessToken)
```

## Pull requests

### Get a pull request

You can get a single pull request for a repository using `get`; it takes as arguments:

- the repository coordinates (`owner` and `name` of the repository).
- the pull request number

To get a single pull request:

```scala mdoc:compile-only
val getPullRequest = gh.pullRequests.getPullRequest("47degrees", "github4s", 102)
val response = getPullRequest.unsafeRunSync()
response.result match {
  case Left(e) => println(s"Something went wrong: ${e.getMessage}")
  case Right(r) => println(r)
}
```

The `result` on the right is the corresponding [PullRequest][pr-scala].

See [the API doc](https://developer.github.com/v3/pulls/#get-a-single-pull-request) for full reference.

### List pull requests

You can list the pull requests for a repository using `list`; it takes as arguments:

- the repository coordinates (`owner` and `name` of the repository).
- a list of [PRFilter](https://github.com/47degrees/github4s/blob/master/github4s/shared/src/main/scala/github4s/free/domain/PullRequest.scala).

As an example, let's say we want the open pull requests in <https://github.com/scala/scala> sorted
by popularity:

```scala mdoc:compile-only
import github4s.domain._
val prFilters = List(PRFilterOpen, PRFilterSortPopularity)
val listPullRequests = gh.pullRequests.listPullRequests("scala", "scala", prFilters)
val response = listPullRequests.unsafeRunSync()
response.result match {
  case Left(e) => println(s"Something went wrong: ${e.getMessage}")
  case Right(r) => println(r)
}
```

The `result` on the right is the matching [List[PullRequest]][pr-scala].

See [the API doc](https://developer.github.com/v3/pulls/#list-pull-requests) for full reference.

### List the files in a pull request

You can also list the files for a pull request using `listFiles`; it takes as arguments:

- the repository coordinates (`owner` and `name` of the repository).
- the pull request number.

To list the files for a pull request:

```scala mdoc:compile-only
val listPullRequestFiles = gh.pullRequests.listFiles("47degrees", "github4s", 102)
val response = listPullRequestFiles.unsafeRunSync()
response.result match {
  case Left(e) => println(s"Something went wrong: ${e.getMessage}")
  case Right(r) => println(r)
}
```

the `result` on the right is the [List[PullRequestFile]][pr-scala].

See [the API doc](https://developer.github.com/v3/pulls/#list-pull-requests-files) for full
reference.

### Create a pull request

If you want to create a pull request, we can follow two different methods.

On the one hand, we can pass the following parameters:

 - the repository coordinates (`owner` and `name` of the repository).
 - `title` (as part of the `NewPullRequestData` object): Title for the pull request.
 - `body` (as part of the `NewPullRequestData` object): Description for the pull request.
 - `head`: The name of the branch where your changes are implemented.
 - `base`: The name of the branch you want the changes pulled into.
 - `maintainerCanModify`: Optional. Indicates whether maintainers can modify the pull request. `true` by default.

```scala mdoc:compile-only
import github4s.domain.NewPullRequestData

val createPullRequestData = gh.pullRequests.createPullRequest(
  "47deg",
  "github4s",
  NewPullRequestData("title", "body"),
  "my-branch",
  "base-branch",
  Some(true))
val response = createPullRequestData.unsafeRunSync()
response.result match {
  case Left(e) => println(s"Something went wrong: ${e.getMessage}")
  case Right(r) => println(r)
}
```

On the other hand, we can pass an `issue` id (through `NewPullRequestIssue` object)
instead of the title and body.

**NOTE**: This option deletes the issue.

```scala mdoc:compile-only
import github4s.domain.NewPullRequestIssue
val createPullRequestIssue = gh.pullRequests.createPullRequest(
  "47deg",
  "github4s",
  NewPullRequestIssue(105),
  "my-branch",
  "base-branch",
  Some(true))
val response = createPullRequestIssue.unsafeRunSync()
response.result match {
  case Left(e) => println(s"Something went wrong: ${e.getMessage}")
  case Right(r) => println(r)
}
```

See [the API doc](https://developer.github.com/v3/pulls/#create-a-pull-request) for full reference.

## Reviews

### List pull request reviews

You can list the reviews for a pull request using `listReviews`; it takes as arguments:

- the repository coordinates (`owner` and `name` of the repository).
- the pull request id.

As an example, if we wanted to see all the reviews for pull request 139 of `47degrees/github4s`:

```scala mdoc:compile-only
val listReviews = gh.pullRequests.listReviews(
  "47deg",
  "github4s",
  139)
val response = listReviews.unsafeRunSync()
response.result match {
  case Left(e) => println(s"Something went wrong: ${e.getMessage}")
  case Right(r) => println(r)
}
```

The `result` on the right is the matching [List[PullRequestReview]][pr-scala].

See [the API doc](https://developer.github.com/v3/pulls/reviews/#list-reviews-on-a-pull-request) for full reference.

### Get an individual review

You can get an individual review for a pull request using `getReview`; it takes as arguments:

- the repository coordinates (`owner` and `name` of the repository).
- the pull request id.
- the review id.

As an example, if we wanted to see review 39355613 for pull request 139 of `47degrees/github4s`:

```scala mdoc:compile-only
val review = gh.pullRequests.getReview(
  "47deg",
  "github4s",
  139,
  39355613)
val response = review.unsafeRunSync()
response.result match {
  case Left(e) => println(s"Something went wrong: ${e.getMessage}")
  case Right(r) => println(r)
}
```

The `result` on the right is the matching [PullRequestReview][pr-scala].

See [the API doc](https://developer.github.com/v3/pulls/reviews/#get-a-single-review) for full reference.

As you can see, a few features of the pull request endpoint are missing. As a result, if you'd like
to see a feature supported, feel free to create an issue and/or a pull request!

[pr-scala]: https://github.com/47degrees/github4s/blob/master/github4s/src/main/scala/github4s/domain/PullRequest.scala
