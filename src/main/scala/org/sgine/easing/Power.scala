package org.sgine.easing

import scala.math._

object Power {
	private val exponent = 2.0
	
	def easeIn(time: Double, start: Double, change: Double, duration: Double): Double = {
		val t = time / duration
		pow(t, exponent)
	}
	
	def easeOut(time: Double, start: Double, change: Double, duration: Double): Double = {
		val t = time / duration
		1.0 - pow(1.0 - t, exponent)
	}
}