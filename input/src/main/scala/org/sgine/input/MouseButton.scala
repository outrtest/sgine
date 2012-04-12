package org.sgine.input

import org.sgine.{Enumerated, EnumEntry}

/**
 * MouseButton is an enum that represents a specific mouse button.
 *
 * @author Matt Hicks <mhicks@sgine.org>
 */
sealed class MouseButton extends EnumEntry[MouseButton]

object MouseButton extends Enumerated[MouseButton] {
  val Left = new MouseButton
  val Right = new MouseButton
  val Middle = new MouseButton
}