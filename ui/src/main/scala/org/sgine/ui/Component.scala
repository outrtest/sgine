package org.sgine.ui

import org.sgine.input.event.MouseEventSupport
import org.sgine.property.{PropertyParent, Property}
import org.sgine.{Listenable, UpdatableInvocation}

/**
 *
 *
 * @author Matt Hicks <mhicks@sgine.org>
 */
trait Component extends PropertyParent with MouseEventSupport with UpdatableInvocation {
  val visible = Property[Boolean](true)
  val mouseEnabled = Property[Boolean](true)

  object location extends PropertyParent {
    val x = Property[Double](0.0)
    val y = Property[Double](0.0)
    val z = Property[Double](0.0)
  }

  object size extends PropertyParent {
    val width = Property[Double](0.0)
    val height = Property[Double](0.0)
  }

  def hitTest(x: Double, y: Double) = {
    if (visible() && mouseEnabled()) {
      val x1 = location.x()
      val x2 = x1 + size.width()
      val y1 = location.y()
      val y2 = y1 + size.height()
      x >= x1 && x <= x2 && y >= y1 && y <= y2
    }
    else {
      false
    }
  }

  def onUpdate(listenables: Listenable[_]*)(f: => Unit) = {
    val function = () => f
    listenables.foreach(l => {
      val listener = (oldValue: Any, newValue: Any) => {
        delayedHandling(l, function)
      }
      l.listeners += listener
    })
  }

  def destroy() = {
  }
}

object Component {
  val mouse = Property[Component]()
}