package org.sgine.ui

import org.sgine.core.Resource

import org.sgine.easing.Elastic

import org.sgine.event.EventHandler
import org.sgine.event.ProcessingMode

import org.sgine.input.Mouse
import org.sgine.input.event.MouseReleaseEvent

import org.sgine.math.mutable._

import org.sgine.property.adjust.EasingNumericAdjuster

import org.sgine.render.Renderer
import org.sgine.render.scene.RenderableScene

import org.sgine.scene.GeneralNodeContainer

import org.lwjgl.opengl.GL11._
import org.lwjgl.opengl.GL15._
import org.lwjgl.util.glu.GLU._

object TestMousePicking {
	val r = Renderer.createFrame(1024, 768, "Test Mouse Picking")
	val component = new Image()
	
	def main(args: Array[String]): Unit = {
		val scene = new GeneralNodeContainer()
		component.location.x := -200.0
		component.location.z := -500.0
		component.scale.x := 1.5
		component.rotation.y := Math.Pi / -4.0
		component.source := Resource("resource/puppies.jpg")			// 700x366
		scene += component
		
		r.renderable := RenderableScene(scene, false)
		
		Mouse.listeners += EventHandler(mouseReleased, processingMode = ProcessingMode.Blocking)
	}
	
	private def mouseReleased(evt: MouseReleaseEvent) = {
		val v = Vector3()
		
		println(r.translateLocal(evt.x, evt.y, component.matrix(), v))
	}
}