package org.sgine.property.animate

class PausingAnimator[T](var pause: Double, var animator: PropertyAnimator[T]) extends PropertyAnimator[T] {
	private var _target: T = _
	private var paused = 0.0
	
	def apply(current: T, target: T, elapsed: Double): T = {
		if (_target != target) {
			paused = 0.0
			_target = target
		}
		if (paused >= pause) {
			if (animator != null) {
				animator(current, target, elapsed)
			} else {
				target
			}
		} else {
			paused += elapsed
			current
		}
	}
}