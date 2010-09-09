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

import scala.math._

trait CachedComponent extends Component with BoundingObject {
	val pixelFormat = new AdvancedProperty[Int](GL_RGBA, this)
	
	protected var texture: StreamingTexture = _
	protected var image = new RenderImage()
	
	private var initialized = false
	
	def drawComponent() = {
		if (!initialized) {
			configureListeners()
			initialized = true
		}
		image()
	}
	
	private def redraw(buffer: ByteBuffer) = {
		draw(buffer)
		
		image.width = dimension.width()
		image.height = dimension.height()
		if ((dimension.width() != dimension.width()) || (dimension.height() != dimension.height())) {
			dimension.width := dimension.width()
			dimension.height := dimension.height()
		}
	}
	
	protected def draw(buffer: ByteBuffer): Unit
	
	protected def configureListeners() = {
		dimension.width.listeners += EventHandler(invalidateCache, processingMode = ProcessingMode.Blocking)
		dimension.height.listeners += EventHandler(invalidateCache, processingMode = ProcessingMode.Blocking)
		
		true
	}
	
	def invalidateCache(evt: PropertyChangeEvent[_] = null) = {
		if ((dimension.width() > 0) && (dimension.height() > 0)) {
			if (texture == null) {
				texture = new StreamingTexture(round(dimension.width()).toInt, round(dimension.height()).toInt)
				info("Streaming: " + dimension.width() + "x" + dimension.height())
				texture.internalTextureFormat = GL_RGBA
				texture.pixelTextureFormat = pixelFormat()
				texture.updateFunction = redraw
				image.texture = texture
			}
			texture.invalidateTexture()
		}
	}
}