package org.sgine.ui

import org.sgine.core.Color
import org.sgine.core.Resource

import org.sgine.render.Renderer
import org.sgine.render.scene.RenderableScene

import org.sgine.scene.GeneralNodeContainer
import org.sgine.scene.ext.ResolutionNode

object TestResolution {
	def main(args: Array[String]): Unit = {
		val r = Renderer.createFrame(1024, 768, "Test Resolution")
		r.verticalSync := false
		
		val scene = new GeneralNodeContainer()
		
		val r1024 = new GeneralNodeContainer() with ResolutionNode
		r1024.setResolution(1024.0, 768.0)
		scene += r1024
		
		val c1024 = new Image()
		c1024.source := Resource("1024.jpg")
		c1024.alpha := 0.5
		r1024 += c1024
		
		val r640 = new GeneralNodeContainer() with ResolutionNode
		r640.setResolution(640.0, 480.0)
		scene += r640
		
		val c640 = new Image()
		c640.source := Resource("640.jpg")
		c640.alpha := 0.5
		r640 += c640
		
		r.renderable := RenderableScene(scene, r)
	}
}