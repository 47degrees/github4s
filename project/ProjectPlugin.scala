import com.typesafe.sbt.site.SitePlugin.autoImport._
import microsites._
import microsites.MicrositesPlugin.autoImport._
import sbt.Keys._
import sbt._
import scoverage.ScoverageKeys._
import sbtunidoc.ScalaUnidocPlugin.autoImport._
import mdoc.MdocPlugin.autoImport._

object ProjectPlugin extends AutoPlugin {

  override def trigger: PluginTrigger = allRequirements

  object autoImport {

    lazy val V = new {
      val base64: String    = "0.2.9"
      val cats: String      = "2.1.1"
      val circe: String     = "0.13.0"
      val http4s: String    = "0.21.4"
      val paradise: String  = "2.1.1"
      val scalamock: String = "4.4.0"
      val scalatest: String = "3.1.2"
      val silencer: String  = "1.7.0"
    }

    lazy val docsMappingsAPIDir: SettingKey[String] =
      settingKey[String]("Name of subdirectory in site target directory for api docs")

    lazy val micrositeSettings = Seq(
      micrositeName := "Github4s",
      micrositeDescription := "Github API wrapper written in Scala",
      micrositeBaseUrl := "github4s",
      micrositeDocumentationUrl := "docs",
      micrositeAuthor := "Github4s contributors",
      micrositeGithubToken := Option(System.getenv().get("GITHUB_TOKEN")),
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
      scalacOptions ~= (_ filterNot Set(
        "-Ywarn-unused-import",
        "-Xlint",
        "-Xfatal-warnings"
      ).contains),
      docsMappingsAPIDir in ScalaUnidoc := "api",
      addMappingsToSiteDir(mappings in (ScalaUnidoc, packageDoc), docsMappingsAPIDir in ScalaUnidoc)
    )

    lazy val coreDeps = Seq(
      libraryDependencies ++= Seq(
        "org.typelevel"         %% "cats-core"           % V.cats,
        "io.circe"              %% "circe-core"          % V.circe,
        "io.circe"              %% "circe-generic"       % V.circe,
        "io.circe"              %% "circe-literal"       % V.circe,
        "com.github.marklister" %% "base64"              % V.base64,
        "org.http4s"            %% "http4s-client"       % V.http4s,
        "org.http4s"            %% "http4s-circe"        % V.http4s,
        "io.circe"              %% "circe-parser"        % V.circe     % Test,
        "org.scalamock"         %% "scalamock"           % V.scalamock % Test,
        "org.scalatest"         %% "scalatest"           % V.scalatest % Test,
        "org.http4s"            %% "http4s-blaze-client" % V.http4s    % Test,
        "com.github.ghik"        % "silencer-lib"        % V.silencer  % Provided cross CrossVersion.full,
        compilerPlugin("com.github.ghik" % "silencer-plugin" % V.silencer cross CrossVersion.full)
      ),
      libraryDependencies ++= on(2, 12)(
        compilerPlugin("org.scalamacros" %% "paradise" % V.paradise cross CrossVersion.full)
      ).value
    )

  }

  override def projectSettings: Seq[Def.Setting[_]] =
    Seq(
      scalacOptions ++= on(2, 13)("-Ymacro-annotations").value,
      coverageFailOnMinimum := true
    )

  def on[A](major: Int, minor: Int)(a: A): Def.Initialize[Seq[A]] =
    Def.setting {
      CrossVersion.partialVersion(scalaVersion.value) match {
        case Some(v) if v == (major, minor) => Seq(a)
        case _                              => Nil
      }
    }
}
