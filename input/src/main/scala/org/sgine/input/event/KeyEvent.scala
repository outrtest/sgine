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

/**
 * KeyPressedEvent is a special kind of event that fires for every update that a key is held down.
 *
 * This is also a singleThreaded event, so asynchronous or concurrent listeners will never receive this event.
 */
class KeyPressedEvent(val key: Key) extends KeyEvent {
  private var _time: Double = _
  private var _delta: Double = _

  val state = KeyState.Down

  /**
   * Represents the amount of time the key has been pressed.
   */
  def time = _time

  /**
   * Represents the current delta.
   */
  def delta = _delta

  override def singleThreaded = true

  def duplicate() = this

  override def toString = "KeyPressedEvent(key = %s, time = %s, delta = %s)".format(key, time, delta)
}

object KeyPressedEvent {
  /**
   * Updates the supplied event setting the key pressed state and firing events.
   */
  def update(event: KeyPressedEvent, delta: Double, listenable: Listenable) = {
    if (event.key.pressed) {
      event._time += delta
      event._delta = delta
    } else {
      event.key._down = true
    }
    Keyboard.fire(event)
    if (listenable != null) {
      listenable.fire(event)
    }
  }

  /**
   * Releases the key for the supplied event.
   */
  def released(event: KeyPressedEvent) = {
    event._time = 0.0
    event._delta = 0.0
    event.key._down = false
  }
}