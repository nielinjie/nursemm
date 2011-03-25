package nielinjie
package nursemm

import org.specs._
import nursemm.DateFormat._
object CC extends Specification {
  "activites parsing" in {
    val string="""2010-01-22       PA00941296      LIUZHI002
        2010-01-22       PA00942386      LIUZHI002
      """
    Activity.fromCCOutput(string) must equalTo(
      List(
        Activity("PA00941296","LIUZHI002",defaultDateFormat.parse("2010-01-22"),List()),
        Activity("PA00942386","LIUZHI002",defaultDateFormat.parse("2010-01-22"),List())
      ))
  }
  "review parsing" in {
    val string="\"d:\\CCshare\\nielinjie001_oas_fms_2p_proj\\oas_fms\\fms_j2ee\\src\\config\\biz\\sqlmap-mapping-bank.xml@@\\main\\oas_fms_2p_int\\oas_fms_dev2.44.0\\17\", \"d:\\CCshare\\nielinjie001_oas_fms_2p_proj\\oas_fms\\fms_j2ee\\src\\config\\biz\\sqlmap-mapping-bank.xml@@\\main\\oas_fms_2p_int\\oas_fms_dev2.44.0\\16\", \"d:\\CCshare\\nielinjie001_oas_fms_2p_proj\\oas_fms\\fms_j2ee\\src\\config\\biz\\sqlmap-mapping-bank.xml@@\\main\\oas_fms_2p_int\\oas_fms_dev2.44.0\\15\""
    Review.fromCCOutput(string) must equalTo (
      List(
        Review("d:\\CCshare\\nielinjie001_oas_fms_2p_proj\\oas_fms\\fms_j2ee\\src\\config\\biz\\sqlmap-mapping-bank.xml","\\main\\oas_fms_2p_int\\oas_fms_dev2.44.0\\17",UnReviewed,""),
        Review("d:\\CCshare\\nielinjie001_oas_fms_2p_proj\\oas_fms\\fms_j2ee\\src\\config\\biz\\sqlmap-mapping-bank.xml","\\main\\oas_fms_2p_int\\oas_fms_dev2.44.0\\16",UnReviewed,""),
        Review("d:\\CCshare\\nielinjie001_oas_fms_2p_proj\\oas_fms\\fms_j2ee\\src\\config\\biz\\sqlmap-mapping-bank.xml","\\main\\oas_fms_2p_int\\oas_fms_dev2.44.0\\15",UnReviewed,"")
      )
    )
  }
}