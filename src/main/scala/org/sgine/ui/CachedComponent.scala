package org.sgine.ui

import java.nio.ByteBuffer

import org.sgine.bounding.BoundingObject
import org.sgine.bounding.mutable.BoundingQuad

import org.sgine.event.Event
import org.sgine.event.EventHandler
import org.sgine.event.ProcessingMode

import org.sgine.log._

import org.sgine.property.AdvancedProperty
import org.sgine.property.event.PropertyChangeEvent

import org.sgine.render.RenderImage
import org.sgine.render.StreamingTexture

import org.lwjgl.opengl.GL11._
import org.lwjgl.opengl.GL12._

trait CachedComponent extends Component with BoundingObject {
	val width = new AdvancedProperty[Int](0, this)
	val height = new AdvancedProperty[Int](0, this)
	val pixelFormat = new AdvancedProperty[Int](GL_RGBA, this)
	
	protected var texture: StreamingTexture = _
	protected var image = new RenderImage()
	
	private var initialized = false
	
	protected[ui] def drawComponent() = {
		if (!initialized) {
			configureListeners()
			initialized = true
		}
		image()
	}
	
	private def redraw(buffer: ByteBuffer) = {
		draw(buffer)
		
		image.width = width()
		image.height = height()
		if ((dimension.width() != width()) || (dimension.height() != height())) {
			dimension.width := width()
			dimension.height := height()
		}
	}
	
	protected def draw(buffer: ByteBuffer): Unit
	
	protected def configureListeners() = {
		width.listeners += EventHandler(invalidateCache, processingMode = ProcessingMode.Blocking)
		height.listeners += EventHandler(invalidateCache, processingMode = ProcessingMode.Blocking)
		
		true
	}
	
	def invalidateCache(evt: PropertyChangeEvent[_] = null) = {
		if ((width() > 0) && (height() > 0)) {
			if (texture == null) {
				texture = new StreamingTexture(width(), height())
				info("Streaming: " + width() + "x" + height())
				texture.internalTextureFormat = GL_RGBA
				texture.pixelTextureFormat = pixelFormat()
				texture.updateFunction = redraw
				image.texture = texture
			}
			texture.invalidateTexture()
		}
	}
}