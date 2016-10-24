import de.heikoseeberger.sbtheader.license.MIT
import catext.Dependencies._

val dev  = Seq(Dev("47 Degrees (twitter: @47deg)", "47 Degrees"))
val gh   = GitHubSettings("com.fortysevendeg", "github4s", "47 Degrees", mit)
val vAll = Versions(versions, libraries, scalacPlugins)

lazy val buildSettings = Seq(
    name := gh.proj,
    organization := gh.org,
    organizationName := gh.publishOrg,
    description := "Github API wrapper written in Scala",
    startYear := Option(2016),
    homepage := Option(url("http://47deg.github.io/github4s/")),
    organizationHomepage := Option(new URL("http://47deg.com")),
    scalaVersion := "2.11.8",
    scalafmtConfig in ThisBuild := Some(file(".scalafmt")),
    headers := Map(
      "scala" -> MIT("2016", "47 Degrees, LLC. <http://www.47deg.com>")
    )
  ) ++ reformatOnCompileSettings ++
    sharedReleaseProcess ++
    pgpSettings ++
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

lazy val dependencies = addLibs(vAll,
                                "cats-free",
                                "circe-core",
                                "circe-generic",
                                "circe-parser",
                                "simulacrum") ++
    addTestLibs(vAll, "scalatest") ++
    addCompilerPlugins(vAll, "paradise") ++
    Seq(
      libraryDependencies ++= Seq(
        "org.scalaj" %% "scalaj-http" % "2.2.1",
        "org.mock-server" % "mockserver-netty" % "3.10.4" % "test"
      ))

lazy val docsDependencies = libraryDependencies ++= Seq(
    "com.ironcorelabs" %% "cats-scalatest"  % "1.1.2"  % "test",
    "org.mock-server"  % "mockserver-netty" % "3.10.4" % "test"
  )

lazy val scalazDependencies = addLibs(vAll, "scalaz-concurrent")

lazy val github4s = (project in file("github4s"))
  .settings(moduleName := "github4s")
  .settings(buildSettings: _*)
  .settings(dependencies: _*)
  .enablePlugins(AutomateHeaderPlugin)

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
  .dependsOn(github4s)
  .enablePlugins(AutomateHeaderPlugin)

lazy val root =
  (project in file(".")).aggregate(github4s, scalaz, docs).settings(buildSettings: _*)
