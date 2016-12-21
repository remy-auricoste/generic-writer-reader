
logLevel := Level.Warn

resolvers += "Sonatype Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots/"

resolvers += "Typesafe repository" at "https://repo.typesafe.com/typesafe/maven-releases/"

resolvers += "maven" at "http://repo1.maven.org/maven2"

addSbtPlugin("com.typesafe.play" % "sbt-plugin" % "2.5.8")

addSbtPlugin("com.typesafe.sbt" %% "sbt-scalariform" % "1.3.0")