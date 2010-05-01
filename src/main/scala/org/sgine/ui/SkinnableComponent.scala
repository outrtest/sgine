package org.sgine.ui

import org.sgine.bounding.BoundingObject
import org.sgine.bounding.event.BoundingChangeEvent
import org.sgine.bounding.mutable.BoundingQuad

import org.sgine.core.Resource

import org.sgine.event.Event
import org.sgine.event.EventHandler
import org.sgine.event.ProcessingMode

import org.sgine.ui.ext.AdvancedComponent
import org.sgine.ui.ext.PaddingComponent

trait SkinnableComponent extends CompositeComponent with BoundingObject {
	private val skin = new Scale9()
	
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
	
	listeners += EventHandler(boundingChanged, ProcessingMode.Blocking)

	override protected def initComponent() = {
		this += face
		this += skin
		
		skin(normalResource, skinX1, skinY1, skinX2, skinY2)
	}
	
	private def boundingChanged(evt: BoundingChangeEvent) = {
		skin.width := _bounding.width
		skin.height := _bounding.height
	}
	
	def setSize(width: Double, height: Double) = {
		_bounding.width = width
		_bounding.height = height
		
		val evt = new BoundingChangeEvent(this, _bounding)
		Event.enqueue(evt)
	}
}