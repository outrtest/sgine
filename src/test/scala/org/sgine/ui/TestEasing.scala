package org.sgine.ui

import org.sgine.core.Resource

import org.sgine.easing.Easing

import org.sgine.effect.CompositeEffect
import org.sgine.effect.PauseEffect
import org.sgine.effect.PropertyChangeEffect

import org.sgine.event.EventHandler

import org.sgine.input.Key
import org.sgine.input.Keyboard
import org.sgine.input.event.KeyPressEvent

import org.sgine.property.animate.EasingNumericAnimator

import org.sgine.render.Debug
import org.sgine.render.StandardDisplay

object TestEasing extends StandardDisplay with Debug {
	private lazy val image = new Image(Resource("puppies.jpg"))
	private lazy val label = new Label()

	private var easingIndex = 0
	
	private val animator = new EasingNumericAnimator(null, 1.0)
	
	def setup() = {
		renderer.verticalSync := true
		
		image.scale.set(0.5)
		image.location.x.animator = animator
		scene += image
		
		updateEasing()
		label.location.y := -350.0
		scene += label
		
		Keyboard.listeners += EventHandler(processKey)
		
		// Move the cube back and forth perpetually on the x-axis
		val me0 = new PauseEffect(1.0)
		val me1 = new PropertyChangeEffect(image.location.x, -300.0)
		val me2 = new PropertyChangeEffect(image.location.x, 300.0)
		val move = new CompositeEffect(me0, me1, me0, me2)
		move.repeat = -1
		move.start()
	}
	
	private def processKey(evt: KeyPressEvent) = {
		if (evt.key == Key.Left) {
			if (easingIndex > 0) {
				easingIndex -= 1
				updateEasing()
			}
		} else if (evt.key == Key.Right) {
			if (easingIndex < Easing.values.length - 1) {
				easingIndex += 1
				updateEasing()
			}
		}
	}
	
	private def updateEasing() = {
		val easing = Easing.values(easingIndex)
		animator.easing = easing.f
		label.text := "Easing: " + easing.name
	}
}