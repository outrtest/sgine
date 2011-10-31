package org.sgine.property

/**
 *
 * @author Matt Hicks <mhicks@sgine.org>
 */
trait MutableProperty[T] extends Property[T] with Function1[T, Unit] {
  def value_=(v: T) = apply(v)

  def :=(v: T) = apply(v)
}











