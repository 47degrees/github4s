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

The following examples assume the following imports and token:

```scala mdoc:silent
import github4s.Github
import github4s.GithubIOSyntax._
import cats.effect.IO
import scala.concurrent.ExecutionContext.Implicits.global

implicit val IOContextShift = IO.contextShift(global)
val accessToken = sys.env.get("GITHUB4S_ACCESS_TOKEN")
```
They also make use of `cats.Id`, but any type container `F` implementing `ConcurrentEffect` will do.

LiftIO syntax for `cats.Id` and `Future` are provided in `GithubIOSyntax`.

## Users

### Get a user

Get information for a particular user.

You can get a user using `get`, it takes as argument:

- `username`: of the user to retrieve.

```scala mdoc:compile-only
val getUser = Github[IO](accessToken).users.get("rafaparadela")
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
val getAuth = Github[IO](accessToken).users.getAuth()
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
val getUsers = Github[IO](accessToken).users.getUsers(1)
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
val getFollowing = Github[IO](accessToken).users.getFollowing("rafaparadela")
val response = getFollowing.unsafeRunSync()
response.result match {
  case Left(e) => println(s"Something went wrong: ${e.getMessage}")
  case Right(r) => println(r)
}
```

The `result` on the right is the corresponding [List[User]][user-scala].

See [the API doc](https://developer.github.com/v3/users/followers/#list-users-followed-by-another-use) for full reference.

[user-scala]: https://github.com/47degrees/github4s/blob/master/github4s/src/main/scala/github4s/domain/User.scala
