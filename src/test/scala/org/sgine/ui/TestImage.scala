package org.sgine.ui

import org.sgine.core.Resource

import org.sgine.easing.Elastic

import org.sgine.property.adjust.EasingNumericAdjuster

import org.sgine.render.Renderer
import org.sgine.render.scene.RenderableScene

import org.sgine.scene.GeneralNodeContainer

object TestImage {
	def main(args: Array[String]): Unit = {
		val r = Renderer.createFrame(1024, 768, "Test RenderScene")
		
		val scene = new GeneralNodeContainer()
		val component = new Image()
		component.location.z := -1000.0
		component.source := Resource("resource/puppies.jpg")
		scene += component
		
		r.renderable := RenderableScene(scene)
	}
}