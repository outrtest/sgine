package org.sgine.visual

import org.sgine.event._
import org.sgine.property._
import org.sgine.property.container._

trait Shape extends PropertyContainer {
	val mesh = new AdvancedProperty[Mesh](null, this)
	val material = new AdvancedProperty[Material](null, this)
}