addCommandAlias("ci-test", "+scalafmtCheckAll; +scalafmtSbtCheck; +mdoc; +test")
addCommandAlias("ci-docs", "+mdoc; headerCreateAll")
addCommandAlias("ci-microsite", "publishMicrosite")

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

lazy val microsite: Project = project
  .dependsOn(github4s)
  .enablePlugins(MicrositesPlugin)
  .enablePlugins(ScalaUnidocPlugin)
  .settings(micrositeSettings: _*)
  .settings(skip in publish := true)
  .settings(unidocProjectFilter in (ScalaUnidoc, unidoc) := inProjects(github4s, microsite))

lazy val `project-docs` = (project in file(".docs"))
  .aggregate(github4s)
  .dependsOn(github4s)
  .settings(moduleName := "github4s-project-docs")
  .settings(mdocIn := file(".docs"))
  .settings(mdocOut := file("."))
  .settings(skip in publish := true)
  .enablePlugins(MdocPlugin)
