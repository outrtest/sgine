package org.sgine.bounding.mutable

import org.sgine.bounding.{BoundingQuad => ImmutableBoundingQuad}

class BoundingQuad protected() extends ImmutableBoundingQuad {
	override def width_=(_width: Double) = this._width = _width
	override def height_=(_height: Double) = this._height = _height
}

object BoundingQuad {
	def apply() = new BoundingQuad()
	
	def apply(width: Double, height: Double) = {
		val b = new BoundingQuad()
		b.width = width
		b.height = height
		
		b
	}
}