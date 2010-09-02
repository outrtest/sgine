package org.sgine.property.animate

trait PropertyAnimator[T] extends Function3[T, T, Double, T] {
	def apply(current: T, target: T, elapsed: Double): T
}