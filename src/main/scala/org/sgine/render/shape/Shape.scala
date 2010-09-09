package org.sgine.render.shape

import java.util.concurrent.atomic.AtomicReference

import org.sgine.core._

import org.sgine.render.shape.renderer._
import org.sgine.render.shape.renderer.lwjgl._

class Shape private() extends Function0[Unit] {
	var cull: Face = Face.None
	var material: Material = Material.AmbientAndDiffuse
	var mode: ShapeMode = ShapeMode.Triangles
	
	def vertex = _dataVertex.get
	def color = _dataColor.get
	def texture = _dataTexture.get
	def normal = _dataNormal.get
	
	def vertex_=(data: VertexData) = apply(data)
	def color_=(data: ColorData) = apply(data)
	def texture_=(data: TextureData) = apply(data)
	def normal_=(data: NormalData) = apply(data)
	
	private val _dataVertex = new AtomicReference[VertexData]
	private val _dataColor = new AtomicReference[ColorData]
	private val _dataTexture = new AtomicReference[TextureData]
	private val _dataNormal = new AtomicReference[NormalData]
	
	private val _updateVertex = new AtomicReference[VertexData]
	private val _updateColor = new AtomicReference[ColorData]
	private val _updateTexture = new AtomicReference[TextureData]
	private val _updateNormal = new AtomicReference[NormalData]

	private lazy val renderer = createRenderer()
	
	def dirty = _updateVertex.get != null ||
				_updateColor.get != null ||
				_updateTexture.get != null ||
				_updateNormal.get != null
	
	def hasColor = color != null
	def hasTexture = texture != null
	def hasNormal = normal != null
				
	def apply() = {
		update()
		
		render()
	}
	
	def update() = {
		if (dirty) {
			// Updates
			val dataVertex = _updateVertex.getAndSet(null)
			val dataColor = _updateColor.getAndSet(null)
			val dataTexture = _updateTexture.getAndSet(null)
			val dataNormal = _updateNormal.getAndSet(null)
			
			val vc = dataVertex match {
				case null => false
				case d => {
					_dataVertex.set(d)
					true
				}
			}
			
			val cc = dataColor match {
				case null => false
				case d => {
					_dataColor.set(d)
					true
				}
			}
			
			val tc = dataTexture match {
				case null => false
				case d => {
					_dataTexture.set(d)
					true
				}
			}
			
			val nc = dataNormal match {
				case null => false
				case d => {
					_dataNormal.set(d)
					true
				}
			}
			
			renderer.update(this, vc, cc, tc, nc)
		}
		
//		if (dataVertex != null) {
//			renderer.update(this, vertexChanged = true)
//			_dataVertex.set(dataVertex)
//		}
//		
//		val dataColor = _updateColor.getAndSet(null)
//		if (dataColor != null) {
//			renderer.update(this, colorChanged = true)
//			_dataColor.set(dataColor)
//		}
//		
//		val dataTexture = _updateTexture.getAndSet(null)
//		if (dataTexture != null) {
//			renderer.update(this, textureChanged = true)
//			_dataTexture.set(dataTexture)
//		}
//		
//		val dataNormal = _updateNormal.getAndSet(null)
//		if (dataNormal != null) {
//			renderer.update(this, normalChanged = true)
//			_dataNormal.set(dataNormal)
//		}
	}
	
	def render() = {
		// Render
		val dataVertex = _dataVertex.get
		if (dataVertex != null) {
			renderer.render(this)
		}
	}
	
	def apply(data: VertexData) = _updateVertex.set(data)
	
	def apply(data: ColorData) = _updateColor.set(data)
	
	def apply(data: TextureData) = _updateTexture.set(data)
	
	def apply(data: NormalData) = _updateNormal.set(data)
	
	private def createRenderer(): ShapeRenderer = {
		if (LWJGLVBOShapeRenderer.capable) {
			new LWJGLVBOShapeRenderer()
		} else {
			new LWJGLImmediateModeShapeRenderer()
		}
	}
	
	def bytes = {
		var b = vertex.length * (3 * 4)
		if (hasNormal) {
			b += vertex.length * (3 * 4)
		}
		if (hasColor) {
			b += vertex.length * (4 * 4)
		}
		if (hasTexture) {
			b += vertex.length * (2 * 4)
		}
		
		b
	}
}

object Shape {
	def apply() = new Shape()
}