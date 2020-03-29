---
layout: docs
title: Project API
permalink: project
---

# Project API

Note: The Projects API is currently available for developers to preview. During the preview period,
the API may change without advance notice. Please see the blog post for full details. To access the
API during the preview period, you must provide a custom media type in the `Accept` header:
 `application/vnd.github.inertia-preview+json`

Github4s supports the [Project API](https://developer.github.com/v3/projects/). As a result,
with Github4s, you can interact with:

- [Project](#project)
  - [List repository projects](#list-repository-projects)
  - [List projects](#list-projects)
  - [Columns](#columns)
    - [List project columns](#list-project-columns)
  - [Cards](#cards)
    - [List project cards](#list-project-cards-by-column)

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

val accessToken = sys.env.get("G4S_TOKEN")
val gh = Github[IO](httpClient, accessToken)
```

## Project

### List repository projects

You can list the projects for a particular repository with `listProjectsRepository`; it takes as arguments:

- `owner`: name of the owner for which we want to retrieve the projects.
- `repo`: name of the repository for which we want to retrieve the projects.
- `state`: filter projects returned by their state. Can be either `open`, `closed`, `all`. Default: `open`, optional
- `pagination`: Limit and Offset for pagination, optional.
- `header`: headers to include in the request, optional.

To list the projects for owner `47deg` and repository `github4s`:

```scala mdoc:compile-only
val listProjectsRepository = gh.projects.listProjectsRepository(
    owner = "47deg",
    repo = "github4s",
    headers = Map("Accept" -> "application/vnd.github.inertia-preview+json"))
val response = listProjectsRepository.unsafeRunSync()
response.result match {
  case Left(e) => println(s"Something went wrong: ${e.getMessage}")
  case Right(r) => println(r)
}
```

The `result` on the right is the corresponding [List[Project]][project-scala].

See [the API doc](https://developer.github.com/v3/projects/#list-repository-projects) for full reference.

[project-scala]: https://github.com/47degrees/github4s/blob/master/github4s/src/main/scala/github4s/domain/Project.scala


### List projects

You can list the project for a particular organization with `listProjects`; it takes as arguments:

- `org`: name of the organization for which we want to retrieve the projects.
- `state`: filter projects returned by their state. Can be either `open`, `closed`, `all`. Default: `open`, optional
- `pagination`: Limit and Offset for pagination, optional.
- `header`: headers to include in the request, optional.

To list the projects for organization `47deg`:

```scala mdoc:compile-only
val listProjects = gh.projects.listProjects(
    org = "47deg",
    headers = Map("Accept" -> "application/vnd.github.inertia-preview+json"))
val response = listProjects.unsafeRunSync()
response.result match {
  case Left(e) => println(s"Something went wrong: ${e.getMessage}")
  case Right(r) => println(r)
}
```

The `result` on the right is the corresponding [List[Project]][project-scala].

See [the API doc](https://developer.github.com/v3/projects/#list-organization-projects) for full reference.

[project-scala]: https://github.com/47degrees/github4s/blob/master/github4s/src/main/scala/github4s/domain/Project.scala

### Columns

#### List project columns

You can list the columns for a particular project with `listColumns`; it takes as arguments:

- `project_id`: project id for which we want to retrieve the columns.
- `pagination`: Limit and Offset for pagination, optional.
- `header`: headers to include in the request, optional.

To list the columns for project_id `1910444`:

```scala mdoc:compile-only
val listColumns = gh.projects.listColumns(
    project_id = 1910444,
    headers = Map("Accept" -> "application/vnd.github.inertia-preview+json"))
val response = listColumns.unsafeRunSync()
response.result match {
  case Left(e) => println(s"Something went wrong: ${e.getMessage}")
  case Right(r) => println(r)
}
```

The `result` on the right is the corresponding [List[Column]][column-scala].

See [the API doc](https://developer.github.com/v3/projects/columns/#list-project-columns) for full reference.

[column-scala]: https://github.com/47degrees/github4s/blob/master/github4s/src/main/scala/github4s/domain/Project.scala

### Cards

#### List project cards by column

You can list the cards for a particular column with `listCards`; it takes as arguments:

- `column_id`: column id for which we want to retrieve the cards.
- `archived_state`: filters the project cards that are returned by the card's state.
Can be one of `all`,`archived`, or `not_archived`. Default: `not_archived`, optional.
- `pagination`: Limit and Offset for pagination, optional.
- `header`: headers to include in the request, optional.

To list the columns for project_id `8271018`:

```scala mdoc:compile-only
val listCards = gh.projects.listCards(
    column_id = 8271018,
    headers = Map("Accept" -> "application/vnd.github.inertia-preview+json"))
val response = listCards.unsafeRunSync()
response.result match {
  case Left(e) => println(s"Something went wrong: ${e.getMessage}")
  case Right(r) => println(r)
}
```

The `result` on the right is the corresponding [List[Card]][card-scala].

See [the API doc](https://developer.github.com/v3/projects/cards/#list-project-cards) for full reference.

[card-scala]: https://github.com/47degrees/github4s/blob/master/github4s/src/main/scala/github4s/domain/Project.scala
