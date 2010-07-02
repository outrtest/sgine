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
	
	override def transform(matrix: inMat3x4d) = {
		origin := matrix.transformVector(origin)
		direction := normalize(matrix.transformVector(direction))
		
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
	def apply(origin: Vec3d = Vec3d(0.0), direction: Vec3d = Vec3d(0.0)) = new Ray(origin, direction)
}