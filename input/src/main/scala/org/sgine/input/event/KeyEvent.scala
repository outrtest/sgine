package org.sgine.input.event

import org.sgine.event.Event
import org.sgine.input.{KeyState, Key}

/**
 * KeyEvents
 *
 * @author Matt Hicks <mhicks@sgine.org>
 */
sealed trait KeyEvent extends Event {
  def state: KeyState
}

case class KeyDownEvent(key: Key) extends KeyEvent {
  val state = KeyState.Down
}

case class KeyUpEvent(key: Key) extends KeyEvent {
  val state = KeyState.Up
}

case class KeyTypeEvent(character: Char) extends KeyEvent {
  val state = KeyState.Typed
}