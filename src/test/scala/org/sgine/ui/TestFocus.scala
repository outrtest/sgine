package org.sgine.ui

import org.sgine.core.Color

import org.sgine.render.Renderer
import org.sgine.render.scene.RenderableScene

import org.sgine.scene.GeneralNodeContainer
import org.sgine.scene.ext.MatrixNode

import org.sgine.ui.skin.windows.Button

object TestFocus {
	def main(args: Array[String]): Unit = {
		val r = Renderer.createFrame(1024, 768, "Test Focus")
		r.verticalSync := false
		
		val scene = new GeneralNodeContainer() with MatrixNode
		scene.localMatrix().translate(0.0, 0.0, -600.0)
		
		val component1 = new Button()
		component1.text := "Button 1"
		component1.location.y := 100.0
		scene += component1
		
		val component2 = new Button()
		component2.text := "Button 2"
		component2.focused := true
		scene += component2
		
		val component3 = new Button()
		component3.text := "Button 3"
		component3.location.y := -100.0
		scene += component3
		
		r.renderable := RenderableScene(scene)
		
		Thread.sleep(1000)
		
		var n: org.sgine.scene.Node = component1
		while (n != null) {
			println(n.hierarchyString)
			n = n.nextNode()
		}
	}
}