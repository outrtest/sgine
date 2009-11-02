package com.sgine.property;

trait Property[T] {
	protected var _value:T = _;

	def value = _value;
	
	def value_=(_value:T):Property[T];
	
	def :=(_value:T):Property[T] = {
		return value = _value;
	}
	
	def apply():T = {
		_value;
	}
	
	def update(_value:T) = {
		this._value = _value;
	}
}