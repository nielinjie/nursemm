package nielinjie
package nursemm

import swing._
import event._
import reactive._
import nielinjie.util.ui._

object MainPanel extends SimpleSwingApplication with Observing with Operations {
  def top = new MainFrame {
    title = "First Swing App"
    contents = new MigPanel("fill", "[fill,:300:]", "[fill,:400:][]") {
      add(listActivity, "wrap")
      add(new MigPanel("fill,debug", "[fill,:300:][]", "[fill,:300:]") {
        add(listReview, "")
        add(detailPanel, "")
      }, "")
    }
  }

  def detailPanel = new MigPanel("fill,debug", "[fill,:300:]", "[][][][][fill,:200:]") {
    add(radioPassed, "wrap")
    add(radioNoPass, "wrap")
    add(radioUnReviewed, "wrap")
    add(diffButton, "wrap")
    add(textMemo, "wrap")
  }

  reactions += {
    case WindowClosed(top) => {
      println("closing")
      System.exit(0)
    }
  }

  val mdActivity = new MasterDetail[Activity](Nil)
  mdActivity.detailBind = Some(Bind(
  {
    case Some(act) => {
      mdReview.setMaster(act.reviews)
    }
    case None => //TODO disable
  }, {
    act => {
      mdReview.saveDetail
      act.copy(reviews = mdReview.getMaster)
    }
  }
  ))

  val mdReview = new MasterDetail[Review](Nil)
  mdReview.detailBind = Some(Bind({
    case Some(review) => {
      reviewingGroup.enable(true)
      textMemo.text = review.memo
      statusRadioGroup.status = Some(review.status)
    }
    case None => {
      reviewingGroup.clean
      reviewingGroup.enable(false)
    }
  }, {
    review => review.copy(memo = textMemo.text, status = statusRadioGroup.status.get)
  }))

  val listActivity = new ListView[Activity]()
  val listReview = new ListView[Review]()

  val textMemo = new TextField("")
  val radioNoPass = new RadioButton("NoPass")
  val radioPassed = new RadioButton("Passed")
  val radioUnReviewed = new RadioButton("UnReviewd")
  val statusRadioGroup = new ButtonGroup(radioPassed, radioNoPass, radioUnReviewed) {
    val map = Map[AbstractButton, ReviewStatus](radioPassed -> Passed, radioNoPass -> NoPass, radioUnReviewed -> UnReviewed)
    val map2 = Map[ReviewStatus, AbstractButton](Passed -> radioPassed, NoPass -> radioNoPass, UnReviewed -> radioUnReviewed)

    def status = this.selected.map(map.apply(_))

    def status_=(s: Option[ReviewStatus]) = {
      s match {
        case Some(st) => this.select(map2.apply(st))
        case None => this.buttons.foreach {
          _.selected = false
        }
      }
    }
  }

  val diffButton = new Button("diff...")

  val reviewingGroup = WidgetUtil.group(textMemo, radioPassed, radioNoPass, radioUnReviewed, diffButton)
  val reviewGroup = reviewingGroup.++(listReview)

  import SwingSupport._

  bindToListView(mdActivity, listActivity)
  bindToListView(mdReview, listReview)

  init()
}

trait Operations {

  self: MainPanel.type =>


  def init() =
    mdActivity.setMaster(FackCCFacade.activities(FackCCFacade.currentStream))


}
