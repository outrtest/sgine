package org.sgine.math.mutable

object Vector3 {
	def apply(x: Double, y: Double, z: Double) = {
		val v = new Vector3()
		v.x = x
		v.y = y
		v.z = z
		
		v
	}
	
	def apply() = new Vector3()
	
	def apply(tuple3 : (Double,Double,Double) ) : Vector3 = Vector3(tuple3._1, tuple3._2, tuple3._3)
	
	/**
	 * A vector with coordinates (0,0,0)
	 */
	def Zero = Vector3(0, 0, 0)

	/**
	 * A vector with coordinates (0,0,0)
	 */
	def Origo = Zero

	/**
	 * A unit vector in the direction of the x axis.	Coordinates (1,0,0)
	 */
	def UnitX = Vector3(1, 0, 0)

	/**
	 * A unit vector in the direction of the y axis.	Coordinates (0,1,0)
	 */
	def UnitY = Vector3(0, 1, 0)

	/**
	 * A unit vector in the direction of the z axis.	Coordinates (0,0,1)
	 */
	def UnitZ = Vector3(0, 0, 1)

	/**
	 * A vector with coordinates (1,1,1)
	 */
	// TODO: Is there some more correct name for this?
	def Ones = Vector3(1, 1, 1)
}

class Vector3 protected() extends org.sgine.math.Vector3 {
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
	
	def set(x: Double, y: Double, z: Double) = {
		this.x = x
		this.y = y
		this.z = z
		
		this
	}
	
	def normalize() = {
		val li = 1.0 / Math.sqrt(x * x + y * y + z * z)
		x *= li
		y *= li
		z *= li
		
		this
	}
}