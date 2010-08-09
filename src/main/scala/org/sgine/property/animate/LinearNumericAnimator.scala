package org.sgine.property.animate

class LinearNumericAnimator(multiplier: Double) extends PropertyAnimator[Double] {
	def apply(current: Double, target: Double, elapsed: Double): Double = {
		val adjustment = elapsed * multiplier
		var adjusted = current;
		if (current < target) {
			adjusted += adjustment;
			if (adjusted > target) {
				adjusted = target;
			}
		} else if (current > target) {
			adjusted -= adjustment;
			if (adjusted < target) {
				adjusted = target;
			}
		}
		return adjusted;
	}
}
