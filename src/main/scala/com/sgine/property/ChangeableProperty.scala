package com.sgine.property

trait ChangeableProperty[T] extends Property[T] {
	private var oldValue:T = _;
	
	abstract override def apply(value:T):Property[T] = {
		apply(value, true);
	}
	
	def apply(value:T, invokeListeners:Boolean):Property[T] = {
		val p = super.apply(value);
		
		if (invokeListeners) {
			changed();
		}
		
		p;
	}
	
	def changed():Unit = {
		val newValue = apply();
		
		if (newValue != oldValue) {
			changed(oldValue, newValue);
		}
		
		oldValue = newValue;
	}
	
	def changed(oldValue:T, newValue:T):Unit
}
