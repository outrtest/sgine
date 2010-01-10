package org.sgine.visual

import org.sgine.property._
import org.sgine.property.container._
import org.sgine.visual.bound._

trait Bounding extends PropertyContainer {
	val volume = new AdvancedProperty[BoundingVolume](null, this)
}