package org.sgine.property.bind

import org.sgine.property.BindingProperty

class DirectBinding[T, O](val property: BindingProperty[T], converter: (O) => T) extends Binding[O] {
	def :=(value: O) = property := converter(value)
}