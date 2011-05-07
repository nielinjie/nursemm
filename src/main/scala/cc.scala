package nielinjie
package nursemm

object CCFacade {
  //  def getStream:Stream={
  //    Stream("")
  //  }
}

object FackCCFacade {
  def currentStream():Option[Stream] = Some(Stream("fack-stream"))

  def activities(stream: Stream) = {
    List(
      Activity("cd1", "jason", DateFormat.defaultDateFormat.parse("2011-03-01"), List(
        Review("/a.file", "1", NoPass, "no"),
        Review("/b.file", "3", NoPass, "noway")

      )),
      Activity("cd2", "kate", DateFormat.defaultDateFormat.parse("2011-04-01"), List(
        Review("/c.file", "1", UnReviewed, "no"),
        Review("/d.file", "2", Passed, "noway")
      ))
    )
  }
}
