package org.sgine.input

import org.powerscala.{Enumerated, EnumEntry}

/**
 * MouseButton is an enum that represents a specific mouse button.
 *
 * @author Matt Hicks <mhicks@sgine.org>
 */
sealed class MouseButton extends EnumEntry[MouseButton] {
  protected[input] var _down = false

  def pressed = _down
}

object MouseButton extends Enumerated[MouseButton] {
  val Left = new MouseButton
  val Right = new MouseButton
  val Middle = new MouseButton

  /**
   * Returns all the buttons that are currently pressed.
   */
  def pressed() = values.collect {
    case button if (button.pressed) => button
  }
}