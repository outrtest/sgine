package org.sgine.property

import org.sgine._;
import org.sgine.property.animate._;

import org.sgine.work.Updatable

/**
 * AnimatingProperty trait provides time-based changes to occur over
 * time rather than immediate change when <code>apply(t:T)</code> is
 * called.
 * 
 * @author Matt Hicks
 */
trait AnimatingProperty[T] extends Property[T] with Animatable[T] {
	var animator: PropertyAnimator[T] = null
	
	def target = if ((animator != null) && (animator.property != null)) {
		animator.target
	} else {
		apply()
	}
	
	abstract override def apply(value: T): Property[T] = {
		if (animator != null) {
			animator(this, value)
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
		
		if (animator != null) {
			animator.disable()
		}
		
		this
	}
	
//	abstract override def update(time:Double) = {
//		super.update(time)
//		
//		if (animator != null) {
//			val current: T = apply()
//			
//			if (current != _target) {
//				val result: T = animator(current, _target, time)
//				
//				super.apply(result)
//			}
//		}
//	}
	
	protected[property] def setAnimation(value: T) = {
		super.apply(value)
	}
	
	def isAnimating() = if (animator != null) {
		animator.enabled
	} else {
		false
	}
	
	def waitForTarget() = {
		while (isAnimating) {
			Thread.sleep(10)
		}
	}
	
	abstract override def resolveElement(key: String) = key match {
		case "animator" => Some(animator)
		case "target" => Some(target)
		case _ => super.resolveElement(key)
	}
}

trait Animatable[T] extends Property[T] {
	protected[property] def setAnimation(value: T): Unit
}