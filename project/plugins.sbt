ThisBuild / libraryDependencySchemes ++= Seq(
  "org.scala-lang.modules" %% "scala-xml" % VersionScheme.Always
)
addSbtPlugin("com.github.sbt"      % "sbt-ci-release"           % "1.5.12")
addSbtPlugin("com.47deg"           % "sbt-microsites"           % "1.4.4")
addSbtPlugin("org.scoverage"       % "sbt-scoverage"            % "2.0.9")
addSbtPlugin("org.scalameta"       % "sbt-scalafmt"             % "2.5.2")
addSbtPlugin("org.scalameta"       % "sbt-mdoc"                 % "2.5.1")
addSbtPlugin("com.github.sbt"      % "sbt-unidoc"               % "0.5.0")
addSbtPlugin("de.heikoseeberger"   % "sbt-header"               % "5.10.0")
addSbtPlugin("com.alejandrohdezma" % "sbt-codecov"              % "0.2.1")
addSbtPlugin("com.alejandrohdezma" % "sbt-github"               % "0.11.13")
addSbtPlugin("com.alejandrohdezma" % "sbt-github-header"        % "0.11.13")
addSbtPlugin("com.alejandrohdezma" % "sbt-github-mdoc"          % "0.11.11")
addSbtPlugin("com.alejandrohdezma" % "sbt-remove-test-from-pom" % "0.1.0")
addSbtPlugin("org.typelevel"       % "sbt-tpolecat"             % "0.5.0")
addSbtPlugin("org.portable-scala"  % "sbt-scalajs-crossproject" % "1.3.2")
addSbtPlugin("org.scala-js"        % "sbt-scalajs"              % "1.15.0")
addSbtPlugin("com.typesafe"        % "sbt-mima-plugin"          % "1.1.3")
