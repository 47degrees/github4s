# Changelog

## 2017/04/03 - Version 0.14.0

This release:

* Changes the description field in `Repository` to `Option[String]`
* Adds the operation for creating releases

## 2017/03/31 - Version 0.13.0

This release:

* Adds the git ops for listing pull requests (#70)
* Includes the ops for creating tags and references (#71)
* Changes the `RepoStatus` model, making optional these fields:

```diff
--    open_issues: Int,
--    watchers: Int,
--    network_count: Int,
--    subscribers_count: Int,
++    open_issues: Option[Int],
++    watchers: Option[Int],
++    network_count: Option[Int],
++    subscribers_count: Option[Int],
```

## 2017/03/28 - Version 0.12.1

This release adds some git methods (#67)

* Adds the git ops for fetching and creating commits
* Disables the minimum test coverage for `scalaz` and `github4sjs` modules

## 2017/03/22 - Version 0.12.0

This release:

* Makes the library compatible with Scala 2.12.
* It also integrates `sbt-org-policies` plugin to make easier its maintainability.
* Upgrades the dependencies (through sbt-org-policies plugin).

## 2017/03/22 - Version 0.11.1

Adds these new fields to the User model:

* `company`: Option[String]
* `blog`: Option[String]
* `location`: Option[String]
* `bio`: Option[String]

Thanks @fedefernandez for your time!

## 2017/03/16 - Version 0.11.0

This release brings:

* Migrates to new group id `com.47deg`, so now you can use `github4s` in this way:

```
libraryDependencies += "com.47deg" %% "github4s" % "0.11.0"
```

* This new version also upgrades some of the used libraries, such as `cats`, `circe` and `scalaz`.

## 2017/01/09 - Version 0.10.0

This release adds two main changes:

On one hand, it adds support for **Scala 2.10**. For this, the `RepoUrls` model has been changed by replacing all optional URL fields with a Map, avoiding the limit of 22 fields in case classes. Because of that, this release breaks the compatibility with all projects that are currently using the [Github Repository](https://github.com/47deg/github4s/blob/master/github4s/shared/src/main/scala/github4s/free/domain/Repository.scala) model.

On the other hand, it adds some operations related to **GitHub Issues**:

* [List issues](https://developer.github.com/v3/issues/#list-issues)
* [Search issues](https://developer.github.com/v3/search/#search-issues)
* [Create issue](https://developer.github.com/v3/issues/#create-an-issue)
* [Edit issue](https://developer.github.com/v3/issues/#edit-an-issue)

Great work @fedefernandez ! Thanks!

## 2016/11/04 - Version 0.9.0

This version introduces compatibility with scala-js. This means that the project has been split into several modules:

* `github4sJVM` (containing the code compatible with the JVM environment)
* `github4sJS` (containing the code compatible with scala-js)
* `github4s` (containing all the shared code compatible with both the JVM and JS environments)

Based on FREE, github4s allows its users to be executed in different monadic contexts (i.e.: Monix Task, Id...). Please note that in order to use github4s in a scala-js compatible application, you'll be restricted to using Future. To accommodate these different options, several exec methods have been added to the Github class, in order to make it easier to use:

* `exec[M[_], C]`: takes a monad M and an Http Client, also takes an optional parameter to include custom headers for the API requests. Instances for scalaj.http and roshttp clients are included through implicits in both the JS and JVM-compatible modules. Please take a look at the documentation for details on this.
* `execFuture[C]`: easier to use version if using Futures. Please use roshttp as your http client (SimpleHttpResponse type) if you're using github4s in an scala-js project.
* `execK[M[_], Map[String, String], C]`: exposes the inner Kleisli behind the curtain of the exec methods.

Awesome work @jdesiloniz!

## 2016/05/12 - Version 0.1

This first approach includes several functions related to Users, Repositories and Authentication process.

- Users:
	- Get user
	- Get authenticated user
	- Get users

- Repositories:
	- Get repository
	- List commits

- Authentication:
	- New authorization
	- Get authorize url (oAuth step 1)
	- Get access token (oAuth step 2)
