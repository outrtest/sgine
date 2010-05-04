package org.sgine.ui

import org.sgine.bounding.BoundingObject
import org.sgine.bounding.event.BoundingChangeEvent
import org.sgine.bounding.mutable.BoundingQuad

import org.sgine.core.Resource

import org.sgine.event.Event
import org.sgine.event.EventHandler
import org.sgine.event.ProcessingMode

import org.sgine.input.event._

import org.sgine.property.event.PropertyChangeEvent

import org.sgine.ui.ext.AdvancedComponent
import org.sgine.scene.ext.FocusableNode
import org.sgine.ui.ext.PaddingComponent

trait SkinnableComponent extends CompositeComponent with BoundingObject with PaddingComponent with FocusableNode {
	protected val skin = new Scale9()
	
	protected val _bounding = new BoundingQuad()
	
	protected def normalResource: Resource
	protected def hoverResource: Resource
	protected def pressedResource: Resource
	protected def focusedResource: Resource
	
	protected def face: Component
	
	protected def skinX1: Double
	protected def skinY1: Double
	protected def skinX2: Double
	protected def skinY2: Double
	
	private var pressed: Boolean = false
	private var over: Boolean = false
	
	configureListeners()

	override protected def initComponent() = {
		this += face
		this += skin
		
		skin(normalResource, skinX1, skinY1, skinX2, skinY2)
		updateState()
	}
	
	protected def setSize(width: Double, height: Double) = {
		_bounding.width = width + padding.left() + padding.right()
		_bounding.height = height + padding.top() + padding.bottom()
		
		val evt = new BoundingChangeEvent(this, _bounding)
		Event.enqueue(evt)
	}
	
	private def configureListeners() = {
		listeners += EventHandler(boundingChanged, ProcessingMode.Blocking)
		
		// Mouse states for button
		listeners += EventHandler(mouseOver, ProcessingMode.Blocking)
		listeners += EventHandler(mouseOut, ProcessingMode.Blocking)
		listeners += EventHandler(mouseDown, ProcessingMode.Blocking)
		listeners += EventHandler(mouseUp, ProcessingMode.Blocking)
		
		// Focus states for button
		focused.listeners += EventHandler(focusChange, ProcessingMode.Blocking)
	}
	
	private def boundingChanged(evt: BoundingChangeEvent) = {
		skin.width := _bounding.width
		skin.height := _bounding.height
	}
	
	protected def updateBounding(evt: BoundingChangeEvent = null) = {
		face match {
			case bo: BoundingObject => {
				bo.bounding() match {
					case bq: BoundingQuad => {
						setSize(bq.width, bq.height)
					}
					case _ => // No other supported bounding types
				}
			}
			case _ => // No bounding object
		}
	}
	
	private def mouseOver(evt: MouseOverEvent) = {
		over = true
		updateState()
	}
	
	private def mouseOut(evt: MouseOutEvent) = {
		over = false
		pressed = false
		updateState()
	}
	
	private def mouseDown(evt: MousePressEvent) = {
		pressed = true
		focused := true
		updateState()
	}
	
	private def mouseUp(evt: MouseReleaseEvent) = {
		pressed = false
		updateState()
	}
	
	private def focusChange(evt: PropertyChangeEvent[Boolean]) = {
		updateState()
	}
	
	private def updateState() = {
		if (pressed) {
			skin.source := pressedResource
		} else if (over) {
			skin.source := hoverResource
		} else if (focused()) {
			skin.source := focusedResource
		} else {
			skin.source := normalResource
		}
	}
}