package org.sgine.property

import org.sgine._;
import org.sgine.property.adjust._;

import org.sgine.work.Updatable

/**
 * AnimatingProperty trait provides time-based changes to occur over
 * time rather than immediate change when <code>apply(t:T)</code> is
 * called.
 * 
 * @author Matt Hicks
 */
trait AnimatingProperty[T] extends Property[T] with Updatable {
	var animator: Function3[T, T, Double, T] = null
	
	private[property] var _target: T = apply();
	
	def target = _target
	
	abstract override def apply(value:T):Property[T] = {
		initUpdatable()
		
		if (animator != null) {
			_target = value
		} else {
			set(value)
		}
		
		this
	}
	
	/**
	 * Explicitly sets value to the target to avoid any "animating" from occurring
	 */
	def set(value: T): Property[T] = {
		super.apply(value)
		_target = value
		
		if (animator != null) {
			animator(_target, _target, 1.0)
		}
		
		this
	}
	
	abstract override def update(time:Double) = {
		super.update(time)
		
		if (animator != null) {
			val current: T = apply()
			
			if (current != _target) {
				val result: T = animator(current, _target, time)
				
				super.apply(result)
			}
		}
	}
	
	def isAnimating() = apply() != _target
	
	def waitForTarget() = {
		while (isAnimating) {
			Thread.sleep(10);
		}
	}
}