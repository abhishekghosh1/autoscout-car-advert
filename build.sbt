name := "autoscout-car-advert"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.7"

libraryDependencies ++= Seq(
  jdbc,
  cache,
  ws,
  "org.scalatestplus.play" %% "scalatestplus-play" % "1.5.1" % Test
)

//enable swagger-ui
libraryDependencies += "org.webjars" % "swagger-ui" % "2.2.0"

// AWS Scala-wrapped SDK
libraryDependencies += "com.github.seratch" %% "awscala" % "0.5.+"

// Extra Play filters
libraryDependencies += filters


// Assembly merge strategy for certain files
assemblyMergeStrategy in assembly := {
  case PathList("META-INF", "MANIFEST.MF") => MergeStrategy.discard
  case x         => MergeStrategy.first
}

// Scala test exclude non-src classes
coverageExcludedPackages := "<empty>;controllers\\..*Reverse.*; router.Routes.*;"
