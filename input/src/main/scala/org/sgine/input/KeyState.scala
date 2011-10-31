package org.sgine.input

import org.sgine.EnumEntry

/**
 *
 *
 * @author Matt Hicks <mhicks@sgine.org>
 */
sealed class KeyState extends EnumEntry[KeyState]

object KeyState {
  val Down = new KeyState()
  val Up = new KeyState()
  val Typed = new KeyState()
}