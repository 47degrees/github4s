import com.typesafe.sbt.site.SitePlugin.autoImport._
import microsites._
import microsites.MicrositesPlugin.autoImport._
import sbt.Keys._
import sbt._
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
      val base64: String       = "0.2.9"
      val cats: String         = "2.0.0"
      val catsEffect: String   = "2.0.0"
      val circe: String        = "0.12.3"
      val circeJackson: String = "0.12.1"
      val paradise: String     = "2.1.1"
      val simulacrum: String   = "0.19.0"
      val scala212: String     = "2.12.10"
      val scala213: String     = "2.13.1"
      val scalaj: String       = "2.4.2"
      val scalamock: String    = "4.4.0"
      val scalaTest: String    = "3.1.0"
    }

    lazy val micrositeSettings = Seq(
      micrositeName := "Github4s",
      micrositeDescription := "Github API wrapper written in Scala",
      micrositeBaseUrl := "github4s",
      micrositeDocumentationUrl := "/github4s/docs.html",
      micrositeGithubOwner := "47deg",
      micrositeGithubRepo := "github4s",
      micrositeAuthor := "Github4s contributors",
      micrositeCompilingDocsTool := WithTut,
      micrositeOrganizationHomepage := "https://github.com/47deg/github4s/blob/master/AUTHORS.md",
      micrositeExtraMdFiles := Map(
        file("CHANGELOG.md") -> ExtraMdFileConfig(
          "changelog.md",
          "page",
          Map("title" -> "Changelog", "section" -> "changelog", "position" -> "2")
        )
      ),
      includeFilter in makeSite := "*.html" | "*.css" | "*.png" | "*.jpg" | "*.gif" | "*.js" | "*.swf" | "*.md",
      scalacOptions in Tut ~= (_ filterNot Set("-Ywarn-unused-import", "-Xlint").contains)
    )

    lazy val coreDeps = Seq(
      libraryDependencies ++= Seq(
        %%("cats-core", V.cats),
        %%("cats-free", V.cats),
        %%("simulacrum", V.simulacrum),
        %%("circe-core", V.circe),
        %%("circe-generic", V.circe),
        "io.circe" %% "circe-jackson28" % V.circeJackson,
        %%("base64", V.base64),
        %%("scalaj", V.scalaj),
        %%("circe-parser", V.circe)  % Test,
        %%("scalamock", V.scalamock) % Test,
        %%("scalatest", V.scalaTest) % Test,
        "org.mock-server"            % "mockserver-netty" % "5.8.0" % Test excludeAll ExclusionRule(
          "com.twitter")
      ),
      libraryDependencies ++= (CrossVersion.partialVersion(scalaBinaryVersion.value) match {
        case Some((2, 13)) => Seq.empty[ModuleID]
        case _             => Seq(compilerPlugin(%%("paradise", V.paradise) cross CrossVersion.full))
      })
    )

    lazy val docsDependencies: Def.Setting[Seq[ModuleID]] = libraryDependencies += %%(
      "scalatest",
      V.scalaTest)

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
      crossScalaVersions := Seq(V.scala212, V.scala213),
      scalacOptions := {
        val withStripedLinter = scalacOptions.value filterNot Set("-Xlint", "-Xfuture").contains
        CrossVersion.partialVersion(scalaBinaryVersion.value) match {
          case Some((2, 13)) => withStripedLinter :+ "-Ymacro-annotations"
          case _             => withStripedLinter
        }
      },
      orgGithubTokenSetting := "GITHUB4S_ACCESS_TOKEN",
      orgBadgeListSetting := List(
        TravisBadge.apply(_),
        GitterBadge.apply(_),
        CodecovBadge.apply(_),
        MavenCentralBadge.apply(_),
        LicenseBadge.apply(_),
        ScalaLangBadge.apply(_),
        GitHubIssuesBadge.apply(_)
      ),
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
