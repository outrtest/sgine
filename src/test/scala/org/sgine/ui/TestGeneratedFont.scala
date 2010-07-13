package org.sgine.ui

import scala.io.Source

import org.sgine.input.event.MousePressEvent

import org.sgine.render.Renderer
import org.sgine.render.font.BitmapFontCreator
import org.sgine.render.font.FontManager
import org.sgine.render.scene.RenderableScene

import org.sgine.scene.GeneralNodeContainer
import org.sgine.scene.ext.ResolutionNode

object TestGeneratedFont {
	def main(args: Array[String]): Unit = {
		val r = Renderer.createFrame(1024, 768, "Test RenderScene")
		
		val scene = new GeneralNodeContainer() with ResolutionNode
		scene.setResolution(1024, 768)
		
		val component = new Label()
		component.font := FontManager("Arial32")
		component.text := "Hello World! Arial32"
		scene += component
		
		val component2 = new Image()
		val texture = BitmapFontCreator(new java.awt.Font("Arial", java.awt.Font.PLAIN, 32))
		val renderImage = org.sgine.render.RenderImage(texture)
		component2.renderImage := renderImage
		component2.location.z := -1.0
		component2.location.x := 258.0
		component2.location.y := -245.0
		scene += component2
		
		r.renderable := RenderableScene(scene)
	}
}