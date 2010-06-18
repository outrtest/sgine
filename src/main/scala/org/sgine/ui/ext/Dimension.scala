package org.sgine.ui.ext

import org.sgine.property.container.PropertyContainer

import org.sgine.ui.Component

class Dimension(override val parent: Component) extends PropertyContainer {
	val width = new NumericProperty(0.0, this)
	val height = new NumericProperty(0.0, this)
	val depth = new NumericProperty(0.0, this)
}