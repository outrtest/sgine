package org.sgine.ui

import org.sgine.core.Color
import org.sgine.core.Resource
import org.sgine.core.mutable.{Color => MutableColor}

import org.sgine.easing.Elastic
import org.sgine.easing.Linear

import org.sgine.effect.CompositeEffect
import org.sgine.effect.PropertyChangeEffect
import org.sgine.effect.PropertySetEffect

import org.sgine.property.adjust.EasingNumericAdjuster

import org.sgine.render.Renderer
import org.sgine.render.scene.RenderableScene

import org.sgine.scene.GeneralNodeContainer

import scala.math._

object TestCube {
	def main(args: Array[String]): Unit = {
		val r = Renderer.createFrame(1024, 768, "Test Cube", 4, 8, 4, 4)
		r.verticalSync := true
		
		val scene = new GeneralNodeContainer()
		
		val cube = new ImageCube()
		val c = MutableColor(Color.White)
		cube.color := c
//		cube.alpha := 0.5
		cube.location.z := -1000.0
		cube.rotation.x.adjuster = new EasingNumericAdjuster(Linear.easeIn, 2.0)
		cube.rotation.y.adjuster = new EasingNumericAdjuster(Linear.easeIn, 4.0)
		cube.rotation.z.adjuster = new EasingNumericAdjuster(Linear.easeIn, 6.0)
		cube.location.x.adjuster = new EasingNumericAdjuster(Elastic.easeInOut, 4.0)
		cube(Resource("sgine_256.png"), 256.0, 256.0)
		cube.front().listeners += test _
		scene += cube
		
		r.renderable := RenderableScene(scene, showFPS = true)
		
		// Rotate the cube perpetually on the x-axis
		val rx1 = new PropertyChangeEffect(cube.rotation.x, Pi * 2.0)
		val rx2 = new PropertySetEffect(cube.rotation.x, 0.0)
		val rotateX = new CompositeEffect(rx1, rx2)
		rotateX.repeat = -1
		
		// Rotate the cube perpetually on the y-axis
		val ry1 = new PropertyChangeEffect(cube.rotation.y, Pi * 2.0)
		val ry2 = new PropertySetEffect(cube.rotation.y, 0.0)
		val rotateY = new CompositeEffect(ry1, ry2)
		rotateY.repeat = -1
		
		// Rotate the cube perpetually on the z-axis
		val rz1 = new PropertyChangeEffect(cube.rotation.z, Pi * 2.0)
		val rz2 = new PropertySetEffect(cube.rotation.z, 0.0)
		val rotateZ = new CompositeEffect(rz1, rz2)
		rotateZ.repeat = -1
		
		// Move the cube back and forth perpetually on the x-axis
		val me1 = new PropertyChangeEffect(cube.location.x, -600.0)
		val me2 = new PropertyChangeEffect(cube.location.x, 600.0)
		val move = new CompositeEffect(me1, me2)
		move.repeat = -1
		
		// Start effects
		rotateX.start()
		rotateY.start()
		rotateZ.start()
		move.start()
	}
	
	private def test(evt: org.sgine.event.Event) = {
		println(evt)
	}
}