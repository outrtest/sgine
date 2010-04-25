package org.sgine.ui

import org.sgine.core.Resource

import org.sgine.bounding.BoundingObject
import org.sgine.bounding.event.BoundingChangeEvent
import org.sgine.bounding.mutable.BoundingQuad

import org.sgine.event.Event
import org.sgine.event.EventHandler
import org.sgine.event.ProcessingMode

import org.sgine.input.event._

import org.sgine.property.AdvancedProperty
import org.sgine.property.state.State

import org.sgine.render.font.Font
import org.sgine.render.font.FontManager

import org.sgine.ui.ext.AdvancedComponent
import org.sgine.ui.ext.PaddingComponent

class Button extends CompositeComponent with AdvancedComponent with BoundingObject with PaddingComponent {
	val font = new AdvancedProperty[Font](null, this)
	val text = new AdvancedProperty[String]("", this)
	
	private val asset = new Scale9()
	private val label = new Label()
	
	label.font bind font
	label.text bind text
	
	private val normalResource = Resource("scale9/windows/button/normal.png")
	private val hoverResource = Resource("scale9/windows/button/hover.png")
	private val pressedResource = Resource("scale9/windows/button/pressed.png")
	
	padding.top := 10.0
	padding.bottom := 10.0
	padding.left := 25.0
	padding.right := 25.0
	label.color := org.sgine.core.Color(0.0, 0.0, 0.0, 1.0)
	
	private var pressed: Boolean = false
	private var over: Boolean = false
	
	configureListeners()
	
	protected val _bounding = new BoundingQuad()
	
	val children = label :: asset :: Nil
	
	// TODO: make skinnable
	asset(normalResource, 3.0, 3.0, 4.0, 5.0)
	
	font := FontManager("Arial")			// TODO: default font?
		
	private def configureListeners() = {
		// Listen for size changes to font
		label.listeners += EventHandler(boundingChanged, ProcessingMode.Blocking)
		
		// Mouse states for button
		listeners += EventHandler(mouseOver, ProcessingMode.Blocking)
		listeners += EventHandler(mouseOut, ProcessingMode.Blocking)
		listeners += EventHandler(mouseDown, ProcessingMode.Blocking)
		listeners += EventHandler(mouseUp, ProcessingMode.Blocking)
	}
	
	private def mouseOver(evt: MouseOverEvent) = {
		over = true
		updateButtonState()
	}
	
	private def mouseOut(evt: MouseOutEvent) = {
		over = false
		pressed = false
		updateButtonState()
	}
	
	private def mouseDown(evt: MousePressEvent) = {
		pressed = true
		updateButtonState()
	}
	
	private def mouseUp(evt: MouseReleaseEvent) = {
		pressed = false
		updateButtonState()
	}
	
	private def updateButtonState() = {
		if (pressed) {
			asset.source := pressedResource
		} else if (over) {
			asset.source := hoverResource
		} else {
			asset.source := normalResource
		}
	}
	
	private def boundingChanged(evt: BoundingChangeEvent) = {
		val bounding = evt.bounding.asInstanceOf[BoundingQuad]
		val newWidth = bounding.width + padding.left() + padding.right()
		val newHeight = bounding.height + padding.top() + padding.bottom()
		asset.width := newWidth
		asset.height := newHeight
		_bounding.width = newWidth
		_bounding.height = newHeight
		
		val e = new BoundingChangeEvent(this, _bounding)
		Event.enqueue(e)
	}
}