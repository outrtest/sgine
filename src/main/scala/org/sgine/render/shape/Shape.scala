package org.sgine.render.shape

import org.sgine.core.Face
import org.sgine.core.Material

import org.sgine.render.shape.renderer.ShapeRenderer
import org.sgine.render.shape.renderer.lwjgl.LWJGLImmediateModeShapeRenderer
import org.sgine.render.shape.renderer.lwjgl.LWJGLVBOShapeRenderer

import scala.math._

trait Shape extends Function0[Unit] {
	protected lazy val renderer = createRenderer()
	
	def cull: Face
	def material: Material
	def mode: ShapeMode
	def vertex: VertexData
	def color: ColorData
	def texture: TextureData
	def normal: NormalData
	
	def vertexLatest = vertex
	def colorLatest = color
	def textureLatest = texture
	def normalLatest = normal
	
	def hasColor = color != null
	def hasTexture = texture != null
	def hasNormal = normal != null
	
	private lazy val initialized = initializeRenderer()
	
	private def initializeRenderer() = {
		renderer.update(this, true, true, true, true)
		
		true
	}
	
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
	
	def apply() = {
		if (initialized) {
			render()
		}
	}
	
	def render() = {
		// Render
		val dataVertex = vertex
		if (dataVertex != null) {
			renderer.render(this)
		}
	}
	
	def size = {
		var minX = 0.0
		var maxX = 0.0
		var minY = 0.0
		var maxY = 0.0
		var minZ = 0.0
		var maxZ = 0.0
		
		for (v <- vertexLatest.data) {
			minX = min(v.x, minX)
			maxX = max(v.x, maxX)
			minY = min(v.y, minY)
			maxY = max(v.y, maxY)
			minZ = min(v.z, minZ)
			maxZ = max(v.z, maxZ)
		}
		
		simplex3d.math.doublem.renamed.Vec3(maxX - minX, maxY - minY, maxZ - minZ)
	}
}

object Shape {
}

case class ImmutableShape(vertex: VertexData,
						  color: ColorData = null,
						  texture: TextureData = null,
						  normal: NormalData = null,
						  cull: Face = Face.Back,
						  material: Material = null,
						  mode: ShapeMode = ShapeMode.Triangles) extends Shape 