package org.sgine.property.bind

import org.sgine.property.BindingProperty

class DirectBinding[T](val property: BindingProperty[T]) extends Binding[T] {
	def :=(value: T) = property := value
}