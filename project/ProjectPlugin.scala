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
import mdoc.MdocPlugin.autoImport._
import sbtorgpolicies.model.GitHubSettings

object ProjectPlugin extends AutoPlugin {

  override def trigger: PluginTrigger = allRequirements

  override def requires: Plugins = OrgPoliciesPlugin

  object autoImport {

    lazy val V = new {
      val base64: String     = "0.2.9"
      val cats: String       = "2.1.1"
      val catsEffect: String = "2.1.1"
      val circe: String      = "0.13.0"
      val paradise: String   = "2.1.1"
      val simulacrum: String = "0.19.0"
      val scala212: String   = "2.12.10"
      val scala213: String   = "2.13.1"
      val http4s: String     = "0.21.2"
      val scalamock: String  = "4.4.0"
      val scalaTest: String  = "3.1.1"
      val silencer: String   = "1.6.0"
    }

    lazy val micrositeSettings = Seq(
      micrositeName := "Github4s",
      micrositeDescription := "Github API wrapper written in Scala",
      micrositeBaseUrl := "github4s",
      micrositeDocumentationUrl := "docs",
      micrositeGithubOwner := "47degrees",
      micrositeGithubRepo := "github4s",
      micrositeAuthor := "Github4s contributors",
      micrositeCompilingDocsTool := WithMdoc,
      micrositePushSiteWith := GitHub4s,
      micrositeOrganizationHomepage := "https://github.com/47degrees/github4s/blob/master/AUTHORS.md",
      micrositePalette := Map(
        "brand-primary"   -> "#3D3832",
        "brand-secondary" -> "#f90",
        "white-color"     -> "#FFFFFF"
      ),
      micrositeExtraMdFiles := Map(
        file("CHANGELOG.md") -> ExtraMdFileConfig(
          "changelog.md",
          "page",
          Map(
            "title"     -> "Changelog",
            "section"   -> "home",
            "position"  -> "3",
            "permalink" -> "changelog"
          )
        )
      ),
      micrositeExtraMdFilesOutput := mdocIn.value,
      includeFilter in makeSite := "*.html" | "*.css" | "*.png" | "*.jpg" | "*.gif" | "*.js" | "*.swf" | "*.md" | "*.svg",
      scalacOptions ~= (_ filterNot Set("-Ywarn-unused-import", "-Xlint", "-Xfatal-warnings").contains)
    )

    lazy val coreDeps = Seq(
      libraryDependencies ++= Seq(
        %%("cats-core", V.cats),
        %%("cats-free", V.cats),
        %%("simulacrum", V.simulacrum),
        %%("circe-core", V.circe),
        %%("circe-generic", V.circe),
        "io.circe" %% "circe-literal" % V.circe,
        %%("base64", V.base64),
        "org.http4s"                 %% "http4s-blaze-client" % V.http4s,
        "org.http4s"                 %% "http4s-circe" % V.http4s,
        %%("circe-parser", V.circe)  % Test,
        %%("scalamock", V.scalamock) % Test,
        %%("scalatest", V.scalaTest) % Test,
        "org.mock-server"            % "mockserver-netty" % "5.10" % Test excludeAll ExclusionRule(
          "com.twitter"
        ),
        compilerPlugin("com.github.ghik" % "silencer-plugin" % V.silencer cross CrossVersion.full),
        "com.github.ghik" % "silencer-lib" % V.silencer % Provided cross CrossVersion.full
      ),
      libraryDependencies ++= (CrossVersion.partialVersion(scalaBinaryVersion.value) match {
        case Some((2, 13)) => Seq.empty[ModuleID]
        case _             => Seq(compilerPlugin(%%("paradise", V.paradise) cross CrossVersion.full))
      })
    )

    lazy val docsDependencies: Def.Setting[Seq[ModuleID]] =
      libraryDependencies += %%("scalatest", V.scalaTest)

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
      orgGithubSetting := GitHubSettings(
        organization = "47degrees",
        project = (name in LocalRootProject).value,
        organizationName = "47 Degrees",
        groupId = "com.47deg",
        organizationHomePage = url("http://47deg.com"),
        organizationEmail = "hello@47deg.com"
      ),
      orgProjectName := "Github4s",
      description := "Github API wrapper written in Scala",
      startYear := Option(2016),
      resolvers += Resolver.sonatypeRepo("snapshots"),
      scalaVersion := V.scala213,
      crossScalaVersions := Seq(V.scala212, V.scala213),
      scalacOptions := {
        val withStripedLinter = scalacOptions.value filterNot Set("-Xlint", "-Xfuture").contains
        (CrossVersion.partialVersion(scalaBinaryVersion.value) match {
          case Some((2, 13)) => withStripedLinter :+ "-Ymacro-annotations"
          case _             => withStripedLinter
        }) :+ "-language:higherKinds"
      },
      orgGithubTokenSetting := "GITHUB_TOKEN",
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
        "docs/mdoc".asRunnableItem
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
    ) ++ shellPromptSettings ++ sharedScoverageSettings(70d)
}
