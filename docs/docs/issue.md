---
layout: docs
title: Issue API
permalink: issue
---

# Issue API

Github4s supports the [Issue API](https://developer.github.com/v3/issues/). As a result,
with Github4s, you can interact with:

- [Issues](#issues)
  - [Create an issue](#create-an-issue)
  - [Edit an issue](#edit-an-issue)
  - [List issues](#list-issues)
  - [Get a single issue](#get-a-single-issue)
  - [Search issues](#search-issues)
- [Comments](#comments)
  - [List comments](#list-comments)
  - [Create a comment](#create-a-comment)
  - [Edit a comment](#edit-a-comment)
  - [Delete a comment](#delete-a-comment)
- [Labels](#labels)
  - [List labels for this repository](#list-labels-for-this-repository)
  - [List labels](#list-labels)
  - [Add labels](#add-labels)
  - [Remove a label](#remove-a-label)
- [Assignees](#assignees)
  - [List available assignees](#list-available-assignees)
- [Milestones](#milestones)
  - [List milestones for a respository](#list-milestones-for-a-repository)

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

## Issues

### Create an issue

You can create an issue using `createIssue`; it takes as arguments:

- the repository coordinates (`owner` and `name` of the repository).
- the content of the issue (`title` and `body`).
- other optional parameters: `milestone id`, `labels` and `assignees` which are only taken into account
if you have push access to the repository.

To create an issue:

```scala mdoc:compile-only
val createIssue =
  Github[IO](accessToken).issues.createIssue("47deg", "github4s", "Github4s", "is awesome", None, List("Label"), List("Assignee"))

createIssue.unsafeRunSync match {
  case Left(e) => println(s"Something went wrong: ${e.getMessage}")
  case Right(r) => println(r.result)
}
```

The `result` on the right is the created [Issue][issue-scala].

See [the API doc](https://developer.github.com/v3/issues/#create-an-issue) for full reference.


### Edit an issue

You can edit an existing issue using `editIssue`; it takes as arguments:

- the repository coordinates (`owner` and `name` of the repository).
- the issue `number`.
- the updated `state` of the issue (open or closed).
- the edited `content` of the issue (title and body).
- other optional parameters: `milestone id`, `labels` and `assignees` which are only taken into account
if you have push access to the repository.

To edit an issue:

```scala mdoc:compile-only
val editIssue =
  Github[IO](accessToken).issues.editIssue("47deg", "github4s", 1, "open", "Github4s", "is still awesome", None, List("Label"), List("Assignee"))

editIssue.unsafeRunSync match {
  case Left(e) => println(s"Something went wrong: ${e.getMessage}")
  case Right(r) => println(r.result)
}
```

the `result` on the right is the edited [Issue][issue-scala].

See [the API doc](https://developer.github.com/v3/issues/#edit-an-issue) for full reference.


### List issues

You can also list issues for a repository through `listIssues`; it takes as arguments:

- the repository coordinates (`owner` and `name` of the repository).

To list the issues for a repository:

```scala mdoc:compile-only
val listIssues = Github[IO](accessToken).issues.listIssues("47deg", "github4s")

listIssues.unsafeRunSync match {
  case Left(e) => println(s"Something went wrong: ${e.getMessage}")
  case Right(r) => println(r.result)
}
```

The `result` on the right is the corresponding [List[Issue]][issue-scala]. Note that it will
contain pull requests as Github considers pull requests as issues.

See [the API doc](https://developer.github.com/v3/issues/#list-issues-for-a-repository)
for full reference.

### Get a single issue

You can also get a single issue of a repository through `getIssue`; it takes as arguments:

- the repository coordinates (`owner` and `name` of the repository).
- `number`: The issue number.

To get a single issue from a repository:

```scala mdoc:compile-only
val issue = Github[IO](accessToken).issues.getIssue("47deg", "github4s", 17)

issue.unsafeRunSync match {
  case Left(e) => println(s"Something went wrong: ${e.getMessage}")
  case Right(r) => println(r.result)
}
```

The `result` on the right is the corresponding [Issue][issue-scala]. Note that it will
return pull requests as Github considers pull requests as issues.

See [the API doc](https://developer.github.com/v3/issues/#get-a-single-issue)
for full reference.

### Search issues

Lastly, you can also search issues all across Github thanks to `searchIssues`; it takes as
arguments:

- a `query` string (the URL encoding is taken care of by Github4s).
- a list of [SearchParam](https://github.com/47deg/github4s/blob/master/github4s/shared/src/main/scala/github4s/free/domain/SearchParam.scala).

Let's say we want to search for the Scala bugs (<https://github.com/scala/bug>) which contain
the "existential" keyword in their title:

```scala mdoc:compile-only
import github4s.domain._
val searchParams = List(
  OwnerParamInRepository("scala/bug"),
  IssueTypeIssue,
  SearchIn(Set(SearchInTitle))
)
val searchIssues = Github[IO](accessToken).issues.searchIssues("existential", searchParams)

searchIssues.unsafeRunSync match {
  case Left(e) => println(s"Something went wrong: ${e.getMessage}")
  case Right(r) => println(r.result)
}
```

The `result` on the right is a [SearchIssuesResult][issue-scala].

See [the API doc](https://developer.github.com/v3/search/#search-issues) for full reference.

## Comments

### List comments

You can list comments of an issue with the following parameters:

 - the repository coordinates (`owner` and `name` of the repository).
 - `number`: The issue number.

 To list comments:

```scala mdoc:compile-only
val commentList = Github[IO](accessToken).issues.listComments("47deg", "github4s", 17)
commentList.unsafeRunSync match {
  case Left(e) => println(s"Something went wrong: ${e.getMessage}")
  case Right(r) => println(r.result)
}
```

The `result` on the right is the corresponding [List[Comment]][issue-scala]

See [the API doc](https://developer.github.com/v3/issues/comments/#list-comments-on-an-issue) for full reference.

### Create a comment

You can create a comment for an issue with the following parameters:

 - the repository coordinates (`owner` and `name` of the repository).
 - `number`: The issue number.
 - `body`: The comment description.

 To create a comment:

```scala mdoc:compile-only
val createcomment = Github[IO](accessToken).issues.createComment("47deg", "github4s", 17, "this is the comment")
createcomment.unsafeRunSync match {
  case Left(e) => println(s"Something went wrong: ${e.getMessage}")
  case Right(r) => println(r.result)
}
```

The `result` on the right is a [Comment][issue-scala].

See [the API doc](https://developer.github.com/v3/issues/comments/#create-a-comment) for full reference.


### Edit a comment

You can edit a comment from an issue with the following parameters:

 - the repository coordinates (`owner` and `name` of the repository).
 - `id`: The comment id.
 - `body`: The new comment description.

 To edit a comment:

```scala mdoc:compile-only
val editComment = Github[IO](accessToken).issues.editComment("47deg", "github4s", 20, "this is the new comment")
editComment.unsafeRunSync match {
  case Left(e) => println(s"Something went wrong: ${e.getMessage}")
  case Right(r) => println(r.result)
}
```

The `result` on the right is a [Comment][issue-scala].

See [the API doc](https://developer.github.com/v3/issues/comments/#edit-a-comment) for full reference.


### Delete a comment

You can delete a comment from an issue with the following parameters:

 - the repository coordinates (`owner` and `name` of the repository).
 - `id`: The comment id.

 To delete a comment:

```scala mdoc:compile-only
val deleteComment = Github[IO](accessToken).issues.deleteComment("47deg", "github4s", 20)
deleteComment.unsafeRunSync match {
  case Left(e) => println(s"Something went wrong: ${e.getMessage}")
  case Right(r) => println(r.result)
}
```

The `result` on the right is `Unit`.

See [the API doc](https://developer.github.com/v3/issues/comments/#delete-a-comment) for full reference.

## Labels

### List labels for this repository

You can list labels for an issue with the following parameters:

 - the repository coordinates (`owner` and `name` of the repository).
 - `pagination`: Limit and Offset for pagination, optional.

 To list labels:

```scala mdoc:compile-only
val labelListRepository = Github[IO](accessToken).issues.listLabelsRepository("47deg", "github4s")
labelListRepository.unsafeRunSync match {
  case Left(e) => println(s"Something went wrong: ${e.getMessage}")
  case Right(r) => println(r.result)
}
```

The `result` on the right is the corresponding [List[Label]][issue-scala]

See [the API doc](https://developer.github.com/v3/issues/labels/#list-all-labels-for-this-repository) for full reference.


### List labels

You can list labels for an issue with the following parameters:

 - the repository coordinates (`owner` and `name` of the repository).
 - `number`: The issue number.

 To list labels:

```scala mdoc:compile-only
val labelList = Github[IO](accessToken).issues.listLabels("47deg", "github4s", 17)
labelList.unsafeRunSync match {
  case Left(e) => println(s"Something went wrong: ${e.getMessage}")
  case Right(r) => println(r.result)
}
```

The `result` on the right is the corresponding [List[Label]][issue-scala]

See [the API doc](https://developer.github.com/v3/issues/labels/#list-labels-on-an-issue) for full reference.

### Add labels

You can add existing labels to an issue with the following parameters:

 - the repository coordinates (`owner` and `name` of the repository).
 - `number`: The issue number.
 - `labels`: The existing labels that require adding.

 To add existing labels to an issue:

```scala mdoc:compile-only
val assignedLabelList = Github[IO](accessToken).issues.addLabels("47deg", "github4s", 17, List("bug", "code review"))
assignedLabelList.unsafeRunSync match {
  case Left(e) => println(s"Something went wrong: ${e.getMessage}")
  case Right(r) => println(r.result)
}
```

The `result` on the right is the corresponding assigned [List[Label]][issue-scala]

See [the API doc](https://developer.github.com/v3/issues/labels/#add-labels-to-an-issue) for full reference.

### Remove a label

You can remove a label from an issue with the following parameters:

 - the repository coordinates (`owner` and `name` of the repository).
 - `number`: The issue number.
 - `label`: The label that requires removing.

 To remove an existing label from an issue:

```scala mdoc:compile-only
val removedLabelList = Github[IO](accessToken).issues.removeLabel("47deg", "github4s", 17, "bug")
removedLabelList.unsafeRunSync match {
  case Left(e) => println(s"Something went wrong: ${e.getMessage}")
  case Right(r) => println(r.result)
}
```

The `result` on the right is the corresponding removed [List[Label]][issue-scala]

See [the API doc](https://developer.github.com/v3/issues/labels/#remove-a-label-from-an-issue) for full reference.

## Assignees

### List available assignees

You can list available assignees for issues in repo with the following parameters:

 - the repository coordinates (`owner` and `name` of the repository).
 - `pagination`: Limit and Offset for pagination, optional.

 To list available assignees:

```scala mdoc:compile-only
val assignees = Github[IO](accessToken).issues.listAvailableAssignees("47deg", "github4s")
assignees.unsafeRunSync match {
  case Left(e) => println(s"Something went wrong: ${e.getMessage}")
  case Right(r) => println(r.result)
}
```

The `result` on the right is the corresponding [List[User]][user-scala]

See [the API doc](https://developer.github.com/v3/issues/assignees/#list-assignees) for full reference.

As you can see, a few features of the issue endpoint are missing.

As a result, if you'd like to see a feature supported, feel free to create an issue and/or a pull request!

[issue-scala]: https://github.com/47deg/github4s/blob/master/github4s/src/main/scala/github4s/domain/Issue.scala
[user-scala]: https://github.com/47deg/github4s/blob/master/github4s/src/main/scala/github4s/domain/User.scala

## Milestones

### List milestones for a repository

You can list the milestone for a particular organization and repository with `listMilestones`; it takes arguments:

 - `owner`: name of the owner for which we want to retrieve the milestones.
 - `repo`: name of the repository for which we want to retrieve the milestones.
 - `state`: filter projects returned by their state. Can be either `open`, `closed`, `all`. Default: `open`, optional
 - `sort`: what to sort results by. Either `due_on` or `completeness`. Default: `due_on`, optional
 - `direction` the direction of the sort. Either `asc` or `desc`. Default: `asc`, optional
 - `pagination`: Limit and Offset for pagination, optional.
 - `header`: headers to include in the request, optional.

 To list the milestone for owner `47deg` and repository `github4s`:

```scala mdoc:compile-only
val milestones = Github[IO](accessToken).issues.listMilestones("47deg", "github4s", Some("open"), None, None)
milestones.unsafeRunSync match {
  case Left(e) => println(s"Something went wrong: ${e.getMessage}")
  case Right(r) => println(r.result)
}
```

The `result` on the right is the corresponding [List[Milestone]][milestone-scala]

See [the API doc](https://developer.github.com/v3/issues/milestones/#list-milestones-for-a-repository) for full reference.

[milestone-scala]: https://github.com/47deg/github4s/blob/master/github4s/src/main/scala/github4s/domain/Milestone.scala
