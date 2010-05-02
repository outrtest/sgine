package org.sgine.ui

import org.sgine.core.Color

import org.sgine.render.Renderer
import org.sgine.render.scene.RenderableScene

import org.sgine.scene.GeneralNodeContainer
import org.sgine.scene.MatrixNode

import org.sgine.ui.skin.windows.Button

object TestButton {
	def main(args: Array[String]): Unit = {
		val r = Renderer.createFrame(1024, 768, "Test RenderScene")
		r.verticalSync := false
		
		val scene = new GeneralNodeContainer() with MatrixNode
		scene.localMatrix().translate(0.0, 0.0, -600.0)
		
		val component = new Button()
		component.text := "Test Button"
		component.color := Color(1.0, 1.0, 1.0, 0.5)
		scene += component
		
		r.renderable := RenderableScene(scene)
	}
}