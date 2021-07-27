---
layout: docs
title: Organization API
permalink: organization
---

# Organization API

Github4s supports the [Organization API](https://developer.github.com/v3/orgs/). As a result,
with Github4s, you can interact with:

- [Members](#members)
  - [List members](#list-members)
- [Outside Collaborators](#outside-collaborators)
  - [List outside collaborators](#list-outside-collaborators)

The following examples assume the following code:

```scala mdoc:silent

import cats.effect.IO
import github4s.Github
import org.http4s.client.{Client, JavaNetClientBuilder}


val httpClient: Client[IO] = JavaNetClientBuilder[IO].create // You can use any http4s backend

val accessToken = sys.env.get("GITHUB_TOKEN")
val gh = Github[IO](httpClient, accessToken)
```

## Members

### List members

You can list the members for a particular organization with `listMembers`; it takes as arguments:

- `org`: name of the organization for which we want to retrieve the members.
- `filter`: to retrieve "all" or only "2fa_disabled" users, optional.
- `role`: to retrieve "all", only non-owners ("member") or only owners ("admin"), optional.
- `pagination`: Limit and Offset for pagination, optional.

To list the members for organization `47deg`:

```scala mdoc:compile-only
val listMembers = gh.organizations.listMembers("47deg")
listMembers.flatMap(_.result match {
  case Left(e)  => IO.println(s"Something went wrong: ${e.getMessage}")
  case Right(r) => IO.println(r)
})
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

```scala mdoc:compile-only
val outsideCollaborators = gh.organizations.listOutsideCollaborators("47deg")
outsideCollaborators.flatMap(_.result match {
  case Left(e)  => IO.println(s"Something went wrong: ${e.getMessage}")
  case Right(r) => IO.println(r)
})
```

The `result` on the right is the corresponding [List[User]][user-scala].

See [the API doc](https://developer.github.com/v3/orgs/outside_collaborators/#list-outside-collaborators) for full reference.

As you can see, a few features of the organization endpoint are missing.

As a result, if you'd like to see a feature supported, feel free to create an issue and/or a pull request!

[user-scala]: https://github.com/47degrees/github4s/blob/master/github4s/src/main/scala/github4s/domain/User.scala
