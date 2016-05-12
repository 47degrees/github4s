organization := "com.fortysevendeg"

name := "github4s"

version := "0.1"

scalaVersion := "2.11.8"

libraryDependencies ++= Seq(
  "org.typelevel" %% "cats" % "0.4.0",
  "org.scalaz" %% "scalaz-concurrent" % "7.1.4",
  "org.scalaj" %% "scalaj-http" % "2.2.1",
  "io.circe" %% "circe-core" % "0.3.0",
  "io.circe" %% "circe-generic" % "0.3.0",
  "io.circe" %% "circe-parser" % "0.3.0",
  "org.scalatest" %% "scalatest" % "2.2.6" % "test",
  "com.ironcorelabs" %% "cats-scalatest" % "1.1.2" % "test",
  "org.mockito" % "mockito-all" % "1.8.4"
)