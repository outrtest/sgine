package org.sgine.math

class Ray protected(_origin: Vector3, _direction: Vector3) {
	def origin = _origin
	def direction = _direction
	
	def pointAtDistance(d: Double) = Vector3(origin.x + (direction.x * d), origin.y + (direction.y * d), origin.z + (direction.z * d))
	
	def transform(m: Matrix4) = Ray(m.transform(origin), m.transform(direction, 0.0).normalized)
	
	def translate(): Vector3 = {
		val t = -origin.z / direction.z
		Vector3(
				origin.x + direction.x * t,
				origin.y + direction.y * t,
				origin.z + direction.z * t
				)
	}
	
	override def toString() = "Ray(origin = " + origin + ", direction = " + direction + ")"
}

object Ray {
	def apply(origin: Vector3, direction: Vector3) = new Ray(origin, direction)
}