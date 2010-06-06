package org.sgine.bounding

import org.sgine.math.Vector3

trait Bounding {
	def within(v: Vector3): Boolean
}