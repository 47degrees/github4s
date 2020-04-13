addCommandAlias("ci-test", "+scalafmtCheck; +scalafmtSbtCheck; +docs/mdoc; +test")
addCommandAlias("ci-docs", "project-docs/mdoc; headerCreateAll")

skip in publish := true

lazy val github4s = project
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

lazy val docs = project
  .aggregate(github4s)
  .dependsOn(github4s)
  .settings(micrositeSettings: _*)
  .settings(skip in publish := true)
  .enablePlugins(MicrositesPlugin)

lazy val `project-docs` = (project in file(".docs"))
  .aggregate(github4s)
  .dependsOn(github4s)
  .settings(moduleName := "github4s-project-docs")
  .settings(mdocIn := file(".docs"))
  .settings(mdocOut := file("."))
  .settings(skip in publish := true)
  .enablePlugins(MdocPlugin)
