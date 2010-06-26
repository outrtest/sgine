package org.sgine.ui

import org.sgine.core.Resource

import org.sgine.easing.Elastic

import org.sgine.property.animate.EasingNumericAnimator
import org.sgine.property.state.State

import org.sgine.render.Renderer
import org.sgine.render.scene.RenderableScene

import org.sgine.scene.GeneralNodeContainer
import org.sgine.scene.ext.ResolutionNode

import scala.math._

object TestStates {
	def main(args: Array[String]): Unit = {
		val r = Renderer.createFrame(1024, 768, "Test States")
		r.verticalSync := true
		
		val scene = new GeneralNodeContainer() with ResolutionNode
		scene.setResolution(1024, 768)
		
		val component = new Image()
		component.source := Resource("puppies.jpg")
		
		// Animators cause the state transition instead of just jumping into place
		component.rotation.z.adjuster = new EasingNumericAnimator(Elastic.easeInOut, 1.0)
		component.location.x.adjuster = new EasingNumericAnimator(Elastic.easeInOut, 1.0)
		component.location.y.adjuster = new EasingNumericAnimator(Elastic.easeInOut, 1.0)
		
		val state = new State("Test1")
		state.add("rotation.z", Pi / 4.0)
		state.add("location.x", 200.0)
		state.add("location.y", -200.0)
		component.states += state
		
		val state2 = new State("Test2")
		state2.add("source", Resource("sgine_256.png"))
		component.states += state2
		
		scene += component
		
		r.renderable := RenderableScene(scene)
		
		while (true) {
			Thread.sleep(1000)
			component.states.activate("Test1")
			Thread.sleep(1000)
			component.states.activate("Test2")
			Thread.sleep(1000)
			component.states.deactivate("Test1")
			Thread.sleep(1000)
			component.states.deactivate("Test2")
		}
	}
}