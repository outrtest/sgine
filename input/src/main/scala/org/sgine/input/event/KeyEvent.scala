package org.sgine.input.event

import org.sgine.input.{Keyboard, KeyState, Key}
import org.sgine.event.{Listenable, Event}

/**
 * KeyEvents
 *
 * @author Matt Hicks <mhicks@sgine.org>
 */
sealed trait KeyEvent extends Event {
  def state: KeyState
}

case class KeyDownEvent(key: Key, target: Listenable = Keyboard) extends KeyEvent {
  val state = KeyState.Down
}

case class KeyUpEvent(key: Key, target: Listenable = Keyboard) extends KeyEvent {
  val state = KeyState.Up
}

case class KeyTypeEvent(character: Char, target: Listenable = Keyboard) extends KeyEvent {
  val state = KeyState.Typed
}