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
import org.sgine.property.adjust.LinearNumericAdjuster

import org.sgine.render.Renderer
import org.sgine.render.scene.RenderableScene

import org.sgine.scene.GeneralNodeContainer

import scala.math._

object TestCube {
	def main(args: Array[String]): Unit = {
		val r = Renderer.createFrame(1024, 768, "Test Cube", 4, 8, 4, 4)
		r.verticalSync := false
		
		val scene = new GeneralNodeContainer()
		
		val cube = new ImageCube()
		val c = MutableColor(Color.White)
		cube.color := c
//		cube.alpha := 0.5
		cube.location.z := -1000.0
		cube.rotation.x.adjuster = new LinearNumericAdjuster(2.0)
		cube.rotation.y.adjuster = new LinearNumericAdjuster(2.0)
		cube.rotation.z.adjuster = new LinearNumericAdjuster(2.0)
		cube.location.x.adjuster = new EasingNumericAdjuster(Elastic.easeInOut, 3.0)
		cube(Resource("sgine_256.png"), 256.0, 256.0)
		cube.front().listeners += test _
		scene += cube
		
		r.renderable := RenderableScene(scene, showFPS = true)
		
		cube.rotation.x := Double.MaxValue
		cube.rotation.y := Double.MaxValue
		cube.rotation.z := Double.MaxValue

		// Move the cube back and forth perpetually on the x-axis
		val me1 = new PropertyChangeEffect(cube.location.x, -600.0)
		val me2 = new PropertyChangeEffect(cube.location.x, 600.0)
		val move = new CompositeEffect(me1, me2)
		move.repeat = -1
		move.start()
	}
	
	private def test(evt: org.sgine.event.Event) = {
		println(evt)
	}
}