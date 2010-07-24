package org.sgine.ui

import org.sgine.core.Face
import org.sgine.core.Resource

import org.sgine.render.Debug
import org.sgine.render.RenderSettings
import org.sgine.render.StandardDisplay

object TestCulling extends StandardDisplay with Debug {
	override val settings = RenderSettings.High
	
	def setup() = {
		val component = new Image(Resource("puppies.jpg"))
		component.rotation.y.animator = new org.sgine.property.animate.LinearNumericAnimator(2.0)
		component.rotation.y := Double.MaxValue
		scene += component
		
		val label = new Label()
		label.location.y := -350.0
		label.text := "Culling: Back"
		scene += label
		
		while (true) {
			Thread.sleep(5000)
			component.cull() match {
				case Face.Back => {
					component.cull := Face.Front
					label.text := "Culling: Front"
				}
				case Face.Front => {
					component.cull := Face.Both
					label.text := "Culling: Both"
				}
				case Face.Both => {
					component.cull := Face.None
					label.text := "Culling: None"
				}
				case _ => {
					component.cull := Face.Back
					label.text := "Culling: Back"
				}
			}
		}
	}
}