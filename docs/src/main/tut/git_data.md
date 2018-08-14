---
layout: docs
title: Git Data API
---

# Git Data API

Github4s supports the [Git Data API](https://developer.github.com/v3/git/). As a result,
with Github4s, you can:

- [Get a Reference](#get-a-reference)
- [Create a Reference](#create-a-reference)
- [Update a Reference](#update-a-reference)
- [Get a Commit](#get-a-commit)
- [Create a Commit](#create-a-commit)
- [Create a Blob](#create-a-blob)
- [Get a Tree](#get-a-tree)
- [Create a Tree](#create-a-tree)
- [Create a Tag](#create-a-tag)

For more information on the Git object database,
please read the [Git Internals](https://git-scm.com/book/en/v1/Git-Internals) chapter of the Pro Git book.

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

## Git Data

## References

### Get a Reference

The ref must be formatted as `heads/branch`, not just `branch`.
For example, the call to get the data, the `master` branch will be `heads/master`.

If the `ref` doesn't exist in the repository, but existing `refs` start with `ref` they will be
returned as an array. For example, a call to get the data for a branch named `feature`,
which doesn't exist, would return head refs including `featureA` and `featureB` which do.

You can get a reference using `getReference`, it takes as arguments:

- the repository coordinates (`owner` and `name` of the repository).
- `ref`: ref formatted as `heads/branch`.

```tut:silent
val getReference = Github(accessToken).gitData.getReference("47deg", "github4s", "heads/master")

getReference.exec[cats.Id, HttpResponse[String]]() match {
  case Left(e) => println(s"Something went wrong: s{e.getMessage}")
  case Right(r) => println(r.result)
}
```

The `result` on the right is the corresponding [List[Ref]][gitdata-scala].

See [the API doc](https://developer.github.com/v3/git/refs/#get-a-reference) for full reference.


### Create a Reference

The ref must be formatted as `heads/branch`, not just `branch`.
For example, the call to get the data, the `master` branch will be `heads/master`.

You can create a reference using `createReference`; it takes as arguments:

- the repository coordinates (`owner` and `name` of the repository).
- `ref`: The name of the fully qualified reference (e.g.: `refs/heads/master`).
If it doesn't start with 'refs' and has at least two slashes, it will be rejected.
- `sha`: the SHA1 value to set this reference.

```scala
val createReference = Github(accessToken).gitData.createReference(
  "47deg",
  "github4s",
  "refs/heads/master",
  "d3b048c1f500ee5450e5d7b3d1921ed3e7645891")

createReference.exec[cats.Id, HttpResponse[String]]() match {
  case Left(e) => println(s"Something went wrong: ${e.getMessage}")
  case Right(r) => println(r.result)
}
```

The `result` on the right is the created [Ref][gitdata-scala].

See [the API doc](https://developer.github.com/v3/git/refs/#create-a-reference) for full reference.


### Update a Reference

You can update a reference using `updateReference`; it takes as arguments:

- the repository coordinates (`owner` and `name` of the repository).
- `ref`: ref formatted as heads/branch.
- `sha`: the SHA1 value to set this reference.
- `force`: Indicates whether to force the update or to make sure the update is a fast-forward update.
Setting it to `false` will make sure you're not overwriting work. Default: `false`.

```scala
val updateReference = Github(accessToken).gitData.updateReference(
  "47deg",
  "github4s",
  "heads/master",
  "d3b048c1f500ee5450e5d7b3d1921ed3e7645891")

updateReference.exec[cats.Id, HttpResponse[String]]() match {
  case Left(e) => println(s"Something went wrong: ${e.getMessage}")
  case Right(r) => println(r.result)
}
```

The `result` on the right is the updated [Ref][gitdata-scala].

See [the API doc](https://developer.github.com/v3/git/refs/#update-a-reference) for full reference.

## Commits

### Get a Commit

You can get a commit using `getCommit`; it takes as arguments:

- the repository coordinates (`owner` and `name` of the repository).
- `sha`: the sha of the commit.

```tut:silent
val getCommit = Github(accessToken).gitData.getCommit("47deg", "github4s", "d3b048c1f500ee5450e5d7b3d1921ed3e7645891")

getCommit.exec[cats.Id, HttpResponse[String]]() match {
  case Left(e) => println(s"Something went wrong: ${e.getMessage}")
  case Right(r) => println(r.result)
}
```

The `result` on the right is the corresponding [RefCommit][gitdata-scala].

See [the API doc](https://developer.github.com/v3/git/commits/#get-a-commit) for full reference.


### Create a Commit

You can create a commit using `createCommit`; it takes as arguments:

- the repository coordinates (`owner` and `name` of the repository).
- `message`: the new commit's message.
- `tree`: the SHA of the tree object this commit points to.
- `parents`: the SHAs of the commits that are the parents of this commit. If omitted or empty,
the commit will be written as a root commit. For a single parent, an array of one SHA should be provided;
for a merge commit, an array of more than one should be provided.
- `author`: object containing information about the author.

```scala
val createCommit = Github(accessToken).gitData.createCommit(
  "47deg",
  "github4s",
  "New access token",
  "827efc6d56897b048c772eb4087f854f46256132",
  List("d3b048c1f500ee5450e5d7b3d1921ed3e7645891"))

createCommit.exec[cats.Id, HttpResponse[String]]() match {
  case Left(e) => println(s"Something went wrong: ${e.getMessage}")
  case Right(r) => println(r.result)
}
```

The `result` on the right is the created [RefCommit][gitdata-scala].

See [the API doc](https://developer.github.com/v3/git/commits/#create-a-commit) for full reference.

## Blobs

### Create a Blob

You can create a blob using `createBlob`; it takes as arguments:

- the repository coordinates (`owner` and `name` of the repository).
- `content`: the new blob's content.
- `encoding`: the encoding used for content. Currently, "utf-8" and "base64" are supported. Default: "utf-8".

```scala
val createBlob = Github(accessToken).gitData.createBlob("47deg", "github4s", "New access token")

createBlob.exec[cats.Id, HttpResponse[String]]() match {
  case Left(e) => println(s"Something went wrong: ${e.getMessage}")
  case Right(r) => println(r.result)
}
```

The `result` on the right is the created [RefObject][gitdata-scala].

See [the API doc](https://developer.github.com/v3/git/blobs/#create-a-blob) for full reference.

## Trees

As you probably know, Git can be considered as a tree structure.
Each commit creates a new node in that tree.
We can even assume that all the Git commands or methods provided by the API
are just tools to navigate this tree and to manipulate it.

In the following sections, we'll see how Github4s provides methods to wrap the Git API.


### Get a Tree

You can get a tree using `getTree`; it takes as arguments:

- the repository coordinates (`owner` and `name` of the repository).
- `sha`: the sha of the commit.
- `recursive`: flag whether to get the tree recursively.

```tut:silent
val getCommit = Github(accessToken).gitData.getTree("47deg", "github4s", "d3b048c1f500ee5450e5d7b3d1921ed3e7645891",true)

getCommit.exec[cats.Id, HttpResponse[String]]() match {
  case Left(e) => println(s"Something went wrong: ${e.getMessage}")
  case Right(r) => println(r.result)
}
```

The `result` on the right is the corresponding [TreeResult][gitdata-scala].

See [the API doc](https://developer.github.com/v3/git/trees/#get-a-tree) for full reference.

### Create a Tree

The tree creation API will take nested entries as well. If both a tree and a nested entries modifying
that tree are specified, it will overwrite the contents of that tree with the new path contents
write out a new tree.

IMPORTANT: If you don't set ´baseTree´, the commit will be created on top of everything;
however, it will only contain your changes, the rest of your files will show up as deleted.

You can create a tree using `createTree`; it takes as arguments:

- the repository coordinates (`owner` and `name` of the repository).
- `baseTree`: the SHA1 of the tree you want to update with new data.
- `treeDataList`: list (of path, mode, type, and sha/blob) specifying a tree structure.
- `path`: The file referenced in the tree.
- `mode`: The file mode; one of 100644 for file (blob), 100755 for executable (blob),
 040000 for subdirectory (tree), 160000 for submodule (commit),
 or 120000 for a blob that specifies the path of a symlink.
- `type`: Either blob, tree, or commit.
- `sha`: The SHA1 checksum ID of the object in the tree.
- `content`: The content you want this file to have.
 GitHub will write this blob out and use that SHA for this entry. Use either this or `tree.sha`.

```scala
val createTree = Github(accessToken).gitData.createTree(
  "47deg",
  "github4s",
  Some("827efc6d56897b048c772eb4087f854f46256132"),
  List(TreeDataSha(
    "project/plugins.sbt",
    "100644",
    "blob",
    "827efc6d56897b048c772eb4087f854f46256132")),
  "project/plugins.sbt",
  "100644",
  "blob",
  "827efc6d56897b048c772eb4087f854f46256132",
  "Sample Body")

createTree.exec[cats.Id, HttpResponse[String]]() match {
  case Left(e) => println(s"Something went wrong: ${e.getMessage}")
  case Right(r) => println(r.result)
}
```

The `result` on the right is the created [TreeResult][gitdata-scala].

See [the API doc](https://developer.github.com/v3/git/trees/#create-a-tree) for full reference.

## Tag

### Create a Tag

You can create a tag using `createTag`; it takes as arguments:

- the repository coordinates (`owner` and `name` of the repository).
- `tag`: the tag.
- `message`: the new tag message.
- `objectSha`: the SHA of the git object this is tagging.
- `objectType`: the type of the object we're tagging.
Normally this is a `commit`, but it can also be a `tree` or a `blob`.
- `tagger`: Optional object containing information about the individual creating the tag.

```scala
val createTag = Github(accessToken).gitData.createTag(
  "47deg",
  "github4s",
  "v0.1.1",
  "New access token",
  "d3b048c1f500ee5450e5d7b3d1921ed3e7645891",
  "commit",
  Some(RefAuthor("2014-11-07T22:01:45Z", "rafaparadela", "developer@47deg.com")))

createTag.exec[cats.Id, HttpResponse[String]]() match {
  case Left(e) => println(s"Something went wrong: ${e.getMessage}")
  case Right(r) => println(r.result)
}
```

The `result` on the right is the created [Tag][gitdata-scala].

See [the API doc](https://developer.github.com/v3/git/tags/#create-a-tag-object) for full reference.

As you can see, a few features of the git data endpoint are missing.

As a result, if you'd like to see a feature supported, feel free to create an issue and/or a pull request!

[gitdata-scala]: https://github.com/47deg/github4s/blob/master/github4s/shared/src/main/scala/github4s/free/domain/GitData.scala
