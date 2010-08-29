package org.sgine.property

import org.sgine.event._

import org.sgine.property.container._

import scala.reflect.Manifest

class AdvancedProperty[T] private(override protected implicit val manifest: Manifest[T]) extends MutableProperty[T] with DependentProperty[T] with ListenableProperty[T] with NamedProperty[T] with BindingProperty[T] with AnimatingProperty[T] with EventDelegationProperty[T] with CombiningProperty[T] {
	protected var _name: String = _
	private var _dependency: Property[T] = _
	private var _combine: Property[T] = _
	private var _combineFunction: (T, T) => T = _
	
	def dependency = _dependency
	def combine = _combine
	def combineFunction = _combineFunction
	
	def this(value: T = null, parent: Listenable = null, name: String = null, dependency: Property[T] = null, combine: Property[T] = null, combineFunction: (T, T) => T = null, listener: EventHandler = null)(implicit manifest: Manifest[T]) = {
		this()

		apply(value)
		this.parent = parent
		_name = name
		_dependency = dependency
		_combine = combine
		_combineFunction = combineFunction
		if (listener != null) {
			listeners += listener
		}
	}
}