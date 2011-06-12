package nielinjie
package nursemm

import java.io.File
import java.util.Date
import DateFormat._
import scala.util.matching.Regex
import util.io.{XStreamSerializer, HasId, FileDB}

case class Project(stream: Stream, activities: List[Activity])

object Project {
  val dbRootPath = new File("./doc/codeReview")
  implicit val projectId: HasId[Project] = new HasId[Project] {
    def getId(project: Project) = project.stream.name
  }
  implicit val serializer = new XStreamSerializer[Project]

  lazy val db = new FileDB[Project](dbRootPath)

  def fromDB(stream: Stream) = db.get(stream.name)

  def toDB(project: Project) = db.put(project)

  def fromCC(stream: Stream) = FackCCFacade.activities(stream).map(Project(stream, _))

}

case class Stream(name: String)

object Stream {
  def fromCCOutput(string: String) = {
    Stream(string.split(":").last)
  }
}

case class Activity(cdName: String, user: String, date: Date, reviews: List[Review])

object Activity {
  def fromCCOutput(ccOutput: String): List[Activity] = {
    ccOutput.trim.lines.map {
      line =>
        val parts = line.trim.split("\\s+")
        Activity(parts.tail.head, parts.last, defaultDateFormat.parse(parts.head), List())
    }.toList
  }
}

case class Review(path: String, version: String, status: ReviewStatus, memo: String)

object Review {
  def fromCCOutput(cc: String): List[Review] = {
    val format = """"(.*?)@@(.*?)"""".r
    format.findAllIn(cc).matchData.map {
      path_version =>
        Review(path_version.group(1), path_version.group(2), UnReviewed, "")
    }.toList
  }
}

sealed trait ReviewStatus

case object Passed extends ReviewStatus

case object NoPass extends ReviewStatus

case object UnReviewed extends ReviewStatus