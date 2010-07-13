package org.sgine.visual

import org.sgine.property._
import org.sgine.property.container._

import simplex3d.math._
import simplex3d.math.doublem._

trait Spatial extends PropertyContainer {
	val location = new AdvancedProperty[Vec3d](null, this)
}