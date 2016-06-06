logLevel := Level.Warn

addSbtPlugin("org.scalariform"    % "sbt-scalariform"     % "1.6.0")
addSbtPlugin("com.jsuereth"       % "sbt-pgp"             % "1.0.0")
addSbtPlugin("org.scoverage"      % "sbt-scoverage"       % "1.3.5")
addSbtPlugin("org.scoverage"      % "sbt-coveralls"       % "1.1.0")
addSbtPlugin("com.typesafe.sbt"   % "sbt-site"            % "1.0.0")
addSbtPlugin("org.tpolecat"       % "tut-plugin"          % "0.4.2")
addSbtPlugin("com.typesafe.sbt"   % "sbt-ghpages"         % "0.5.4" exclude("com.typesafe.sbt", "sbt-git"))
addSbtPlugin("com.typesafe.sbt"   % "sbt-git"             % "0.8.5")