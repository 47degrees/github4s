---
layout: docs
title: Team API
permalink: team
---

# Team API

Github4s supports the [Team API](https://developer.github.com/v3/teams/). As a result,
with Github4s, you can interact with:

- [Team](#team)
  - [List team](#list-team)

The following examples assume the following code:

```scala mdoc:silent

import cats.effect.IO
import github4s.Github
import org.http4s.client.{Client, JavaNetClientBuilder}


val httpClient: Client[IO] = JavaNetClientBuilder[IO].create // You can use any http4s backend

val accessToken = sys.env.get("GITHUB_TOKEN")
val gh = Github[IO](httpClient, accessToken)
```

## Team

### List team

You can list the teams for a particular organization with `listTeams`; it takes as arguments:

- `org`: name of the organization for which we want to retrieve the teams.
- `pagination`: Limit and Offset for pagination, optional.

To list the teams for organization `47deg`:

```scala mdoc:compile-only
val listTeams = gh.teams.listTeams("47deg")
listTeams.flatMap(_.result match {
  case Left(e)  => IO.println(s"Something went wrong: ${e.getMessage}")
  case Right(r) => IO.println(r)
})
```

The `result` on the right is the corresponding [List[Team]][team-scala].

See [the API doc](https://developer.github.com/v3/teams/#list-teams) for full reference.


[team-scala]: https://github.com/47degrees/github4s/blob/master/github4s/src/main/scala/github4s/domain/Team.scala
