package org.sgine.ui

import org.sgine.bounding.BoundingObject
import org.sgine.bounding.mutable.BoundingQuad

import org.sgine.core.Resource

import org.sgine.event.EventHandler
import org.sgine.event.ProcessingMode

import org.sgine.property.AdvancedProperty
import org.sgine.property.ListenableProperty
import org.sgine.property.MutableProperty
import org.sgine.property.event.PropertyChangeEvent

import org.sgine.render.{Image => RenderImage, TextureUtil}

import org.sgine.ui.ext.AdvancedComponent

class Image extends AdvancedComponent with BoundingObject {
	val renderImage = new MutableProperty[RenderImage](null) with ListenableProperty[RenderImage]
	
	val source = new AdvancedProperty[Resource](null, this)
	
	protected val _bounding = BoundingQuad()
	
	configureListeners()
	
	def drawComponent() = {
		if (renderImage() != null) {
			renderImage().draw()
		}
	}
	
	private def configureListeners() = {
		source.listeners += EventHandler(sourceChanged, ProcessingMode.Blocking)
		renderImage.listeners += EventHandler(renderImageChanged, ProcessingMode.Blocking)
	}
	
	private def sourceChanged(evt: PropertyChangeEvent[Resource]) = {
		val t = TextureUtil(source().url)
		renderImage := RenderImage(t)
	}
	
	private def renderImageChanged(evt: PropertyChangeEvent[RenderImage]) = {
		val i = evt.newValue
		_bounding.width = i.width
		_bounding.height = i.height
	}
}