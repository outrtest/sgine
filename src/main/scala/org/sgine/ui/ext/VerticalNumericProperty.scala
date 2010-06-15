package org.sgine.ui.ext

import org.sgine.core.VerticalAlignment
import org.sgine.core.VerticalAlignment._

import org.sgine.event.Listenable

import org.sgine.property.AdvancedProperty

class VerticalNumericProperty(value: Double, parent: Listenable) extends NumericProperty(value, parent) {
	val align = new AdvancedProperty[VerticalAlignment](VerticalAlignment.Middle, this)
}