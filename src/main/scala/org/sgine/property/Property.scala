package org.sgine.property;

/**
 * Property provides an abstraction and hierarchical control over objects and
 * an alternative methodology to getter/setter principles.
 * 
 * @author Matt Hicks
 */
trait Property[T] extends (() => T) with (T => Property[T]) {
	def :=(value: T): Property[T] = {
		apply(value);
	}
	
	def get() = apply()
}