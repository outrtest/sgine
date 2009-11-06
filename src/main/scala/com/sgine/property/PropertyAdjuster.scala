package com.sgine.property

trait PropertyAdjuster[T] extends Modifiable[T] {
	protected var current:T
	protected var target:T
	
	abstract override def modify(value:T):T = {
		var v = super.modify(value);
		
		target = v;
		
		current;
	}
	
	def adjust(source:T, destination:T, percentage:Double):T
}