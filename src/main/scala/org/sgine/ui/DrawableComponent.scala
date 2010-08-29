package org.sgine.ui

import java.nio.ByteBuffer
import java.nio.ByteOrder

import java.util.concurrent.atomic.AtomicBoolean

import org.sgine.bounding.BoundingObject
import org.sgine.bounding.mutable.BoundingQuad

import org.sgine.event.Event
import org.sgine.event.EventHandler
import org.sgine.event.ProcessingMode

import org.sgine.property.AdvancedProperty
import org.sgine.property.ListenableProperty
import org.sgine.property.MutableProperty
import org.sgine.property.event.PropertyChangeEvent

import org.sgine.render.RenderImage
import org.sgine.render.StreamingTexture
import org.sgine.render.TextureUtil

import org.sgine.util.GeneralReusableGraphic

import org.sgine.work.DefaultWorkManager

import scala.math._

trait DrawableComponent extends CachedComponent {
	val painter = new AdvancedProperty[(java.awt.Graphics2D) => Unit](null, this)
	
	protected def draw(buffer: ByteBuffer): Unit = {
		val painter = this.painter()
		if (painter != null) {
			val g = GeneralReusableGraphic(round(dimension.width()).toInt, round(dimension.height()).toInt)
			try {
				painter(g)
				
				TextureUtil.image2Buffer(GeneralReusableGraphic(), buffer)
			} finally {
				GeneralReusableGraphic.release()
			}
		}
	}
	
	override protected def configureListeners() = {
		super.configureListeners()
		painter.listeners += EventHandler(invalidateCache, processingMode = ProcessingMode.Blocking)
		
		true
	}
}

object DrawableComponent {
	def apply() = new DefaultDrawableComponent
}

class DefaultDrawableComponent extends DrawableComponent