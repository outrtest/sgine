package org.sgine.ui

import java.util.concurrent.atomic.AtomicBoolean

import org.sgine.bounding.BoundingObject

import org.sgine.core._

import org.sgine.event.Event
import org.sgine.event.EventHandler

import org.sgine.property.AdvancedProperty
import org.sgine.property.event.PropertyChangeEvent

import org.sgine.render.Renderer
import org.sgine.render.Texture
import org.sgine.render.shape._

import simplex3d.math.doublem.renamed._

trait ShapeComponent extends Component {
	protected val _cull = new AdvancedProperty[Face](Face.Back, this)
	protected val _mode = new AdvancedProperty[ShapeMode](ShapeMode.Triangles, this)
	protected val _vertices = new AdvancedProperty[Seq[Vec3]](Nil, this)
	protected val _material = new AdvancedProperty[Material](null, this)
	protected val _colors = new AdvancedProperty[Seq[Color]](null, this)
	protected val _texcoords = new AdvancedProperty[Seq[Vec2]](null, this)
	protected val _normal = new AdvancedProperty[Seq[Vec3]](null, this)
	
	private val dirty = new AtomicBoolean(false)
	
	protected var texture: Texture = _
	protected val shape = MutableShape()
	
	configureListeners()
	
	private def configureListeners() = {
		val handler = EventHandler(shapeChanged, ProcessingMode.Blocking)
		_cull.listeners += handler
		_mode.listeners += handler
		_vertices.listeners += handler
		_material.listeners += handler
		_colors.listeners += handler
		_texcoords.listeners += handler
		_normal.listeners += handler
	}
	
	override def update(renderer: Renderer) = {
		super.update(renderer)
		
		if (dirty.compareAndSet(true, false)) {
			updateShape()
		}
		
		shape.update()
	}
	
	def drawComponent() = {
		if (texture != null) {
			texture.bind()
		} else {
			Texture.unbind()
		}
		shape.render()
	}
	
	protected def updateShape() = {
		if (_vertices() != Nil) {
			shape.mode = _mode()
			shape.cull = _cull()
			shape.material = _material()
			shape.vertex = VertexData(_vertices())
			_colors() match {
				case null =>
				case c => shape.color = ColorData(c)
			}
			_texcoords() match {
				case null =>
				case t => shape.texture = TextureData(t)
			}
			_normal() match {
				case null =>
				case n => shape.normal = NormalData(n)
			}
		}
	}
	
	private def shapeChanged(evt: PropertyChangeEvent[_]) = {
		dirty.set(true)
	}
}

object ShapeComponent {
	def apply() = new ShapeComponentImpl()
}

class ShapeComponentImpl extends ShapeComponent		// TODO: make BoundingObject