package com.sgine.property

trait BindingProperty[T] extends ChangeableProperty[T] {
	protected var bindings:List[BindingProperty[T]] = Nil
	
	def bind(p:BindingProperty[T]) = {
		p.bindings = this :: p.bindings
	}
	
	def changed(oldValue:T, newValue:T):Unit = {
		bindings.foreach(_ := newValue)
	}
}