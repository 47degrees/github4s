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

## Team

### List team

You can list the teams for a particular organization with `listTeams`; it takes as arguments:

- `org`: name of the organization for which we want to retrieve the teams.
- `pagination`: Limit and Offset for pagination, optional.

To list the teams for organization `47deg`:

```scala mdoc:compile-only
val listTeams = Github[IO](accessToken).teams.listTeams("47deg")
val response = listTeams.unsafeRunSync()
response.result match {
  case Left(e) => println(s"Something went wrong: ${e.getMessage}")
  case Right(r) => println(r)
}
```

The `result` on the right is the corresponding [List[Team]][team-scala].

See [the API doc](https://developer.github.com/v3/teams/#list-teams) for full reference.


[team-scala]: https://github.com/47deg/github4s/blob/master/github4s/src/main/scala/github4s/domain/Team.scala
