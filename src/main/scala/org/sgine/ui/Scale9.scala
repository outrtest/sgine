package org.sgine.ui

import org.sgine.bounding.BoundingObject
import org.sgine.bounding.event.BoundingChangeEvent
import org.sgine.bounding.mutable.BoundingQuad

import org.sgine.core.Resource

import org.sgine.event.Event
import org.sgine.event.EventHandler
import org.sgine.event.ProcessingMode

import org.sgine.property.AdvancedProperty
import org.sgine.property.event.PropertyChangeEvent

import org.sgine.render.{Image => RenderImage, Texture, TextureManager}

import org.sgine.ui.ext.AdvancedComponent

class Scale9 extends CompositeComponent with AdvancedComponent with BoundingObject {
	private val topLeft = new Image()
	private val top = new Image()
	private val topRight = new Image()
	private val left = new Image()
	private val center = new Image()
	private val right = new Image()
	private val bottomLeft = new Image()
	private val bottom = new Image()
	private val bottomRight = new Image()
	
	private var x1, y1, x2, y2: Double = _
	
	protected val _bounding = new BoundingQuad()

	val source = new AdvancedProperty[Resource](null, this)
	val width = new AdvancedProperty[Double](0.0, this)
	val height = new AdvancedProperty[Double](0.0, this)
	
	source.listeners += EventHandler(updateResource, ProcessingMode.Blocking)
	width.listeners += EventHandler(updateSize, ProcessingMode.Blocking)
	height.listeners += EventHandler(updateSize, ProcessingMode.Blocking)
	
	this += topLeft
	this += top
	this += topRight
	this += left
	this += center
	this += right
	this += bottomLeft
	this += bottom
	this += bottomRight
	
	def apply(source: Resource, x1: Double, y1: Double, x2: Double, y2: Double) = {
		this.x1 = x1
		this.x2 = x2
		this.y1 = y1
		this.y2 = y2
		
		// Load texture
		this.source := source
		updateResource(null)
		val t = topLeft.renderImage().texture
		
		// Create properly defined RenderImages
		val tli = topLeft.renderImage()
		tli.x = 0.0
		tli.y = 0.0
		tli.width = x1
		tli.height = y1
		
		val ti = top.renderImage()
		ti.x = x1
		ti.y = 0.0
		ti.width = x2 - x1
		ti.height = y1
		
		val tri = topRight.renderImage()
		tri.x = x2
		tri.y = 0.0
		tri.width = t.width - x2
		tri.height = y1
		
		val li = left.renderImage()
		li.x = 0.0
		li.y = y1
		li.width = x1
		li.height = y2 - y1
		
		val ci = center.renderImage()
		ci.x = x1
		ci.y = y1
		ci.width = x2 - x1
		ci.height = y2 - y1
		
		val ri = right.renderImage()
		ri.x = x2
		ri.y = y1
		ri.width = t.width - x2
		ri.height = y2 - y1
		
		val bli = bottomLeft.renderImage()
		bli.x = 0.0
		bli.y = y2
		bli.width = x1
		bli.height = t.height - y2
		
		val bi = bottom.renderImage()
		bi.x = x1
		bi.y = y2
		bi.width = x2 - x1
		bi.height = t.height - y2
		
		val bri = bottomRight.renderImage()
		bri.x = x2
		bri.y = y2
		bri.width = t.width - x2
		bri.height = t.height - y2
		
		// Update width and height if zero
		if (width() == 0.0) {
			width := t.width
		}
		if (height() == 0.0) {
			height := t.height
		}
		
		// Spread to width
		updateSize()
	}
	
	private def updateSize(evt: Event = null) = {
		if ((width() > 0.0) && (height() > 0.0) && (topLeft.renderImage() != null)) {
			val textureWidth = topLeft.renderImage().texture.width
			val textureHeight = topLeft.renderImage().texture.height
			val preWidth = x1 + (textureWidth - x2)
			val preHeight = y1 + (textureHeight - y2)
			
			val wide = textureWidth - preWidth
			val tall = textureHeight - preHeight
			
			val destWidth = width() - preWidth
			val destHeight = height() - preHeight
			
			val scaleWidth = destWidth / wide
			val scaleHeight = destHeight / tall
			
			top.scale.x := scaleWidth
			center.scale.x := scaleWidth
			bottom.scale.x := scaleWidth
			
			left.scale.y := scaleHeight
			center.scale.y := scaleHeight
			right.scale.y := scaleHeight
			
			val posX1 = (x1 / -2.0) - destWidth / 2.0
			val posX2 = posX1 + (x1 / 2.0) + (destWidth / 2.0)
			val posX3 = posX2 + (destWidth / 2.0) + ((textureWidth - x2) / 2.0)
			val posY1 = ((y1 / 2.0) + destHeight / 2.0)
			val posY2 = posY1 - (y1 / 2.0) - (destHeight / 2.0)
			val posY3 = posY2 - (destHeight / 2.0) - ((textureHeight - y2) / 2.0)
			
			topLeft.location.set(posX1, posY1)
			top.location.set(posX2, posY1)
			topRight.location.set(posX3, posY1)
			
			left.location.set(posX1, posY2)
			center.location.set(posX2, posY2)
			right.location.set(posX3, posY2)
			
			bottomLeft.location.set(posX1, posY3)
			bottom.location.set(posX2, posY3)
			bottomRight.location.set(posX3, posY3)
			
			_bounding.width = width()
			_bounding.height = height()
			
			val evt = new BoundingChangeEvent(this, _bounding)
			Event.enqueue(evt)
		}
	}
	
	private def updateResource(evt: PropertyChangeEvent[Resource]) = {
		// Load texture
		val t = TextureManager(source())
		
		if (topLeft.renderImage() == null) {
			topLeft.renderImage := new RenderImage()
			top.renderImage := new RenderImage()
			topRight.renderImage := new RenderImage()
			left.renderImage := new RenderImage()
			center.renderImage := new RenderImage()
			right.renderImage := new RenderImage()
			bottomLeft.renderImage := new RenderImage()
			bottom.renderImage := new RenderImage()
			bottomRight.renderImage := new RenderImage()
		}
		
		topLeft.renderImage().texture = t
		top.renderImage().texture = t
		topRight.renderImage().texture = t
		left.renderImage().texture = t
		center.renderImage().texture = t
		right.renderImage().texture = t
		bottomLeft.renderImage().texture = t
		bottom.renderImage().texture = t
		bottomRight.renderImage().texture = t
	}
}