package org.sgine.property;

/**
 * MutableProperty provides an extremely simple Property implementation
 * that may be modified. Calls to <code>apply(t:T)</code> will modify the
 * current instance and return a reference back to itself.
 * 
 * @author Matt Hicks
 */
class MutableProperty[T] extends Property[T] {
	def this(initialValue: T) = {
		this()
		apply(initialValue)
	}

	protected var value: T = _

	def apply(): T = {
		value;
	}
	
	def apply(value: T): Property[T] = {
		this.value = value;
		
		this;
	}
}