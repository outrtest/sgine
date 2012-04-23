package org.sgine.property.workflow

import org.sgine.property.MutableProperty
import org.sgine.workflow.WorkflowItem

/**
 * @author Matt Hicks <mhicks@sgine.org>
 */
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
