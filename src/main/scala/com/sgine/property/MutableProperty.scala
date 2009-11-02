package com.sgine.property;

class MutableProperty[T] extends Property[T] {
	def this(_value:T) = {
		this();
		this._value = _value;
	}
	
	def value_=(_value:T):Property[T] = {
		this._value = _value;
		this;
	}
}
