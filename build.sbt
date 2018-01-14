import sbtorgpolicies.runnable.syntax._

pgpPassphrase := Some(getEnvVar("PGP_PASSPHRASE").getOrElse("").toCharArray)
pgpPublicRing := file(s"$gpgFolder/pubring.gpg")
pgpSecretRing := file(s"$gpgFolder/secring.gpg")

lazy val root = (project in file("."))
  .settings(moduleName := "github4s-root")
  .aggregate(allModules: _*)
  .dependsOn(allModulesDeps: _*)
  .settings(noPublishSettings: _*)

lazy val github4s = (crossProject in file("github4s"))
  .settings(moduleName := "github4s")
  .enablePlugins(BuildInfoPlugin)
  .settings(
    buildInfoKeys := Seq[BuildInfoKey](
      name,
      version,
      "token" -> sys.env.getOrElse("GITHUB4S_ACCESS_TOKEN", "")),
    buildInfoPackage := "github4s"
  )
  .crossDepSettings(commonCrossDeps: _*)
  .settings(standardCommonDeps: _*)
  .jvmSettings(jvmDeps: _*)
  .jsSettings(jsDeps: _*)
  .jsSettings(sharedJsSettings: _*)
  .jsSettings(testSettings: _*)

lazy val github4sJVM = github4s.jvm
lazy val github4sJS  = github4s.js

lazy val scalaz = (project in file("scalaz"))
  .settings(moduleName := "github4s-scalaz")
  .settings(scalazDependencies: _*)
  .dependsOn(github4sJVM)

lazy val catsEffect = (crossProject in file("cats-effect"))
  .settings(moduleName := "github4s-cats-effect")
  .crossDepSettings(catsEffectDependencies: _*)
  .jsSettings(sharedJsSettings: _*)
  .jsSettings(testSettings: _*)
  .dependsOn(github4s)

lazy val catsEffectJVM = catsEffect.jvm
lazy val catsEffectJS  = catsEffect.js

/////////////////////
//// ALL MODULES ////
/////////////////////

lazy val jvmModules: Seq[ProjectReference] = Seq(
  github4sJVM,
  scalaz,
  catsEffectJVM
)

lazy val jsModules: Seq[ProjectReference] = Seq(
  github4sJS,
  catsEffectJS
)

lazy val allModules: Seq[ProjectReference] = jvmModules ++ jsModules

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

//////////
// MISC //
//////////

addCommandAlias("validateDocs", ";project docs;tut;project root")
addCommandAlias(
  "validateJVM",
  (toCompileTestList(jvmModules) ++ List("project root")).asCmd)
addCommandAlias("validateJS", (toCompileTestList(jsModules) ++ List("project root")).asCmd)
addCommandAlias(
  "validate",
  ";clean;compile;coverage;validateJVM;coverageReport;coverageAggregate;coverageOff")
