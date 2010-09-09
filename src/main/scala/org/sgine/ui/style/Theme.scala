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
	
	val button = org.sgine.ui.Button
	val textInput = org.sgine.ui.TextInput
	
	// Apply the default theme
	WindowsTheme()
}