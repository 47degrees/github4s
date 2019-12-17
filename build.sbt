pgpPassphrase := Some(getEnvVar("PGP_PASSPHRASE").getOrElse("").toCharArray)
pgpPublicRing := file(s"$gpgFolder/pubring.gpg")
pgpSecretRing := file(s"$gpgFolder/secring.gpg")

lazy val root = (project in file("."))
  .settings(moduleName := "github4s-root")
  .aggregate(allModules: _*)
  .dependsOn(allModulesDeps: _*)
  .settings(noPublishSettings: _*)

lazy val github4s =
  (project in file("github4s"))
    .settings(moduleName := "github4s")
    .enablePlugins(BuildInfoPlugin)
    .settings(
      buildInfoKeys := Seq[BuildInfoKey](
        name,
        version,
        "token" -> sys.env.getOrElse("GITHUB4S_ACCESS_TOKEN", "")),
      buildInfoPackage := "github4s"
    )
    .settings(coreDeps: _*)

lazy val catsEffect =
  (project in file("cats-effect"))
    .settings(moduleName := "github4s-cats-effect")
    .settings(catsEffectDependencies: _*)
    .dependsOn(github4s)

/////////////////////
//// ALL MODULES ////
/////////////////////

lazy val allModules: Seq[ProjectReference] = Seq(github4s, catsEffect)

lazy val allModulesDeps: Seq[ClasspathDependency] =
  allModules.map(ClasspathDependency(_, None))

//////////
// DOCS //
//////////

lazy val docs = (project in file("docs"))
  .dependsOn(allModulesDeps: _*)
  .settings(moduleName := "github4s-docs")
  .settings(micrositeSettings: _*)
  .settings(docsDependencies: _*)
  .settings(noPublishSettings: _*)
  .enablePlugins(MicrositesPlugin)
