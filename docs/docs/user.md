---
layout: docs
title: User API
permalink: user
---

# User API

Github4s supports the [User API](https://developer.github.com/v3/users/). As a result,
with Github4s, you can interacts with:

- [Users](#users)
  - [Get a user](#get-a-user)
  - [Get an authenticated user](#get-an-authenticated-user)
  - [Get a list of users](#get-a-list-of-users)
  - [List users followed by another user](#list-users-followed-by-another-user)

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

val accessToken = sys.env.get("G4S_TOKEN")
val gh = Github[IO](httpClient, accessToken)
```

## Users

### Get a user

Get information for a particular user.

You can get a user using `get`, it takes as argument:

- `username`: of the user to retrieve.

```scala mdoc:compile-only
val getUser = gh.users.get("rafaparadela")
val response = getUser.unsafeRunSync()
response.result match {
  case Left(e) => println(s"Something went wrong: ${e.getMessage}")
  case Right(r) => println(r)
}
```

The `result` on the right is the corresponding [User][user-scala].

See [the API doc](https://developer.github.com/v3/users/#get-a-single-user) for full reference.


### Get an authenticated user

Get information of the authenticated user making the API call.

You can get an authenticated user using `getAuth`:

```scala mdoc:compile-only
val getAuth = gh.users.getAuth()
val response = getAuth.unsafeRunSync()
response.result match {
  case Left(e) => println(s"Something went wrong: ${e.getMessage}")
  case Right(r) => println(r)
}
```

The `result` on the right is the corresponding [User][user-scala].

See [the API doc](https://developer.github.com/v3/users/#get-the-authenticated-user) for full reference.


### Get a list of users

You can get a list of users using `getUsers`, it takes as arguments:

- `since`: The integer ID of the last User that you've seen.
- `pagination`: Limit and Offset for pagination.

```scala mdoc:compile-only
val getUsers = gh.users.getUsers(1)
val response = getUsers.unsafeRunSync()
response.result match {
  case Left(e) => println(s"Something went wrong: ${e.getMessage}")
  case Right(r) => println(r)
}
```

The `result` on the right is the corresponding [List[User]][user-scala].

See [the API doc](https://developer.github.com/v3/users/#get-all-users) for full reference.

As you can see, a few features of the user endpoint are missing.

As a result, if you'd like to see a feature supported, feel free to create an issue and/or a pull request!

### List users followed by another user

You can get a list of users followed by another user using `getFollowing`, it takes as argument:

- `username`: of the user to retrieve.

```scala mdoc:compile-only
val getFollowing = gh.users.getFollowing("rafaparadela")
val response = getFollowing.unsafeRunSync()
response.result match {
  case Left(e) => println(s"Something went wrong: ${e.getMessage}")
  case Right(r) => println(r)
}
```

The `result` on the right is the corresponding [List[User]][user-scala].

See [the API doc](https://developer.github.com/v3/users/followers/#list-users-followed-by-another-use) for full reference.

[user-scala]: https://github.com/47degrees/github4s/blob/master/github4s/src/main/scala/github4s/domain/User.scala
