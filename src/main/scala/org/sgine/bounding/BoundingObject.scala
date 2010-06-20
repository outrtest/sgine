package org.sgine.bounding

import org.sgine.property.DelegateProperty

trait BoundingObject {
	val bounding = new DelegateProperty(() => _bounding)
	
	protected def _bounding: Bounding
}