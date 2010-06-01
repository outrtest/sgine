package org.sgine.ui

import org.sgine.core.Color
import org.sgine.core.Resource

import org.sgine.render.Renderer
import org.sgine.render.CacheableRenderable
import org.sgine.render.scene.RenderableScene

import org.sgine.scene.GeneralNodeContainer

object TestCaching {
	def main(args: Array[String]): Unit = {
		val r = Renderer.createFrame(1024, 768, "Test Caching")
		r.verticalSync := false
		
		val scene = new GeneralNodeContainer() with CacheableRenderable
		val component = new Image()
		component.location.z := -500.0
		component.source := Resource("puppies.jpg")
		component.color := Color(1.0, 1.0, 1.0, 0.5)
		scene += component
		
		r.renderable := RenderableScene(scene)
		
		Thread.sleep(5000)
		scene.cache := true
	}
}