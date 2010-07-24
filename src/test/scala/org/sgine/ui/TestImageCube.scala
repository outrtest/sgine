package org.sgine.ui

import org.sgine.core.Color
import org.sgine.core.Resource
import org.sgine.core.mutable.{Color => MutableColor}

import org.sgine.easing.Elastic

import org.sgine.effect._

import org.sgine.property.animate.EasingNumericAnimator
import org.sgine.property.animate.LinearNumericAnimator

import org.sgine.render.Debug
import org.sgine.render.Renderer
import org.sgine.render.RenderSettings
import org.sgine.render.StandardDisplay
import org.sgine.render.TextureManager
import org.sgine.render.TexturedQuad
import org.sgine.render.scene.RenderableScene

import org.sgine.scene.MutableNodeContainer

import scala.math._

object TestImageCube extends StandardDisplay with Debug {
	override val settings = RenderSettings.High
	
	def setup() = {
		val c = ImageCubeCreator.createCube(scene, 10.0, 20.0, 0.5)
	}
	
	private def test(evt: org.sgine.event.Event) = {
		println(evt)
	}
}

object ImageCubeCreator {
	private lazy val quad = createQuad()
	
	private def createQuad() = {
		val quad = new TexturedQuad()
		quad.texture := TextureManager(Resource("sgine_256.png"))
		quad.width := 256.0
		quad.height := 256.0
		
		quad
	}
	
	def createCube(scene: MutableNodeContainer, y: Double, z: Double, scale: Double) = {
		val cube = new ImageCube()
		val c = MutableColor(Color.White)
		cube.color := c
//		cube.alpha := 0.5
		cube.scale.set(scale)
		cube.location.y := (y * 30.0) - 350.0
		cube.location.z := (z * 90.0) - 1800.0
		cube.rotation.x.animator = new LinearNumericAnimator(2.0)
		cube.rotation.y.animator = new LinearNumericAnimator(2.0)
		cube.rotation.z.animator = new LinearNumericAnimator(2.0)
		cube.location.x.animator = new EasingNumericAnimator(Elastic.easeInOut, 3.0)
//		cube(Resource("sgine_256.png"), 256.0, 256.0)
		cube(quad)
		cube.update(0.0)
		scene += cube
		
		cube.rotation.x := Double.MaxValue
		cube.rotation.y := Double.MaxValue
		cube.rotation.z := Double.MaxValue

		// Move the cube back and forth perpetually on the x-axis
		val me0 = new PauseEffect(random * 2.0 + 0.5)
		val me1 = new PropertyChangeEffect(cube.location.x, -400.0)
		val me2 = new PropertyChangeEffect(cube.location.x, 400.0)
		val move = new CompositeEffect(me0, me1, me2)
		move.repeat = -1
		move.start()
		
		cube
	}
}