package org.sgine.property.workflow

import org.sgine.property.MutableProperty
import org.sgine.workflow.WorkflowBuilder

/**
 * @author Matt Hicks <mhicks@sgine.org>
 */
class PropertyAnimatorBuilder(builder: WorkflowBuilder) {
  def and(property: MutableProperty[Double]) = {
    builder.add(PropertyAnimator(property, Double.NegativeInfinity, Double.NegativeInfinity))
  }

  def moveTo(destination: Double) = {
    val items = builder.currentItems.map {
      case item: PropertyAnimator if (item.destination == Double.NegativeInfinity) => item.copy(destination = destination)
      case item => item
    }
    builder.copy(currentItems = items)
  }

  def in(time: Double) = {
    val items = builder.currentItems.map {
      case item: PropertyAnimator if (item.time == Double.NegativeInfinity) => item.copy(time = time)
      case item => item
    }
    builder.copy(currentItems = items)
  }
}
