package org.sgine.ui.ext

import org.sgine.property.container.PropertyContainer

import org.sgine.ui.Component

import org.sgine.property.NumericProperty

class Actual(override val parent: PropertyContainer) extends PropertyContainer {
	val x = new NumericProperty(0.0, this)
	val y = new NumericProperty(0.0, this)
	val z = new NumericProperty(0.0, this)
	
	def set(x: Double, y: Double) = {
		this.x := x
		this.y := y
	}
	
	def set(x: Double, y: Double, z: Double) = {
		this.x := x
		this.y := y
		this.z := z
	}
}