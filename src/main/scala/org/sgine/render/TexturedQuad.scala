package org.sgine.render

import java.util.concurrent.atomic.AtomicBoolean

import org.lwjgl.opengl.GL11._

import org.sgine.core.Face
import org.sgine.core.ProcessingMode

import org.sgine.event.EventHandler
import org.sgine.event.Listenable

import org.sgine.property.AdvancedProperty
import org.sgine.property.NumericProperty
import org.sgine.property.event.PropertyChangeEvent

import org.sgine.render.shape._

import simplex3d.math.doublem.renamed._

class TexturedQuad extends Function0[Unit] {
	val texture = new AdvancedProperty[Texture](null)
	val x = new NumericProperty(0.0)
	val y = new NumericProperty(0.0)
	val width = new NumericProperty(0.0)
	val height = new NumericProperty(0.0)
	val displayWidth = new NumericProperty(0.0)
	val displayHeight = new NumericProperty(0.0)
	val cull = new AdvancedProperty[Face](Face.Back)
	
	private val shape = MutableShape()
	private val dirty = new AtomicBoolean(false)
	
	Listenable.listenTo(EventHandler(propChanged, ProcessingMode.Blocking), x, y, width, height, displayWidth, displayHeight)
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
				shape.mode = ShapeMode.Quads
				shape.cull = cull()
				
				texture() match {
					case null =>
					case t => {		// Assign texture coords
						val x1 = x() / t.width
						val y1 = y() / t.height
						val x2 = (x() + width()) / t.width
						val y2 = (y() + height()) / t.height
						shape.texture = TextureData(List(Vec2(x1, y2), Vec2(x2, y2), Vec2(x2, y1), Vec2(x1, y1)))
					}
				}
				
				val w = displayWidth() / 2.0
				val h = displayHeight() / 2.0
				shape.vertex = VertexData(List(Vec3(-w, -h, 0.0),
									 Vec3(w, -h, 0.0),
									 Vec3(w, h, 0.0),
									 Vec3(-w, h, 0.0)))
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
	
	private def propChanged(evt: PropertyChangeEvent[_]) = {
		dirty.set(true)
	}
	
	private def cullChanged(evt: PropertyChangeEvent[Face]) = {
		dirty.set(true)
	}
}