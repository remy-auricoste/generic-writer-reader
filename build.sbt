
name := "generic-writer-reader"

version := "1.0"

scalaVersion := "2.11.6"

scalacOptions ++= Seq("-deprecation", "-feature")

javacOptions ++= Seq("-source", "1.8", "-target", "1.8")

resolvers ++= Seq(
  "Typesafe repository" at "http://repo.typesafe.com/typesafe/releases/"
)

libraryDependencies ++= Seq(
  json
  , specs2
  , "com.tzavellas" % "sse-guice" % "0.7.1"
)

testOptions += Tests.Argument(TestFrameworks.JUnit, "-v", "-a")