package org.sgine.ui.skin

import java.util.concurrent.atomic.AtomicBoolean

import org.sgine.core.Face
import org.sgine.core.Resource
import org.sgine.core.ProcessingMode

import org.sgine.event.EventHandler
import org.sgine.event.Listenable

import org.sgine.property.AdvancedProperty
import org.sgine.property.NumericProperty
import org.sgine.property.event.PropertyChangeEvent

import org.sgine.render.Texture
import org.sgine.render.TextureManager
import org.sgine.render.shape.MutableShape
import org.sgine.render.shape.ShapeMode
import org.sgine.render.shape.TextureData
import org.sgine.render.shape.VertexData

import simplex3d.math.doublem.renamed._

class Scale9Skin extends Skin {
	val source = new AdvancedProperty[Resource](null, this)
	val x1 = new NumericProperty(0.0, this)
	val y1 = new NumericProperty(0.0, this)
	val x2 = new NumericProperty(0.0, this)
	val y2 = new NumericProperty(0.0, this)
	
	private val dirty = new AtomicBoolean()
	
	protected var texture: Texture = _
	protected val shape = MutableShape()
	
	Listenable.listenTo(EventHandler(invalidateEvent, ProcessingMode.Blocking),
						x1,
						y1,
						x2,
						y2)
	
	source.listeners += EventHandler(resourceChanged, ProcessingMode.Blocking)
	
	def draw() = {
		if (dirty.get) {
			dirty.set(false)
			updateShape()
		}
		
		if (texture != null) {
			texture.bind()
		} else {
			Texture.unbind()
		}
		shape()		// Do we need to always call this?
		shape.render()
	}
	
	def apply(source: Resource, x1: Double, y1: Double, x2: Double, y2: Double) = {
		this.source := source
		this.x1 := x1
		this.y1 := y1
		this.x2 := x2
		this.y2 := y2
	}
	
	def invalidate() = {
		dirty.set(true)
	}
	
	def resized() = {
		invalidate()
	}
	
	private def invalidateEvent(evt: PropertyChangeEvent[_]) = invalidate()
	
	private def resourceChanged(evt: PropertyChangeEvent[_]) = texture = TextureManager(source())
	
	private def updateShape() = {
		if (isValid) {
			// Avoid asynchronous changes while we're working
			val texture = this.texture
			val width = component.size.width()
			val height = component.size.height()
			val x1 = this.x1()
			val y1 = this.y1()
			val x2 = this.x2()
			val y2 = this.y2()
			val z = -0.1
			
			shape.mode = ShapeMode.Quads
			shape.cull = Face.None
			
			val vertices = Scale9Skin.vertices(x1, y1, x2, y2, z, texture.width, texture.height, width, height)
			shape.vertex = VertexData(vertices)
			
			// Apply texture coords
			val texcoords = Scale9Skin.texture(x1, y1, x2, y2, texture.width, texture.height)
			shape.texture = TextureData(texcoords)
		}
	}
	
	private def isValid() = {
		texture != null &&
		x2() - x1() > 0.0 &&
		y2() - y1() > 0.0 &&
		component != null &&
		component.size.width() > 0.0 &&
		component.size.height() > 0.0
	}
}

object Scale9Skin {
	def vertices(x1: Double, y1: Double, x2: Double, y2: Double, z: Double, twidth: Double, theight: Double, width: Double, height: Double) = {
		val sx0 = width / -2.0
		val sx1 = sx0 + x1
		val sx3 = width / 2.0
		val sx2 = sx3 - (twidth - x2)
		val sy0 = height / 2.0
		val sy1 = sy0 - y1
		val sy3 = height / -2.0
		val sy2 = sy3 + (theight - y2)
		
		var l: List[Vec3] = Nil
		
		// Top-Left
		l = Vec3(sx0, sy1, z) :: l
		l = Vec3(sx1, sy1, z) :: l
		l = Vec3(sx1, sy0, z) :: l
		l = Vec3(sx0, sy0, z) :: l
		
		// Top
		l = Vec3(sx1, sy1, z) :: l
		l = Vec3(sx2, sy1, z) :: l
		l = Vec3(sx2, sy0, z) :: l
		l = Vec3(sx1, sy0, z) :: l
		
		// Top-Right
		l = Vec3(sx2, sy1, z) :: l
		l = Vec3(sx3, sy1, z) :: l
		l = Vec3(sx3, sy0, z) :: l
		l = Vec3(sx2, sy0, z) :: l
		
		// Left
		l = Vec3(sx0, sy2, z) :: l
		l = Vec3(sx1, sy2, z) :: l
		l = Vec3(sx1, sy1, z) :: l
		l = Vec3(sx0, sy1, z) :: l
		
		// Center
		l = Vec3(sx1, sy2, z) :: l
		l = Vec3(sx2, sy2, z) :: l
		l = Vec3(sx2, sy1, z) :: l
		l = Vec3(sx1, sy1, z) :: l
		
		// Right
		l = Vec3(sx2, sy2, z) :: l
		l = Vec3(sx3, sy2, z) :: l
		l = Vec3(sx3, sy1, z) :: l
		l = Vec3(sx2, sy1, z) :: l
		
		// Bottom-Left
		l = Vec3(sx0, sy3, z) :: l
		l = Vec3(sx1, sy3, z) :: l
		l = Vec3(sx1, sy2, z) :: l
		l = Vec3(sx0, sy2, z) :: l
		
		// Bottom
		l = Vec3(sx1, sy3, z) :: l
		l = Vec3(sx2, sy3, z) :: l
		l = Vec3(sx2, sy2, z) :: l
		l = Vec3(sx1, sy2, z) :: l
		
		// Bottom-Right
		l = Vec3(sx2, sy3, z) :: l
		l = Vec3(sx3, sy3, z) :: l
		l = Vec3(sx3, sy2, z) :: l
		l = Vec3(sx2, sy2, z) :: l
		
		l.reverse
	}
	
	def texture(x1: Double, y1: Double, x2: Double, y2: Double, width: Double, height: Double) = {
		val sx0 = 0.0
		val sx1 = x1 / width
		val sx2 = x2 / width
		val sx3 = 1.0
		val sy0 = 0.0
		val sy1 = y1 / height
		val sy2 = y2 / height
		val sy3 = 1.0
		
		var l: List[Vec2] = Nil
		
		// Top-Left
		l = Vec2(sx0, sy1) :: l
		l = Vec2(sx1, sy1) :: l
		l = Vec2(sx1, sy0) :: l
		l = Vec2(sx0, sy0) :: l
		
		// Top
		l = Vec2(sx1, sy1) :: l
		l = Vec2(sx2, sy1) :: l
		l = Vec2(sx2, sy0) :: l
		l = Vec2(sx1, sy0) :: l
		
		// Top-Right
		l = Vec2(sx2, sy1) :: l
		l = Vec2(sx3, sy1) :: l
		l = Vec2(sx3, sy0) :: l
		l = Vec2(sx2, sy0) :: l
		
		// Left
		l = Vec2(sx0, sy2) :: l
		l = Vec2(sx1, sy2) :: l
		l = Vec2(sx1, sy1) :: l
		l = Vec2(sx0, sy1) :: l
		
		// Center
		l = Vec2(sx1, sy2) :: l
		l = Vec2(sx2, sy2) :: l
		l = Vec2(sx2, sy1) :: l
		l = Vec2(sx1, sy1) :: l
		
		// Right
		l = Vec2(sx2, sy2) :: l
		l = Vec2(sx3, sy2) :: l
		l = Vec2(sx3, sy1) :: l
		l = Vec2(sx2, sy1) :: l
		
		// Bottom-Left
		l = Vec2(sx0, sy3) :: l
		l = Vec2(sx1, sy3) :: l
		l = Vec2(sx1, sy2) :: l
		l = Vec2(sx0, sy2) :: l
		
		// Bottom
		l = Vec2(sx1, sy3) :: l
		l = Vec2(sx2, sy3) :: l
		l = Vec2(sx2, sy2) :: l
		l = Vec2(sx1, sy2) :: l
		
		// Bottom-Right
		l = Vec2(sx2, sy3) :: l
		l = Vec2(sx3, sy3) :: l
		l = Vec2(sx3, sy2) :: l
		l = Vec2(sx2, sy2) :: l
		
		l.reverse
	}
}