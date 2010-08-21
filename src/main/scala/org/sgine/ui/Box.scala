package org.sgine.ui

import java.util.concurrent.atomic.AtomicBoolean

import org.sgine.bounding.BoundingObject
import org.sgine.bounding.mutable.BoundingBox

import org.sgine.core.Color
import org.sgine.core.Face
import org.sgine.core.Resource

import org.sgine.event.Event
import org.sgine.event.EventHandler
import org.sgine.event.ProcessingMode

import org.sgine.property.AdvancedProperty
import org.sgine.property.NumericProperty
import org.sgine.property.event.NumericPropertyChangeEvent
import org.sgine.property.event.PropertyChangeEvent

import org.sgine.render.Renderer
import org.sgine.render.TextureManager
import org.sgine.render.shape.MutableShapeData
import org.sgine.render.shape.ShapeMode

import simplex3d.math.doublem.renamed._

class Box extends Component with ShapeComponent {
	val source = new AdvancedProperty[Resource](null, this)
	val cull = _cull
	val material = _material
	/**
	 * If false width, height, and depth will update automatically
	 * based on the associated texture.
	 */
	val manualSize = new AdvancedProperty[Boolean](false, this)
	
	_mode := ShapeMode.Quads
	source.listeners += EventHandler(sourceChanged, ProcessingMode.Blocking)
	private val numericHandler = EventHandler(numericChanged, ProcessingMode.Blocking)
	dimension.width.listeners += numericHandler
	dimension.height.listeners += numericHandler
	dimension.depth.listeners += numericHandler
	
	private val data = MutableShapeData(ShapeMode.Quads, 24)
	
	def this(source: Resource) = {
		this()
		
		this.source := source
	}
	
	private def sourceChanged(evt: PropertyChangeEvent[Resource]) = {
		texture = TextureManager(source())
		if (!manualSize()) {
			dimension.width := texture.width
			dimension.height := texture.height
			dimension.depth := texture.width
			
			updateVertices()
		}
		_texcoords := Box.DefaultTextureCoords
	}
	
	private def numericChanged(evt: NumericPropertyChangeEvent) = {
		updateVertices()
	}
	
	private def updateVertices() = {
		val w = dimension.width() / 2.0
		val h = dimension.height() / 2.0
		val d = dimension.depth() / 2.0
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