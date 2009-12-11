package org.sgine.math.mutable

class Vector3 extends org.sgine.math.Vector3 {
	override def x_=(_x: Double) = this._x = _x
	override def y_=(_y: Double) = this._y = _y
	override def z_=(_z: Double) = this._z = _z
	
	def +=(other: org.sgine.math.Vector3) = {
		x += other.x
		y += other.y
		z += other.z
		
		this
	}
	
	def -=(other: org.sgine.math.Vector3) = {
		x -= other.x
		y -= other.y
		z -= other.z
		
		this
	}
	
	def *=(other: org.sgine.math.Vector3) = {
		x *= other.x
		y *= other.y
		z *= other.z
		
		this
	}
	
	def /=(other: org.sgine.math.Vector3) = {
		x /= other.x
		y /= other.y
		z /= other.z
		
		this
	}
	
	def +=(x: Double, y: Double, z: Double) = {
		this.x += x
		this.y += y
		this.z += z
		
		this
	}
	
	def -=(x: Double, y: Double, z: Double) = {
		this.x -= x
		this.y -= y
		this.z -= z
		
		this
	}
}