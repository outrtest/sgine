package org.sgine.math.mutable

object Vector2 {
	def apply(x: Double, y: Double) = {
		val v = new Vector2()
		v.x = x
		v.y = y
		
		v
	}
	
	def apply( tuple2 : (Double,Double) ) : Vector2 = Vector2( tuple2._1, tuple2._2 )

	/**
	 * A vector with coordinates (0,0)
	 */
	def Zero = Vector2(0, 0)

	/**
	 * A vector with coordinates (0,0)
	 */
	def Origo = Zero

	/**
	 * A unit vector in the direction of the x axis.	Coordinates (1,0)
	 */
	def UnitX = Vector2(1, 0)

	/**
	 * A unit vector in the direction of the y axis.	Coordinates (0,1)
	 */
	def UnitY = Vector2(0, 1)


	/**
	 * A vector with coordinates (1,1)
	 */
	// TODO: Is there some more correct name for this?
	def Ones = Vector2(1, 1)
}

class Vector2 protected() extends org.sgine.math.Vector2 {
	override def x_=(_x: Double) = this._x = _x
	override def y_=(_y: Double) = this._y = _y
	
		def +=(other: org.sgine.math.Vector2) = {
		x += other.x
		y += other.y
		
		this
	}
	
	def -=(other: org.sgine.math.Vector2) = {
		x -= other.x
		y -= other.y
		
		this
	}
	
	def *=(other: org.sgine.math.Vector2) = {
		x *= other.x
		y *= other.y
		
		this
	}
	
	def /=(other: org.sgine.math.Vector2) = {
		x /= other.x
		y /= other.y
		
		this
	}
	
	def +=(x: Double, y: Double) = {
		this.x += x
		this.y += y
		
		this
	}
	
	def -=(x: Double, y: Double) = {
		this.x -= x
		this.y -= y
		
		this
	}
}
