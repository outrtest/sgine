package org.sgine.ui

import org.sgine.core.Resource

import org.sgine.event.EventHandler
import org.sgine.event.ProcessingMode

import org.sgine.input.event.MouseOutEvent
import org.sgine.input.event.MousePressEvent
import org.sgine.input.event.MouseReleaseEvent

trait PressableComponent extends SkinnableComponent {
	protected def pressedResource: Resource
	
	private var pressed = false
	
	override protected def updateState() = {
		if (pressed) {
			skin.source := pressedResource
		} else {
			super.updateState()
		}
	}
	
	private def configureListeners() = {
		// Mouse states for button
		listeners += EventHandler(mouseOut, ProcessingMode.Blocking)
		listeners += EventHandler(mouseDown, ProcessingMode.Blocking)
		listeners += EventHandler(mouseUp, ProcessingMode.Blocking)
	}
	
	private def mouseOut(evt: MouseOutEvent) = {
		pressed = false
		updateState()
	}
	
	private def mouseDown(evt: MousePressEvent) = {
		pressed = true
		updateState()
	}
	
	private def mouseUp(evt: MouseReleaseEvent) = {
		pressed = false
		updateState()
	}
}