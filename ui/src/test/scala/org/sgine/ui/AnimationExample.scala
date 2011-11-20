package org.sgine.ui

import org.sgine.property.MutableProperty
import org.sgine.workflow.{WorkflowItem, Workflow}

/**
 *
 *
 * @author Matt Hicks <mhicks@sgine.org>
 */
object AnimationExample extends UI {
  implicit def p2pa(property: MutableProperty[Double]) = PropertyAnimator(property)

  val image = Image("sgine.png")
  contents += image

  val workflow = image.location.x and image.location.y moveTo 200.0 in 5.0
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

  def and(property: MutableProperty[Double]) = {
    PropertyAnimatorWorkflow(List(this, this.copy(property = property)))
  }
}

case class PropertyAnimatorWorkflow(propertyAnimators: List[PropertyAnimator]) extends Workflow(propertyAnimators) {
  def moveTo(destination: Double) = {
    PropertyAnimatorWorkflow(propertyAnimators.map(pa => pa.copy(destination = destination)))
  }

  def in(time: Double) = {
    PropertyAnimatorWorkflow(propertyAnimators.map(pa => pa.copy(time = time)))
  }

  def and(property: MutableProperty[Double]) = {
    PropertyAnimatorWorkflow(propertyAnimators.head.copy(property = property) :: propertyAnimators)
  }
}