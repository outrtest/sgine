package org.sgine.ui.ext

import org.sgine.event.Listenable

import org.sgine.property.AdvancedProperty

class NumericProperty(value: Double, parent: Listenable) extends AdvancedProperty[Double](value, parent) {
	def +=(v: Double) = this := this() + v
	
	def -=(v: Double) = this := this() - v
}