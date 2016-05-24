import com.typesafe.sbt.SbtGhPages.ghpages
import com.typesafe.sbt.SbtGit.git

import scalariform.formatter.preferences._
import com.typesafe.sbt.SbtScalariform
import com.typesafe.sbt.SbtScalariform.ScalariformKeys


lazy val buildSettings = Seq(
  organization := "com.fortysevendeg",
  organizationName := "47 Degrees",
  description := "Github API wrapper written in Scala",
  startYear := Option(2016),
  homepage := Option(url("http://47deg.github.io/github4s/")),
  organizationHomepage := Option(new URL("http://47deg.com")),
  scalaVersion := "2.11.8",
  licenses := Seq("Apache License, Version 2.0" -> url("http://www.apache.org/licenses/LICENSE-2.0.txt"))
)

lazy val dependencies = libraryDependencies ++= Seq(
  "org.typelevel" %% "cats" % "0.6.0",
  "org.scalaz" %% "scalaz-concurrent" % "7.1.4",
  "org.scalaj" %% "scalaj-http" % "2.2.1",
  "io.circe" %% "circe-core" % "0.5.0-M1",
  "io.circe" %% "circe-generic" % "0.5.0-M1",
  "io.circe" %% "circe-parser" % "0.5.0-M1",
  "org.scalatest" %% "scalatest" % "2.2.6" % "test",
  "com.ironcorelabs" %% "cats-scalatest" % "1.1.2" % "test"
)

lazy val scalariformSettings = SbtScalariform.scalariformSettings ++ Seq(
  ScalariformKeys.preferences := ScalariformKeys.preferences.value
      .setPreference(SpacesWithinPatternBinders, true)
      .setPreference(SpaceBeforeColon, false)
      .setPreference(SpaceInsideParentheses, false)
      .setPreference(SpaceInsideBrackets, false)
      .setPreference(SpacesAroundMultiImports, true)
      .setPreference(PreserveSpaceBeforeArguments, false)
      .setPreference(CompactStringConcatenation, false)
      .setPreference(DanglingCloseParenthesis, Force)
      .setPreference(CompactControlReadability, false)
      .setPreference(AlignParameters, false)
      .setPreference(AlignArguments, true)
      .setPreference(AlignSingleLineCaseStatements, false)
      .setPreference(DoubleIndentClassDeclaration, false)
      .setPreference(IndentLocalDefs, false)
      .setPreference(MultilineScaladocCommentsStartOnFirstLine, false)
      .setPreference(PlaceScaladocAsterisksBeneathSecondAsterisk, true)
      .setPreference(RewriteArrowSymbols, true))


lazy val noPublishSettings = Seq(
  publish := (),
  publishLocal := (),
  publishArtifact := false)

lazy val tutDirectoriesSettings = Seq(
  tutSourceDirectory := sourceDirectory.value / "tut",
  tutTargetDirectory := sourceDirectory.value / "jekyll"
)

lazy val github4sSettings = buildSettings ++ dependencies ++ scalariformSettings

lazy val ghpagesSettings = ghpages.settings ++ Seq(git.remoteRepo := "git@github.com:47deg/github4s.git")

lazy val docsSettings = buildSettings ++ noPublishSettings ++ tutSettings ++ tutDirectoriesSettings ++ ghpagesSettings

lazy val github4s = (project in file("."))
    .settings(moduleName := "github4s")
    .settings(github4sSettings:_*)

lazy val docs = (project in file("docs"))
    .settings(moduleName := "github4s-docs")
    .settings(docsSettings: _*)
    .enablePlugins(JekyllPlugin)
    .dependsOn(github4s)

