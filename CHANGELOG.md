# Changelog

## 05/08/2017 - Version 0.14.7

Release changes:

* Replace foldLeft with traverse in Decoders ([#101](https://github.com/47deg/github4s/pull/101))
* Documentation for the PR API ([#102](https://github.com/47deg/github4s/pull/102))
* Publish Microsite automatically when merging in master branch ([#103](https://github.com/47deg/github4s/pull/103))
* Removes annoying compiler warnings reported by -Xlint flag ([#104](https://github.com/47deg/github4s/pull/104))
* Made request success check consistent between scala and scala js ([#112](https://github.com/47deg/github4s/pull/112))
* Create pull request api ([#109](https://github.com/47deg/github4s/pull/109))
* Replace issue id by issue number in the doc ([#114](https://github.com/47deg/github4s/pull/114))
* Unify JVM and JS tests ([#115](https://github.com/47deg/github4s/pull/115))
* Issues API unit tests ([#116](https://github.com/47deg/github4s/pull/116))
* Documentation for the gist API ([#121](https://github.com/47deg/github4s/pull/121))
* Make sure sbt is executable in travis ([#122](https://github.com/47deg/github4s/pull/122))
* Support notifications api ([#123](https://github.com/47deg/github4s/pull/123))
* Releases 0.14.7 ([#124](https://github.com/47deg/github4s/pull/124))
* Upgrades sbt org policies plugin ([#125](https://github.com/47deg/github4s/pull/125))


## 04/25/2017 - Version 0.14.6

Release changes:

* List pull request files endpoint ([#99](https://github.com/47deg/github4s/pull/99))
* Fixes head repo decode failure ([#100](https://github.com/47deg/github4s/pull/100))


## 04/24/2017 - Version 0.14.5

Release changes:

* Doc for the issue api ([#93](https://github.com/47deg/github4s/pull/93))
* Avoids executing create and edit issue operations ([#95](https://github.com/47deg/github4s/pull/95))
* Tries to fix OOM issues. Bumps sbt version ([#92](https://github.com/47deg/github4s/pull/92))
* Releases 0.14.5 ([#98](https://github.com/47deg/github4s/pull/98))


## 04/21/2017 - Version 0.14.4

Release changes:

* Updated advertised version in the readme to 0.14.3 ([#86](https://github.com/47deg/github4s/pull/86))
* Remove duplicated circe-parser dependency ([#87](https://github.com/47deg/github4s/pull/87))
* Documentation for the status API ([#88](https://github.com/47deg/github4s/pull/88))
* PullRequest's head ([#90](https://github.com/47deg/github4s/pull/90))
* Upgrades Project ([#91](https://github.com/47deg/github4s/pull/91))


## 04/17/2017 - Version 0.14.3

Release changes:

* Status API ([#83](https://github.com/47deg/github4s/pull/83))
* Bumps library version ([#85](https://github.com/47deg/github4s/pull/85)) 
      

## 04/10/2017 - Version 0.14.2

Release changes:

* Upgrades sbt-org-policies ([#81](https://github.com/47deg/github4s/pull/81))
* Fixes BuildInfo Code Dependency ([#82](https://github.com/47deg/github4s/pull/82)) 
      

## 04/05/2017 - Version 0.14.1

Release changes:

* Updates changelog ([#75](https://github.com/47deg/github4s/pull/75))
* Bumps sbt-org-policies plugin version ([#76](https://github.com/47deg/github4s/pull/76))
* Fixes Github token through env var ([#77](https://github.com/47deg/github4s/pull/77))
* Upgrades sbt-org-policies plugin ([#78](https://github.com/47deg/github4s/pull/78))
* Adds the get contents operation ([#80](https://github.com/47deg/github4s/pull/80)) 
      

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