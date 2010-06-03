package org.sgine.property

import java.util.concurrent._

trait BindingProperty[T] extends ChangeableProperty[T] {
	protected var bindings: List[BindingProperty[T]] = Nil
	
	def bind(p: BindingProperty[T]) = {
		apply(p())		// Synchronize the values
		
		p.synchronized {
			p.bindings = this :: p.bindings
		}
	}
	
	def unbind(p: BindingProperty[T]) = {
		p.synchronized {
			p.bindings = p.bindings.filterNot(_ == this)
		}
	}
	
	abstract override def changed(oldValue: T, newValue: T): Unit = {
		super.changed(oldValue, newValue)
		
		for (b <- bindings) {
			b := newValue
		}
	}
	
	// TODO: support checking getter on update
}