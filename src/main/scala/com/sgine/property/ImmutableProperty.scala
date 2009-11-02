package com.sgine.property;

class ImmutableProperty[T](value:T) extends Property[T] {
	def apply():T = {
		value;
	}
	
	def apply(value:T):Property[T] = {
		new ImmutableProperty[T](value);
	}
}