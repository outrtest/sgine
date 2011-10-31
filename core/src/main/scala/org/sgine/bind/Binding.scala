package org.sgine.bind

import org.sgine._

/**
 * Represents a listener to see changes and reflect them back to <code>binded</code> when they occur.
 *
 * This is used directly by Binding and should probably not be used directly.
 *
 * @author Matt Hicks <mhicks@sgine.org>
 */
class Binding[T](binded: Function1[T, Unit]) extends Listener[T] {
  def apply(oldValue: T, newValue: T) = {
    binded(newValue)
  }
}