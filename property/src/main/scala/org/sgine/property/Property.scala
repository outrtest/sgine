package org.sgine.property

import backing.{LocalBacking, AtomicBacking, VolatileVariableBacking, VariableBacking}

/**
 * Property represents an object containing a value.
 */
trait Property[T] extends Function0[T] {
  /**
   * Retrieves the value of the property.
   */
  def value = apply()
}

object Property {
  /**
   * Creates a new StandardProperty with VariableBacking.
   */
  def apply[T]() = new StandardProperty[T] with VariableBacking[T]

  /**
   * Creates a new StandardProperty with VariableBacking and the value supplied.
   */
  def apply[T](value: T) = {
    val p = apply[T]()
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
  def volatile[T]() = new StandardProperty[T] with VolatileVariableBacking[T]

  /**
   * Creates a new StandardProperty with AtomicBacking.
   */
  def atomic[T]() = new StandardProperty[T] with AtomicBacking[T]

  /**
   * Creates a new StandardProperty with LocalBacking.
   */
  def local[T]() = new StandardProperty[T] with LocalBacking[T]
}