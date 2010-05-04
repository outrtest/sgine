package org.sgine.ui

import org.sgine.core.Resource

import org.sgine.property.state.State

import org.sgine.render.Renderer
import org.sgine.render.scene.RenderableScene

import org.sgine.scene.GeneralNodeContainer

import scala.math._

object TestStates {
	def main(args: Array[String]): Unit = {
		val r = Renderer.createFrame(1024, 768, "Test States")
		r.verticalSync := false
		
		val scene = new GeneralNodeContainer()
		val component = new Image()
		component.location.z := -500.0
		component.source := Resource("puppies.jpg")
		
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