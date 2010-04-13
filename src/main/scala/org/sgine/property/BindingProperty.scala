package org.sgine.property

import java.util.concurrent._
import scala.collection.JavaConversions._

trait BindingProperty[T] extends ChangeableProperty[T] {
	protected var bindings = new CopyOnWriteArraySet[BindingProperty[T]]()
	
	def bind(p: BindingProperty[T]) = {
		apply(p())		// Synchronize the values
		
		p.bindings.add(this)
	}
	
	def unbind(p: BindingProperty[T]) = {
		p.bindings.remove(this)
	}
	
	abstract override def changed(oldValue: T, newValue: T): Unit = {
		super.changed(oldValue, newValue)
		
		bindings.foreach(_ := newValue)
	}
	
	// TODO: support checking getter on update
}