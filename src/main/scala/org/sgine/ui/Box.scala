package org.sgine.ui

import java.util.concurrent.atomic.AtomicBoolean

import org.sgine.bounding.BoundingObject
import org.sgine.bounding.event.BoundingChangeEvent
import org.sgine.bounding.mutable.BoundingBox

import org.sgine.core.Color
import org.sgine.core.Face
import org.sgine.core.Resource

import org.sgine.event.Event
import org.sgine.event.EventHandler
import org.sgine.event.ProcessingMode

import org.sgine.property.AdvancedProperty
import org.sgine.property.NumericProperty
import org.sgine.property.NumericPropertyChangeEvent
import org.sgine.property.event.PropertyChangeEvent

import org.sgine.render.Renderer
import org.sgine.render.TextureManager
import org.sgine.render.shape.MutableShapeData
import org.sgine.render.shape.ShapeMode

import org.sgine.ui.ext.AdvancedComponent

import simplex3d.math.doublem.renamed._

class Box extends AdvancedComponent with ShapeComponent with BoundingObject {
	val source = new AdvancedProperty[Resource](null, this)
	val cull = _cull
	val material = _material
	/**
	 * If false width, height, and depth will update automatically
	 * based on the associated texture.
	 */
	val manualSize = new AdvancedProperty[Boolean](false, this)
	val width = new NumericProperty(0.0, this)
	val height = new NumericProperty(0.0, this)
	val depth = new NumericProperty(0.0, this)
	
	_mode := ShapeMode.Quads
	source.listeners += EventHandler(sourceChanged, ProcessingMode.Blocking)
	private val numericHandler = EventHandler(numericChanged, ProcessingMode.Blocking)
	width.listeners += numericHandler
	height.listeners += numericHandler
	depth.listeners += numericHandler
	
	protected val _bounding = BoundingBox()
	
	private val data = MutableShapeData(ShapeMode.Quads, 24)
	
	def this(source: Resource) = {
		this()
		
		this.source := source
	}
	
	private def sourceChanged(evt: PropertyChangeEvent[Resource]) = {
		texture = TextureManager(source())
		if (!manualSize()) {
			width := texture.width
			height := texture.height
			depth := texture.width
			
			updateVertices()
		}
		_texcoords := Box.DefaultTextureCoords
	}
	
	private def numericChanged(evt: NumericPropertyChangeEvent) = {
		updateVertices()
	}
	
	private def updateVertices() = {
		val w = width() / 2.0
		val h = height() / 2.0
		val d = depth() / 2.0
		_vertices := List(	// Front Face
							Vec3(-w, -h, d),		// Bottom-Left
							Vec3(w, -h, d),			// Bottom-Right
							Vec3(w, h, d),			// Top-Right
							Vec3(-w, h, d),			// Top-Left
							// Left Face
							Vec3(-w, -h, -d),		// Bottom-Left
							Vec3(-w, -h, d),		// Bottom-Right
							Vec3(-w, h, d),			// Top-Right
							Vec3(-w, h, -d),		// Top-Left
							// Back Face
							Vec3(-w, h, -d),		// Top-Left
							Vec3(w, h, -d),			// Top-Right
							Vec3(w, -h, -d),		// Bottom-Right
							Vec3(-w, -h, -d),		// Bottom-Left
							// Right Face
							Vec3(w, h, -d),			// Top-Left
							Vec3(w, h, d),			// Top-Right
							Vec3(w, -h, d),			// Bottom-Right
							Vec3(w, -h, -d),		// Bottom-Left
							// Top Face
							Vec3(-w, h, d),			// Bottom-Left
							Vec3(w, h, d),			// Bottom-Right
							Vec3(w, h, -d),			// Top-Right
							Vec3(-w, h, -d),		// Top-Left
							// Bottom Face
							Vec3(-w, -h, -d),		// Top-Left
							Vec3(w, -h, -d),		// Top-Right
							Vec3(w, -h, d),			// Bottom-Right
							Vec3(-w, -h, d)			// Bottom-Left
						 )
						 
		// Update bounding
		if ((_bounding.width != width()) || (_bounding.height != height()) || (_bounding.depth != depth())) {
			_bounding.width = width()
			_bounding.height = height()
			_bounding.depth = depth()
			
			val e = new BoundingChangeEvent(this, _bounding)
			Event.enqueue(e)
		}
	}
}

object Box {
	lazy val DefaultTextureCoords = generateDefaultTexCoords()
	
	private def generateDefaultTexCoords() = {
		val x1 = 0.0
		val y1 = 0.0
		val x2 = 1.0
		val y2 = 1.0
		
		List(	// Front Face
			Vec2(x1, y2),
			Vec2(x2, y2),
			Vec2(x2, y1),
			Vec2(x1, y1),
			// Left Face
			Vec2(x1, y2),
			Vec2(x2, y2),
			Vec2(x2, y1),
			Vec2(x1, y1),
			// Back Face
			Vec2(x2, y1),
			Vec2(x1, y1),
			Vec2(x1, y2),
			Vec2(x2, y2),
			// Right Face
			Vec2(x2, y1),
			Vec2(x1, y1),
			Vec2(x1, y2),
			Vec2(x2, y2),
			// Top Face
			Vec2(x1, y2),
			Vec2(x2, y2),
			Vec2(x2, y1),
			Vec2(x1, y1),
			// Bottom Face
			Vec2(x2, y1),
			Vec2(x1, y1),
			Vec2(x1, y2),
			Vec2(x2, y2)
		)
	}
}