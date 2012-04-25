package org.sgine.ui.event

import org.sgine.ui.{FocusManager, Focusable}
import org.sgine.event.Event

/**
 * @author Matt Hicks <mhicks@sgine.org>
 */
trait FocusEvent extends Event {
  def component: Focusable

  def focusManager: FocusManager
}

case class FocusGained(component: Focusable, focusManager: FocusManager) extends FocusEvent

case class FocusLost(component: Focusable, focusManager: FocusManager) extends FocusEvent