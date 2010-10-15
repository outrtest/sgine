package org.sgine.ui

import org.sgine.core.Resource

import org.sgine.render.Debug
import org.sgine.render.StandardDisplay

import simplex3d.math.doublem.renamed._

object TestCamera extends StandardDisplay with Debug {
	def setup() = {
		val container = new Container()
		scene += container
		
		val box = new Box()
		box.source := Resource("sgine_256.png")
		container += box
		
		container.rotation.y.animator = new org.sgine.property.animate.LinearNumericAnimator(1.5)
		container.rotation.y := Double.MaxValue
		
		while (true) {
			renderer.camera.view := container.worldMatrix()
			Thread.sleep(5)
		}
	}
}