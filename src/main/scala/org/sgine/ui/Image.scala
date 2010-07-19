package org.sgine.ui

import java.util.concurrent.atomic.AtomicBoolean

import org.sgine.bounding.BoundingObject
import org.sgine.bounding.event.BoundingChangeEvent
import org.sgine.bounding.mutable.BoundingQuad

import org.sgine.core.Resource

import org.sgine.event.Event
import org.sgine.event.EventHandler
import org.sgine.event.ProcessingMode

import org.sgine.property.AdvancedProperty
import org.sgine.property.ListenableProperty
import org.sgine.property.MutableProperty
import org.sgine.property.event.PropertyChangeEvent

import org.sgine.render.{Renderer, RenderImage, TextureManager}

import org.sgine.ui.event.ImageUpdateEvent
import org.sgine.ui.ext.AdvancedComponent

class Image extends AdvancedComponent with BoundingObject {
	val renderImage = new MutableProperty[RenderImage](null) with ListenableProperty[RenderImage]
	
	val source = new AdvancedProperty[Resource](null, this)
	
	protected val _bounding = BoundingQuad()
	
	private val imageDirty = new AtomicBoolean(false)
	
	configureListeners()
	
	def this(source: Resource) = {
		this()
		
		this.source := source
	}
	
	override def update(renderer: Renderer) = {
		super.update(renderer)
		
		renderImage() match {
			case null =>
			case r => {
				r.update()
				
				if (imageDirty.compareAndSet(true, false)) {
					Event.enqueue(ImageUpdateEvent(this))
				}
			}
		}
	}
	
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
		val t = TextureManager(source())
		renderImage := RenderImage(t)
		
		imageDirty.set(true)
	}
	
	private def renderImageChanged(evt: PropertyChangeEvent[RenderImage]) = {
		val i = evt.newValue
		if ((_bounding.width != i.width) || (_bounding.height != i.height)) {
			_bounding.width = i.width
			_bounding.height = i.height
			
			val e = new BoundingChangeEvent(this, _bounding)
			Event.enqueue(e)
		}
	}
	
	override def toString() = "Image(" + source + ")"
}