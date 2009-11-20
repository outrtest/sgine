package com.sgine.property;

trait Property[T] extends (() => T) with (T => Property[T]) {
	def :=(value:T):Property[T] = {
		apply(value);
	}
}