ThisBuild / organization := "com.47deg"
ThisBuild / crossScalaVersions := Seq("2.12.10", "2.13.1")

addCommandAlias("ci-test", "+scalafmtCheckAll; +scalafmtSbtCheck; +mdoc; +test")
addCommandAlias("ci-docs", "+mdoc; headerCreateAll")
addCommandAlias("ci-microsite", "publishMicrosite")

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
