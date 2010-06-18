package org.sgine.ui.ext

import org.sgine.core.NumericUnit
import org.sgine.core.NumericUnit._

import org.sgine.event.Listenable

import org.sgine.property.AdvancedProperty

class NumericProperty(value: Double, parent: Listenable) extends AdvancedProperty[Double](value, parent) {
	val units = new AdvancedProperty[NumericUnit](NumericUnit.Pixel, this)
	
	def apply(value: String): NumericProperty = this := value
	
	def :=(value: String): NumericProperty = {
		if ((value == null) || ("auto".equalsIgnoreCase(value))) {
			units := NumericUnit.Auto
			this := 0.0
		} else if (value.endsWith("%")) {
			units := NumericUnit.Percent
			this := value.substring(0, value.length - 1).toDouble
		} else if (value.endsWith("px")) {
			units := NumericUnit.Pixel
			this := value.substring(0, value.length - 2).toDouble
		} else {
			units := NumericUnit.Pixel
			this := value.toDouble
		}
		
		this
	}
}