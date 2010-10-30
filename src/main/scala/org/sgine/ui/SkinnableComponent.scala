package org.sgine.ui

import org.sgine.bounding.BoundingObject
import org.sgine.bounding.mutable.BoundingQuad

import org.sgine.core.ProcessingMode
import org.sgine.core.Resource

import org.sgine.event.Event
import org.sgine.event.EventHandler

import org.sgine.input.event._

import org.sgine.property.event.PropertyChangeEvent

import org.sgine.scene.ext.FocusableNode

trait SkinnableComponent extends CompositeComponent with FocusableNode {
	protected val skin = new Scale9()
	
	protected def normalResource: Resource
	protected def hoverResource: Resource
	protected def focusedResource: Resource
	
	protected def face: Component
	
	protected def skinX1: Double
	protected def skinY1: Double
	protected def skinX2: Double
	protected def skinY2: Double
	
	private var over: Boolean = false
	
	configureListeners()

	override protected def initComponent() = {
		this += skin
		this += face
		
		skin(normalResource, skinX1, skinY1, skinX2, skinY2)
		
		skin.size.width bind size.actual.width
		skin.size.height bind size.actual.height
		
		updateState()
	}
	
	protected def setSize(width: Double, height: Double) = {
		size.actual.width := width + padding.left() + padding.right()
		size.actual.height := height + padding.top() + padding.bottom()
	}
	
	private def configureListeners() = {
//		bounding.listeners += EventHandler(boundingChanged, ProcessingMode.Blocking)
		
		// Mouse states for button
		listeners += EventHandler(mouseOver, ProcessingMode.Blocking)
		listeners += EventHandler(mouseOut, ProcessingMode.Blocking)
		listeners += EventHandler(mouseDown, ProcessingMode.Blocking)
		listeners += EventHandler(mouseUp, ProcessingMode.Blocking)
		
		// Focus states for button
		focused.listeners += EventHandler(focusChange, ProcessingMode.Blocking)
	}
	
//	private def boundingChanged(evt: PropertyChangeEvent[_]) = {
//		skin.width := size.width()
//		skin.height := size.height()
//	}
	
	protected def updateBounding(evt: PropertyChangeEvent[_] = null) = {
		face match {
			case bo: BoundingObject => setSize(bo.bounding().width, bo.bounding().height)
			case _ => // No bounding object
		}
	}
	
	private def mouseOver(evt: MouseOverEvent) = {
		over = true
		updateState()
	}
	
	private def mouseOut(evt: MouseOutEvent) = {
		over = false
		updateState()
	}
	
	private def mouseDown(evt: MousePressEvent) = {
		updateState()
	}
	
	private def mouseUp(evt: MouseReleaseEvent) = {
		updateState()
	}
	
	private def focusChange(evt: PropertyChangeEvent[Boolean]) = {
		updateState()
	}
	
	protected def updateState() = {
		if (over) {
			skin.source := hoverResource
		} else if (focused()) {
			skin.source := focusedResource
		} else {
			skin.source := normalResource
		}
	}
}