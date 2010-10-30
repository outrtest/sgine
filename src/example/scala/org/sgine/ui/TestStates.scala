package org.sgine.ui

import org.sgine.core.Resource

import org.sgine.easing._

import org.sgine.property.animate.EasingNumericAnimator
import org.sgine.property.state.State

import org.sgine.render.Debug
import org.sgine.render.StandardDisplay

import scala.math._

object TestStates extends StandardDisplay with Debug {
	def setup() = {
		val component = new Image()
		
		component.source := Resource("puppies.jpg")
		
		// Animators cause the state transition instead of just jumping into place
		component.rotation.z.animator = new EasingNumericAnimator(Bounce.easeOut, 1.0)
		component.location.x.animator = new EasingNumericAnimator(Elastic.easeInOut, 1.0)
		component.location.y.animator = new EasingNumericAnimator(Elastic.easeInOut, 1.0)
		component.location.z.animator = new EasingNumericAnimator(Elastic.easeInOut, 1.0)
		
		val state = component.states("test1")
		state("rotation.z") = Pi / 4.0
		state("location.x") = 200.0
		state("location.y") = -200.0
		
		component.states("test2")("source") = Resource("sgine_256.png")
		component.states("test2")("location.z") = 300.0
		
		scene += component
		
		while (true) {
			Thread.sleep(1000)
			component.states.activate("test1")
			Thread.sleep(1000)
			component.states.activate("test2")
			Thread.sleep(1000)
			component.states.deactivate("test1")
			Thread.sleep(1000)
			component.states.deactivate("test2")
		}
	}
}