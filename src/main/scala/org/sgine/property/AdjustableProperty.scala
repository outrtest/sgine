package org.sgine.property

import org.sgine._;
import org.sgine.property.adjust._;

/**
 * AdjustableProperty trait provides time-based changes to occur over
 * time rather than immediate change when <code>apply(t:T)</code> is
 * called. This trait requires the <code>update</code> method be invoked
 * in order to asynchronously adjust the value to its target value.
 * 
 * @author Matt Hicks
 */
trait AdjustableProperty[T] extends Property[T] with Updatable {
	var adjuster:Function3[T, T, Double, T] = null
	
	private var target:T = apply();
	
	abstract override def apply(value:T):Property[T] = {
		if (adjuster != null) {
			target = value
		} else {
			super.apply(value)
		}
		
		this
	}
	
	def update(time:Double) = {
		if (adjuster != null) {
			val current:T = apply()
			
			if (current != target) {
				val result:T = adjuster(current, target, time)
				
				super.apply(result)
			}
		}
	}
	
	def isAdjusting() = apply() != target
	
	def waitForTarget() = {
		while (isAdjusting) {
			Thread.sleep(10);
		}
	}
}