package org.sgine.ui

import org.sgine.core.Color
import org.sgine.core.Face
import org.sgine.core.Resource

import org.sgine.event.EventHandler
import org.sgine.event.ProcessingMode

import org.sgine.property.AdvancedProperty
import org.sgine.property.NumericProperty
import org.sgine.property.NumericPropertyChangeEvent
import org.sgine.property.event.PropertyChangeEvent

import org.sgine.render.TextureManager
import org.sgine.render.shape.MutableShapeData
import org.sgine.render.shape.ShapeMode

import org.sgine.ui.ext.AdvancedComponent

import simplex3d.math.doublem.renamed._

class Box extends AdvancedComponent with ShapeComponent {
	val source = new AdvancedProperty[Resource](null, this)
	val cull = new AdvancedProperty[Face](Face.Back, this)
	/**
	 * If false width, height, and depth will update automatically
	 * based on the associated texture.
	 */
	val manualSize = new AdvancedProperty[Boolean](false, this)
	val width = new NumericProperty(0.0, this)
	val height = new NumericProperty(0.0, this)
	val depth = new NumericProperty(0.0, this)
	
	source.listeners += EventHandler(sourceChanged, ProcessingMode.Blocking)
	cull.listeners += EventHandler(cullChanged, ProcessingMode.Blocking)
	private val numericChanged = EventHandler((evt: NumericPropertyChangeEvent) => updateShape(), ProcessingMode.Blocking)
	width.listeners += numericChanged
	height.listeners += numericChanged
	depth.listeners += numericChanged
	
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
		}
		
		updateShape()
	}
	
	private def cullChanged(evt: PropertyChangeEvent[Face]) = updateShape()
	
	private def updateShape() = {
		val data = MutableShapeData(ShapeMode.Quads, 24)
		data.cull = cull()
		
		val w = width() / 2.0
		val h = height() / 2.0
		data.vertices = List(	// Front Face
								Vec3(-w, -h, w),		// Bottom-Left
								Vec3(w, -h, w),			// Bottom-Right
								Vec3(w, h, w),			// Top-Right
								Vec3(-w, h, w),			// Top-Left
								// Left Face
								Vec3(-w, -h, -w),		// Bottom-Left
								Vec3(-w, -h, w),		// Bottom-Right
								Vec3(-w, h, w),			// Top-Right
								Vec3(-w, h, -w),		// Top-Left
								// Back Face
								Vec3(-w, h, -w),		// Top-Left
								Vec3(w, h, -w),			// Top-Right
								Vec3(w, -h, -w),		// Bottom-Right
								Vec3(-w, -h, -w),		// Bottom-Left
								// Right Face
								Vec3(w, h, -w),			// Top-Left
								Vec3(w, h, w),			// Top-Right
								Vec3(w, -h, w),			// Bottom-Right
								Vec3(w, -h, -w),		// Bottom-Left
								// Top Face
								Vec3(-w, h, w),			// Bottom-Left
								Vec3(w, h, w),			// Bottom-Right
								Vec3(w, h, -w),			// Top-Right
								Vec3(-w, h, -w),		// Top-Left
								// Bottom Face
								Vec3(-w, -h, -w),		// Top-Left
								Vec3(w, -h, -w),		// Top-Right
								Vec3(w, -h, w),			// Bottom-Right
								Vec3(-w, -h, w)			// Bottom-Left
							 )
		
		texture match {
			case null =>
			case t => {
				val x1 = 0.0
				val y1 = 0.0
				val x2 = 1.0
				val y2 = 1.0
				
				data.textureCoords = List(	// Front Face
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
							 
		shape(data)
	}
}