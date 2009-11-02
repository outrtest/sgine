package com.sgine.property;

class MutableProperty[T] extends Changeable[T] with Property[T] {
	protected var value:T = _;

	def apply():T = {
		value;
	}
	
	def apply(value:T):Property[T] = {
		val old = this.value;
		this.value = value;
		
		changed(old, value);
		
		this;
	}
	
	def changed(oldValue:T, newValue:T) = {
		println("SUPER!");
	}
}

abstract class Changeable[T] {
	def changed(oldValue:T, newValue:T)
}