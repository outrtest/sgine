package org.sgine.property.animate

import org.sgine.easing.Easing
import org.sgine.core.Color
import scala.math.abs

/*
 * Animator for color values, using the specified easing function.
 */
class EasingColorAnimator (var easing: Easing.EasingFunction, var multiplier: Double, var dynamic: Boolean = true) extends PropertyAnimator[Color] {
	private var _target: Color = _
	private var startValue: Color = _
	private var timeToTarget: Double = _
	private var timeElapsed: Double = _

	def apply(current: Color, target: Color, elapsed: Double): Color = {
		if (_target != target) {		// Target changed
			_target = target
			startValue = current
			timeElapsed = 0.0
			if (dynamic) {
				timeToTarget = multiplier
			} else {
				timeToTarget = (multiplier * difference(startValue, target))
			}
		} else {
			timeElapsed += elapsed
		}

		if (timeElapsed >= timeToTarget) {
			target
		} else {
	      val r = easing(timeElapsed, startValue.red, target.red - startValue.red, timeToTarget)
	      val g = easing(timeElapsed, startValue.green, target.green- startValue.green, timeToTarget)
	      val b = easing(timeElapsed, startValue.blue, target.blue - startValue.blue, timeToTarget)
	      val a = easing(timeElapsed, startValue.alpha, target.alpha - startValue.alpha, timeToTarget)
	
	      Color(r, g, b, a)
		}
	}

  private def difference(a: Color, b: Color): Double = {
    ( abs(a.red - b.red) +
      abs(a.green - b.green) +
      abs(a.blue - b.blue) +
      abs(a.alpha - b.alpha) ) / 4.0
  }
}