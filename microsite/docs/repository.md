---
layout: docs
title: Repository API
permalink: repository
---

# Repository API

Github4s supports the [Repository API](https://developer.github.com/v3/repos/). As a result,
with Github4s, you can interact with:

- [Repositories](#repositories)
  - [Get a repository](#get-a-repository)
  - [List organization repositories](#list-organization-repositories)
  - [List user repositories](#list-user-repositories)
  - [List contributors](#list-contributors)
  - [List collaborators](#list-collaborators)
  - [Check user is a repository collaborator](#check-if-a-user-is-a-repository-collaborator)
  - [Get repository permissions for a user](#get-repository-permissions-for-a-user)
- [Commits](#commits)
  - [List commits on a repository](#list-commits-on-a-repository)
- [Contents](#contents)
  - [Get contents](#get-contents)
  - [Create a File](#create-a-file)
  - [Update a File](#update-a-file)
  - [Delete a File](#delete-a-file)
- [Releases](#releases)
  - [List of releases](#list-of-releases)
  - [Get a single release](#get-a-single-release)
  - [The latest release](#the-latest-release)
  - [Create a release](#create-a-release)
- [Statuses](#statuses)
  - [Create a status](#create-a-status)
  - [List statuses for a specific Ref](#list-statuses-for-a-specific-ref)
  - [Get the combined status of a specific Ref](#get-the-combined-status-for-a-specific-ref)

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

val accessToken = sys.env.get("GITHUB_TOKEN")
val gh = Github[IO](httpClient, accessToken)
```

## Repositories

### Get a repository

You can get a repository using `get`; it takes as arguments:

- the repository coordinates (`owner` and `name` of the repository).

To get a repository:

```scala mdoc:compile-only
val getRepo = gh.repos.get("47degrees", "github4s")
val response = getRepo.unsafeRunSync()
response.result match {
  case Left(e) => println(s"Something went wrong: ${e.getMessage}")
  case Right(r) => println(r)
}
```

The `result` on the right is the get [Repository][repository-scala].

See [the API doc](https://developer.github.com/v3/repos/#get) for full
reference.

### List organization repositories

You can retrieve the list of repositories for a particular organization using `listOrgRepos`; it
takes as arguments:

- `org`: The organization name.
- `type`: The optional type of the returned repositories, can be "all", "public", "private",
"forks", "sources" or "member", defaults to "all".
- `pagination`: Limit and Offset for pagination.

To list the repositories for an organization:

```scala mdoc:compile-only
val listOrgRepos = gh.repos.listOrgRepos("47deg")
val response = listOrgRepos.unsafeRunSync()
response.result match {
  case Left(e) => println(s"Something went wrong: ${e.getMessage}")
  case Right(r) => println(r)
}
```

The `result` on the right is the corresponding [List[Repository]][repository-scala].

See [the API doc](https://developer.github.com/v3/repos/#list-organization-repositories) for full
reference.

### List user repositories

You can retrieve the list of repositories for a particular user using `listUserRepos`; it
takes as arguments:

- `user`: The user name.
- `type`: The optional type of the returned repositories, can be "all", "owner" or "member", defaults to "owner".
- `pagination`: Limit and Offset for pagination.

To list the repositories for a user:

```scala mdoc:compile-only
val listUserRepos = gh.repos.listUserRepos("rafaparadela")
val response = listUserRepos.unsafeRunSync()
response.result match {
  case Left(e) => println(s"Something went wrong: ${e.getMessage}")
  case Right(r) => println(r)
}
```

The `result` on the right is the corresponding [List[Repository]][repository-scala].

See [the API doc](https://developer.github.com/v3/repos/#list-repositories-for-a-user) for full
reference.

### List contributors

List contributors to the specified repository,
sorted by the number of commits per contributor in descending order.

You can list contributors using `listContributors`, it takes as arguments:

- the repository coordinates (`owner` and `name` of the repository).
- `anon` Set to 1 or true to include anonymous contributors in results.
- `pagination`: Limit and Offset for pagination, optional.

To list contributors:

```scala mdoc:compile-only
val listContributors = gh.repos.listContributors("47degrees", "github4s", Some("true"))
val response = listContributors.unsafeRunSync()
response.result match {
  case Left(e) => println(s"Something went wrong: ${e.getMessage}")
  case Right(r) => println(r)
}
```

The `result` on the right is the corresponding [List[User]][user-scala].

See [the API doc](https://developer.github.com/v3/repos/#list-contributors) for full
reference.

### List collaborators

List collaborators in the specified repository.

You can list collaborators using `listCollaborators`, it takes as arguments:

- the repository coordinates (`owner` and `name` of the repository).
- `affiliation`, one of `outside`, `direct`, or `all` (default `all`).
For more information take a look at [the API doc](https://developer.github.com/v3/repos/collaborators/#parameters).
- `pagination`: Limit and Offset for pagination, optional.

```scala mdoc:compile-only
val listCollaborators = gh.repos.listCollaborators("47degrees", "github4s", Some("all"))
val response = listCollaborators.unsafeRunSync()
response.result match {
  case Left(e) => println(s"Something went wrong: ${e.getMessage}")
  case Right(r) => println(r)
}
```

The `result` on the right is the corresponding [List[User]][user-scala].

See [the API doc](https://developer.github.com/v3/repos/collaborators/#list-collaborators) for full
reference.

### Check if a user is a repository collaborator

Returns whether a given user is a repository collaborator or not.

```scala mdoc:compile-only
val userIsCollaborator = gh.repos.userIsCollaborator("47degrees", "github4s", "rafaparadela")
val response = userIsCollaborator.unsafeRunSync()
response.result match {
  case Left(e) => println(s"Something went wrong: ${e.getMessage}")
  case Right(r) => println(r)
}
```

The `result` on the right is `Boolean`

See [the API doc](https://developer.github.com/v3/repos/collaborators/#check-if-a-user-is-a-repository-collaborator) 
for full reference.

### Get repository permissions for a user

Checks the repository permission of a collaborator. 

The possible repository permissions are `admin`, `write`, `read`, and `none`.

```scala mdoc:compile-only
val userRepoPermission = gh.repos.getRepoPermissionForUser("47degrees", "github4s", "rafaparadela")
val response = userRepoPermission.unsafeRunSync()
response.result match {
  case Left(e) => println(s"Something went wrong: ${e.getMessage}")
  case Right(r) => println(r)
}
```

The `result` on the right is the corresponding [UserRepoPermission][repository-scala].

See [the API doc](https://developer.github.com/v3/repos/collaborators/#get-repository-permissions-for-a-user) for full
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

```scala mdoc:compile-only
val listCommits =
  gh.repos.listCommits(
  "47deg",
  "github4s",
  Some("d3b048c1f500ee5450e5d7b3d1921ed3e7645891"),
  Some("README.md"),
  Some("developer@47deg.com"),
  Some("2014-11-07T22:01:45Z"),
  Some("2014-11-07T22:01:45Z"))
val response = listCommits.unsafeRunSync()
response.result match {
  case Left(e) => println(s"Something went wrong: ${e.getMessage}")
  case Right(r) => println(r)
}
```

The `result` on the right is the corresponding [List[Commit]][repository-scala].

See [the API doc](https://developer.github.com/v3/repos/commits/#list-commits-on-a-repository) for full
reference.

## Branches

### List branches on a repository

You can list branches using `listBranches`, it takes as arguments:

- the repository coordinates (`owner` and `name` of the repository).
- `protected` Only protected branches.
- `pagination`: Limit and Offset for pagination, optional.

To list branches:

```scala mdoc:compile-only
val listBranches =
  gh.repos.listBranches(
  "47deg",
  "github4s")
val response = listBranches.unsafeRunSync()
response.result match {
  case Left(e) => println(s"Something went wrong: ${e.getMessage}")
  case Right(r) => println(r)
}
```

The `result` on the right is the corresponding [List[Branch]][repository-scala].

See [the API doc](https://developer.github.com/v3/repos/branches/#list-branches) for full reference.

## Contents

### Get contents

This method returns the contents of a file or directory in a repository.

You can get contents using `getContents`, it takes as arguments:

- the repository coordinates (`owner` and `name` of the repository).
- `path`: The content path.
- `ref`: The name of the `commit/branch/tag`. Default: the repository’s default branch (usually `master`).
- `pagination`: Limit and Offset for pagination, optional.

To get contents:

```scala mdoc:compile-only
val getContents = gh.repos.getContents("47degrees", "github4s", "README.md", Some("heads/master"))
val response = getContents.unsafeRunSync()
response.result match {
  case Left(e) => println(s"Something went wrong: ${e.getMessage}")
  case Right(r) => println(r)
}
```

The `result` on the right is the corresponding [NonEmptyList[Content]][repository-scala].

See [the API doc](https://developer.github.com/v3/repos/contents/#get-contents) for full
reference.

### Create a File

This method creates a new file in an existing repository.

You can create a new file using `createFile`, it takes as arguments:

- the repository coordinates (`owner` and `name` of the repository).
- `path`: The path of the new file to be created, *without* a leading slash.
- `message`: The message to use for creating the commit.
- `content`: The content of the new file, as an array of bytes.
- `branch`: The branch to add the commit to. If omitted, this defaults to the repository's default branch.
- `committer`: An optional committer to associate with the commit. If omitted, the authenticated user's information is used for the commit.
- `author`: An optional author to associate with the commit. If omitted, the committer is used (if present).

To create a file:
```scala mdoc:compile-only
val getContents = gh.repos.createFile("47degrees", "github4s", "new-file.txt", "create a new file", "file contents".getBytes)

getContents.unsafeRunSync().result match {
  case Left(e) => println(s"We could not create your file because ${e.getMessage}")
  case Right(r) => println(r)
}
```

See [the API doc](https://developer.github.com/v3/repos/contents/#create-or-update-a-file) for full reference.

### Update a File

This method updates an existing file in a repository.

You can create a new file using `createFile`, it takes as arguments:

- the repository coordinates (`owner` and `name` of the repository).
- `path`: The path of the new file to be created, *without* a leading slash.
- `message`: The message to use for creating the commit.
- `content`: The content of the new file, as an array of bytes.
- `sha`: The blob SHA of the file being replaced. (This is returned as part of a `getContents` call). GitHub uses this value to perform optimistic locking. If the file has been updated since, the update call will fail.
- `branch`: The branch to add the commit to. If omitted, this defaults to the repository's default branch.
- `committer`: An optional committer to associate with the commit. If omitted, the authenticated user's information is used for the commit.
- `author`: An optional author to associate with the commit. If omitted, the committer is used (if present).

To create a file:
```scala mdoc:compile-only
val getContents = gh.repos.updateFile("47degrees", "github4s", "README.md", "A terser README.", "You read me right.".getBytes,"a52d080d2cf85e08bfcb441b437d3982398e8f8f6a58388f55d6b6cf51cb5365")

getContents.unsafeRunSync().result match {
  case Left(e) => println(s"We could not update your file because ${e.getMessage}")
  case Right(r) => println(r)
}
```

See [the API doc](https://developer.github.com/v3/repos/contents/#create-or-update-a-file) for full reference.

### Delete a File

This method deletes an existing file in a repository.

You can create a new file using `deleteFile`, it takes as arguments:

- the repository coordinates (`owner` and `name` of the repository).
- `path`: The path of the new file to be created, *without* a leading slash.
- `message`: The message to use for creating the commit.
- `sha`: The blob SHA of the file being replaced. (This is returned as part of a `getContents` call). GitHub uses this value to perform optimistic locking. If the file has been updated since, the update call will fail.
- `branch`: The branch to add the commit to. If omitted, this defaults to the repository's default branch.
- `committer`: An optional committer to associate with the commit. If omitted, the authenticated user's information is used for the commit.
- `author`: An optional author to associate with the commit. If omitted, the committer is used (if present).

To create a file:
```scala mdoc:compile-only
val getContents = gh.repos.deleteFile("47degrees", "github4s", "README.md", "Actually, we don't need a README.", "a52d080d2cf85e08bfcb441b437d3982398e8f8f6a58388f55d6b6cf51cb5365")

getContents.unsafeRunSync().result match {
  case Left(e) => println(s"We could not delete this file because ${e.getMessage}")
  case Right(r) => println(r)
}
```

See [the API doc](https://developer.github.com/v3/repos/contents/#delete-a-file) for full reference.

## Releases

### List of releases

You can list releases using `listReleases`, it takes as arguments:

- the repository coordinates (`owner` and `name` of the repository).
- `pagination` Limit and Offset for pagination, optional.

To list releases:

```scala mdoc:compile-only
val listReleases =
  gh.repos.listReleases(
  "47deg",
  "github4s",
   None,
   Map.empty)
val response = listReleases.unsafeRunSync()
response.result match {
  case Left(e) => println(s"Something went wrong: ${e.getMessage}")
  case Right(r) => println(r)
}
```

The `result` on the right is the corresponding [List[Release]][repository-scala].

### Get a single release

You can get a release using `getRelease`, it takes as arguments:

- the release coordinates (`releaseId` the id of the release)
- the repository coordinates (`owner` and `name` of the repository).

Get a release by release id:

```scala mdoc:compile-only
val getRelease =
  gh.repos.getRelease(
  123,
  "47deg",
  "github4s")
val response = getRelease.unsafeRunSync()
response.result match {
  case Left(e) => println(s"Something went wrong: ${e.getMessage}")
  case Right(r) => println(r)
}
```

The `result` on the right is the corresponding [Option[Release]][repository-scala].


### The latest release

You can get the latest release using `latestRelease`, it takes as arguments:

- the repository coordinates (`owner` and `name` of the repository).

Get the latest release:

```scala mdoc:compile-only
val latestRelease =
  gh.repos.latestRelease(
  "47deg",
  "github4s")
val response = latestRelease.unsafeRunSync()
response.result match {
  case Left(e) => println(s"Something went wrong: ${e.getMessage}")
  case Right(r) => println(r)
}
```

The `result` on the right is the corresponding [Option[Release]][repository-scala].

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

```scala mdoc:compile-only
val createRelease =
  gh.repos.createRelease("47degrees", "github4s", "v0.1.0", "v0.1.0", "New access token", Some("master"), Some(false), Some(false))
val response = createRelease.unsafeRunSync()
response.result match {
  case Left(e) => println(s"Something went wrong: ${e.getMessage}")
  case Right(r) => println(r)
}
```

The `result` on the right is the created [Release][repository-scala].

See [the API doc](https://developer.github.com/v3/repos/releases/#create-a-release) for full
reference.

## Statuses

### Create a status

You can create a status using `createStatus`; it takes as arguments:

- the repository coordinates (`owner` and `name` of the repository).
- the `SHA` of the commit for which we want to create a status.
- the state of the status we want to create (can be pending, success, failure or error).
- other optional parameters: target url, description and context.

To create a status:

```scala mdoc:compile-only
val createStatus = gh.repos.createStatus("47degrees", "github4s", "aaaaaa", "pending", None, None, None)
val response = createStatus.unsafeRunSync()
response.result match {
  case Left(e) => println(s"Something went wrong: ${e.getMessage}")
  case Right(r) => println(r)
}
```

The `result` on the right is the created [Status][repository-scala].

See [the API doc](https://developer.github.com/v3/repos/statuses/#create-a-status) for full
reference.

### List statuses for a specific ref

You can also list statuses through `listStatuses`; it take as arguments:

- the repository coordinates (`owner` and `name` of the repository).
- a git ref (a `SHA`, a branch `name` or a tag `name`).
- `pagination`: Limit and Offset for pagination, optional.

To list the statuses for a specific ref:

```scala mdoc:compile-only
val listStatuses = gh.repos.listStatuses("47degrees", "github4s", "heads/master")
val response = listStatuses.unsafeRunSync()
response.result match {
  case Left(e) => println(s"Something went wrong: ${e.getMessage}")
  case Right(r) => println(r)
}
```

The `result` on the right is the corresponding [List[Status]][repository-scala].

See [the API doc](https://developer.github.com/v3/repos/statuses/#list-statuses-for-a-specific-ref)
for full reference.

### Get the combined status for a specific ref

Lastly, you can also get the combined status thanks to `getCombinedStatus`; it takes the same
arguments as the operation listing statuses:

```scala mdoc:compile-only
val combinedStatus = gh.repos.getCombinedStatus("47degrees", "github4s", "heads/master")
val response = combinedStatus.unsafeRunSync()
response.result match {
  case Left(e) => println(s"Something went wrong: ${e.getMessage}")
  case Right(r) => println(r)
}
```

The `result` on the right is the corresponding [CombinedStatus][repository-scala].

Note that the state of the combined status is the product of a heuristic detailed in
[the API documentation](https://developer.github.com/v3/repos/statuses/#get-the-combined-status-for-a-specific-ref).

As you can see, a few features of the repository endpoint are missing.

As a result, if you'd like to see a feature supported, feel free to create an issue and/or a pull request!

[repository-scala]: https://github.com/47degrees/github4s/blob/master/github4s/src/main/scala/github4s/domain/Repository.scala
[user-scala]: https://github.com/47degrees/github4s/blob/master/github4s/src/main/scala/github4s/domain/User.scala
