package org.sgine.ui.ext

import org.sgine.property.container.PropertyContainer

import org.sgine.ui.Component

import org.sgine.property.AdvancedProperty
import org.sgine.property.NumericProperty
import org.sgine.property.style.Stylized

class Clip(override val parent: Component) extends Stylized {
	val enabled = new AdvancedProperty[Boolean](false, this)
	val x1 = new NumericProperty(0.0, this)
	val y1 = new NumericProperty(0.0, this)
	val x2 = new NumericProperty(0.0, this)
	val y2 = new NumericProperty(0.0, this)
	val adjustX = new NumericProperty(0.0, this)
	val adjustY = new NumericProperty(0.0, this)
	val adjustXAmount = new NumericProperty(50.0, this)
}