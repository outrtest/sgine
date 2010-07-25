package org.sgine.ui

import org.sgine.core.Resource

import org.sgine.easing.Elastic

import org.sgine.effect._

import org.sgine.property.animate.EasingNumericAnimator
import org.sgine.property.animate.LinearNumericAnimator

import org.sgine.render.Debug
import org.sgine.render.RenderSettings
import org.sgine.render.StandardDisplay

import org.sgine.ui.ext.AdvancedComponent

import scala.math._

object TestStressBoxes extends StandardDisplay with Debug {
	override val settings = RenderSettings.High
	
	def setup() = {
		var box: Box = null
		for (row <- 0 until 40) {
			for (column <- 0 until 40) {
				val y = (row * 30.0) - 350.0
				val z = (column * 90.0) - 1800.0
				if ((row == 0) && (column == 0)) {
					box = createBox(y, z, 0.05)
				} else {
					createInstance(box, y, z, 0.05)
				}
			}
		}
	}
	
	def createBox(y: Double, z: Double, scale: Double) = {
		val box = new Box()
		box.scale.set(scale)
		box.location.set(0.0, y, z)
		box.source := Resource("sgine_256.png")
		
		animate(box)
		
		box.update(0.0)
		scene += box
		
		box
	}
	
	def createInstance(component: Component, y: Double, z: Double, scale: Double) = {
		val instance = ComponentInstance(component)
		instance.scale.set(scale)
		instance.location.set(0.0, y, z)
		
		animate(instance)
		
		instance.update(0.0)
		scene += instance
		
		instance
	}
	
	private def animate(component: AdvancedComponent) = {
		component.rotation.x.animator = new LinearNumericAnimator(2.0)
		component.rotation.y.animator = new LinearNumericAnimator(2.0)
		component.rotation.z.animator = new LinearNumericAnimator(2.0)
		component.location.x.animator = new EasingNumericAnimator(Elastic.easeInOut, 3.0)
		
		component.rotation.x := Double.MaxValue
		component.rotation.y := Double.MaxValue
		component.rotation.z := Double.MaxValue

		// Move the cube back and forth perpetually on the x-axis
		val me0 = new PauseEffect(random * 2.0 + 0.5)
		val me1 = new PropertyChangeEffect(component.location.x, -400.0)
		val me2 = new PropertyChangeEffect(component.location.x, 400.0)
		val move = new CompositeEffect(me0, me1, me2)
		move.repeat = -1
		move.start()
	}
}