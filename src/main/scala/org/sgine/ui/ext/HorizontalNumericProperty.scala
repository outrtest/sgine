package org.sgine.ui.ext

import org.sgine.core.HorizontalAlignment
import org.sgine.core.HorizontalAlignment._

import org.sgine.event.Listenable

import org.sgine.property.AdvancedProperty
import org.sgine.property.NumericProperty

class HorizontalNumericProperty(value: Double, parent: Listenable) extends NumericProperty(value, parent) {
	val align = new AdvancedProperty[HorizontalAlignment](HorizontalAlignment.Center, this)
}