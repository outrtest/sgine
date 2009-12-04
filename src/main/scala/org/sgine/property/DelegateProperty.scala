package com.sgine.property

class DelegateProperty[T](getter:Function0[T], setter:Function1[T, Unit]) extends Property[T] {
	def apply():T = {
		getter();
	}
	
	def apply(value:T):Property[T] = {
		setter(value);
		
		this;
	}
}
