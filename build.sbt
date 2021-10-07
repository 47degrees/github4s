import ProjectPlugin.on

ThisBuild / organization := "com.47deg"

val scala212         = "2.12.15"
val scala213         = "2.13.6"
val scala3Version    = "3.0.2"
val scala2Versions   = Seq(scala212, scala213)
val allScalaVersions = scala2Versions :+ scala3Version
ThisBuild / scalaVersion       := scala213
ThisBuild / crossScalaVersions := allScalaVersions

addCommandAlias("ci-test", "scalafmtCheckAll; scalafmtSbtCheck; mdoc; ++test")
addCommandAlias("ci-docs", "github; mdoc; headerCreateAll; publishMicrosite")
addCommandAlias("ci-publish", "github; ci-release")

publish / skip := true

lazy val github4s = (crossProject(JSPlatform, JVMPlatform))
  .crossType(CrossType.Pure)
  .withoutSuffixFor(JVMPlatform)
  .settings(coreDeps: _*)
  .settings(
    // Increase number of inlines, needed for circe semiauto derivation
    scalacOptions ++= on(3, 9)(Seq("-Xmax-inlines", "20")).value.flatten
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
