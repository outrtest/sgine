package org.sgine.ui

import java.util.concurrent.atomic.AtomicBoolean

import org.sgine.bounding.BoundingObject
import org.sgine.bounding.event.BoundingChangeEvent
import org.sgine.bounding.mutable.BoundingQuad

import org.sgine.event.Event
import org.sgine.event.EventHandler
import org.sgine.event.ProcessingMode

import org.sgine.property.AdvancedProperty
import org.sgine.property.ListenableProperty
import org.sgine.property.MutableProperty
import org.sgine.property.event.PropertyChangeEvent

import org.sgine.render.{Image => RenderImage}
import org.sgine.render.Texture
import org.sgine.render.TextureUtil

import org.sgine.ui.ext.AdvancedComponent

import org.sgine.util.GeneralReusableGraphic

trait DrawableComponent extends Component with BoundingObject {
	val painter = new AdvancedProperty[(java.awt.Graphics2D) => Unit](null, this)
	
	val width = new AdvancedProperty[Int](0, this)
	val height = new AdvancedProperty[Int](0, this)
	
	protected val _bounding = BoundingQuad()
	
	private val dirty = new AtomicBoolean(true)
	
	private val texture = new Texture(width(), height())
	private var image = new RenderImage()
	
	configureListeners()
	
	protected def drawComponent() = {
		if (dirty.get) {
			val painter = this.painter()
			if (painter != null) {
				dirty.set(false)
				
				val g = GeneralReusableGraphic(width(), height())
				try {
					painter(g)
					
					TextureUtil(texture, GeneralReusableGraphic(), 0, 0, width(), height())
					image.texture = texture
					image.x = 0.0
					image.y = 0.0
					image.width = width()
					image.height = height()
				} finally {
					GeneralReusableGraphic.release()
				}
				
				if ((_bounding.width != width()) || (_bounding.height != height())) {
					_bounding.width = width()
					_bounding.height = height()
					
					val e = new BoundingChangeEvent(this, _bounding)
					Event.enqueue(e)
				}
			}
		}
		
		image()
	}
	
	private def configureListeners() = {
		width.listeners += EventHandler(invalidateDrawing, processingMode = ProcessingMode.Blocking)
		height.listeners += EventHandler(invalidateDrawing, processingMode = ProcessingMode.Blocking)
		painter.listeners += EventHandler(invalidateDrawing, processingMode = ProcessingMode.Blocking)
	}
	
	def invalidateDrawing(evt: PropertyChangeEvent[_] = null) = {
		dirty.set(true)
	}
}

object DrawableComponent {
	def apply() = new DefaultDrawableComponent
}

class DefaultDrawableComponent extends DrawableComponent with AdvancedComponent