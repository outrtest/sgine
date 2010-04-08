package org.sgine.ui

import org.sgine.core.Resource

import org.sgine.easing._

import org.sgine.event.EventHandler

import org.sgine.input.Key
import org.sgine.input.Keyboard
import org.sgine.input.event.KeyPressEvent

import org.sgine.math.mutable.MatrixPropertyContainer

import org.sgine.property.adjust.EasingNumericAdjuster

import org.sgine.render.Renderer
import org.sgine.render.scene.RenderableScene

import org.sgine.scene.GeneralNodeContainer

object TestCube {
	val cube = new ImageCube()
	
	def main(args: Array[String]): Unit = {
		val r = Renderer.createFrame(1024, 768, "Test Cube")
		r.verticalSync := false
		
		val scene = new GeneralNodeContainer()
		
		cube.location.z := -1000.0
		cube.rotation.x.adjuster = new EasingNumericAdjuster(Cubic.easeIn, 1.0)
		cube.rotation.y.adjuster = new EasingNumericAdjuster(Cubic.easeIn, 1.0)
		cube(Resource("resource/sgine_256.png"), 256.0, 256.0)
		scene += cube
		
		r.renderable := RenderableScene(scene)
		
		Keyboard.listeners += EventHandler(keyPress _)
	}
	
	private def keyPress(evt: KeyPressEvent) = {
		println("KeyPress: " + evt)
		if (evt.key == Key.Left) {
			cube.rotation.y := cube.rotation.y.target + (Math.Pi / 2.0)
		} else if (evt.key == Key.Right) {
			cube.rotation.y := cube.rotation.y.target + (Math.Pi / -2.0)
		} else if (evt.key == Key.Up) {
			cube.rotation.x := cube.rotation.x.target + (Math.Pi / 2.0)
		} else if (evt.key == Key.Down) {
			cube.rotation.x := cube.rotation.x.target + (Math.Pi / -2.0)
		}
	}
}