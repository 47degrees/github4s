import de.heikoseeberger.sbtheader.license.MIT
import PgpKeys.gpgCommand

pgpPassphrase := Some(sys.env.getOrElse("PGP_PASSPHRASE", "").toCharArray)
pgpPublicRing := file(s"${sys.env.getOrElse("PGP_FOLDER", ".")}/pubring.gpg")
pgpSecretRing := file(s"${sys.env.getOrElse("PGP_FOLDER", ".")}/secring.gpg")

val dev = Seq(Dev("47 Degrees (twitter: @47deg)", "47 Degrees"))
val gh  = GitHubSettings("com.47deg", "github4s", "47 Degrees", mit)

lazy val buildSettings = Seq(
    name := gh.proj,
    organization := gh.org,
    organizationName := gh.publishOrg,
    description := "Github API wrapper written in Scala",
    startYear := Option(2016),
    homepage := Option(url("http://47deg.github.io/github4s/")),
    organizationHomepage := Option(new URL("http://47deg.com")),
    scalaVersion := "2.11.8",
    crossScalaVersions := Seq("2.10.6", scalaVersion.value),
    scalacOptions ++= (scalaBinaryVersion.value match {
    case "2.10" => Seq("-Xdivergence211")
    case _      => Nil
  }),
    scalafmtConfig in ThisBuild := Some(file(".scalafmt")),
    headers := Map(
      "scala" -> MIT("2016", "47 Degrees, LLC. <http://www.47deg.com>")
    )
  ) ++ reformatOnCompileSettings ++
    sharedCommonSettings ++
    miscSettings ++
    sharedReleaseProcess ++
    credentialSettings ++
    sharedPublishSettings(gh, dev)

lazy val micrositeSettings = Seq(
  micrositeName := "github4s",
  micrositeDescription := "Github API wrapper written in Scala",
  micrositeBaseUrl := "github4s",
  micrositeDocumentationUrl := "/github4s/docs.html",
  micrositeGithubOwner := "47deg",
  micrositeGithubRepo := "github4s",
  includeFilter in makeSite := "*.html" | "*.css" | "*.png" | "*.jpg" | "*.gif" | "*.js" | "*.swf" | "*.md"
)

lazy val commonDeps =
  Seq(
    libraryDependencies ++= Seq(
      "org.typelevel" %%% "cats"          % "0.9.0",
      "io.circe"      %%% "circe-core"    % "0.7.0",
      "io.circe"      %%% "circe-generic" % "0.7.0",
      "io.circe"      %%% "circe-parser"  % "0.7.0",
      "org.scalatest" %%% "scalatest" % "3.0.0" % "test",
      "com.github.marklister" %%% "base64" % "0.2.3",
      compilerPlugin(
        "org.scalamacros" % "paradise" % "2.1.0" cross CrossVersion.full
      )
    ))

lazy val jvmDeps = Seq(
  libraryDependencies ++= Seq(
    "org.scalaj" %% "scalaj-http" % "2.3.0",
    "org.mock-server" % "mockserver-netty" % "3.10.4" % "test"
  ))

lazy val jsDeps = Seq(
  libraryDependencies ++= Seq(
    "fr.hmil" %%% "roshttp" % "2.0.0-RC1"
  )
)

lazy val docsDependencies = libraryDependencies ++= Seq(
    "com.ironcorelabs" %% "cats-scalatest"  % "1.1.2"  % "test",
    "org.mock-server"  % "mockserver-netty" % "3.10.4" % "test"
  )

lazy val scalazDependencies = Seq(
  libraryDependencies +=
    "org.scalaz" %% "scalaz-concurrent" % "7.2.9"
)

lazy val root = (project in file("."))
  .settings(buildSettings: _*)
  .aggregate(github4sJVM, github4sJS, scalaz, docs)

/** github4s - cross project that provides cross platform support.*/
lazy val github4s = (crossProject in file("github4s"))
  .settings(moduleName := "github4s")
  .enablePlugins(AutomateHeaderPlugin)
  .enablePlugins(BuildInfoPlugin)
  .settings(
    buildInfoKeys := Seq[BuildInfoKey](name,
                                       version,
                                       "token" -> sys.env.getOrElse("GITHUB4S_ACCESS_TOKEN", "")),
    buildInfoPackage := "github4s"
  )
  .settings(buildSettings: _*)
  .settings(commonDeps: _*)
  .jvmSettings(jvmDeps: _*)
  .jsSettings(sharedJsSettings: _*)
  .jsSettings(testSettings: _*)
  .jsSettings(jsDeps: _*)

lazy val github4sJVM = github4s.jvm
lazy val github4sJS  = github4s.js

lazy val docs = (project in file("docs"))
  .dependsOn(scalaz)
  .settings(moduleName := "github4s-docs")
  .settings(buildSettings: _*)
  .settings(micrositeSettings: _*)
  .settings(docsDependencies: _*)
  .settings(noPublishSettings: _*)
  .enablePlugins(MicrositesPlugin)

lazy val scalaz = (project in file("scalaz"))
  .settings(moduleName := "github4s-scalaz")
  .settings(buildSettings: _*)
  .settings(scalazDependencies: _*)
  .dependsOn(github4sJVM)
  .enablePlugins(AutomateHeaderPlugin)

lazy val testSettings = Seq(
  fork in Test := false
)
