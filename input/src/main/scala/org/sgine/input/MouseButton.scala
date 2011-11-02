package org.sgine.input

import org.sgine.{Enumerated, EnumEntry}

/**
 *
 *
 * @author Matt Hicks <mhicks@sgine.org>
 */
sealed class MouseButton(override val ordinal: Int) extends EnumEntry[MouseButton]

object MouseButton extends Enumerated[MouseButton] {
  val Left = new MouseButton(0)
  val Right = new MouseButton(1)
  val Middle = new MouseButton(2)
}