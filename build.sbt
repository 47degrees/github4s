pgpPassphrase := Some(getEnvVar("PGP_PASSPHRASE").getOrElse("").toCharArray)

lazy val root = (project in file("."))
  .settings(moduleName := "github4s-root")
  .aggregate(github4s)
  .dependsOn(github4s)
  .settings(noPublishSettings: _*)

lazy val github4s =
  (project in file("github4s"))
    .settings(moduleName := "github4s")
    .enablePlugins(BuildInfoPlugin)
    .settings(
      buildInfoKeys := Seq[BuildInfoKey](
        name,
        version,
        "token" -> sys.env.getOrElse("GITHUB_TOKEN", "")
      ),
      buildInfoPackage := "github4s"
    )
    .settings(coreDeps: _*)

//////////
// DOCS //
//////////

lazy val docs = (project in file("docs"))
  .aggregate(github4s)
  .dependsOn(github4s)
  .settings(moduleName := "github4s-docs")
  .settings(micrositeSettings: _*)
  .settings(noPublishSettings: _*)
  .enablePlugins(MicrositesPlugin)

addCommandAlias("ci-test", "+scalafmtCheck; +scalafmtSbtCheck; +docs/mdoc; +test")
addCommandAlias("ci-docs", "docs/mdoc")
