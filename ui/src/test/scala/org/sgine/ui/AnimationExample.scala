package org.sgine.ui

import org.sgine.property.MutableProperty
import org.sgine.workflow.{Asynchronous, WorkflowItem, Workflow}

/**
 * @author Matt Hicks <mhicks@sgine.org>
 */
object AnimationExample extends UI {
  implicit def p2wb(property: MutableProperty[Double]) = new WorkflowBuilder(property)

  implicit def wb2w(builder: WorkflowBuilder) = builder.workflow

  val image = Image("sgine.png")
  contents += image

  val time = 1.0
  val wf = image.location.x and image.location.y moveTo 200.0 in time then
    image.location.x and image.location.y moveTo 0.0 in time then
    image.location.x moveTo -200.0 and image.location.y moveTo 200.0 in time then
    image.location.x and image.location.y moveTo 0.0 in time then
    image.location.x and image.location.y moveTo -200.0 in time then
    image.location.x and image.location.y moveTo 0.0 in time then
    image.location.x moveTo 200.0 and image.location.y moveTo -200.0 in time then
    image.location.x and image.location.y moveTo 0.0 in time then
    image.rotation.z moveTo 360.0 in time then
    image.alpha moveTo 0.0 in time
  image.updates += wf
}

class WorkflowBuilder {
  def this(property: MutableProperty[Double]) = {
    this()
    and(property)
  }

  case class AnimatedProperty(property: MutableProperty[Double], destination: Option[Double] = None, time: Option[Double] = None)

  private var workflows = List.empty[Workflow]
  private var current = List.empty[AnimatedProperty]

  def and(property: MutableProperty[Double]) = {
    current = AnimatedProperty(property, previousDestination, previousTime) :: current
    this
  }

  def then(property: MutableProperty[Double]) = {
    if (current.nonEmpty) {
      workflows = new Workflow(animators(current)) with Asynchronous :: workflows
      current = Nil
    }
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

  def workflow = {
    var list: List[WorkflowItem] = workflows
    if (current.nonEmpty) {
      list = new Workflow(animators(current)) with Asynchronous :: list
    }
    new Workflow(list.reverse)
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

  def act(delta: Float) = {
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