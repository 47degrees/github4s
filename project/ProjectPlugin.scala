import com.typesafe.sbt.site.jekyll.JekyllPlugin.autoImport._
import de.heikoseeberger.sbtheader.HeaderKey.headers
import de.heikoseeberger.sbtheader.license.MIT
import microsites.MicrositesPlugin.autoImport._
import sbt.Keys._
import sbt._
import sbtorgpolicies.OrgPoliciesPlugin.autoImport._
import sbtorgpolicies._
import sbtorgpolicies.model._

object ProjectPlugin extends AutoPlugin {

  override def trigger: PluginTrigger = allRequirements

  override def requires: Plugins = OrgPoliciesPlugin

  object autoImport {

    lazy val micrositeSettings = Seq(
      micrositeName := "github4s",
      micrositeDescription := "Github API wrapper written in Scala",
      micrositeBaseUrl := "github4s",
      micrositeDocumentationUrl := "/github4s/docs.html",
      micrositeGithubOwner := "47deg",
      micrositeGithubRepo := "github4s",
      includeFilter in Jekyll := "*.html" | "*.css" | "*.png" | "*.jpg" | "*.gif" | "*.js" | "*.swf" | "*.md"
    )

    lazy val testSettings = Seq(
      fork in Test := false
    )

    lazy val commonDeps =
      Seq(
        libraryDependencies ++= Seq(
          %%("cats"),
          %%("circe-core"),
          %%("circe-generic"),
          %%("circe-parser"),
          %%("base64"),
          %%("circe-parser"),
          %%("scalatest") % "test",
          "org.mockito" % "mockito-core" % "2.7.19" % "test",
          compilerPlugin(%%("paradise") cross CrossVersion.full)
        ))

    lazy val jvmDeps = Seq(
      libraryDependencies ++= Seq(
        %%("scalaj"),
        "org.mock-server" % "mockserver-netty" % "3.10.4" % "test" excludeAll ExclusionRule(
          "com.twitter")
      )
    )

    lazy val jsDeps = Seq(libraryDependencies += %%("roshttp"))

    lazy val docsDependencies = libraryDependencies += %%("scalatest")

    lazy val scalazDependencies = Seq(libraryDependencies += %%("scalaz-concurrent"))
  }

  override def projectSettings =
    Seq(
      name := "github4s",
      description := "Github API wrapper written in Scala",
      startYear := Option(2016),
      resolvers += Resolver.sonatypeRepo("snapshots"),
      orgLicenseSetting := sbtorgpolicies.model.MITLicense,
      scalaVersion := scalac.`2.12`,
      crossScalaVersions := "2.10.6" :: scalac.crossScalaVersions,
      scalaOrganization := "org.scala-lang",
      scalacOptions ++= (scalaBinaryVersion.value match {
        case "2.10" => Seq("-Xdivergence211")
        case _      => Nil
      }),
      headers := Map(
        "scala" -> MIT("2016", "47 Degrees, LLC. <http://www.47deg.com>")
      ),
      // This is necessary to prevent packaging the BuildInfo with
      // sensible information like the Github token. Do not remove.
      mappings in (Compile, packageBin) ~= { (ms: Seq[(File, String)]) =>
        ms filter {
          case (_, toPath) =>
            !toPath.startsWith("github4s/BuildInfo")
        }
      }
    ) ++ shellPromptSettings
}
