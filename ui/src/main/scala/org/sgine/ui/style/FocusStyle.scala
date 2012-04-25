package org.sgine.ui.style

import org.sgine.ui.event.{FocusLost, FocusGained}


/**
 * @author Matt Hicks <mhicks@sgine.org>
 */
trait FocusStyle extends Style {
  val focus = StyleProperty("focus")

  component.listeners.synchronous {
    case evt: FocusGained => add(focus)
    case evt: FocusLost => remove(focus)
  }
}
