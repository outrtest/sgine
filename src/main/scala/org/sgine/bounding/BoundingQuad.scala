package org.sgine.bounding

import simplex3d.math._
import simplex3d.math.doublem._

class BoundingQuad protected() extends Bounding {
	protected var _width: Double = 0.0
	protected var _height: Double = 0.0
	
	def width = _width
	protected def width_=(_width: Double) = this._width = _width
	def height = _height
	protected def height_=(_height: Double) = this._height = _height
	def depth = 0.0
	
	def within(v: Vec3d) = v.x >= width / -2.0 && v.x <= width / 2.0 && v.y >= height / -2.0 && v.y <= height / 2.0
	
	override def toString() = "BoundingQuad(" + width + "x" + height + ")"
}

object BoundingQuad {
	def apply(width: Double, height: Double) = {
		val bp = new BoundingQuad()
		bp._width = width
		bp._height = height
		
		bp
	}
}