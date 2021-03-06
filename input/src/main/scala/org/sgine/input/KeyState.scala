package org.sgine.input

import org.powerscala.{Enumerated, EnumEntry}


/**
 * KeyState is an enum that represents the current state of a Key.
 *
 * @author Matt Hicks <mhicks@sgine.org>
 */
sealed class KeyState extends EnumEntry[KeyState]

object KeyState extends Enumerated[KeyState] {
  val Down = new KeyState()
  val Up = new KeyState()
  val Typed = new KeyState()
}