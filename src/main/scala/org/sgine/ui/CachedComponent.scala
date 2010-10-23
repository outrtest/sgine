package org.sgine.ui

import java.nio.ByteBuffer

import org.sgine.bounding.BoundingObject
import org.sgine.bounding.mutable.BoundingQuad

import org.sgine.core.ProcessingMode

import org.sgine.event.Event
import org.sgine.event.EventHandler

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
	
	override def initComponent() = {
		configureListeners()
	}
	
	def drawComponent() = {
		image()
	}
	
	private def redraw(buffer: ByteBuffer) = {
		draw(buffer)
		
		image.width = size.width()
		image.height = size.height()
		if ((size.width() != size.width()) || (size.height() != size.height())) {
			size.width := size.width()
			size.height := size.height()
		}
	}
	
	protected def draw(buffer: ByteBuffer): Unit
	
	protected def configureListeners() = {
		size.width.listeners += EventHandler(invalidateCache, processingMode = ProcessingMode.Blocking)
		size.height.listeners += EventHandler(invalidateCache, processingMode = ProcessingMode.Blocking)
		
		true
	}
	
	def invalidateCache(evt: PropertyChangeEvent[_] = null) = {
		if ((size.width() > 0) && (size.height() > 0)) {
			if (texture == null) {
				texture = new StreamingTexture(round(size.width()).toInt, round(size.height()).toInt)
				info("Streaming: " + size.width() + "x" + size.height())
				texture.internalTextureFormat = GL_RGBA
				texture.pixelTextureFormat = pixelFormat()
				texture.updateFunction = redraw
				image.texture = texture
			}
			texture.invalidateTexture()
		}
	}
}