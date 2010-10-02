package org.sgine.bounding

import org.sgine.event.Listenable

import org.sgine.property.AdvancedProperty

trait BoundingObject extends Listenable {
	val bounding = new AdvancedProperty[Bounding](null, this)
}