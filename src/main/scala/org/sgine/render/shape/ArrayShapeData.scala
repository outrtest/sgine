package org.sgine.render.shape

import org.sgine.core.Color

import org.sgine.render.Face
import org.sgine.render.Material

import simplex3d.math.doublem._

class ArrayShapeData private(val mode: Int, val length: Int) extends ShapeData {
	val cull = Face.Front
	val material = Material.AmbientAndDiffuse
	def color(index: Int) = colors(index)
	def vertex(index: Int) = vertices(index)
	def texture(index: Int) = null
	def normal(index: Int) = null
	val hasColor = colors != null
	val hasTexture = false
	val hasNormal = false
	
	val vertices = new Array[Vec3d](length)
	var colors: Array[Color] = _
}

object ArrayShapeData {
	def apply(mode: Int, length: Int) = new ArrayShapeData(mode, length)
}