---
layout: docs
title: Activity API
permalink: activity
---

# Activity API

Github4s supports the [Activity API](https://developer.github.com/v3/activity/). As a result,
with Github4s, you can interact with:

- [Notifications](#notifications)
  - [Set a thread subscription](#set-a-thread-subscription)
- [Starring](#starring)
  - [List stargazers](#list-stargazers)
  - [List starred repositories](#list-starred-repositories)

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

## Notifications

### Set a Thread Subscription

This lets you subscribe or unsubscribe from a conversation.

Unsubscribing from a conversation mutes all future notifications (until you comment or get @mentioned once more).

You can subscribe or unsubscribe using `setThreadSub`; it takes as arguments:

- `id`: Thread id from which you subscribe or unsubscribe.
- `subscribed`: Determines if notifications should be received from this thread.
- `ignored`: Determines if all notifications should be blocked from this thread.

```scala mdoc:compile-only
val threadSub = gh.activities.setThreadSub(5, true, false)
val response = threadSub.unsafeRunSync()
response.result match {
  case Left(e) => println(s"Something went wrong: ${e.getMessage}")
  case Right(r) => println(r)
}
```

The `result` on the right is the created or deleted [Subscription][activity-scala].

See [the API doc](https://developer.github.com/v3/activity/notifications/#set-a-thread-subscription) for full reference.

## Starring

### List stargazers

You can list the users starring a specific repository with `listStargazers`; it takes as arguments:

- the repository coordinates (`owner` and `name` of the repository).
- `timeline`: whether or not to return the date at which point the user starred the repository.
- `pagination`: Limit and Offset for pagination, optional.

To list the stargazers of 47degrees/github4s:

```scala mdoc:compile-only
val listStargazers = gh.activities.listStargazers("47degrees", "github4s", true)
val response = listStargazers.unsafeRunSync()
response.result match {
  case Left(e) => println(s"Something went wrong: ${e.getMessage}")
  case Right(r) => println(r)
}
```

The `result` on the right is the corresponding [List[Stargazer]][activity-scala].

See [the API doc](https://developer.github.com/v3/activity/starring/#list-stargazers) for full
reference.

### List starred repositories

You can list the repositories starred by a particular user with `listStarredRepositories`; it takes
as arguments:

- `username`: name of the user for which we want to retrieve the starred repositories.
- `timeline`: whether or not to return the date at which point the user starred the repository.
- `sort`: how to sort the result, can be "created" (when the repo was starred) or "updated" (when
the repo was last pushed to), optional.
- `direction`: "asc" or "desc", optional.
- `pagination`: Limit and Offset for pagination, optional.

To list the starred repositories for user `rafaparadela`:

```scala mdoc:compile-only
val listStarredRepositories = gh.activities.listStarredRepositories("rafaparadela", true)
val response = listStarredRepositories.unsafeRunSync()
response.result match {
  case Left(e) => println(s"Something went wrong: ${e.getMessage}")
  case Right(r) => println(r)
}
```

The `result` on the right is the corresponding [List[StarredRepository]][activity-scala].

See [the API doc](https://developer.github.com/v3/activity/starring/#list-repositories-being-starred)
for full reference.

As you can see, a few features of the activity endpoint are missing.

As a result, if you'd like to see a feature supported, feel free to create an issue and/or a pull request!

[activity-scala]: https://github.com/47degrees/github4s/blob/master/github4s/src/main/scala/github4s/domain/Activity.scala
