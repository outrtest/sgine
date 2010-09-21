package org.sgine.property

trait FilteredProperty[T] extends Property[T] {
	def filter: T => T
	def filterType: FilterType
	
	abstract override def apply(value: T): Property[T] = {
		if ((filterType == FilterType.Modify) && (filter != null)) {
			super.apply(filter(value))
		} else {
			super.apply(value)
		}
		
		this
	}
	
	abstract override def apply() = {
		val value = super.apply()
		
		if ((filterType == FilterType.Retrieve) && (filter != null)) {
			filter(value)
		} else {
			value
		}
	}
}