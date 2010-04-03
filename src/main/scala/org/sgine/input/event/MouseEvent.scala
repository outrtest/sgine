package org.sgine.input.event

import org.sgine.event.Event

import org.sgine.input.Mouse

// TODO: update to use current mouse component instead of Mouse
object MouseEvent {
	val Press = 1
	val Release = 2
	val Move = 3
	val Wheel = 4
	
	def apply(button: Int, state: Boolean, wheel: Int, x: Double, y: Double, dx: Double, dy: Double): MouseEvent = {
		if (button == -1) {		// Not a button event
			if (wheel != 0) {	// Wheel
				new MouseWheelEvent(x, y, wheel, dx, dy)
			} else {			// Move
				new MouseMoveEvent(x, y, dx, dy)
			}
		} else {				// Button event
			if (state) {		// Pressed
				new MousePressEvent(button, x, y, dx, dy)
			} else {			// Released
				new MouseReleaseEvent(button, x, y, dx, dy)
			}
		}
	}
}

class MouseEvent protected(val eventType: Int,
						   val x: Double,
						   val y: Double,
						   val deltaX: Double,
						   val deltaY: Double) extends Event(Mouse)

class MouseButtonEvent protected(eventType: Int,
								 val button: Int,
								 state: Boolean,
								 x: Double,
								 y: Double,
								 deltaX: Double,
								 deltaY: Double) extends MouseEvent(eventType, x, y, deltaX, deltaY)

class MousePressEvent(button: Int,
					  x: Double,
					  y: Double,
					  deltaX: Double,
					  deltaY: Double) extends MouseButtonEvent(MouseEvent.Press, button, true, x, y, deltaX, deltaY) {
	override def toString() = "Pressed: " + button + " (" + x + "x" + y + ") Delta: (" + deltaX + "x" + deltaY + ")"
}

class MouseReleaseEvent(button: Int,
						x: Double,
						y: Double,
						deltaX: Double,
						deltaY: Double) extends MouseButtonEvent(MouseEvent.Release, button, false, x, y, deltaX, deltaY) {
	override def toString() = "Released: " + button + " (" + x + "x" + y + ") Delta: (" + deltaX + "x" + deltaY + ")"
}

class MouseMoveEvent(x: Double,
					 y: Double,
					 deltaX: Double,
					 deltaY: Double) extends MouseEvent(MouseEvent.Move, x, y, deltaX, deltaY) {
	override def toString() = "Moved: (" + x + "x" + y + ") Delta: (" + deltaX + "x" + deltaY + ")"
}

class MouseWheelEvent(x: Double,
					  y: Double,
					  val wheel: Int,
					  deltaX: Double,
					  deltaY: Double) extends MouseEvent(MouseEvent.Wheel, x, y, deltaX, deltaY) {
	override def toString() = "Wheel: " + wheel + " (" + x + "x" + y + ") Delta: (" + deltaX + "x" + deltaY + ")"
}