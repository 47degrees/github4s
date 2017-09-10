---
layout: docs
title: Activity API
---

# Activity API

Github4s supports the [Activity API](https://developer.github.com/v3/activity/). As a result,
with Github4s, you can interact with:

- [Notifications](#notifications)
  - [Set a thread subscription](#set-a-thread-subscription)
- [Starring](#starring)
  - [List stargazers](#list-stargazers)
  - [List starred repositories](#list-starred-repositories)

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

## Notifications

### Set a Thread Subscription

This lets you subscribe or unsubscribe from a conversation.

Unsubscribing from a conversation mutes all future notifications (until you comment or get @mentioned once more).

You can subscribe or unsubscribe using `setThreadSub`; it takes as arguments:

- `id`: Thread id from which you subscribe or unsubscribe.
- `subscribed`: Determines if notifications should be received from this thread.
- `ignored`: Determines if all notifications should be blocked from this thread.

```scala
val threadSub = Github(accessToken).activities.setThreadSub(5, true, false)
threadSub.exec[cats.Id, HttpResponse[String]]() match {
  case Left(e) => println(s"Something went wrong: ${e.getMessage}")
  case Right(r) => println(r.result)
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

To list the stargazers of 47deg/github4s:

```tut:silent
val listStargazers = Github(accessToken).activities.listStargazers("47deg", "github4s", true)
listStargazers.exec[cats.Id, HttpResponse[String]]() match {
  case Left(e) => println(s"Something went wrong: ${e.getMessage}")
  case Right(r) => println(r.result)
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

```tut:silent
val listStarredRepositories = Github(accessToken).activities.listStarredRepositories("rafaparadela", true)
listStarredRepositories.exec[cats.Id, HttpResponse[String]]() match {
  case Left(e) => println(s"Something went wrong: ${e.getMessage}")
  case Right(r) => println(r.result)
}
```

The `result` on the right is the corresponding [List[StarredRepository]][activity-scala].

See [the API doc](https://developer.github.com/v3/activity/starring/#list-repositories-being-starred)
for full reference.

As you can see, a few features of the activity endpoint are missing.

As a result, if you'd like to see a feature supported, feel free to create an issue and/or a pull request!

[activity-scala]: https://github.com/47deg/github4s/blob/master/github4s/shared/src/main/scala/github4s/free/domain/Activity.scala
