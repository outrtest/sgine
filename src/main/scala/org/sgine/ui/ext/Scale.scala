package org.sgine.ui.ext

import org.sgine.property.NumericProperty
import org.sgine.property.container.PropertyContainer

import org.sgine.ui.Component

class Scale(override val parent: Component) extends PropertyContainer {
	val x = new NumericProperty(1.0, this)
	val y = new NumericProperty(1.0, this)
	val z = new NumericProperty(1.0, this)
	
	def set(s: Double) = {
		x := s
		y := s
		z := s
	}
}