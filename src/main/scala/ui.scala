package nielinjie
package nursemm

import swing._
import event._

import reactive._
import nielinjie.util.ui._
import util.ui.Bind
import util.ui.event._
import scalaz._
import sun.tools.jar.Main

object MainPanel extends SimpleSwingApplication with Observing
with ProjectInfoPanel with ActivityPanel with FilterPanel with ReviewPanel
with Operations with BusinessObject {
  def top = new MainFrame with WindowEventSupport {
    title = "First Swing App"
    contents = new MigPanel("fill", "[fill,:300:]", "[][fill,:400:][][]") {
      add(projectInfoPanel, "wrap")
      add(listActivity, "wrap")
      add(filterPanel, "wrap")
      add(reviewPanel, "")
    }

    override def onClosing(w: Window) = {
      println("closing")
      System.exit(0)
    }
  }


  val projectBind: Bind[Project] = Bind.readOnly({
    projectOrError: Validation[String, Project] => projectOrError.fold({
      error: String =>
      //TODO clean and disable everything
    }, {
      project =>
        mdActivity.setMaster(project.activities)
    })
  }).andThen(infoBind)


  import SwingSupport._
  import Bind._

  bindToListView(mdActivity, listActivity)
  bindToListView(mdReview, listReview)

  val activityGroup = reviewListGroup.++(listActivity)

  project = loadProject()
  projectBind.push(project)
}

trait ActivityPanel {
  self: MainPanel.type =>
  val mdActivity = new MasterDetail[Activity](Nil)
  mdActivity.detailBind = Some(Bind(Bind.noErrorHandle(
  {
    case Some(act) => {
      mdReview.setMaster(act.reviews)
    }
    case None => //TODO disable
  }), {
    act => {
      mdReview.saveDetail
      act.map(_.copy(reviews = mdReview.getMaster))
    }
  }
  ))

  val listActivity = new ListView[Activity]() {
    renderer = ListView.Renderer(a => "%s - %s - %tD %<tR - %d file(s)".format(a.cdName, a.user, a.date, a.reviews.size))
  }

}

trait ProjectInfoPanel {
  self: MainPanel.type =>

  import EventSupport._

  def projectInfoPanel = new MigPanel("fill", "[fill,grow][][]", "[]") {
    add(projectNameLabel, "")
    add(saveButton, "")
  }

  val projectNameLabel = new Label("")
  val saveButton = new Button("save")
  saveButton.clicked.foreach {
    x =>
      mdReview.saveDetail
      mdActivity.saveDetail
      project = project.map(_.copy(activities = mdActivity.getMaster))
      saveProject(project)
  }
  val infoGroup = WidgetUtil.group(projectNameLabel, saveButton)
  val infoBind: Bind[Project] = Bind.readOnly({
    vP => vP.fold({
      error =>
        projectNameLabel.text = error
        infoGroup.enable(false)
    }, {
      p =>
        projectNameLabel.text = p.stream.name
        infoGroup.enable(true)
    })
  })
}

trait ReviewPanel {
  self: MainPanel.type =>
  def reviewPanel = new MigPanel("fill", "[fill,grow][]", "[fill,:300:]") {
    add(listReview, "")
    add(detailPanel, "")
  }

  def detailPanel = new MigPanel("fill", "[fill,:300:]", "[][][][][fill,:200:]") {
    add(radioPassed, "wrap")
    add(radioNoPass, "wrap")
    add(radioUnReviewed, "wrap")
    add(diffButton, "wrap")
    add(textMemo, "wrap")
  }

  val mdReview = new MasterDetail[Review](Nil)
  mdReview.detailBind = Some(Bind(Bind.noErrorHandle({
    case Some(review) => {
      detailGroup.enable(true)
      textMemo.text = review.memo
      statusRadioGroup.status = Some(review.status)
    }
    case None => {
      //detailGroup.clean
      detailGroup.enable(false)
    }
  }), {
    review => review.map(_.copy(memo = textMemo.text, status = statusRadioGroup.status.get))
  }))
  val listReview = new ListView[Review]() {
    renderer = ListView.Renderer(r => "%s - %s - %s -%s".format(r.path, r.version, r.status.toString, r.memo))
  }

  val textMemo = new TextField("")
  val radioNoPass = new RadioButton("NoPass")
  val radioPassed = new RadioButton("Passed")
  val radioUnReviewed = new RadioButton("UnReviewd")
  val statusRadioGroup = new ButtonGroup(radioPassed, radioNoPass, radioUnReviewed) {
    val map = Map[AbstractButton, ReviewStatus](radioPassed -> Passed(), radioNoPass -> NoPass(), radioUnReviewed -> UnReviewed())
    val map2 = Map[ReviewStatus, AbstractButton](Passed() -> radioPassed, NoPass() -> radioNoPass, UnReviewed() -> radioUnReviewed)

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
}

trait FilterPanel {
  self: MainPanel.type =>

  import EventSupport._

  def filterPanel = new MigPanel("fill", "[fill,grow][]", "[][]") {
    add(filterCheckbox, "")
    add(filterConfigButton, "wrap")
    add(statusFiltersPanel,"")
    // add(filterHelpButton, "")
  }

  val filterCheckbox = new CheckBox("Source file select strategy. (Not implemented yet)")
  val filterConfigButton = new Button("How to Config...")
  filterCheckbox.enabled = false
  filterConfigButton.enabled = false
  //val filterHelpButton = new Button("What is this?")
  filterCheckbox.toggled.foreach {
    b =>
      println("toggled - " + b)
  }


  val statusNames = List("Nopass", "UnReviewed", "Passed")
  val status = List(NoPass(), UnReviewed(), Passed())
  val checkboxes = statusNames.map(new CheckBox(_))
  checkboxes.foreach {
    cb =>
      cb.selected=true
      cb.toggled.foreach {
        b=>
        mdReview.setFilter(findFilter)
      }
  }
  val filters = status.map {
    st: ReviewStatus =>
      new Filter[Review] {
        def filter(review: Review) = review.status == st
      }
  }
  val statusFiltersPanel=new MigPanel("fill","[][][]","[]"){
    checkboxes.foreach{
      add(_,"")
    }
  }

  def findFilter:Option[Filter[Review]] = {
    val needApplyed=checkboxes.zip(filters).filter({
      //cf:(CheckBox,Filter[Review])=>
      //cf
        _._1.selected
    }).map({
      _._2
    })
    needApplyed.reduceLeftOption(_ or _)
  }
}