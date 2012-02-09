package org.sgine.property

/**
 * ImmutableProperty is created once and read-only.
 *
 * @author Matt Hicks <mhicks@sgine.org>
 */
case class ImmutableProperty[T](v: T) extends Property[T] {
  def apply() = v
}