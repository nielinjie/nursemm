package nielinjie
package nursemm

import swing._
import event._
import reactive._
import nielinjie.util.ui._
import nieinjie.util.ui.Bind

object MainPanel extends SimpleSwingApplication with Observing with Operations {
  def top = new MainFrame {
    title = "First Swing App"
    contents = new MigPanel("fill", "[fill,:300:]", "[fill,:400:][]") {
      add(listActivity, "wrap")
      add(reviewPanel, "")
    }
  }

  def reviewPanel = new MigPanel("fill,debug", "[fill,:300:][]", "[fill,:300:]") {
    add(listReview, "")
    add(detailPanel, "")
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

  val projectLabel = new Label("Project Info")
  val projectBind: Bind[Project] = Bind.readOnly({
    case Some(project) => {
      projectLabel.text = project.stream.name
      mdActivity.setMaster(project.activities)
    }
    case None => {
      //TODO clean and disable everything
    }
  })

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
      detailGroup.enable(true)
      textMemo.text = review.memo
      statusRadioGroup.status = Some(review.status)
    }
    case None => {
      detailGroup.clean
      detailGroup.enable(false)
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

  val detailGroup = WidgetUtil.group(textMemo, radioPassed, radioNoPass, radioUnReviewed, diffButton)
  val reviewListGroup = detailGroup.++(listReview)
  val activityGroup = reviewListGroup.++(listActivity)

  import SwingSupport._

  bindToListView(mdActivity, listActivity)
  bindToListView(mdReview, listReview)

  projectBind.push(loadProject)
}

trait Operations {
  self: MainPanel.type =>
  def loadProject(): Option[Project] = {
    FackCCFacade.currentStream match {
      case Some(stream) => {
        Project.fromDB(stream) match {
          case Some(p) => Some(p)
          case None => Some(Project.fromCC(stream))
        }
      }
      case None => None
    }
  }
}

trait DomainObjects {
  self: MainPanel.type =>
}