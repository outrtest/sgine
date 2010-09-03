package org.sgine.property.animate

import org.sgine.property.Animatable

import org.sgine.work.Updatable

trait PropertyAnimator[T] extends Function3[T, T, Double, T] with Updatable {
	@volatile private var _property: Animatable[T] = _
	@volatile private var _target: T = _
	
	final def property = _property
	final def target = _target
	
	def apply(property: Animatable[T], target: T) = {
		initUpdatable()
		
		_property = property
		_target = target
	}
	
	def disable() = {
		_property = null
	}
	
	def enabled = property != null
	
	def apply(current: T, target: T, elapsed: Double): T
	
	abstract override def update(time: Double) = {
		super.update(time)
		
		// TODO: support time-based instead of target based finishing
		val p = property
		val t = _target
		if ((p != null) && (t != null)) {
			val c = p()
			val r = apply(c, t, time)
			
			if (r == t) {
				disable()
			}
			
			p.setAnimation(r)
		}
	}
}