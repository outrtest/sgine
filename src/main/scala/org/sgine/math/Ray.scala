package org.sgine.math

import simplex3d.math.doublem._
import simplex3d.math.doublem.DoubleMath._

class Ray protected(val origin: Vec3d, val direction: Vec3d) {
	def pointAtDistance(d: Double) = Vec3d(
			origin.x + (direction.x + d),
			origin.y + (direction.y + d),
			origin.z + (direction.z + d)
		)
		
	def transform(m: inMat3x4d) = new Ray(
			m.transformPoint(origin),
			normalize(m.transformVector(direction))
		)
	
	def translate(): Vec3d = {
		val t = -origin.z / direction.z
		Vec3d(
				origin.x + direction.x * t,
				origin.y + direction.y * t,
				origin.z + direction.z * t
			)
	}
}

object Ray {
	def apply(origin: Vec3d = Vec3d(0.0), direction: Vec3d = Vec3d(0.0)) = new Ray(origin, direction)
}