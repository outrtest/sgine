package org.sgine.bus

/**
 *
 *
 * @author Matt Hicks <mhicks@sgine.org>
 */
trait FilteredNode extends Node {
  /**
   * Return true if the message should be filtered out, false if the message should be accepted.
   */
  protected def filter(message: Any): Boolean

  abstract override protected[bus] def receive(message: Any) = if (!filter(message)) {
    super.receive(message)
  } else {
    Routing.Continue
  }
}