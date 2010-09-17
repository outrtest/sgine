package org.sgine.ui

import org.sgine.core._

import org.sgine.render.Debug
import org.sgine.render.StandardDisplay
import org.sgine.render.RenderSettings

object TestDisk extends StandardDisplay with Debug {
	override val settings = RenderSettings.High
	
	def setup() = {
		val image = new Image(Resource("puppies.jpg"))
		image.location.z := -300.0
		image.scale(2.0)
		scene += image
		
		val component = new Disk()
		component.cull := org.sgine.core.Face.None
		component.manualSize := true
		component.dimension.width := 512.0
		component.dimension.height := 256.0
		component.color := Color.Blue
//		component.source := Resource("sgine_256.png")
		component.alpha := 0.5
		scene += component
		
		component.rotation.x.animator = new org.sgine.property.animate.LinearNumericAnimator(1.5)
		component.rotation.x := Double.MaxValue
		component.rotation.y.animator = new org.sgine.property.animate.LinearNumericAnimator(1.5)
		component.rotation.y := Double.MaxValue
		component.rotation.z.animator = new org.sgine.property.animate.LinearNumericAnimator(1.5)
		component.rotation.z := Double.MaxValue
	}
}