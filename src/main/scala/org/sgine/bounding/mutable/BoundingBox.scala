package org.sgine.bounding.mutable

import org.sgine.bounding.{BoundingBox => ImmutableBoundingBox}

class BoundingBox protected() extends ImmutableBoundingBox {
	override def width_=(_width: Double) = this._width = _width
	override def height_=(_height: Double) = this._height = _height
	override def depth_=(_depth: Double) = this._depth = _depth
	
	def set(width: Double, height: Double, depth: Double) = {
		this.width = width
		this.height = height
		this.depth = depth
	}
}

object BoundingBox {
	def apply() = new BoundingBox()
	
	def apply(width: Double, height: Double, depth: Double) = {
		val b = new BoundingBox()
		b.width = width
		b.height = height
		b.width = width
		
		b
	}
}