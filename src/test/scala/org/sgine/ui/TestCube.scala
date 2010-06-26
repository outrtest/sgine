package org.sgine.ui

import org.sgine.core.Color
import org.sgine.core.Resource
import org.sgine.core.mutable.{Color => MutableColor}

import org.sgine.easing.Elastic
import org.sgine.easing.Linear

import org.sgine.effect.CompositeEffect
import org.sgine.effect.PropertyChangeEffect
import org.sgine.effect.PropertySetEffect

import org.sgine.property.animate.EasingNumericAnimator
import org.sgine.property.animate.LinearNumericAnimator

import org.sgine.render.Debug
import org.sgine.render.Renderer
import org.sgine.render.StandardDisplay
import org.sgine.render.scene.RenderableScene

import org.sgine.scene.GeneralNodeContainer

import scala.math._

object TestCube extends StandardDisplay with Debug {
	def setup() = {
		val cube = new ImageCube()
		val c = MutableColor(Color.White)
		cube.color := c
//		cube.alpha := 0.5
		cube.scale.set(0.5)
		cube.rotation.x.adjuster = new LinearNumericAnimator(2.0)
		cube.rotation.y.adjuster = new LinearNumericAnimator(2.0)
		cube.rotation.z.adjuster = new LinearNumericAnimator(2.0)
		cube.location.x.adjuster = new EasingNumericAnimator(Elastic.easeInOut, 3.0)
		cube(Resource("sgine_256.png"), 256.0, 256.0)
		cube.front().listeners += test _
		scene += cube
		
		cube.rotation.x := Double.MaxValue
		cube.rotation.y := Double.MaxValue
		cube.rotation.z := Double.MaxValue

		// Move the cube back and forth perpetually on the x-axis
		val me1 = new PropertyChangeEffect(cube.location.x, -400.0)
		val me2 = new PropertyChangeEffect(cube.location.x, 400.0)
		val move = new CompositeEffect(me1, me2)
		move.repeat = -1
		move.start()
	}
	
	private def test(evt: org.sgine.event.Event) = {
		println(evt)
	}
}