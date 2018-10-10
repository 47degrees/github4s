---
layout: docs
title: Organization API
---

# Organization API

Github4s supports the [Organization API](https://developer.github.com/v3/orgs/). As a result,
with Github4s, you can interact with:

- [Members](#members)
  - [List members](#list-members)
- [Outside Collaborators](#outside-collaborators)
  - [List outside collaborators](#list-outside-collaborators)

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

## Members

### List members

You can list the members for a particular organization with `listMembers`; it takes as arguments:

- `org`: name of the organization for which we want to retrieve the members.
- `filter`: to retrieve "all" or only "2fa_disabled" users, optional.
- `role`: to retrieve "all", only non-owners ("member") or only owners ("admin"), optional.
- `pagination`: Limit and Offset for pagination, optional.

To list the members for organization `47deg`:

```tut:silent
val listMembers = Github(accessToken).organizations.listMembers("47deg")
listMembers.exec[cats.Id, HttpResponse[String]]() match {
  case Left(e) => println(s"Something went wrong: ${e.getMessage}")
  case Right(r) => println(r.result)
}
```

The `result` on the right is the corresponding [List[User]][user-scala].

See [the API doc](https://developer.github.com/v3/orgs/members/#members-list) for full reference.

## Outside Collaborators

### List outside collaborators

You can list the outside collaborators of your organization with `listOutsideCollaborators`; it takes as arguments:

- `org`: name of the organization for which we want to retrieve the outside collaborators.
- `filter`: to retrieve "all" or only "2fa_disabled" users, optional.
- `pagination`: Limit and Offset for pagination, optional.

To list the outside collaborators for organization `47deg`:

```tut:silent
val outsideCollaborators = Github(accessToken).organizations.listOutsideCollaborators("47deg")
outsideCollaborators.exec[cats.Id, HttpResponse[String]]() match {
  case Left(e) => println(s"Something went wrong: ${e.getMessage}")
  case Right(r) => println(r.result)
}
```

The `result` on the right is the corresponding [List[User]][user-scala].

See [the API doc](https://developer.github.com/v3/orgs/outside_collaborators/#list-outside-collaborators) for full reference.

As you can see, a few features of the organization endpoint are missing.

As a result, if you'd like to see a feature supported, feel free to create an issue and/or a pull request!

[user-scala]: https://github.com/47deg/github4s/blob/master/github4s/shared/src/main/scala/github4s/free/domain/User.scala
