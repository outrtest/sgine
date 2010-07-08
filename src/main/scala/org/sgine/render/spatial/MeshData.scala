package org.sgine.render.spatial

import org.sgine.core.Color

import simplex3d.math.doublem.Vec2d
import simplex3d.math.doublem.Vec3d

trait MeshData {
	def mode: Int
	def length: Int
	def color(index: Int): Color
	def vertex(index: Int): Vec3d
	def texture(index: Int): Vec2d
	def hasColor: Boolean
	def hasTexture: Boolean
	def bytes = {
		var b = length * (3 * 4)
		if (hasColor) {
			b += length * (4 * 4)
		}
		if (hasTexture) {
			b += length * (2 * 4)
		}
		
		b
	}
}