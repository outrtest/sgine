package org.sgine.visual

import org.sgine.math._
import org.sgine.property._
import org.sgine.property.container._

trait Spatial extends PropertyContainer {
	val location = new AdvancedProperty[Vector3](null, this)
}