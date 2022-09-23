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
      val bm4                       = "0.3.1"
      val cats: String              = "2.8.0"
      val circe: String             = "0.14.3"
      val expecty                   = "0.16.0"
      val http4s: String            = "0.23.16"
      val http4sBlazeClient: String = "0.23.12"
      val paradise: String          = "2.1.1"
      val scalacheck                = "1.16.0"
      val scalacheckShapeless       = "1.3.0"
      val scalacheckPlusScalatest   = "3.2.11.0"
      val scalatest: String         = "3.2.13"
      val shapeless3                = "3.1.0"
    }

    lazy val docsMappingsAPIDir: SettingKey[String] =
      settingKey[String]("Name of subdirectory in site target directory for api docs")

    lazy val micrositeSettings = Seq(
      micrositeName                 := "Github4s",
      micrositeDescription          := "Github API wrapper written in Scala",
      micrositeBaseUrl              := "github4s",
      micrositeDocumentationUrl     := "docs",
      micrositeAuthor               := "Github4s contributors",
      micrositeGithubToken          := Option(System.getenv().get("GITHUB_TOKEN")),
      micrositePushSiteWith         := GitHub4s,
      micrositeOrganizationHomepage := "https://github.com/47degrees/github4s/blob/main/AUTHORS.md",
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
      makeSite / includeFilter := "*.html" | "*.css" | "*.png" | "*.jpg" | "*.gif" | "*.js" | "*.swf" | "*.md" | "*.svg",
      scalacOptions ~= (_ filterNot Set(
        "-Ywarn-unused-import",
        "-Xlint",
        "-Xfatal-warnings"
      ).contains),
      ScalaUnidoc / docsMappingsAPIDir := "api",
      addMappingsToSiteDir(
        ScalaUnidoc / packageDoc / mappings,
        ScalaUnidoc / docsMappingsAPIDir
      )
    )

    lazy val coreDeps = Seq(
      libraryDependencies ++= Seq(
        "org.typelevel"        %% "cats-core"           % V.cats,
        "io.circe"             %% "circe-core"          % V.circe,
        "io.circe"             %% "circe-generic"       % V.circe,
        "org.http4s"           %% "http4s-client"       % V.http4s,
        "org.http4s"           %% "http4s-circe"        % V.http4s,
        "io.circe"             %% "circe-parser"        % V.circe                   % Test,
        "com.eed3si9n.expecty" %% "expecty"             % V.expecty                 % Test,
        "org.scalatest"        %% "scalatest"           % V.scalatest               % Test,
        "org.http4s"           %% "http4s-blaze-client" % V.http4sBlazeClient       % Test,
        "org.http4s"           %% "http4s-dsl"          % V.http4s                  % Test,
        "org.http4s"           %% "http4s-server"       % V.http4s                  % Test,
        "org.scalacheck"       %% "scalacheck"          % V.scalacheck              % Test,
        "org.scalatestplus"    %% "scalacheck-1-15"     % V.scalacheckPlusScalatest % Test
      ),
      libraryDependencies ++= on(2, 12)(
        compilerPlugin("org.scalamacros" %% "paradise" % V.paradise cross CrossVersion.full)
      ).value,
      libraryDependencies ++= on(2)(
        "com.github.alexarchambault" %% "scalacheck-shapeless_1.15" % V.scalacheckShapeless % Test
      ).value,
      libraryDependencies ++= on(2)(
        compilerPlugin("com.olegpy" %% "better-monadic-for" % V.bm4)
      ).value,
      libraryDependencies ++= on(3)(
        "org.typelevel" %% "shapeless3-deriving" % V.shapeless3 % Test
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

  def on[A](major: Int)(a: A): Def.Initialize[Seq[A]] =
    Def.setting {
      CrossVersion.partialVersion(scalaVersion.value) match {
        case Some(v) if v._1 == major => Seq(a)
        case _                        => Nil
      }
    }
}
