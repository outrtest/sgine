package org.sgine.property.workflow

import org.sgine.property.MutableProperty
import org.sgine.workflow.WorkflowItem
import org.sgine.easing.Easing

/**
 * @author Matt Hicks <mhicks@sgine.org>
 */
case class PropertyAnimator(property: MutableProperty[Double],
                            destination: Double = 0.0,
                            time: Double = 0.0,
                            easing: Easing = null) extends WorkflowItem {
  private var elapsed: Double = _
  private var start: Double = _

  override def begin() = {
    elapsed = 0.0
    start = property()
  }

  def act(delta: Double) = {
    elapsed += delta
    property := (easing match {
      case null => Easing.LinearIn(elapsed, start, destination, time)
      case e => e(elapsed, start, destination, time)
    })
    elapsed >= time
  }
}
