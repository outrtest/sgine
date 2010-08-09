package org.sgine.ui

import org.sgine.core.Resource

import org.sgine.render.Debug
import org.sgine.render.StandardDisplay
import org.sgine.render.RenderSettings

object TestBox extends StandardDisplay with Debug {
	override val settings = RenderSettings.High
	
	def setup() = {
		val image = new Image(Resource("puppies.jpg"))
		image.location.z := -300.0
		image.scale.set(2.0)
		scene += image
		
		val component = new Box()
		component.cull := org.sgine.core.Face.None
//		component.manualSize := true
//		component.width := 512.0
//		component.height := 256.0
//		component.depth := 512.0
		component.source := Resource("sgine_256.png")
		component.alpha := 0.5
		scene += component
		
		component.rotation.x.animator = new org.sgine.property.animate.LinearNumericAnimator(1.5)
		component.rotation.x := Double.MaxValue
		component.rotation.y.animator = new org.sgine.property.animate.LinearNumericAnimator(1.5)
		component.rotation.y := Double.MaxValue
	}
}