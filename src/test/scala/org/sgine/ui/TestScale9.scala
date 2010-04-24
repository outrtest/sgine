package org.sgine.ui

import org.sgine.core.Resource

import org.sgine.render.Renderer
import org.sgine.render.scene.RenderableScene

import org.sgine.scene.GeneralNodeContainer

object TestScale9 {
	def main(args: Array[String]): Unit = {
		val r = Renderer.createFrame(1024, 768, "Test Scale-9")
		
		val scene = new GeneralNodeContainer()
		
		val component = new Scale9()
		component(Resource("scale9/windows/button/normal.png"), 3.0, 3.0, 4.0, 5.0)
		component.width := 200.0
		component.height := 50.0
		component.location.z := -500.0
		scene += component
		
		r.renderable := RenderableScene(scene)
	}
}