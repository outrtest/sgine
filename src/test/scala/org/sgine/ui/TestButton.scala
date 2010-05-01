package org.sgine.ui

import org.sgine.core.Color

import org.sgine.render.Renderer
import org.sgine.render.scene.RenderableScene

import org.sgine.scene.GeneralNodeContainer

import org.sgine.ui.skin.windows.{Button => WindowsButton}

object TestButton {
	def main(args: Array[String]): Unit = {
		val r = Renderer.createFrame(1024, 768, "Test RenderScene")
		r.verticalSync := false
		
		val scene = new GeneralNodeContainer()
		val component = new WindowsButton()
//		component.text := "Test Button"
		component.setSize(200.0, 100.0)
		component.location.z := -500.0
		component.color := Color(1.0, 1.0, 1.0, 0.5)
		scene += component
		
		r.renderable := RenderableScene(scene)
		
//		Thread.sleep(1000)
//		
//		println(component.worldMatrix())
//		for (c <- component) {
//			println("\t" + c.asInstanceOf[Component].worldMatrix())
//		}
	}
}