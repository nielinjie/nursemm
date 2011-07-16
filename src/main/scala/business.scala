package nielinjie
package nursemm


import scalaz.{Validation, Scalaz}

trait Operations {
  self: MainPanel.type =>
  val ccF = FackCCFacade

  import Scalaz._

  def loadProject(): Validation[String, Project] = {
    for {
      cs <- ccF.currentStream
      proFromDB <- Project.fromDB(cs).fold(_.success, Project.fromCC(cs))
    } yield proFromDB
  }

  def saveProject(project:Validation[String,Project]) ={
    project.foreach{
      project=>
      println(project)
      Project.toDB(project)
    }
  }
}

trait BusinessObject {
  self: MainPanel.type =>
  var project:Validation[String,Project] = _
}
