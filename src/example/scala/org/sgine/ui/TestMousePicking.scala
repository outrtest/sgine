package org.sgine.ui

import org.sgine.core.Resource

import org.sgine.event.EventHandler

import org.sgine.input.event.MouseEvent

import org.sgine.log._

import org.sgine.render.Renderer
import org.sgine.render.scene.RenderableScene

import org.sgine.scene.GeneralNodeContainer
import org.sgine.scene.ext.ResolutionNode

import scala.math._

object TestMousePicking {
	def main(args: Array[String]): Unit = {
		// Create the Renderer
		val r = Renderer.createFrame(1024, 768, "Test Mouse Picking")
		
		// Create a mutable scene
		val scene = new GeneralNodeContainer() with ResolutionNode
		scene.setResolution(1024, 768)
		
		// Create an image to show the puppies
		val component = new Image()
		component.location.x := -200.0
		component.scale.x := 1.5
		component.rotation.y := Pi / -4.0
		component.source := Resource("puppies.jpg")			// 700x366
		scene += component
		
		// Add our scene to the renderer
		r.renderable := RenderableScene(scene, r, false)
		
		// Add a listener to listen to all mouse events to the picture
		component.listeners += EventHandler(mouseEvent)
	}
	
	// The method that is invoked when a mouse event occurs on the picture
	private def mouseEvent(evt: MouseEvent) = {
		info("MouseEvent: " + evt)
	}
}