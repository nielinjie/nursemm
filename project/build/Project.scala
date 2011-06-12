import sbt._

class Project(info: ProjectInfo) extends DefaultProject(info) with IdeaProject {
  val scalaToolsSnapshots = "Scala Tools Snapshots" at "http://scala-tools.org/repo-snapshots/"
  //    val scalazCore = "org.scalaz" %% "scalaz-core" % "6.0-SNAPSHOT"
  //    val scalazHttp = "org.scalaz" %% "scalaz-http" % "6.0-SNAPSHOT"

  val scalaSwing = "org.scala-lang" % "scala-swing" % "2.8.1"
  val scalazCore = "org.scalaz" %% "scalaz-core" % "latest.release"
  //val process = "org.scala-tools.sbt" % "process" % "0.1"

  val specs = "org.specs2" %% "specs2" % "latest.release" % "test"

  val utilData = "nielinjie" %% "util.data" % "latest.release"
  val utilIo = "nielinjie" %% "util.io" % "latest.release"
  val utilUi = "nielinjie" %% "util.ui" % "latest.release"

}


