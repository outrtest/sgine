package org.sgine.ui

import java.util.concurrent.atomic.AtomicBoolean

import org.sgine.bounding.BoundingObject
import org.sgine.bounding.event.BoundingChangeEvent
import org.sgine.bounding.mutable.BoundingQuad

import org.sgine.core.Face
import org.sgine.core.Resource

import org.sgine.event.Event
import org.sgine.event.EventHandler
import org.sgine.event.ProcessingMode

import org.sgine.property.AdvancedProperty
import org.sgine.property.ListenableProperty
import org.sgine.property.MutableProperty
import org.sgine.property.event.PropertyChangeEvent

import org.sgine.render.{Renderer, RenderImage, TextureManager, TexturedQuad}

import org.sgine.ui.event.ImageUpdateEvent
import org.sgine.ui.ext.AdvancedComponent

class Image extends AdvancedComponent with BoundingObject {
	protected[ui] val quad = new TexturedQuad()
	
	val source = new AdvancedProperty[Resource](null, this)
	val cull = new AdvancedProperty[Face](Face.Back, this)
	quad.cull bind cull
	
	protected val _bounding = BoundingQuad()
	
	private val imageDirty = new AtomicBoolean(false)
	
	configureListeners()
	
	def this(source: Resource) = {
		this()
		
		this.source := source
	}
	
	override def update(renderer: Renderer) = {
		super.update(renderer)
		
		quad.update()
		
		if (imageDirty.compareAndSet(true, false)) {
			Event.enqueue(ImageUpdateEvent(this))
		}
	}
	
	def drawComponent() = {
		quad.render()
	}
	
	private def configureListeners() = {
		source.listeners += EventHandler(sourceChanged, ProcessingMode.Blocking)
	}
	
	private def sourceChanged(evt: PropertyChangeEvent[Resource]) = {
		val t = TextureManager(source())
		quad.texture := t
		quad.width := t.width
		quad.height := t.height
		
		if ((_bounding.width != quad.width()) || (_bounding.height != quad.height())) {
			_bounding.width = quad.width()
			_bounding.height = quad.height()
			
			val e = new BoundingChangeEvent(this, _bounding)
			Event.enqueue(e)
		}
		
		imageDirty.set(true)
	}
	
	override def toString() = "Image(" + source + ")"
}