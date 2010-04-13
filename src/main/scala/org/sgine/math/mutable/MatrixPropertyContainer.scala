package org.sgine.math.mutable

import org.sgine.property.ImmutableProperty

trait MatrixPropertyContainer {
	val matrix = new ImmutableProperty[Matrix4](Matrix4().identity())
}