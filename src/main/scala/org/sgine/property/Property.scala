package org.sgine.property;

import org.sgine.path.PathSupport

/**
 * Property provides an abstraction and hierarchical control over objects and
 * an alternative methodology to getter/setter principles.
 * 
 * @author Matt Hicks
 */
trait Property[T] extends (() => T) with (T => Property[T]) with PathSupport {
	def :=(value: T): Property[T] = {
		apply(value);
	}
	
	def get() = apply()
	
	def option() = {
		val v = apply()
		if (v != null) {
			Some(v)
		} else {
			None
		}
	}
	
	def resolveElement(key: String) = {
		if (key == "value") {
			Some(apply())
		} else if (key == "()") {
			Some(apply())
		} else {
			None
		}
	}
	
	override def toString() = "Property(" + apply() + ")"
}