package org.sgine.ui.ext

import org.sgine.property.AdvancedProperty
import org.sgine.property.container.PropertyContainer

import org.sgine.ui.Component

class Rotation(override val parent: Component) extends PropertyContainer {
	val x = new AdvancedProperty(0.0, this)
	val y = new AdvancedProperty(0.0, this)
	val z = new AdvancedProperty(0.0, this)
}