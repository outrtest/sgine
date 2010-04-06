package org.sgine.math

class Ray protected(_origin: Vector3, _normal: Vector3) {
	def origin = _origin
	def normal = _normal
	
	def pointAtDistance(d: Double) = Vector3(origin.x + (normal.x + d), origin.y + (normal.y + d), origin.z + (normal.z + d))
	
	def transform(matrix: Matrix4) = {
		val tOrigin = matrix.transform(origin)
		val store = matrix.transform(pointAtDistance(1.0))
		val tNormal = Vector3(store.x - tOrigin.x, store.y - tOrigin.y, store.z - tOrigin.z)
		Ray(tOrigin, tNormal)
	}
}

object Ray {
	def apply(origin: Vector3, normal: Vector3) = new Ray(origin, normal)
}