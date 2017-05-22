---
layout: docs
title: Repository API
---

# Repository API

Github4s supports the [Repository API](https://developer.github.com/v3/repos/). As a result,
with Github4s, you can:

- [Get a repository](#get-a-repository)
- [List contributors](#list-contributors)
- [List commits on a repository](#list-commits-on-a-repository)
- [Get contents](#get-contents)
- [Create a release](#create-a-release)
- [Create a status](#create-a-status)
- [List status for a specific Ref](#list-status-for-a-specific-ref)
- [Get the combined status of a specific Ref](#get-the-combined-status-for-a-specific-ref)

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
## Repository

### Get a repository

You can get a repository using `get`; it takes as arguments:

- the repository coordinates (`owner` and `name` of the repository).

To get a repository:

```tut:silent
val getRepo =
  Github(accessToken).repos.get("47deg", "github4s")

getRepo.exec[cats.Id, HttpResponse[String]]() match {
  case Left(e) => println("Something went wrong: s{e.getMessage}")
  case Right(r) => println(r.result)
}
```

The `result` on the right is the get [Repository][repository-scala].

See [the API doc](https://developer.github.com/v3/repos/#get) for full
reference.

### List contributors

List contributors to the specified repository,
sorted by the number of commits per contributor in descending order.

You can list contributors using `listContributors`, it takes as arguments:

- the repository coordinates (`owner` and `name` of the repository).
- `anom` Set to 1 or true to include anonymous contributors in results.

To list contributors:

```tut:silent
val listContributors =
  Github(accessToken).repos.listContributors("47deg", "github4s", Some("true"))

listContributors.exec[cats.Id, HttpResponse[String]]() match {
  case Left(e) => println("Something went wrong: s{e.getMessage}")
  case Right(r) => println(r.result)
}
```

The `result` on the right is the corresponding [List[User]][user-scala].

See [the API doc](https://developer.github.com/v3/repos/#list-contributors) for full
reference.

## Commits

### List commits on a repository

You can list commits using `listCommits`, it takes as arguments:

- the repository coordinates (`owner` and `name` of the repository).
- `SHA` or branch to start listing commits from. Default: the repository’s default branch (usually `master`).
- `path`: Only commits containing this file path will be returned.
- `author`: GitHub login or email address by which to filter by commit author.
- `since`: Only commits after this date will be returned. Format: "YYYY-MM-DDTHH:MM:SSZ".
- `until`: Only commits before this date will be returned. Format: "YYYY-MM-DDTHH:MM:SSZ".
- `pagination`: Limit and Offset for pagination.

To list commits:

```tut:silent
val listCommits =
  Github(accessToken).repos.listCommits(
  "47deg",
  "github4s",
  Some("d3b048c1f500ee5450e5d7b3d1921ed3e7645891"),
  Some("README.md"),
  Some("developer@47deg.com"),
  Some("2014-11-07T22:01:45Z"),
  Some("2014-11-07T22:01:45Z"))

listCommits.exec[cats.Id, HttpResponse[String]]() match {
  case Left(e) => println("Something went wrong: s{e.getMessage}")
  case Right(r) => println(r.result)
}
```

The `result` on the right is the corresponding [List[Commit]][repository-scala].

See [the API doc](https://developer.github.com/v3/repos/commits/#list-commits-on-a-repository) for full
reference.

## Contents

### Get contents

This method returns the contents of a file or directory in a repository.

You can get contents using `getContents`, it takes as arguments:

- the repository coordinates (`owner` and `name` of the repository).
- `path`: The content path.
- `ref`: The name of the `commit/branch/tag`. Default: the repository’s default branch (usually `master`).

To get contents:

```tut:silent
val getContents =
  Github(accessToken).repos.getContents("47deg", "github4s", "README.md", Some("heads/master"))

getContents.exec[cats.Id, HttpResponse[String]]() match {
  case Left(e) => println("Something went wrong: s{e.getMessage}")
  case Right(r) => println(r.result)
}
```

The `result` on the right is the corresponding [NonEmptyList[Content]][repository-scala].

See [the API doc](https://developer.github.com/v3/repos/contents/#get-contents) for full
reference.

## Releases

### Create a release

Users with push access to the repository can create a release using `createRelease`; it takes as arguments:

- the repository coordinates (`owner` and `name` of the repository).
- `tag_name`: The name of the tag.
- `name`: The name of the release.
- `body`: Text describing the contents of the tag.
- `target_commitish`: Specifies the commitish value that determines where the `Git tag` is created from.
Can be any branch or commit `SHA`. Unused if the `Git tag` already exists. Default: the repository's default branch (usually `master`).
- `draft`: true to create a draft (unpublished) release, false to create a published one. Default: false.
- `prerelease`: true to identify the release as a pre-release. false to identify the release as a full release. Default: false.

To create a release:

```scala
val createRelease =
  Github(accessToken).repos.createRelease("47deg", "github4s", "v0.1.0", "v0.1.0", "New access token", Some("master"), Some(false), Some(false))

createRelease.exec[cats.Id, HttpResponse[String]]() match {
  case Left(e) => println("Something went wrong: s{e.getMessage}")
  case Right(r) => println(r.result)
}
```

The `result` on the right is the created [Release][repository-scala].

See [the API doc](https://developer.github.com/v3/repos/releases/#create-a-release) for full
reference.

## Status

### Create a status

You can create a status using `createStatus`; it takes as arguments:

- the repository coordinates (`owner` and `name` of the repository).
- the `SHA` of the commit for which we want to create a status.
- the state of the status we want to create (can be pending, success, failure or error).
- other optional parameters: target url, description and context.

To create a status:

```scala
val createStatus =
  Github(accessToken).repos.createStatus("47deg", "github4s", "aaaaaa", "pending")

createStatus.exec[cats.Id, HttpResponse[String]]() match {
  case Left(e) => println("Something went wrong: s{e.getMessage}")
  case Right(r) => println(r.result)
}
```

The `result` on the right is the created [Status][repository-scala].

See [the API doc](https://developer.github.com/v3/repos/statuses/#create-a-status) for full
reference.

### List status for a specific Ref


You can also list status through `listStatus`; it take as arguments:

- the repository coordinates (`owner` and `name` of the repository).
- a git ref (a `SHA`, a branch `name` or a tag `name`).

To list the statuses for a specific ref:

```tut:silent
val listStatus =
  Github(accessToken).repos.listStatus("47deg", "github4s", "heads/master")

listStatus.exec[cats.Id, HttpResponse[String]]() match {
  case Left(e) => println("Something went wrong: s{e.getMessage}")
  case Right(r) => println(r.result)
}
```

The `result` on the right is the corresponding [List[Status]][repository-scala].

See [the API doc](https://developer.github.com/v3/repos/statuses/#list-statuses-for-a-specific-ref)
for full reference.

### Get the combined status for a specific Ref

Lastly, you can also get the combined status thanks to `getCombinedStatus`; it takes the same
arguments as the operation listing statuses:

```tut:silent
val combinedStatus =
  Github(accessToken).repos.getCombinedStatus("47deg", "github4s", "heads/master")

combinedStatus.exec[cats.Id, HttpResponse[String]]() match {
  case Left(e) => println("Something went wrong: s{e.getMessage}")
  case Right(r) => println(r.result)
}
```

The `result` on the right is the corresponding [CombinedStatus][repository-scala].

Note that the state of the combined status is the product of a heuristic detailed in
[the API documentation](https://developer.github.com/v3/repos/statuses/#get-the-combined-status-for-a-specific-ref).

As you can see, a few features of the repository endpoint are missing.

As a result, if you'd like to see a feature supported, feel free to create an issue and/or a pull request!

[repository-scala]: https://github.com/47deg/github4s/blob/master/github4s/shared/src/main/scala/github4s/free/domain/Repository.scala
[user-scala]: https://github.com/47deg/github4s/blob/master/github4s/shared/src/main/scala/github4s/free/domain/User.scala