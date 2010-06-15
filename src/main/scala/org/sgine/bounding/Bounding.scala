package org.sgine.bounding

import org.sgine.math.Vector3

trait Bounding {
	def width: Double
	def height: Double
	def depth: Double
	
	def within(v: Vector3): Boolean
}