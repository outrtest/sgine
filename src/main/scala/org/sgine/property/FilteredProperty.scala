package org.sgine.property

trait FilteredProperty[T] extends Property[T] {
	def filter: T => T
	
	abstract override def apply(value: T): Property[T] = {
		if (filter != null) {
			super.apply(filter(value))
		} else {
			super.apply(value)
		}
		
		this
	}
}