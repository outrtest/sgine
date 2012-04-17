package org.sgine.property.backing

/**
 * VariableBacking utilizes a standard var for the backing store.
 *
 * @author Matt Hicks <mhicks@sgine.org>
 */
class VariableBacking[T] extends Backing[T] {
  private var v: T = _

  protected[property] final def getValue = v

  protected[property] final def setValue(value: T) {
    this.v = value
  }
}