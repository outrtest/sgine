package org.sgine.math.mutable

import org.sgine.math.{Ray => ImmutableRay, Matrix4 => ImmutableMatrix4}

class Ray protected(override val origin: Vector3, override val direction: Vector3) extends ImmutableRay(origin, direction) {
	def pointAtDistance(d: Double, store: Vector3) = {
		store.x = origin.x + (direction.x + d)
		store.y = origin.y + (direction.y + d)
		store.z = origin.z + (direction.z + d)
		
		store
	}
	
	override def transform(matrix: ImmutableMatrix4) = {
		matrix.transformLocal(origin)
		matrix.transformLocal(direction, 0.0).normalize()
		
		this
	}
	
	def translateLocal(v: Vector3) = {
		val t = -origin.z / direction.z
		v.x = origin.x + direction.x * t
		v.y = origin.y + direction.y * t
		v.z = origin.z + direction.z * t
		
		v
	}
}

object Ray {
	def apply(origin: Vector3, direction: Vector3) = new Ray(origin, direction)
	
	def apply() = new Ray(Vector3(), Vector3())
}