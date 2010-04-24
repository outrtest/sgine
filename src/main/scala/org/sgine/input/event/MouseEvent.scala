package org.sgine.input.event

import org.sgine.event.Event
import org.sgine.event.Listenable

import org.sgine.input.Mouse

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

abstract class MouseEvent protected(val eventType: Int,
						   val x: Double,
						   val y: Double,
						   val deltaX: Double,
						   val deltaY: Double,
						   listenable: Listenable = Mouse) extends Event(listenable) {
	def retarget(target: org.sgine.event.Listenable, x: Double, y: Double): Event
}

abstract class MouseButtonEvent protected(eventType: Int,
								 val button: Int,
								 state: Boolean,
								 x: Double,
								 y: Double,
								 deltaX: Double,
								 deltaY: Double,
								 listenable: Listenable = Mouse) extends MouseEvent(eventType, x, y, deltaX, deltaY, listenable)

class MousePressEvent(button: Int,
					  x: Double,
					  y: Double,
					  deltaX: Double,
					  deltaY: Double,
					  listenable: Listenable = Mouse) extends MouseButtonEvent(MouseEvent.Press, button, true, x, y, deltaX, deltaY, listenable) {
	def retarget(target: org.sgine.event.Listenable, x: Double, y: Double): Event = new MousePressEvent(button, x, y, deltaX, deltaY, target)
	
	def retarget(target: Listenable) = retarget(target, x, y)
	
	override def toString() = "Pressed: " + button + " (" + x + "x" + y + ") Delta: (" + deltaX + "x" + deltaY + ")"
}

class MouseReleaseEvent(button: Int,
						x: Double,
						y: Double,
						deltaX: Double,
						deltaY: Double,
						listenable: Listenable = Mouse) extends MouseButtonEvent(MouseEvent.Release, button, false, x, y, deltaX, deltaY, listenable) {
	def retarget(target: org.sgine.event.Listenable, x: Double, y: Double): Event = new MouseReleaseEvent(button, x, y, deltaX, deltaY, target)
	
	def retarget(target: Listenable) = retarget(target, x, y)
	
	override def toString() = "Released: " + button + " (" + x + "x" + y + ") Delta: (" + deltaX + "x" + deltaY + ")"
}

class MouseMoveEvent(x: Double,
					 y: Double,
					 deltaX: Double,
					 deltaY: Double,
					 listenable: Listenable = Mouse) extends MouseEvent(MouseEvent.Move, x, y, deltaX, deltaY, listenable) {
	def retarget(target: org.sgine.event.Listenable, x: Double, y: Double): Event = new MouseMoveEvent(x, y, deltaX, deltaY, target)
	
	def retarget(target: Listenable) = retarget(target, x, y)
	
	override def toString() = "Moved: (" + x + "x" + y + ") Delta: (" + deltaX + "x" + deltaY + ")"
}

class MouseWheelEvent(x: Double,
					  y: Double,
					  val wheel: Int,
					  deltaX: Double,
					  deltaY: Double,
					  listenable: Listenable = Mouse) extends MouseEvent(MouseEvent.Wheel, x, y, deltaX, deltaY, listenable) {
	def retarget(target: org.sgine.event.Listenable, x: Double, y: Double): Event = new MouseWheelEvent(x, y, wheel, deltaX, deltaY, target)
	
	def retarget(target: Listenable) = retarget(target, x, y)
	
	override def toString() = "Wheel: " + wheel + " (" + x + "x" + y + ") Delta: (" + deltaX + "x" + deltaY + ")"
}