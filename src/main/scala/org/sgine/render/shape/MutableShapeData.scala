package org.sgine.render.shape

import org.sgine.core._

import simplex3d.math.doublem._

class MutableShapeData private(val mode: ShapeMode, val length: Int) extends ShapeData {
	var cull: Face = Face.None
	val material = Material.AmbientAndDiffuse
	def color(index: Int) = colors(index)
	def vertex(index: Int) = vertices(index)
	def texture(index: Int) = textureCoords(index)
	def normal(index: Int) = null
	def hasColor = colors != null
	def hasTexture = textureCoords != null
	def hasNormal = normalCoords != null
	
	var vertices: Seq[Vec3d] = null
	var colors: Seq[Color] = null
	var textureCoords: Seq[Vec2d] = null
	var normalCoords: Seq[Vec3d] = null
}

object MutableShapeData {
	def apply(mode: ShapeMode, length: Int) = new MutableShapeData(mode, length)
}