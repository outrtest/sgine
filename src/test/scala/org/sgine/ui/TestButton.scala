package org.sgine.ui

import org.sgine.core.Color

import org.sgine.render.Renderer
import org.sgine.render.scene.RenderableScene

import org.sgine.scene.GeneralNodeContainer
import org.sgine.scene.ext.ResolutionNode

import org.sgine.ui.skin.windows.Button

object TestButton {
	def main(args: Array[String]): Unit = {
		val r = Renderer.createFrame(1024, 768, "Test Button")
		r.verticalSync := false
		
		val scene = new GeneralNodeContainer() with ResolutionNode
		scene.setResolution(1024.0, 768.0)
		
		val component = new Button()
		component.text := "Test Button"
		scene += component
		
		r.renderable := RenderableScene(scene)
	}
}