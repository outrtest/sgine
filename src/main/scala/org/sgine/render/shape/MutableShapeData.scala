package org.sgine.render.shape

import org.sgine.core.Color
import org.sgine.core.Face

import org.sgine.render.Material

import simplex3d.math.doublem._

class ArrayShapeData private(val mode: Int, val length: Int) extends ShapeData {
	val cull = Face.None
	val material = Material.AmbientAndDiffuse
	def color(index: Int) = colors(index)
	def vertex(index: Int) = vertices(index)
	def texture(index: Int) = null
	def normal(index: Int) = null
	def hasColor = colors != null
	val hasTexture = false
	val hasNormal = false
	
	var vertices: Seq[Vec3d] = null
	var colors: Seq[Color] = null
}

object ArrayShapeData {
	def apply(mode: Int, length: Int) = new ArrayShapeData(mode, length)
}