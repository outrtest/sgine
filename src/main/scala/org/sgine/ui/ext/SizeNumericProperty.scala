package org.sgine.ui.ext

import org.sgine.core.SizeMode

import org.sgine.event.Listenable

import org.sgine.property.AdvancedProperty
import org.sgine.property.NumericProperty

class SizeNumericProperty(value: Double, parent: Listenable) extends NumericProperty(value, parent) {
	val mode = new AdvancedProperty[SizeMode](SizeMode.Auto, this)
	
	def apply(value: Double, mode: SizeMode) = {
		this := value
		this.mode := mode
	}
}