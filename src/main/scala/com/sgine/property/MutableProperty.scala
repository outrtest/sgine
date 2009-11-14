package com.sgine.property;

class MutableProperty[T] extends Property[T] {
	protected var value:T = _;

	def apply():T = {
		value;
	}
	
	def apply(value:T):Property[T] = {
		this.value = value;
		
		this;
	}
}