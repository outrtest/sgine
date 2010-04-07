package org.sgine.ui

import org.sgine.core.Resource

import org.sgine.math.mutable.MatrixPropertyContainer

import org.sgine.property.adjust.LinearNumericAdjuster

import org.sgine.render.Renderer
import org.sgine.render.scene.RenderableScene

import org.sgine.scene.GeneralNodeContainer

object TestCube {
	def main(args: Array[String]): Unit = {
		val r = Renderer.createFrame(1024, 768, "Test Cube")
		r.verticalSync := false
		
		val scene = new GeneralNodeContainer()
		
		val cube = new Cube()
		cube.location.z := -1000.0
		cube(Resource("resource/sgine_256.png"), 256.0, 256.0)
		scene += cube
		
		r.renderable := RenderableScene(scene)
		
		Thread.sleep(1000)
		
		cube.rotation.y.adjuster = new LinearNumericAdjuster(2.0)
		cube.rotation.y := Math.Pi * 4.0
	}
}