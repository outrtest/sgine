package org.sgine.ui

import java.util.concurrent.atomic.AtomicBoolean

import org.sgine.bounding.BoundingObject
import org.sgine.bounding.mutable.BoundingQuad

import org.sgine.core.Face
import org.sgine.core.ProcessingMode
import org.sgine.core.Resource

import org.sgine.event.Event
import org.sgine.event.EventHandler

import org.sgine.property.AdvancedProperty
import org.sgine.property.ListenableProperty
import org.sgine.property.MutableProperty
import org.sgine.property.event.PropertyChangeEvent

import org.sgine.render.{Renderer, RenderImage, TextureManager, TexturedQuad}

import org.sgine.ui.event.ImageUpdateEvent
import org.sgine.ui.style._

class Image extends Component {
	protected[ui] val quad = new TexturedQuad()
	
	val source = new AdvancedProperty[Resource](null, this)
	val cull = new AdvancedProperty[Face](null, this, dependency = style.cull)
	quad.cull bind cull
	
	override def style: ImageStyle = Image.style
	
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
		
		if ((size.width() != quad.width()) || (size.height() != quad.height())) {
			size.width := quad.width()
			size.height := quad.height()
		}
		
		imageDirty.set(true)
	}
	
	override def toString() = "Image(source = " + source() + ")"
}

object Image {
	val style = new ImageStyle {
		val alpha = StyleProperty(0.0, this, Component.style.alpha)
		val color = StyleProperty(null, this, Component.style.color)
		val cull = StyleProperty[Face](Face.Back, this)
	}
}

trait ImageStyle extends ComponentStyle {
	val cull: StyleProperty[Face]
}