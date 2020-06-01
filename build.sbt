ThisBuild / organization := "com.47deg"
ThisBuild / scalaVersion := "2.13.2"
ThisBuild / crossScalaVersions := Seq("2.12.11", "2.13.2")

addCommandAlias("ci-test", "scalafmtCheckAll; scalafmtSbtCheck; mdoc; testCovered")
addCommandAlias("ci-docs", "github; mdoc; headerCreateAll; publishMicrosite")
addCommandAlias("ci-publish", "github; ci-release")

skip in publish := true

lazy val github4s = project.settings(coreDeps: _*)

//////////
// DOCS //
//////////

lazy val microsite: Project = project
  .dependsOn(github4s)
  .enablePlugins(MicrositesPlugin)
  .enablePlugins(ScalaUnidocPlugin)
  .settings(micrositeSettings: _*)
  .settings(skip in publish := true)
  .settings(unidocProjectFilter in (ScalaUnidoc, unidoc) := inProjects(github4s, microsite))

lazy val documentation = project
  .enablePlugins(MdocPlugin)
  .settings(mdocOut := file("."))
  .settings(skip in publish := true)
