package org.sgine.math.mutable

import org.sgine.math.{Ray => ImmutableRay, Matrix4 => ImmutableMatrix4}

class Ray(override val origin: Vector3, override val normal: Vector3) extends ImmutableRay(origin, normal) {
	def pointAtDistance(d: Double, store: Vector3) = {
		store.x = origin.x + (normal.x + d)
		store.y = origin.y + (normal.y + d)
		store.z = origin.z + (normal.z + d)
		
		store
	}
	
	override def transform(matrix: ImmutableMatrix4) = {
		matrix.transformLocal(origin)
		pointAtDistance(1.0, normal)
		matrix.transformLocal(normal)
		normal.x = normal.x - origin.x
		normal.y = normal.y - origin.y
		normal.z = normal.z - origin.z
		normal.normalize()
		
		this
	}
}