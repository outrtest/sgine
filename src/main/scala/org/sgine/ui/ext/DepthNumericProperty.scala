package org.sgine.ui.ext

import org.sgine.core.DepthAlignment
import org.sgine.core.DepthAlignment._

import org.sgine.event.Listenable

import org.sgine.property.AdvancedProperty

class DepthNumericProperty(value: Double, parent: Listenable) extends NumericProperty(value, parent) {
	val align = new AdvancedProperty[DepthAlignment](DepthAlignment.Middle, this)
}