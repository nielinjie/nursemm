import sbt._

class Project(info: ProjectInfo) extends DefaultProject(info) with IdeaProject {
  val scalaToolsSnapshots = "Scala Tools Snapshots" at "http://scala-tools.org/repo-snapshots/"
  //    val scalazCore = "org.scalaz" %% "scalaz-core" % "6.0-SNAPSHOT"
  //    val scalazHttp = "org.scalaz" %% "scalaz-http" % "6.0-SNAPSHOT"

  val scalaSwing = "org.scala-lang" % "scala-swing" % "2.8.1"
  val process = "org.scala-tools.sbt" % "process" % "0.1"
  val specs = "org.specs2" %% "specs2" % "1.1" % "test"

  val utilData = "nielinjie" %% "util.data" % "1.0"
  val utilIo = "nielinjie" %% "util.io" % "1.0"
  val utilUi = "nielinjie" %% "util.ui" % "1.0"

}


