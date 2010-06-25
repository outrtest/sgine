package org.sgine.property.animate

import org.sgine.easing.Easing._

import scala.math._

class EasingNumericAnimator(var easing: EasingFunction, multiplier: Double, dynamic: Boolean = true) extends PropertyAnimator[Double] {
	private var target: Double = _
	private var start: Double = _
	private var timeToTarget: Double = _
	private var timeElapsed: Double = _
	
	def apply(current: Double, target: Double, elapsed: Double): Double = {
		if (this.target != target) {		// Target changed
			this.target = target
			start = current
			timeElapsed = 0.0
			if (dynamic) {
				timeToTarget = multiplier
			} else {
				timeToTarget = (multiplier * abs(start - target)) / 100.0
			}
		} else {
			timeElapsed += elapsed
		}
		
		if (timeElapsed >= timeToTarget) {
			target
		} else {
			easing(timeElapsed, start, target - start, timeToTarget)
		}
	}
}