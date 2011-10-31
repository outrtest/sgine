package org.sgine.property.backing

/**
 *
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