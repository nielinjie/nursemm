package nielinjie
package nursemm

import java.util.Date
import java.lang.{ProcessBuilder=>JPBuilder, Process=>JProcess}
import java.text.SimpleDateFormat

object DateFormat {
  val defaultDateFormat = new SimpleDateFormat("yyyy-MM-dd")
}
object ProcessU {
  import scala.sys.process._
  def run(cmd:String)={
    Process(cmd) !!
  }
}