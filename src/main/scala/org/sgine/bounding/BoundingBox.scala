package org.sgine.bounding

import simplex3d.math._
import simplex3d.math.doublem._

class BoundingBox protected() extends Bounding {
	protected var _width = 0.0
	protected var _height = 0.0
	protected var _depth = 0.0
	
	def width = _width
	protected def width_=(_width: Double) = this._width = _width
	def height = _height
	protected def height_=(_height: Double) = this._height = _height
	def depth = _depth
	protected def depth_=(_depth: Double) = this._depth = _depth
	
	def within(v: Vec3d) = v.x >= width / -2.0 && v.x <= width / 2.0 && v.y >= height / -2.0 && v.y <= height / 2.0 && v.z >= depth / -2.0 && v.z <= depth / 2.0
	
	override def toString() = "BoundingBox(" + width + "x" + height + "x" + depth + ")"
}

object BoundingBox {
	def apply(width: Double, height: Double, depth: Double) = {
		val b = new BoundingBox()
		b._width = width
		b._height = height
		b._depth = depth
		
		b
	}
}