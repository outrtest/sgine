package org.sgine.input.event

import org.sgine.input.{KeyState, Key}
import org.sgine.event.Event

/**
 * KeyEvents
 *
 * @author Matt Hicks <mhicks@sgine.org>
 */
sealed trait KeyEvent extends Event {
  def state: KeyState

  def duplicate(): KeyEvent
}

case class KeyDownEvent(key: Key) extends KeyEvent {
  val state = KeyState.Down

  def duplicate() = copy()
}

case class KeyUpEvent(key: Key) extends KeyEvent {
  val state = KeyState.Up

  def duplicate() = copy()
}

case class KeyTypeEvent(character: Char) extends KeyEvent {
  val state = KeyState.Typed

  def duplicate() = copy()
}