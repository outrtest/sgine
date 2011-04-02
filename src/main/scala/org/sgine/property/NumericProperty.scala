package org.sgine.property

import org.sgine.event.EventHandler
import org.sgine.event.Listenable

class NumericProperty (value: Double = 0.0, parent: Listenable = null, name: String = null, dependency: Function0[Double] = null, filter: Double => Double = null, filterType: FilterType = FilterType.Modify, listener: EventHandler = null)(override implicit val manifest: Manifest[Double]) extends AdvancedProperty[Double](value, parent, name, dependency, filter, filterType, listener) {
	def +=(value: Double) = {
		apply(apply() + value)
	}
	
	def -=(value: Double) = {
		apply(apply() - value)
	}
}