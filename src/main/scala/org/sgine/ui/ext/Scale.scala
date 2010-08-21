package org.sgine.ui.ext

import org.sgine.property.NumericProperty
import org.sgine.property.container.PropertyContainer

import org.sgine.ui.Component

class Scale(override val parent: Component) extends PropertyContainer {
	val x = new NumericProperty(1.0, this)
	val y = new NumericProperty(1.0, this)
	val z = new NumericProperty(1.0, this)
	
	def apply(value: Double) = {
		x := value
		y := value
		z := value
	}
	
	def apply(x: Double, y: Double, z: Double) = {
		this.x := x
		this.y := y
		this.z := z
	}
}