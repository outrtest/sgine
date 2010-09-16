package org.sgine.ui

import org.sgine.core._

import org.sgine.easing._

import org.sgine.event.Event
import org.sgine.event.EventHandler

import org.sgine.log._

import org.sgine.property.animate.EasingColorAnimator
import org.sgine.property.animate.EasingNumericAnimator
import org.sgine.property.event.PropertyChangeEvent

import org.sgine.render.Debug
import org.sgine.render.RenderSettings
import org.sgine.render.StandardDisplay

import scala.math._

object TestMorphingBox extends StandardDisplay with Debug {
	private val easingLabel = new Label()
	private val alphaLabel = new Label()
	private val colorLabel = new Label()
	private val box = new Box()
	private val widthAnimator = new EasingNumericAnimator(null, 0.0)
	private val heightAnimator = new EasingNumericAnimator(null, 0.0)
	private val depthAnimator = new EasingNumericAnimator(null, 0.0)
	private val rotationXAnimator = new EasingNumericAnimator(null, 0.0)
	private val rotationYAnimator = new EasingNumericAnimator(null, 0.0)
	private val rotationZAnimator = new EasingNumericAnimator(null, 0.0)
	private val colorAnimator = new EasingColorAnimator(null, 0.0)
	
	override val settings = RenderSettings.High
	
	def setup() = {
		// Configure labels
		easingLabel.location.x.align := "left"
		easingLabel.location.x := -500.0
		easingLabel.location.y := -350.0
		scene += easingLabel
		
		alphaLabel.location.y := -350.0
		scene += alphaLabel
		
		colorLabel.location.x.align := "right"
		colorLabel.location.x := 500.0
		colorLabel.location.y := -350.0
		scene += colorLabel
		
		// Configure box
		box.cull := Face.None
		box.source := Resource("sgine_256.png")
		box.manualSize := true
		scene += box
		
		// Initialize animation
		box.dimension.width.animator = widthAnimator
		box.dimension.height.animator = heightAnimator
		box.dimension.depth.animator = depthAnimator
		box.rotation.x.animator = rotationXAnimator
		box.rotation.y.animator = rotationYAnimator
		box.rotation.z.animator = rotationZAnimator
		box.color.animator = colorAnimator
		
		// Initialize listeners
		val handler = EventHandler(valueChanged, ProcessingMode.Normal)
		box.dimension.width.listeners += handler
		box.dimension.height.listeners += handler
		box.dimension.depth.listeners += handler
		box.rotation.x.listeners += handler
		box.rotation.y.listeners += handler
		box.rotation.z.listeners += handler
		box.color.listeners += handler
		
		// Configure animation
		updateAnimation()
	}
	
	private def valueChanged(evt: PropertyChangeEvent[_]) = {
		if (!evt.adjusting) {
			if (!isAnimating) {
				updateAnimation()
			}
		}
	}
	
	def isAnimating = box.dimension.width.isAnimating ||
					  box.dimension.height.isAnimating ||
					  box.dimension.width.isAnimating ||
					  box.rotation.x.isAnimating ||
					  box.rotation.y.isAnimating ||
					  box.rotation.z.isAnimating ||
					  box.color.isAnimating
	
	private def updateAnimation() = {
		val easing = Easing.random
		val easingFunction = easing.f
		val multiplier = (random * 2.0) + 1.0
		val color = Color.random
		val alpha = random * 1.0
		
		// Update label
		easingLabel.text := String.format("Easing: %1s", easing.name)
		alphaLabel.text := String.format("Alpha: %.2f", alpha.asInstanceOf[AnyRef])
		colorLabel.text := String.format("Color: %1s", color.name)
		
		// Change easing
		widthAnimator.easing = easingFunction
		heightAnimator.easing = easingFunction
		depthAnimator.easing = easingFunction
		rotationXAnimator.easing = easingFunction
		rotationYAnimator.easing = easingFunction
		rotationZAnimator.easing = easingFunction
		colorAnimator.easing = easingFunction
		
		// Change multiplier
		widthAnimator.multiplier = multiplier
		heightAnimator.multiplier = multiplier
		depthAnimator.multiplier = multiplier
		rotationXAnimator.multiplier = multiplier
		rotationYAnimator.multiplier = multiplier
		rotationZAnimator.multiplier = multiplier
		colorAnimator.multiplier = multiplier
		
		// Update size
		box.dimension.width := (random * 500.0) + 20.0
		box.dimension.height := (random * 500.0) + 20.0
		box.dimension.depth := (random * 500.0) + 20.0
		
		// Update rotation
		box.rotation.x := random * 3.0 * Pi
		box.rotation.y := random * 3.0 * Pi
		box.rotation.z := random * 3.0 * Pi
		
		// Update color
		box.color := Color(color.red, color.green, color.blue, alpha)
	}
}