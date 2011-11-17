package org.sgine.property.backing

/**
 * VariableBacking utilizes a standard var for the backing store.
 *
 * @author Matt Hicks <mhicks@sgine.org>
 */
trait VariableBacking[T] {
  private var v: T = _

  protected final def getValue = v

  protected final def setValue(value: T) {
    this.v = value
  }
}