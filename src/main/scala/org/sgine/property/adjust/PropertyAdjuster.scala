package org.sgine.property.adjust

trait PropertyAdjuster[T] extends Function3[T, T, Double, T] {
	def apply(current:T, target:T, elapsed:Double):T
}