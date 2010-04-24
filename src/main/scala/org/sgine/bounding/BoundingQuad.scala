package org.sgine.bounding

import org.sgine.math.Vector3

class BoundingQuad protected() extends Bounding {
	protected var _width: Double = 0.0
	protected var _height: Double = 0.0
	
	def width = _width
	protected def width_=(_width: Double) = this._width = _width
	def height = _height
	protected def height_=(_height: Double) = this._height = _height
	
	def within(v: Vector3) = v.x >= width / -2.0 && v.x <= width / 2.0 && v.y >= height / -2.0 && v.y <= height / 2.0
}

object BoundingQuad {
	def apply(width: Double, height: Double) = {
		val bp = new BoundingQuad()
		bp._width = width
		bp._height = height
		
		bp
	}
}