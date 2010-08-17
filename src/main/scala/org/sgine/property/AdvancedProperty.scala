package org.sgine.property

import org.sgine.event._

import org.sgine.property.container._

import scala.reflect.Manifest

class AdvancedProperty[T](override protected implicit val manifest: Manifest[T]) extends MutableProperty[T] with DependentProperty[T] with ListenableProperty[T] with NamedProperty[T] with BindingProperty[T] with AnimatingProperty[T] with EventDelegationProperty[T] {
	protected var _name: String = _
	private var _dependency: Property[T] = _
	
	def dependency = _dependency
	
	def this(value: T)(implicit manifest: Manifest[T]) = {
		this()
		
		apply(value)
	}
	
	def this(value: T, parent: Listenable)(implicit manifest: Manifest[T]) = {
		this(value)
		
		this.parent = parent
	}
	
	def this(value: T, parent: Listenable, name: String)(implicit manifest: Manifest[T]) = {
		this(value, parent)
		
		_name = name
	}
	
	def this(value: T, parent: Listenable, name: String, dependency: Property[T])(implicit manifest: Manifest[T]) = {
		this(value, parent, name)
		
		_dependency = dependency
	}
	
	def this(value: T, parent: Listenable, name: String, dependency: Property[T], listener: EventHandler)(implicit manifest: Manifest[T]) = {
		this(value, parent, name, dependency)

		listeners += listener
	}
}