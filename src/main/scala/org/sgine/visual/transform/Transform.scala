package org.sgine.visual.transform

import org.sgine.math._
import org.sgine.property._
import org.sgine.property.container._

trait Transform extends PropertyContainer {
	val rotation = new AdvancedProperty[Vector3](Vector3.Zero, this)
	val scale = new AdvancedProperty[Vector3](Vector3.Zero, this)
	val translation = new AdvancedProperty[Vector3](Vector3.Zero, this)
}