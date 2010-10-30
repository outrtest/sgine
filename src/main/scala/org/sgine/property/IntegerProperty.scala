package org.sgine.property

import org.sgine.event.EventHandler
import org.sgine.event.Listenable

class IntegerProperty (value: Int = 0, parent: Listenable = null, name: String = null, dependency: Function0[Int] = null, filter: Int => Int = null, filterType: FilterType = FilterType.Modify, listener: EventHandler = null)(override protected implicit val manifest: Manifest[Int]) extends AdvancedProperty[Int](value, parent, name, dependency, filter, filterType, listener) {
	def +=(value: Int) = {
		apply(apply() + value)
	}
	
	def -=(value: Int) = {
		apply(apply() - value)
	}

	object flag {
		def set(value: Int) = apply(apply() | value)
		
		def remove(value: Int) = apply(apply() ^ value)
		
		def has(value: Int) = (apply() & value) == value
	}
}