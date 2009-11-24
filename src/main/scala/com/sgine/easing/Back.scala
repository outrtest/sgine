package com.sgine.easing

object Back {
	private val overshoot = 1.70158d;
	
	def easeIn(time:Double, start:Double, change:Double, duration:Double):Double = {
		var t = time / duration
		change * t * t * ((overshoot + 1.0) * t - overshoot) + start
	}
	
	def easeOut(time:Double, start:Double, change:Double, duration:Double):Double = {
		val t = time / duration - 1
		change * (time * time * ((overshoot + 1) * time + overshoot) + 1) + start
	}
	
	def easeInOut(time:Double, start:Double, change:Double, duration:Double):Double = {
		val t = time / duration / 2.0
		if (t > 1.0) {
			val o = overshoot * 1.525
			change / 2.0 * (time * time * (o + 1.0) * time - overshoot) + start
		} else {
			val t2 = t - 2.0
//			change / 2.0 * (t2 * time * ((())))
			change
		}
	}
}