---
layout: home
technologies:
 - first: ["Scala", "Github4s is a Github API wrapper purely written in Scala"]
 - second: ["ScalaJS", "Now, with Scala.js support"]
 - third: ["Free Monads", "Github4s is based on a Free Monad Architecture, which helps decoupling of program declaration from program interpretation"]
---

# Github4s

Github4s is a GitHub API wrapper written in Scala.

## Installation

Github4s is compatible with both Scala JVM and Scala.js. It supports 2.10, 2.11 and 2.12.

To get started with SBT, simply add the following to your build.sbt file.

For Scala.jvm:

[comment]: # (Start Replace)

```scala
libraryDependencies += "com.47deg" %% "github4s" % "0.14.7"
```

[comment]: # (End Replace)

For Scala.js:

[comment]: # (Start Replace)

```scala
libraryDependencies += "com.47deg" %%% "github4s" % "0.14.7"
```

[comment]: # (End Replace)
