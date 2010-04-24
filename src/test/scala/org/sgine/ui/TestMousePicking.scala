package org.sgine.ui

import org.sgine.core.Resource

import org.sgine.event.EventHandler

import org.sgine.input.event.MouseEvent

import org.sgine.render.Renderer
import org.sgine.render.scene.RenderableScene

import org.sgine.scene.GeneralNodeContainer

object TestMousePicking {
	def main(args: Array[String]): Unit = {
		// Create the Renderer
		val r = Renderer.createFrame(1024, 768, "Test Mouse Picking")
		
		// Create a mutable scene
		val scene = new GeneralNodeContainer()
		
		// Create an image to show the puppies
		val component = new Image()
		component.location.x := -200.0
		component.location.z := -500.0
		component.scale.x := 1.5
		component.rotation.y := Math.Pi / -4.0
		component.source := Resource("resource/puppies.jpg")			// 700x366
		scene += component
		
		// Add our scene to the renderer
		r.renderable := RenderableScene(scene, false)
		
		// Add a listener to listen to all mouse events to the picture
		component.listeners += EventHandler(mouseEvent)
	}
	
	// The method that is invoked when a mouse event occurs on the picture
	private def mouseEvent(evt: MouseEvent) = {
		println("MouseEvent: " + evt)
	}
}