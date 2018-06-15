---
layout: docs
title: Issue API
---

# Issue API

Github4s supports the [Issue API](https://developer.github.com/v3/issues/). As a result,
with Github4s, you can interact with:

- [Issues](#issues)
  - [Create an issue](#create-an-issue)
  - [Edit an issue](#edit-an-issue)
  - [List issues](#list-issues)
  - [Search issues](#search-issues)
- [Comments](#comments)
  - [Create a comment](#create-a-comment)
  - [Edit a comment](#edit-a-comment)
  - [Delete a comment](#delete-a-comment)

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

## Issues

### Create an issue

You can create an issue using `createIssue`; it takes as arguments:

- the repository coordinates (`owner` and `name` of the repository).
- the content of the issue (`title` and `body`).
- other optional parameters: `milestone id`, `labels` and `assignees` which are only taken into account
if you have push access to the repository.

To create an issue:

```scala
val createIssue =
  Github(accessToken).issues.createIssue("47deg", "github4s", "Github4s", "is awesome")

createIssue.exec[cats.Id, HttpResponse[String]]() match {
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

```scala
val editIssue =
  Github(accessToken).issues.editIssue("47deg", "github4s", 1, "open", "Github4s", "is still awesome")

editIssue.exec[cats.Id, HttpResponse[String]]() match {
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

```tut:silent
val listIssues = Github(accessToken).issues.listIssues("47deg", "github4s")

listIssues.exec[cats.Id, HttpResponse[String]]() match {
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

```tut:silent
val issue = Github(accessToken).issues.getIssue("47deg", "github4s", 123)

issue.exec[cats.Id, HttpResponse[String]]() match {
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

```tut:silent
import github4s.free.domain._
val searchParams = List(
  OwnerParamInRepository("scala/bug"),
  IssueTypeIssue,
  SearchIn(Set(SearchInTitle))
)
val searchIssues = Github(accessToken).issues.searchIssues("existential", searchParams)

searchIssues.exec[cats.Id, HttpResponse[String]]() match {
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

```tut:silent
val commentList = Github(accessToken).issues.listComments("47deg", "github4s", 123)
commentList.exec[cats.Id, HttpResponse[String]]() match {
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

```scala
val createcomment = Github(accessToken).issues.create("47deg", "github4s", 123, "this is the comment")
createcomment.exec[cats.Id, HttpResponse[String]]() match {
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

```scala
val editComment = Github(accessToken).issues.edit("47deg", "github4s", 20, "this is the new comment")
editComment.exec[cats.Id, HttpResponse[String]]() match {
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

```scala
val deleteComment = Github(accessToken).issues.delete("47deg", "github4s", 20)
deleteComment.exec[cats.Id, HttpResponse[String]]() match {
  case Left(e) => println(s"Something went wrong: ${e.getMessage}")
  case Right(r) => println(r.result)
}
```

The `result` on the right is `Unit`.

See [the API doc](https://developer.github.com/v3/issues/comments/#delete-a-comment) for full reference.

## Labels

### List labels

You can list labels for an issue with the following parameters:

 - the repository coordinates (`owner` and `name` of the repository).
 - `number`: The issue number.

 To list labels:

```tut:silent
val labelList = Github(accessToken).issues.listLabels("47deg", "github4s", 123)
labelList.exec[cats.Id, HttpResponse[String]]() match {
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

```tut:silent
val assignedLabelList = Github(accessToken).issues.addLabels("47deg", "github4s", 123, List("bug", "code review"))
assignedLabelList.exec[cats.Id, HttpResponse[String]]() match {
  case Left(e) => println(s"Something went wrong: ${e.getMessage}")
  case Right(r) => println(r.result)
}
```

The `result` on the right is the corresponding assigned [List[Label]][issue-scala]

See [the API doc](https://developer.github.com/v3/issues/labels/#add-labels-to-an-issue) for full reference.

### Remove label

You can remove a label from an issue with the following parameters:

 - the repository coordinates (`owner` and `name` of the repository).
 - `number`: The issue number.
 - `label`: The label that requires removing.

 To remove an existing label from an issue:

```tut:silent
val removedLabelList = Github(accessToken).issues.removeLabel("47deg", "github4s", 123, "bug")
removedLabelList.exec[cats.Id, HttpResponse[String]]() match {
  case Left(e) => println(s"Something went wrong: ${e.getMessage}")
  case Right(r) => println(r.result)
}
```

The `result` on the right is the corresponding removed [List[Label]][issue-scala]

See [the API doc](https://developer.github.com/v3/issues/labels/#remove-a-label-from-an-issue) for full reference.

As you can see, a few features of the issue endpoint are missing.

As a result, if you'd like to see a feature supported, feel free to create an issue and/or a pull request!

[issue-scala]: https://github.com/47deg/github4s/blob/master/github4s/shared/src/main/scala/github4s/free/domain/Issue.scala
