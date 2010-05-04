package org.sgine.ui

import org.sgine.core.Color
import org.sgine.core.Resource

import org.sgine.render.Renderer
import org.sgine.render.font.FontManager
import org.sgine.render.scene.RenderableScene

import org.sgine.scene.GeneralNodeContainer

object TestScale9 {
	def main(args: Array[String]): Unit = {
		val r = Renderer.createFrame(1024, 768, "Test Scale-9")
		r.verticalSync := true
		
		val scene = new GeneralNodeContainer()
		
		val component2 = new Label()
		component2.location.z := -500.0
		component2.font := FontManager("Franklin")
		component2.text := "Hello World!"
		scene += component2
		
		val component = new Scale9()
		component(Resource("scale9/windows/button/hover.png"), 3.0, 3.0, 4.0, 5.0)
		component.width := 300.0
		component.height := 100.0
		component.location.z := -500.0
		scene += component
		
		r.renderable := RenderableScene(scene)
	}
}