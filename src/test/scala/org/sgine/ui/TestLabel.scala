package org.sgine.ui

import scala.io.Source

import org.sgine.input.event.MousePressEvent

import org.sgine.render.Renderer
import org.sgine.render.font.FontManager
import org.sgine.render.scene.RenderableScene

import org.sgine.scene.GeneralNodeContainer

object TestLabel {
	def main(args: Array[String]): Unit = {
		val r = Renderer.createFrame(1024, 768, "Test RenderScene")
		
		val scene = new GeneralNodeContainer()
		val component = new Label()
		component.location.z := -500.0
		component.font := FontManager("Franklin")
		component.text := "Hello World!"
		component.listeners += test _
		scene += component
		
		r.renderable := RenderableScene(scene)
	}
	
	private def test(evt: MousePressEvent) = {
		println("Mouse pressed!")
	}
}