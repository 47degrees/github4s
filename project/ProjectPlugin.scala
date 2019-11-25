import com.typesafe.sbt.site.jekyll.JekyllPlugin.autoImport._
import microsites._
import microsites.MicrositesPlugin.autoImport._
import sbt.Keys._
import sbt._
import sbtorgpolicies.model._
import sbtorgpolicies.OrgPoliciesKeys.orgBadgeListSetting
import sbtorgpolicies.OrgPoliciesPlugin
import sbtorgpolicies.OrgPoliciesPlugin.autoImport._
import sbtorgpolicies.templates.badges._
import sbtorgpolicies.runnable.syntax._
import scoverage.ScoverageKeys
import scoverage.ScoverageKeys._
import tut.TutPlugin.autoImport._

object ProjectPlugin extends AutoPlugin {

  override def trigger: PluginTrigger = allRequirements

  override def requires: Plugins = OrgPoliciesPlugin

  object autoImport {

    lazy val V = new {
      val base64: String             = "0.2.9"
      val cats: String               = "1.6.1"
      val catsEffect: String         = "2.0.0"
      val circe: String              = "0.11.1"
      val paradise: String           = "2.1.1"
      val roshttp: String            = "2.2.4"
      val simulacrum: String         = "0.19.0"
      val scala211: String           = "2.11.12"
      val scala212: String           = "2.12.8"
      val scalaj: String             = "2.4.2"
      val scalamockScalatest: String = "3.6.0"
      val scalaTest: String          = "3.0.8"
      val scalaz: String             = "7.2.29"
    }

    lazy val micrositeSettings = Seq(
      micrositeName := "Github4s",
      micrositeDescription := "Github API wrapper written in Scala",
      micrositeBaseUrl := "github4s",
      micrositeDocumentationUrl := "/github4s/docs.html",
      micrositeGithubOwner := "47deg",
      micrositeGithubRepo := "github4s",
      micrositeAuthor := "Github4s contributors",
      micrositeOrganizationHomepage := "https://github.com/47deg/github4s/blob/master/AUTHORS.md",
      micrositeExtraMdFiles := Map(
        file("CHANGELOG.md") -> ExtraMdFileConfig(
          "changelog.md",
          "page",
          Map("title" -> "Changelog", "section" -> "changelog", "position" -> "2")
        )
      ),
      includeFilter in Jekyll := "*.html" | "*.css" | "*.png" | "*.jpg" | "*.gif" | "*.js" | "*.swf" | "*.md",
      scalacOptions in Tut ~= (_ filterNot Set("-Ywarn-unused-import", "-Xlint").contains)
    )

    lazy val testSettings = Seq(
      fork in Test := false
    )

    lazy val commonCrossDeps = Seq(
      libraryDependencies ++= Seq(
        %%("cats-core", V.cats),
        %%("cats-free", V.cats),
        %%("simulacrum", V.simulacrum),
        %%("circe-core", V.circe),
        %%("circe-generic", V.circe),
        "io.circe" %% "circe-jackson28" % V.circe,
        %%("base64", V.base64),
        %%("circe-parser", V.circe)                    % Test,
        %%("scalamockScalatest", V.scalamockScalatest) % Test,
        %%("scalatest", V.scalaTest)                   % Test
      )
    )

    lazy val standardCommonDeps = Seq(
      libraryDependencies += compilerPlugin(%%("paradise", V.paradise) cross CrossVersion.full)
    )

    lazy val jvmDeps = Seq(
      libraryDependencies ++= Seq(
        %%("scalaj", V.scalaj),
        "org.mock-server" % "mockserver-netty" % "5.7.2" % Test excludeAll ExclusionRule(
          "com.twitter")
      )
    )

    lazy val jsDeps: Def.Setting[Seq[ModuleID]] = libraryDependencies += %%%("roshttp", V.roshttp)

    lazy val docsDependencies: Def.Setting[Seq[ModuleID]] = libraryDependencies += %%(
      "scalatest",
      V.scalaTest)

    lazy val scalazDependencies: Def.Setting[Seq[ModuleID]] =
      libraryDependencies += %%("scalaz-concurrent", V.scalaz)

    lazy val catsEffectDependencies = Seq(
      libraryDependencies ++= Seq(
        %%("cats-effect", V.catsEffect),
        %%("scalatest", V.scalaTest) % Test
      )
    )

    def toCompileTestList(sequence: Seq[ProjectReference]): List[String] = sequence.toList.map {
      p =>
        val project: String = p.asInstanceOf[LocalProject].project
        s"$project/test"
    }
  }

  import autoImport.V

  override def projectSettings: Seq[Def.Setting[_]] =
    Seq(
      name := "github4s",
      orgProjectName := "Github4s",
      description := "Github API wrapper written in Scala",
      startYear := Option(2016),
      resolvers += Resolver.sonatypeRepo("snapshots"),
      scalaVersion := V.scala212,
      crossScalaVersions := Seq(V.scala211, V.scala212),
      scalacOptions ~= (_ filterNot Set("-Xlint").contains),
      orgGithubTokenSetting := "GITHUB4S_ACCESS_TOKEN",
      resolvers += Resolver.bintrayRepo("hmil", "maven"),
      orgBadgeListSetting := List(
        TravisBadge.apply(_),
        GitterBadge.apply(_),
        CodecovBadge.apply(_),
        MavenCentralBadge.apply(_),
        LicenseBadge.apply(_),
        ScalaLangBadge.apply(_),
        ScalaJSBadge.apply(_),
        GitHubIssuesBadge.apply(_)
      ),
      orgSupportedScalaJSVersion := Some("0.6.21"),
      orgScriptTaskListSetting ++= List(
        (ScoverageKeys.coverageAggregate in Test).asRunnableItemFull,
        "docs/tut".asRunnableItem
      ),
      coverageExcludedPackages := "<empty>;github4s\\.scalaz\\..*",
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
