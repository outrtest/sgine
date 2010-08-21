package org.sgine.ui.ext

import org.sgine.property.container.PropertyContainer

import org.sgine.ui.Component

import org.sgine.property.NumericProperty

class Location(override val parent: Component) extends PropertyContainer {
	val x = new HorizontalNumericProperty(0.0, this)
	val y = new VerticalNumericProperty(0.0, this)
	val z = new DepthNumericProperty(0.0, this)
	val actual = new Actual(this)
	
	def apply(x: Double, y: Double) = {
		this.x := x
		this.y := y
	}
	
	def apply(x: Double, y: Double, z: Double) = {
		this.x := x
		this.y := y
		this.z := z
	}
}