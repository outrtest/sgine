package org.sgine.property

import backing.{LocalBacking, AtomicBacking, VolatileVariableBacking, VariableBacking}
import org.sgine.hierarchy.Child
import org.sgine.naming.NamedChild

/**
 * Property represents an object containing a value.
 */
trait Property[T] extends Function0[T] with Child with NamedChild {
  def parent: PropertyParent = null

  /**
   * Retrieves the value of the property.
   */
  def value = apply()
}

object Property {
  /**
   * Creates a new StandardProperty with VariableBacking.
   */
  def apply[T]()(implicit parent: PropertyParent) = new StandardProperty[T]()(parent) with VariableBacking[T]

  /**
   * Creates a new StandardProperty with VariableBacking and the value supplied.
   */
  def apply[T](value: T)(implicit parent: PropertyParent) = {
    val p = apply[T]()(parent)
    p.value = value
    p
  }

  /**
   * Creates a new Property with a value tied to the function supplied.
   */
  def apply[T](f: => T) = new Property[T] {
    def apply() = f
  }

  /**
   * Creates a new StandardProperty with VolatileVariableBacking.
   */
  def volatile[T]()(implicit parent: PropertyParent) = new StandardProperty[T]()(parent) with VolatileVariableBacking[T]

  /**
   * Creates a new StandardProperty with AtomicBacking.
   */
  def atomic[T]()(implicit parent: PropertyParent) = new StandardProperty[T]()(parent) with AtomicBacking[T]

  /**
   * Creates a new StandardProperty with LocalBacking.
   */
  def local[T]()(implicit parent: PropertyParent) = new StandardProperty[T]()(parent) with LocalBacking[T]
}