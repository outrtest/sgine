package org.sgine.ui.ext

import org.sgine.property.AdvancedProperty
import org.sgine.property.container.PropertyContainer

import org.sgine.ui.Component

class Padding(override val parent: Component) extends PropertyContainer {
	val top = new AdvancedProperty(0.0, this)
	val bottom = new AdvancedProperty(0.0, this)
	val left = new AdvancedProperty(0.0, this)
	val right = new AdvancedProperty(0.0, this)
}