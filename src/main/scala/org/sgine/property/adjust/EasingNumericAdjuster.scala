package org.sgine.property.adjust

class EasingNumericAdjuster(easing:Function4[Double, Double, Double, Double, Double], multiplier:Double, dynamic:Boolean = true) extends PropertyAdjuster[Double] {
	private var target:Double = _
	private var start:Double = _
	private var timeToTarget:Double = _
	private var timeElapsed:Double = _
	
	def apply(current:Double, target:Double, elapsed:Double):Double = {
		if (this.target != target) {		// Target changed
			this.target = target
			start = current
			timeElapsed = 0.0
			if (dynamic) {
				timeToTarget = multiplier
			} else {
				timeToTarget = (multiplier * Math.abs(start - target)) / 100.0
			}
		} else {
			timeElapsed += elapsed
		}
		
		easing(timeElapsed, start, target - start, timeToTarget)
	}
}