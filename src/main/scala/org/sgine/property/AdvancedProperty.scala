package org.sgine.property

import org.sgine.event._

class AdvancedProperty[T] extends MutableProperty[T] with ListenableProperty[T] with BindingProperty[T] with AdjustableProperty[T] {
	private var _parent: Listenable = _
	
	def parent = _parent
	
	def this(value: T) = {
		this()
		
		apply(value)
	}
	
	def this(value: T, parent: Listenable) = {
		this()
		
		_parent = parent
		
		apply(value)
	}
}