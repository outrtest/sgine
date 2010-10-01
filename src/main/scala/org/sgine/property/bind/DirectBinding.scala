package org.sgine.property.bind

import org.sgine.property.BindingProperty

class DirectBinding[T, O](val property: BindingProperty[T], converter: (O) => Option[T]) extends Binding[O] {
	def :=(value: O) = converter(value) match {
		case Some(v) => property := v
		case None =>
	}
}