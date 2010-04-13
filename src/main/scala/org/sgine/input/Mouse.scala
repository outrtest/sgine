package org.sgine.input

import org.sgine.event.Event
import org.sgine.event.Listenable

import org.sgine.input.event.MouseEvent

import org.sgine.work.Updatable

import org.lwjgl.input.{Mouse => GLMouse}

object Mouse extends Listenable with Updatable {
	def validate() = {
		if (!GLMouse.isCreated) {
			GLMouse.create()
		}
	}
	
	def update(time: Double) = {
		if (GLMouse.isCreated) {
			while (GLMouse.next()) {
				val button = GLMouse.getEventButton
				val state = GLMouse.getEventButtonState
				val wheel = GLMouse.getEventDWheel
				val x = GLMouse.getEventX
				val y = GLMouse.getEventY
				val dx = GLMouse.getEventDX
				val dy = GLMouse.getEventDY
				
				// TODO: update to context for mouse
				
				val evt = MouseEvent(button, state, wheel, x, y, dx, dy)
				Event.enqueue(evt)
			}
		}
	}
}