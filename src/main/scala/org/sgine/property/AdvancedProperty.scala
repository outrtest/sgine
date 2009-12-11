package org.sgine.property

import org.sgine.event._

class AdvancedProperty[T] extends MutableProperty[T] with ListenableProperty[T] with NamedProperty with BindingProperty[T] with AdjustableProperty[T] {
	private var _parent: Listenable = _
	private var _name: Symbol = _
	
	def this(value: T) = {
		this()
		
		apply(value)
	}
	
	def this(value: T, parent: Listenable) = {
		this(value)
		
		_parent = parent
	}
	
	def this(value: T, parent: Listenable, name: Symbol) = {
		this(value, parent)
		
		_name = name
	}
	
	def parent = _parent
	
	def name = _name
}