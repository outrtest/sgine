package org.sgine.property

class DelegateProperty[T](getter: Function0[T], setter: Function1[T, Unit] = null) extends Property[T] {
	def apply() = getter()
	
	def apply(value: T): Property[T] = {
		if (setter != null) {
			setter(value)
		} else {
			throw new UnsupportedOperationException("No setter is assigned to this DelegateProperty")
		}
		
		this;
	}
}
