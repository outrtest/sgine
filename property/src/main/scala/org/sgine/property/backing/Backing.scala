package org.sgine.property.backing

/**
 * @author Matt Hicks <mhicks@sgine.org>
 */
trait Backing[T] {
  protected[property] def getValue: T

  protected[property] def setValue(value: T): Unit
}
