package org.sgine.bind

import org.sgine.event._

/**
 * Represents a listener to see changes and reflect them back to <code>binded</code> when they occur.
 *
 * This is used directly by Binding and should probably not be used directly.
 *
 * @author Matt Hicks <mhicks@sgine.org>
 */
class Binding[T](binded: Function1[T, Unit]) extends Listener {
  def process(event: Event) = event match {
    case changeEvent: ChangeEvent[T] => binded(changeEvent.newValue)
    case _ => // Ignore everything else
  }
}