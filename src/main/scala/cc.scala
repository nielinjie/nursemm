package nielinjie
package nursemm

import scalaz._
import Scalaz._

object CCFacade {
  //  def getStream:Stream={
  //    Stream("")
  //  }
}

object FackCCFacade {
  def currentStream(): Validation[String, Stream] = (Stream("fack-stream")).success

  def activities(stream: Stream): Validation[String, List[Activity]] = {
    List(
      Activity("cd1", "jason", DateFormat.defaultDateFormat.parse("2011-03-01"), List(
        Review("/a.file", "1", NoPass(), "no"),
        Review("/b.file", "3", NoPass(), "noway")

      )),
      Activity("cd2", "kate", DateFormat.defaultDateFormat.parse("2011-04-01"), List(
        Review("/c.file", "1", UnReviewed(), "no"),
        Review("/d.file", "2", Passed(), "noway")
      ))
    ).success
  }
}
