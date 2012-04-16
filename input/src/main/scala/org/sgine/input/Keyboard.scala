package org.sgine.input

import event.{KeyUpEvent, KeyDownEvent}
import org.sgine.event.Listenable

/**
 * Keyboard represents a listenable object to hear all events that happen on the keyboard.
 *
 * @author Matt Hicks <mhicks@sgine.org>
 */
object Keyboard extends Listenable {
  // Update the Key state
  listeners.synchronous {
    case evt: KeyDownEvent => evt.key._down = true
    case evt: KeyUpEvent => evt.key._down = false
  }
}