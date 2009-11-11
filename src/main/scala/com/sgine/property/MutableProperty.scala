package com.sgine.property;

class MutableProperty[T] extends Changeable[T] with Modifiable[T] with Property[T] {
	protected var value:T = _;

	def apply():T = {
		value;
	}
	
	def apply(value:T):Property[T] = {
		val old = this.value;
		
		this.value = modify(value);
		
		changed(old, value);
		
		this;
	}
	
	def modify(value:T) = value;
	
	def changed(oldValue:T, newValue:T) = {
	}
}

trait Modifiable[T] {
	def modify(value:T):T
}

trait Changeable[T] {
	def changed(oldValue:T, newValue:T)
}