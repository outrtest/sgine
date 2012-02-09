package org.sgine.event

/**
 * ChangeEvent represents a change of value on the target.
 *
 * @author Matt Hicks <mhicks@sgine.org>
 */
case class ChangeEvent[T](target: Listenable, oldValue: T, newValue: T) extends Event