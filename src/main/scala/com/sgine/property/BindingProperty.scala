package com.sgine.property

trait BindingProperty[T] extends ChangeableProperty[T] {
	protected var bindings:List[BindingProperty[T]] = Nil
	
	def bind(p:BindingProperty[T]) = {
		p.bindings = this :: p.bindings
	}
	
	abstract override def changed(oldValue:T, newValue:T):Unit = {
		super.changed(oldValue, newValue)
		
		bindings.foreach(_ := newValue)
	}
}