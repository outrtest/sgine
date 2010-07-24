package org.sgine.ui

import org.sgine.core.Resource

import org.sgine.render.Debug
import org.sgine.render.StandardDisplay
import org.sgine.render.RenderSettings

object TestBox extends StandardDisplay with Debug {
	override val settings = RenderSettings.High
	
	def setup() = {
		val component = new Box()
		component.scale.set(0.5)
//		component.manualSize := true
//		component.width := 512.0
//		component.height := 256.0
//		component.depth := 512.0
		component.source := Resource("sgine_256.png")
		scene += component
		
		component.rotation.x.animator = new org.sgine.property.animate.LinearNumericAnimator(1.5)
		component.rotation.x := Double.MaxValue
		component.rotation.y.animator = new org.sgine.property.animate.LinearNumericAnimator(1.5)
		component.rotation.y := Double.MaxValue
	}
}