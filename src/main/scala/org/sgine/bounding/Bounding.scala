package org.sgine.bounding

import simplex3d.math._
import simplex3d.math.doublem._

trait Bounding {
	def width: Double
	def height: Double
	def depth: Double
	
	def within(v: Vec3d): Boolean
}