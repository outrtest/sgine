package org.sgine.property

import org.sgine.easing.Linear

import org.sgine.property.animate.EasingNumericAnimator

import org.sgine.work.Updatable

object TestAdjustableProperties {
	def main(args: Array[String]): Unit = {
		Updatable.useWorkManager = true
		
		val p = new AdvancedProperty[Double](0.0)
		p.adjuster = new EasingNumericAnimator(Linear.easeIn, 5.0)
		var time = System.currentTimeMillis
		p := 100.0
		p.waitForTarget()
		println("Elapsed: " + (System.currentTimeMillis - time) + " to reach: " + p())
		
		// Stage 2
		time = System.currentTimeMillis
		p.set(0.0)
		p := 100.0
		p.waitForTarget()
		println("Elapsed: " + (System.currentTimeMillis - time) + " to reach: " + p())
	}
}