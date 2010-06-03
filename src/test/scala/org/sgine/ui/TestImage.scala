package org.sgine.ui

import org.sgine.core.Color
import org.sgine.core.Resource

import org.sgine.render.Renderer
import org.sgine.render.scene.RenderableScene

import org.sgine.scene.GeneralNodeContainer
import org.sgine.scene.ext.ResolutionNode

object TestImage {
	def main(args: Array[String]): Unit = {
		val r = Renderer.createFrame(1024, 768, "Test RenderScene")
		
		val scene = new GeneralNodeContainer() with ResolutionNode
		scene.setResolution(1024, 768)
		
		val component = new Image()
		component.source := Resource("puppies.jpg")
		scene += component
		
		r.renderable := RenderableScene(scene)
	}
}