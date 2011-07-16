name := "nursemm"

version := "1.0"

organization := "nielinjie"

scalaVersion := "2.8.1"

resolvers += ScalaToolsSnapshots

resolvers += "wso" at "http://dist.wso2.org/maven2/"

resolvers += "sona" at "http://oss.sonatype.org/content/repositories/snapshots"

libraryDependencies += "org.specs2" %% "specs2" % "latest.release" % "test"


libraryDependencies ++= Seq(
    "org.scalaz" %% "scalaz-core" % "latest.release",
    "nielinjie" %% "util.data" % "latest.release",
    "nielinjie" %% "util.io" % "latest.release",
    "nielinjie" %% "util.ui" % "latest.release"
)

libraryDependencies <<= (scalaVersion, libraryDependencies) { (sv, deps) =>
	deps :+ ("org.scala-lang" % "scala-swing" % sv)
}

fork in run := true

checksums := Nil
