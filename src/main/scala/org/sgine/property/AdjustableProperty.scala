package org.sgine.property

import org.sgine._;
import org.sgine.property.adjust._;

import org.sgine.work.Updatable

/**
 * AdjustableProperty trait provides time-based changes to occur over
 * time rather than immediate change when <code>apply(t:T)</code> is
 * called. This trait requires the <code>update</code> method be invoked
 * in order to asynchronously adjust the value to its target value.
 * 
 * @author Matt Hicks
 */
trait AdjustableProperty[T] extends Property[T] with Updatable {
	var adjuster: Function3[T, T, Double, T] = null
	
	private[property] var _target: T = apply();
	
	def target = _target
	
	abstract override def apply(value:T):Property[T] = {
		if (adjuster != null) {
			_target = value
		} else {
			set(value)
		}
		
		this
	}
	
	/**
	 * Explicitly sets value to the target to avoid any "adjusting" from occurring
	 */
	def set(value: T): Property[T] = {
		super.apply(value)
		_target = value
		
		if (adjuster != null) {
			adjuster(_target, _target, 1.0)
		}
		
		this
	}
	
	def update(time:Double) = {
		if (adjuster != null) {
			val current: T = apply()
			
			if (current != _target) {
				val result: T = adjuster(current, _target, time)
				
				super.apply(result)
			}
		}
	}
	
	def isAdjusting() = apply() != _target
	
	def waitForTarget() = {
		while (isAdjusting) {
			Thread.sleep(10);
		}
	}
}