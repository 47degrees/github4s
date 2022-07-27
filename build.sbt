import ProjectPlugin.on

ThisBuild / organization := "com.47deg"

val scala212         = "2.12.16"
val scala213         = "2.13.8"
val scala3Version    = "3.1.3"
val scala2Versions   = Seq(scala212, scala213)
val allScalaVersions = scala2Versions :+ scala3Version
ThisBuild / scalaVersion       := scala213
ThisBuild / crossScalaVersions := allScalaVersions

addCommandAlias("ci-test", "scalafmtCheckAll; scalafmtSbtCheck; mdoc; ++test")
addCommandAlias("ci-docs", "github; mdoc; headerCreateAll; publishMicrosite")
addCommandAlias("ci-publish", "github; ci-release")

publish / skip := true

lazy val github4s = (crossProject(JSPlatform, JVMPlatform))
  .crossType(CrossType.Full)
  .withoutSuffixFor(JVMPlatform)
  .settings(coreDeps: _*)
  .settings(
    // Increase number of inlines, needed for circe semiauto derivation
    scalacOptions ++= on(3, 9)(Seq("-Xmax-inlines", "20")).value.flatten,
    // See the README for why this is necessary
    // https://github.com/scala-js/scala-js-macrotask-executor/tree/v1.0.0
    // tl;dr: without it, performance problems and concurrency bugs abound
    libraryDependencies += "org.scala-js" %%% "scala-js-macrotask-executor" % "1.0.0" % Test
  )

//////////
// DOCS //
//////////

lazy val microsite: Project = project
  .dependsOn(github4s.jvm)
  .enablePlugins(MicrositesPlugin)
  .enablePlugins(ScalaUnidocPlugin)
  .settings(micrositeSettings: _*)
  .settings(publish / skip := true)
  .settings(ScalaUnidoc / unidoc / unidocProjectFilter := inProjects(github4s.jvm, microsite))

lazy val documentation = project
  .enablePlugins(MdocPlugin)
  .settings(mdocOut := file("."))
  .settings(publish / skip := true)
