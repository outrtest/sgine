package org.sgine.render.shape

import org.sgine.core.Color
import org.sgine.core.Face

import org.sgine.render.Material

import simplex3d.math.doublem.Vec2d
import simplex3d.math.doublem.Vec3d

trait ShapeData {
	def mode: ShapeMode
	def cull: Face
	def material: Material
	def length: Int

	def vertex(index: Int): Vec3d
	
	def color(index: Int): Color
	def hasColor: Boolean
	
	def texture(index: Int): Vec2d
	def hasTexture: Boolean
	
	def normal(index: Int): Vec3d
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