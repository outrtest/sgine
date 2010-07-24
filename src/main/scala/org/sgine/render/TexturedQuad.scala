package org.sgine.render

import java.util.concurrent.atomic.AtomicBoolean

import org.lwjgl.opengl.GL11._

import org.sgine.core.Face

import org.sgine.event.EventHandler
import org.sgine.event.ProcessingMode

import org.sgine.property.AdvancedProperty
import org.sgine.property.NumericProperty
import org.sgine.property.NumericPropertyChangeEvent
import org.sgine.property.event.PropertyChangeEvent

import org.sgine.render.shape.MutableShapeData
import org.sgine.render.shape.Shape

import simplex3d.math.doublem.renamed._

class TexturedQuad extends Function0[Unit] {
	val texture = new AdvancedProperty[Texture](null)
	val x = new NumericProperty(0.0)
	val y = new NumericProperty(0.0)
	val width = new NumericProperty(0.0)
	val height = new NumericProperty(0.0)
	val cull = new AdvancedProperty[Face](Face.Back)
	
	private val shape = Shape()
	private val dirty = new AtomicBoolean(false)
	
	private val changeHandler = EventHandler(propChanged, ProcessingMode.Blocking)
	width.listeners += changeHandler
	height.listeners += changeHandler
	cull.listeners += EventHandler(cullChanged, ProcessingMode.Blocking)
	
	def this(texture: Texture) = {
		this()
		
		this.texture := texture
		width := texture.width
		height := texture.height
	}
	
	def isValid = width() > 0.0 && height() > 0.0
	
	def apply() = {
		update()
		render()
	}
	
	def update() = {
		if (dirty.compareAndSet(true, false)) {
			if (isValid) {
				val data = MutableShapeData(GL_QUADS, 4)
				data.cull = cull()
				
				texture() match {
					case null =>
					case t => {		// Assign texture coords
						val x1 = x() / t.width
						val y1 = y() / t.height
						val x2 = (x() + width()) / t.width
						val y2 = (y() + height()) / t.height
						data.textureCoords = List(Vec2(x1, y2), Vec2(x2, y2), Vec2(x2, y1), Vec2(x1, y1))
					}
				}
				
				val w = width() / 2.0
				val h = height() / 2.0
				data.vertices = List(Vec3(-w, -h, 0.0),
									 Vec3(w, -h, 0.0),
									 Vec3(w, h, 0.0),
									 Vec3(-w, h, 0.0))
				
				shape(data)
			}
		}
	}
	
	def render() = {
		// Bind texture
		texture() match {
			case null =>
			case t => t.bind()
		}
		
		shape.apply()
	}
	
	private def propChanged(evt: NumericPropertyChangeEvent) = {
		dirty.set(true)
	}
	
	private def cullChanged(evt: PropertyChangeEvent[Face]) = {
		dirty.set(true)
	}
}