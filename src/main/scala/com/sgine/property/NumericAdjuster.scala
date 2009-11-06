package com.sgine.property

trait NumericAdjuster[T] extends Modifiable[T] {
	def modify(value:T):T = {
		value;
	}
}