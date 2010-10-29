package org.sgine.property;

import org.sgine.path.PathSupport

import scala.reflect.Manifest

/**
 * Property provides an abstraction and hierarchical control over objects and
 * an alternative methodology to getter/setter principles.
 * 
 * @author Matt Hicks
 */
trait Property[T] extends (() => T) with (T => Property[T]) with PathSupport {
	protected implicit val manifest: Manifest[T]
	
	protected var _defaultValue: T = _
	def defaultValue = _defaultValue
	
	def :=(value: T) = apply(value)
	
	def get() = apply()
	
	def value = apply()
	
	def value_=(value: T) = apply(value)
	
	def option() = {
		val v = apply()
		if (v != null) {
			Some(v)
		} else {
			None
		}
	}
	
	/**
	 * Determines if the value <code>v</code> is a valid value
	 * that can be passed as a value to this generic property.
	 * 
	 * @param v
	 * @return
	 * 		true if the value is acceptable
	 */
	def isValue(v: Any) = {
		if (manifest.erasure.isInstance(v)) {
			true
		} else {
			false
		}
	}
	
	/**
	 * Determines if the class passed is compatible with the
	 * generic type of this property.
	 * 
	 * @param c
	 * @return
	 * 		true if the class is acceptable
	 */
	def isAssignable(c: Class[_]) = {
		val erasure = org.sgine.util.Reflection.boxed(manifest.erasure)
		if (erasure.isAssignableFrom(c)) {
			true
		} else {
			println("Not assignable: " + erasure + " - " + c)
			false
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
	
	override def toString() = "Property[" + manifest.erasure.getSimpleName + "](value = " + apply() + ")"
}