package org.sgine.render.shape

import org.sgine.core._

import simplex3d.math.doublem.renamed._

trait ShapeData {
	def mode: ShapeMode
	def cull: Face
	def material: Material
	def length: Int

	def vertex(index: Int): Vec3
	
	def color(index: Int): Color
	def hasColor: Boolean
	
	def texture(index: Int): Vec2
	def hasTexture: Boolean
	
	def normal(index: Int): Vec3
	def hasNormal: Boolean
	
	def bytes = {
		var b = length * (3 * 4)
		if (hasNormal) {
			b += length * (3 * 4)
		}
		if (hasColor) {
			b += length * (4 * 4)
		}
		if (hasTexture) {
			b += length * (2 * 4)
		}
		
		b
	}
}

object ShapeData {
	def apply(mode: ShapeMode, vertices: Seq[Vec3], cull: Face = Face.Back, material: Material = null, colors: Seq[Color] = null, texture: Seq[Vec2] = null, normal: Seq[Vec3] = null) = {
		ShapeDataImpl(mode, cull, material, vertices, colors, texture, normal)
	}
}

case class ShapeDataImpl(mode: ShapeMode,
						 cull: Face,
						 material: Material,
						 _vertices: Seq[Vec3],
						 _colors: Seq[Color],
						 _texture: Seq[Vec2],
						 _normal: Seq[Vec3]) extends ShapeData {
	val length = _vertices.length
	
	def vertex(index: Int) = _vertices(index)
	def color(index: Int) = _colors(index)
	val hasColor = _colors != null
	def texture(index: Int) = _texture(index)
	val hasTexture = _texture != null
	def normal(index: Int) = _normal(index)
	val hasNormal = _normal != null
}