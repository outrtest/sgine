package org.sgine.ui

import org.sgine.property.MutableProperty
import org.sgine.workflow.item.Delay
import org.sgine.{Enumerated, EnumEntry}
import org.sgine.workflow.{Looping, Asynchronous, WorkflowItem, Workflow}

/**
 * @author Matt Hicks <mhicks@sgine.org>
 */
object AnimationExample extends UI {
  verticalSync := true
  fixedTimestep := 1.0 / 60.0

  implicit def p2wb(property: MutableProperty[Double]) = new WorkflowBuilder(property)

  implicit def wb2w(builder: WorkflowBuilder) = builder.workflow

  val all = Repeat.All

  val image = Image("sgine.png")
  contents += image

  val time = 0.4
  val wf = image.location.x and image.location.y moveTo 200.0 in time then
    image.location.x and image.location.y moveTo 0.0 in time then
    image.location.x moveTo -200.0 and image.location.y moveTo 200.0 in time then
    image.location.x and image.location.y moveTo 0.0 in time then
    image.location.x and image.location.y moveTo -200.0 in time then
    image.location.x and image.location.y moveTo 0.0 in time then
    image.location.x moveTo 200.0 and image.location.y moveTo -200.0 in time then
    image.location.x and image.location.y moveTo 0.0 in time then
    image.rotation.z moveTo 360.0 in time then
    image.rotation.z moveTo 0.0 in time pause time then
    image.color.alpha moveTo 0.0 in time then
    image.color.alpha moveTo 1.0 in time loop Int.MaxValue
  image.updates += wf
}

sealed class Repeat extends EnumEntry[Repeat]

object Repeat extends Enumerated[Repeat] {
  val All = new Repeat()
  val First = new Repeat()
  val Last = new Repeat()
}

class WorkflowBuilder {
  def this(property: MutableProperty[Double]) = {
    this()
    and(property)
  }

  case class AnimatedProperty(property: MutableProperty[Double], destination: Option[Double] = None, time: Option[Double] = None)

  private var loopCount = 0
  private var workflowItems = List.empty[WorkflowItem]
  private var current = List.empty[AnimatedProperty]

  private def updateWorkflowItems() = {
    if (current.nonEmpty) {
      workflowItems = new Workflow(animators(current)) with Asynchronous :: workflowItems
      current = Nil
    }
  }

  def and(property: MutableProperty[Double]) = {
    current = AnimatedProperty(property, previousDestination, previousTime) :: current
    this
  }

  def then(property: MutableProperty[Double]) = {
    updateWorkflowItems()
    and(property)
  }

  def moveTo(destination: Double) = {
    val someDestination = Some(destination)
    val first = current.head
    current = current.map(ap => ap.copy(destination = if (ap.destination == None || ap == first) someDestination else ap.destination))
    this
  }

  def in(time: Double) = {
    val someTime = Some(time)
    val first = current.head
    current = current.map(ap => ap.copy(time = if (ap.time == None || ap == first) someTime else ap.time))
    this
  }

  def pause(time: Double) = {
    updateWorkflowItems()
    workflowItems = Delay(time.toFloat) :: workflowItems
    this
  }

  def delay(time: Double) = pause(time)

  def repeat(r: Repeat) = {
    updateWorkflowItems()
    r match {
      case Repeat.All => workflowItems = workflowItems ::: workflowItems
      case Repeat.First => workflowItems = workflowItems.last :: workflowItems
      case Repeat.Last => workflowItems = workflowItems.head :: workflowItems
    }
    this
  }

  def loop(count: Int) = {
    loopCount = count
    this
  }

  def workflow = {
    var list: List[WorkflowItem] = workflowItems
    if (current.nonEmpty) {
      list = new Workflow(animators(current)) with Asynchronous :: list
    }
    new Workflow(list.reverse) with Looping {
      val loops = loopCount
    }
  }

  private def animators(aps: List[AnimatedProperty]) = aps.map(ap => PropertyAnimator(ap.property, ap.destination.getOrElse(0.0), ap.time.getOrElse(0.0)))

  private def previousDestination = if (current.nonEmpty) {
    current.last.destination
  } else {
    None
  }

  private def previousTime = if (current.nonEmpty) {
    current.last.time
  } else {
    None
  }
}

case class PropertyAnimator(property: MutableProperty[Double], destination: Double = 0.0, time: Double = 0.0) extends WorkflowItem {
  private var multiplier = 0.0

  override def begin() = {
    multiplier = (destination - property()) / time
  }

  def act(delta: Double) = {
    val adjust = delta * multiplier
    val value = property() + adjust
    if (multiplier > 0.0 && value >= destination) {
      property := destination
    } else if (multiplier < 0.0 && value <= destination) {
      property := destination
    } else {
      property := value
    }
    property() == destination
  }
}