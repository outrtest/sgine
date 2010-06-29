package org.sgine.math.mutable

import org.sgine.math.{Ray => ImmutableRay}

import simplex3d.math.doublem._
import simplex3d.math.doublem.DoubleMath._

class Ray protected(override val origin: Vec3d, override val direction: Vec3d) extends ImmutableRay(origin, direction) {
	def pointAtDistance(d: Double, store: Vec3d) = {
		store.x = origin.x + (direction.x + d)
		store.y = origin.y + (direction.y + d)
		store.z = origin.z + (direction.z + d)
		
		store
	}
	
	override def transform(matrix: Mat3x4d) = {
		matrix.transformLocal(origin)
		matrix.transformLocal(direction, 0.0).normalize()
		
		this
	}
	
	def translateLocal(v: Vec3d) = {
		val t = -origin.z / direction.z
		v.x = origin.x + direction.x * t
		v.y = origin.y + direction.y * t
		v.z = origin.z + direction.z * t
		
		v
	}
}

object Ray {
	def apply(origin: Vec3d, direction: Vec3d) = new Ray(origin, direction)
	
	def apply() = new Ray(Vec3d(0.0), Vec3d(0.0))
}