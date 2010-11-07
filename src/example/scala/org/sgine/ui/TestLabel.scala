package org.sgine.ui

import scala.io.Source

import org.sgine.input.event.MousePressEvent

import org.sgine.render.Renderer
import org.sgine.render.font.FontManager
import org.sgine.render.scene.RenderableScene

import org.sgine.scene.GeneralNodeContainer
import org.sgine.scene.ext.ResolutionNode

object TestLabel {
	def main(args: Array[String]): Unit = {
		val r = Renderer.createFrame(1024, 768, "Test RenderScene")
		
		val scene = new GeneralNodeContainer() with ResolutionNode
		scene.setResolution(1024, 768)
		
		val component = new Label()
		component.font := FontManager("Arial", 24.0)
		component.text := "Hello World! Arial"
		scene += component
		
		r.renderable := RenderableScene(scene, r)
	}
}