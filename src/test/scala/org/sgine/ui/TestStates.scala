package org.sgine.ui

import org.sgine.core.Resource

import org.sgine.property.state.State

import org.sgine.render.Renderer
import org.sgine.render.scene.RenderableScene

import org.sgine.scene.GeneralNodeContainer

object TestStates {
	def main(args: Array[String]): Unit = {
		val r = Renderer.createFrame(1024, 768, "Test RenderScene")
		
		val scene = new GeneralNodeContainer()
		val component = new Image()
		component.location.z := -500.0
		component.source := Resource("puppies.jpg")
		scene += component
		
		r.renderable := RenderableScene(scene)
		
		val state = new State()
		state.add("rotation.z", Math.Pi / 4.0)
		state.add("location.x", 200.0)
		state.add("location.y", -200.0)
		state.add("source", Resource("sgine_256.png"))
		
		while (true) {
			Thread.sleep(1000)
			val originals = state.activate(component)
			Thread.sleep(1000)
			state.deactivate(component, originals)
		}
	}
}