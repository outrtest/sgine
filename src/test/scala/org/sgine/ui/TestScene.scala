package org.sgine.ui

import org.sgine.core.Color
import org.sgine.core.Resource

import org.sgine.easing.Linear

import org.sgine.render.Renderer
import org.sgine.render.scene.RenderableScene

import org.sgine.scene.GeneralNodeContainer
import org.sgine.scene.ext.MatrixNode

object TestScene {
	def main(args: Array[String]): Unit = {
		val r = Renderer.createFrame(1024, 768, "Test RenderScene")
		
		val scene = new GeneralNodeContainer()
		
		val container1 = new GeneralNodeContainer() with MatrixNode
		container1.localMatrix().translate(0.0, 0.0, -200.0)
		scene += container1
		
		val container2 = new GeneralNodeContainer() with MatrixNode
		container2.localMatrix().translate(0.0, 0.0, -500.0)
		container1 += container2
		
		val container3 = new GeneralNodeContainer()
		container2 += container3
		
		val component = new Image()
		component.source := Resource("puppies.jpg")
		component.color := Color(1.0, 1.0, 1.0, 0.5)
		component.location.z := 100.0
		container3 += component
		
		r.renderable := RenderableScene(scene)
	}
}