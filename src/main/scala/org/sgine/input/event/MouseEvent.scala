package org.sgine.input.event

import org.sgine.event.Event
import org.sgine.event.Listenable

import org.sgine.input.Mouse

object MouseEvent {
	val Press = 1
	val Release = 2
	val Move = 3
	val Wheel = 4
	val Over = 5
	val Out = 6
	
	def apply(button: Int, state: Boolean, wheel: Int, x: Double, y: Double, dx: Double, dy: Double, listenable: Listenable = Mouse): MouseEvent = {
		if (button == -1) {		// Not a button event
			if (wheel != 0) {	// Wheel
				new MouseWheelEvent(x, y, wheel, dx, dy, listenable)
			} else {			// Move
				new MouseMoveEvent(x, y, dx, dy, listenable)
			}
		} else if (button == -2) {	// MouseOver
			new MouseOverEvent(x, y, dx, dy, listenable)
		} else if (button == -3) {	// MouseOut
			new MouseOutEvent(x, y, dx, dy, listenable)
		} else {				// Button event
			if (state) {		// Pressed
				new MousePressEvent(button, x, y, dx, dy, listenable)
			} else {			// Released
				new MouseReleaseEvent(button, x, y, dx, dy, listenable)
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
	def retarget(target: org.sgine.event.Listenable, x: Double, y: Double): Event = MouseEvent(button, true, 0, x, y, deltaX, deltaY, target)
	
	def retarget(target: Listenable) = retarget(target, x, y)
	
	override def toString() = "Pressed: " + button + " (" + x + "x" + y + ") Delta: (" + deltaX + "x" + deltaY + ")"
}

class MouseReleaseEvent(button: Int,
						x: Double,
						y: Double,
						deltaX: Double,
						deltaY: Double,
						listenable: Listenable = Mouse) extends MouseButtonEvent(MouseEvent.Release, button, false, x, y, deltaX, deltaY, listenable) {
	def retarget(target: org.sgine.event.Listenable, x: Double, y: Double): Event = MouseEvent(button, false, 0, x, y, deltaX, deltaY, target)
	
	def retarget(target: Listenable) = retarget(target, x, y)
	
	override def toString() = "Released: " + button + " (" + x + "x" + y + ") Delta: (" + deltaX + "x" + deltaY + ")"
}

class MouseMoveEvent(x: Double,
					 y: Double,
					 deltaX: Double,
					 deltaY: Double,
					 listenable: Listenable = Mouse) extends MouseEvent(MouseEvent.Move, x, y, deltaX, deltaY, listenable) {
	def retarget(target: org.sgine.event.Listenable, x: Double, y: Double): Event = MouseEvent(-1, false, 0, x, y, deltaX, deltaY, target)
	
	def retarget(target: Listenable) = retarget(target, x, y)
	
	override def toString() = "Moved: (" + x + "x" + y + ") Delta: (" + deltaX + "x" + deltaY + ")"
}

class MouseOverEvent(x: Double,
					 y: Double,
					 deltaX: Double,
					 deltaY: Double,
					 listenable: Listenable = Mouse) extends MouseEvent(MouseEvent.Over, x, y, deltaX, deltaY, listenable) {
	def retarget(target: org.sgine.event.Listenable, x: Double, y: Double): Event = MouseEvent(-2, false, 0, x, y, deltaX, deltaY, target)
	
	def retarget(target: Listenable) = retarget(target, x, y)
	
	override def toString() = "MouseOver: (" + x + "x" + y + ") Delta: (" + deltaX + "x" + deltaY + ")"
}

class MouseOutEvent(x: Double,
					 y: Double,
					 deltaX: Double,
					 deltaY: Double,
					 listenable: Listenable = Mouse) extends MouseEvent(MouseEvent.Out, x, y, deltaX, deltaY, listenable) {
	def retarget(target: org.sgine.event.Listenable, x: Double, y: Double): Event = MouseEvent(-3, false, 0, x, y, deltaX, deltaY, target)
	
	def retarget(target: Listenable) = retarget(target, x, y)
	
	override def toString() = "MouseOut: (" + x + "x" + y + ") Delta: (" + deltaX + "x" + deltaY + ")"
}

class MouseWheelEvent(x: Double,
					  y: Double,
					  val wheel: Int,
					  deltaX: Double,
					  deltaY: Double,
					  listenable: Listenable = Mouse) extends MouseEvent(MouseEvent.Wheel, x, y, deltaX, deltaY, listenable) {
	def retarget(target: org.sgine.event.Listenable, x: Double, y: Double): Event = MouseEvent(-1, false, -1, x, y, deltaX, deltaY, target)
	
	def retarget(target: Listenable) = retarget(target, x, y)
	
	override def toString() = "Wheel: " + wheel + " (" + x + "x" + y + ") Delta: (" + deltaX + "x" + deltaY + ")"
}