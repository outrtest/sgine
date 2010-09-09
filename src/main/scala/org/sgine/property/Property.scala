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