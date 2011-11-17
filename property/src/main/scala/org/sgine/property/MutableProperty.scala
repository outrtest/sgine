package org.sgine.property

/**
 * MutableProperty is the base class for all properties that are mutable.
 *
 * @author Matt Hicks <mhicks@sgine.org>
 */
trait MutableProperty[T] extends Property[T] with Function1[T, Unit] {
  /**
   * Assigns a new value to the property.
   */
  def value_=(v: T) = apply(v)

  /**
   * Assigns a new value to the property. Shortcut for property.value = ...
   */
  def :=(v: T) = apply(v)
}