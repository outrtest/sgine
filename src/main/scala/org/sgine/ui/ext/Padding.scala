package org.sgine.ui.ext

import org.sgine.property.AdvancedProperty
import org.sgine.property.container.PropertyContainer

import org.sgine.ui.Component

class Padding(override val parent: Component) extends PropertyContainer {
	val top = new AdvancedProperty(0.0, this)
	val left = new AdvancedProperty(0.0, this)
	val bottom = new AdvancedProperty(0.0, this)
	val right = new AdvancedProperty(0.0, this)
	
	def apply(value: Double) = {
		top := value
		bottom := value
		left := value
		right := value
	}
	
	def apply(top: Double, left: Double, bottom: Double, right: Double) = {
		this.top := top
		this.left := left
		this.bottom := bottom
		this.right := right
	}
	
	override def toString() = "Padding(top: " + top() + ", left: " + left() + ", bottom: " + bottom() + ", right: " + right() + ")"
}