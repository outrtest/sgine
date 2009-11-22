package com.sgine.property

import com.sgine._;

/**
 * AdjustableProperty trait provides time-based changes to occur over
 * time rather than immediate change when <code>apply(t:T)</code> is
 * called. This trait requires the <code>update</code> method be invoked
 * in order to asynchronously adjust the value to its target value.
 * 
 * @author Matt Hicks
 */
trait AdjustableProperty[T] extends Property[T] with Updatable {
	def update(time:Double) = {
		
	}
}