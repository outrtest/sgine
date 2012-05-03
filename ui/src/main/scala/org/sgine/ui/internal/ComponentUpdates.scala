package org.sgine.ui.internal

import org.sgine.Updatable
import org.sgine.ui.Component

/**
 * @author Matt Hicks <mhicks@sgine.org>
 */
class ComponentUpdates(component: Component) {
  def +=(updatable: Updatable) = component.add(updatable)

  def -=(updatable: Updatable) = component.remove(updatable)

  /**
   * Invokes supplied function with the elapsed time when <code>time</code> has elapsed.
   */
  def every(time: Double)(f: Double => Unit) = {
    component.add(new Updatable {
      private var elapsed = 0.0

      override def update(delta: Double) = {
        elapsed += delta
        if (elapsed >= time) {
          f(elapsed)
          elapsed = 0.0
        }
      }
    })
  }
}
