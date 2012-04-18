package org.sgine.ui.style

import org.sgine.input.event.{MouseReleaseEvent, MousePressEvent, MouseOutEvent, MouseOverEvent}

/**
 * @author Matt Hicks <mhicks@sgine.org>
 */
trait MouseStyle extends Style {
  val mouseOver = StyleProperty("mouseOver")
  val mousePress = StyleProperty("mousePress")

  component.listeners.synchronous {
    case evt: MouseOverEvent => add(mouseOver)
    case evt: MouseOutEvent => remove(mouseOver)
    case evt: MousePressEvent => add(mousePress)
    case evt: MouseReleaseEvent => remove(mousePress)
  }
}