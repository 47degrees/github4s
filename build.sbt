import ProjectPlugin.on
import com.typesafe.tools.mima.core._

Global / onChangedBuildSource := ReloadOnSourceChanges

ThisBuild / organization := "com.47deg"

val scala212         = "2.12.17"
val scala213         = "2.13.10"
val scala3Version    = "3.2.2"
val scala2Versions   = Seq(scala212, scala213)
val allScalaVersions = scala2Versions :+ scala3Version
ThisBuild / scalaVersion       := scala213
ThisBuild / crossScalaVersions := allScalaVersions

disablePlugins(MimaPlugin)

addCommandAlias(
  "ci-test",
  "scalafmtCheckAll; scalafmtSbtCheck; mimaReportBinaryIssues; mdoc; ++test"
)
addCommandAlias("ci-docs", "github; mdoc; headerCreateAll; publishMicrosite")
addCommandAlias("ci-publish", "github; ci-release")

publish / skip := true

lazy val github4s = (crossProject(JSPlatform, JVMPlatform))
  .crossType(CrossType.Full)
  .withoutSuffixFor(JVMPlatform)
  .enablePlugins(MimaPlugin)
  .settings(coreDeps: _*)
  .settings(
    // Increase number of inlines, needed for circe semiauto derivation
    scalacOptions ++= on(3)(Seq("-Xmax-inlines", "48")).value.flatten,
    // See the README for why this is necessary
    // https://github.com/scala-js/scala-js-macrotask-executor/tree/v1.0.0
    // tl;dr: without it, performance problems and concurrency bugs abound
    libraryDependencies += "org.scala-js" %%% "scala-js-macrotask-executor" % "1.0.0" % Test,
    mimaPreviousArtifacts                  := Set("com.47deg" %% "github4s" % "0.32.0"),
    mimaBinaryIssueFilters ++= Seq(
      ProblemFilters.exclude[IncompatibleMethTypeProblem]("github4s.http.HttpClient.this")
    )
  )

//////////
// DOCS //
//////////

lazy val microsite: Project = project
  .dependsOn(github4s.jvm)
  .enablePlugins(MicrositesPlugin)
  .enablePlugins(ScalaUnidocPlugin)
  .disablePlugins(MimaPlugin)
  .settings(micrositeSettings: _*)
  .settings(publish / skip := true)
  .settings(ScalaUnidoc / unidoc / unidocProjectFilter := inProjects(github4s.jvm, microsite))

lazy val documentation = project
  .enablePlugins(MdocPlugin)
  .disablePlugins(MimaPlugin)
  .settings(mdocOut := file("."))
  .settings(publish / skip := true)
