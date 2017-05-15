---
layout: docs
title: User API
---

# User API

Github4s supports the [User API](https://developer.github.com/v3/users/). As a result,
with github4s, you can:

- [Get user](#get-user)
- [Get authenticated user](#get-authenticated-user)
- [Get a list of users](#get-a-list-of-users)

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

## User

### Get user

  /**
    * Get information for a particular user
    *
    * @param accessToken to identify the authenticated user
    * @param headers optional user headers to include in the request
    * @param username of the user to retrieve
    * @return GHResponse[User] User details
    */
  def get(username: String): GHIO[GHResponse[User]] = O.getUser(username, accessToken)

The `result` on the right is the corresponding [List[User]][user-scala].

See [the API doc](https://developer.github.com/v3/activity/notifications/#set-a-thread-subscription) for full reference.


### Get authenticated user

  /**
    * Get information of the authenticated user
    * @param accessToken to identify the authenticated user
    * @param headers optional user headers to include in the request
    * @return GHResponse[User] User details
    */
  def getAuth: GHIO[GHResponse[User]] = O.getAuthUser(accessToken)

The `result` on the right is the corresponding [List[User]][user-scala].

See [the API doc](https://developer.github.com/v3/activity/notifications/#set-a-thread-subscription) for full reference.


### Get a list of users

  /**
    * Get users
    *
    * @param accessToken to identify the authenticated user
    * @param headers optional user headers to include in the request
    * @param since The integer ID of the last User that you've seen.
    * @param pagination Limit and Offset for pagination
    * @return GHResponse[List[User] ] List of user's details
    */
  def getUsers(since: Int, pagination: Option[Pagination] = None): GHIO[GHResponse[List[User]]] =
    O.getUsers(since, pagination, accessToken)

The `result` on the right is the corresponding [List[User]][user-scala].

See [the API doc](https://developer.github.com/v3/activity/notifications/#set-a-thread-subscription) for full reference.

As you can see, a few features of the activity endpoint are missing.

As a result, if you'd like to see a feature supported, feel free to create an issue and/or a pull request!

[user-scala]: https://github.com/47deg/github4s/blob/master/github4s/shared/src/main/scala/github4s/free/domain/User.scala