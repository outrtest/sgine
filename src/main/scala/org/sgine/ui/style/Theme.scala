package org.sgine.ui.style

import org.sgine.core.Color
import org.sgine.core.Resource

import org.sgine.property.AdvancedProperty
import org.sgine.property.ListenableProperty
import org.sgine.property.container.PropertyContainer

import org.sgine.render.font.Font

object Theme extends PropertyContainer with ListenableProperty[Int] {
	val font = new AdvancedProperty[Font](null, this)
	val textColor = new AdvancedProperty[Color](null, this)
	
	val button = new PropertyContainer with ListenableProperty[Int] {
		parent = Theme
		
		val normalSkin = new AdvancedProperty[Resource](null, this)
		val hoverSkin = new AdvancedProperty[Resource](null, this)
		val pressedSkin = new AdvancedProperty[Resource](null, this)
		val focusedSkin = new AdvancedProperty[Resource](null, this)
		
		val skinX1 = new AdvancedProperty[Double](0.0, this)
		val skinY1 = new AdvancedProperty[Double](0.0, this)
		val skinX2 = new AdvancedProperty[Double](0.0, this)
		val skinY2 = new AdvancedProperty[Double](0.0, this)
		
		val paddingTop = new AdvancedProperty[Double](0.0, this)
		val paddingBottom = new AdvancedProperty[Double](0.0, this)
		val paddingLeft = new AdvancedProperty[Double](0.0, this)
		val paddingRight = new AdvancedProperty[Double](0.0, this)
	}
	
	// Apply the default theme
	WindowsTheme()
}